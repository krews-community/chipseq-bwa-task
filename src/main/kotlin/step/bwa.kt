package step
import util.*
import java.nio.file.*
import java.util.zip.GZIPInputStream
import util.CmdRunner
import kotlin.math.max


fun CmdRunner.bwa(rep: Path,bwaIndexFile:Path,pairedEnd: Boolean,useBwaMem:Boolean,nth:Int,output: Path,rep2:Path?,bwa_index_prefix:String?=null) {
    Files.createDirectories(output.parent)
    if(rep2!==null)
    {
      //  make_read_length_file(rep2,output)
    } else {
      //  make_read_length_file(rep,output)
    }
    //untar index file

    val o = output.parent
    this.run("tar xvf $bwaIndexFile -C $o")
    var bwa_ind_prefix =""
    if(bwa_index_prefix!==null)
    {
        bwa_ind_prefix =bwa_index_prefix
    }else {
        bwa_ind_prefix = bwaIndexFile.fileName.toString()
    }
    val bwa_index_prefix_sa = output.parent.resolve( strip_ext_tar(bwa_ind_prefix+".sa"))
    val bwa_index_prefix = output.parent.resolve( strip_ext_tar(bwa_ind_prefix))

    //check if index file exists
    if(!Files.exists(bwa_index_prefix_sa))
    {
        throw Exception("bwa index does not exists $bwa_index_prefix_sa")
    }

    var bam:String
    if(pairedEnd==true && rep2!=null)
    {
        bam= bwa_pe(rep,rep2,bwa_index_prefix,nth,useBwaMem,output)
    } else {
        bam= bwa_se(rep,bwa_index_prefix,nth,useBwaMem,output)
    }

    val si = samtools_index(bam)
    val sf = samtools_flagstat(bam,output)
}

fun CmdRunner.bwa_aln(fastq:Path, bwa_index_prefix:Path, nth:Int, output: String):String
{
    val sai = "$output.sai"
    //run bwa
    this.run("bwa aln -q 5 -l 32 -k 2 -t $nth $bwa_index_prefix $fastq > $sai")
    return sai
}

fun CmdRunner.bwa_se(fastq:Path, bwa_index_prefix:Path, nth:Int,useBwaMem:Boolean, output: Path):String{
    val bam = "$output.bam"
    val sam="$output.sam"

    if(useBwaMem)
    {
       val cmd2 = "bwa mem -M -t ${nth} ${bwa_index_prefix} ${fastq}  | gzip -nc > ${sam}"
       this.run(cmd2)
       val  cmd3  = "samtools view -Su $sam | samtools sort -@ ${nth}  -T ${output} - -o $bam"
       this.run(cmd3)

    } else {
        val sai = bwa_aln(fastq,bwa_index_prefix,nth,output.toString())
        this.run("bwa samse $bwa_index_prefix $sai $fastq | samtools view -Su - | samtools sort - -o $bam")
        rm_f(listOf(sai))
    }

    return bam

}
fun CmdRunner.samtools_index(bam:String):String
{
    val bai = "$bam.bai"
    this.run("samtools index $bam")
    return bai
}
fun CmdRunner.samtools_flagstat(bam:String,output:Path):String
{
    val flagstat_qc ="$output.flagstat.qc"
    this.run("samtools flagstat $bam > $flagstat_qc")
    return flagstat_qc
}
fun make_read_length_file(fastq:Path, output: Path){

    val read_length = get_read_length(fastq)
    val fpath = output.parent.resolve(output.fileName.toString()+".read_length.txt")

    Files.newBufferedWriter(fpath).use { writer ->
        writer.write(read_length.toString())
    }
}
fun get_read_length(fastq:Path):Int {
    val total_reads_to_consider = 1000000
    var line_num = 0
    var total_reads_considered = 0
    var max_length = 0
    val rawInputStream = Files.newInputStream(fastq)
    val inputStream = if (fastq.toString().endsWith(".gz")) GZIPInputStream(rawInputStream) else rawInputStream

    val isr = inputStream.reader()

    val line = isr.useLines { lines ->  for (l in lines) {

        if(line_num.rem(4)==1)
        {
            if(l.trim().length > max_length)
            {
                max_length = l.trim().length
            }
            total_reads_considered +=1
        }
        if(total_reads_considered >=total_reads_to_consider)
        {
            break
        }
        line_num +=1
    }}


    return max_length
}
fun CmdRunner.bwa_pe(fastq1:Path,fastq2:Path,bwa_index_prefix: Path,nth: Int,useBwaMem:Boolean,output: Path):String{

    val sam="$output.sam"
    val badcigar = "$output.badReads"
    val bam = "$output.bam"
    var cmd2:String
    var sai1=""
    var sai2=""
  //  val fastq1_read_length = get_read_length(fastq1)
    if(useBwaMem)
    {
        var cmd = "bwa mem -M -t ${nth} ${bwa_index_prefix} ${fastq1} ${fastq2} | gzip -nc > ${sam}"
        this.run(cmd)

    } else{

         sai1 = bwa_aln(fastq1,bwa_index_prefix, max(1,nth/2),output.toString()+"fstq1")
         sai2 = bwa_aln(fastq2,bwa_index_prefix, max(1,nth/2),output.toString()+"fstq2")

        this.run("bwa sampe $bwa_index_prefix $sai1 $sai2 $fastq1 $fastq2 | gzip -nc > $sam")

    }
    cmd2 = "zcat -f $sam | awk \'BEGIN {{FS=\"\\t\" ; OFS=\"\\t\"}} ! /^@/ && $6!=\"*\" "
    cmd2 += "{{ cigar=$6; gsub(\"[0-9]+D\",\"\",cigar); n = split(cigar,vals,\"[A-Z]\"); s = 0; "
    cmd2 += "for (i=1;i<=n;i++) s=s+vals[i]; seqlen=length($10); "
    cmd2 += "if (s!=seqlen) print $1\"\\t\"; }}\' | "
    cmd2 += "sort | uniq > $badcigar"
    this.run(cmd2)

    //find no. of lines in badcigar file
    val fpath = output.parent.resolve(output.fileName.toString()+".badReads")
    val rawInputStream = Files.newInputStream(fpath)
    val inputStream = if (fpath.toString().endsWith(".gz")) GZIPInputStream(rawInputStream) else rawInputStream

    val isr = inputStream.reader()

    val line:List<String> = isr.readLines()
    val lineCount = line.size

    var cmd3:String
    if(lineCount>0){
        cmd3 = "zcat -f $sam | grep -v -F -f $badcigar | "
        cmd3 += "samtools view -Su - | samtools sort - -o $bam"

    } else {

        cmd3 = "samtools view -Su $sam | samtools sort - -o $bam"

    }
    this.run(cmd3)

    rm_f(listOf(sai1,sai2,sam))
    return bam

}