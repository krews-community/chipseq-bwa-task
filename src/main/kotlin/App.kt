import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.*
import com.github.ajalt.clikt.parameters.types.*
import mu.KotlinLogging
import step.*
import util.*
import java.nio.file.*
import util.CmdRunner
private val log = KotlinLogging.logger {}

data class bwaInput(val name: String,val rep1:Path, val indexFile:Path,val rep2:Path?)

fun main(args: Array<String>) = Cli().main(args)

class Cli : CliktCommand() {


    private val repFile1: List<Path> by option("-repFile1", help = "path to fastq rep file")
        .path(exists = true).multiple().validate { require(it.isNotEmpty()) {"At least one path must be given"} }
    private val repFile2: List<Path?> by option("-repFile2", help = "path to fastq rep2 file")
            .path().multiple(listOf())
    private val name: List<String> by option("-name", help = "name for output file")
            .multiple().validate { require(it.isNotEmpty()) {"At least one name for output prefix must be given"} }
    private val indexFile:List<Path> by option("-indexFile", help = "path to index tar file")
        .path(exists = true).multiple().validate { require(it.isNotEmpty()) {"At least one index file path must be given"} }
    private val outDir by option("-outputDir", help = "path to output Directory")
        .path().required()
    private val pairedEnd: Boolean by option("-pairedEnd", help = "Paired-end BAM.").flag()
    private val use_bwa_mem_for_pe: Boolean by option("-use-bwa-mem-for-pe", help = "Paired-end BAM.").flag()
    private val parallelism: Int by option("-parallelism", help = "Number of threads to parallelize.").int().default(1)


    override fun run() {
        val cmdRunner = DefaultCmdRunner()

        val bwaInputs = mutableListOf<bwaInput>()
        for( (rep1Index,rep1) in repFile1.withIndex())
        {
            val name = name.getOrElse(rep1Index) { name.last()}
            val indexFile = indexFile.getOrElse(rep1Index) { indexFile.last()}
            val rep2 = if(repFile2.isNullOrEmpty()) { null } else { repFile2.getOrElse(rep1Index)  { null}}
            bwaInputs +=  bwaInput(name,rep1,indexFile,rep2)

        }
        cmdRunner.runTask(bwaInputs,pairedEnd,use_bwa_mem_for_pe,parallelism, outDir)
    }
}

/**
 * Runs pre-processing and bwa for raw input files
 *
 * @param bwaInputs bwa Input
 * @param outDir Output Path
 */
fun CmdRunner.runTask(bwaInputs: List<bwaInput>,pairedEnd: Boolean,use_bwa_mem_for_pe:Boolean,nth:Int, outDir: Path) {

    for ( bi in bwaInputs) {
        log.info {
            """
        Running bwa task for
        rep1: ${bi.rep1}
        indexFile: ${bi.indexFile}
        outDir: $outDir
        """.trimIndent()
        }


        bwa(bi.rep1, bi.indexFile,pairedEnd,use_bwa_mem_for_pe, nth,outDir.resolve(bi.name),bi.rep2)
    }
}