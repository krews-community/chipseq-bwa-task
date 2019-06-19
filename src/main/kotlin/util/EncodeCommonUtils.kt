package util

fun strip_ext_bam(bam:String):String{
    val regex = """.(bam|Bam)""".toRegex()
    return regex.replace(bam, "")
}
fun strip_ext_fastq(fastq:String):String {
    val regex = """.(fastq|fq|Fastq|Fq).gz""".toRegex()
    return regex.replace(fastq, "")
}

fun strip_ext_tar(tar:String):String{
    val regex = """.tar""".toRegex()
    return regex.replace(tar, "")
}
fun strip_ext_ta(ta:String):String {
    val regex = """.(tagAlign|TagAlign|ta|Ta).gz""".toRegex()
    return regex.replace(ta, "")
}

fun strip_ext_bed(bed:String):String {
    val regex = """.(bed|Bed).gz""".toRegex()
    return regex.replace(bed, "")
}

fun strip_ext_npeak(npeak:String):String {
    val regex = """.(narrowPeak|NarrowPeak).gz""".toRegex()
    return regex.replace(npeak, "")
}

fun strip_ext_rpeak(rpeak:String):String {
    val regex = """.(regionPeak|RegionPeak).gz""".toRegex()
    return regex.replace(rpeak, "")
}
fun strip_ext_gpeak(gpeak:String):String {
    val regex = """.(gappedPeak|GappedPeak).gz""".toRegex()
    return regex.replace(gpeak, "")
}

fun strip_ext_bpeak(bpeak:String):String {
    val regex = """.(broadPeak|BroadPeak).gz""".toRegex()
    return regex.replace(bpeak, "")
}
fun strip_ext_bigwig(bw:String):String {
    val regex = """.(bigwig|bw)""".toRegex()
    return regex.replace(bw, "")
}

fun strip_ext_gz(f:String):String {
    val regex = """.gz""".toRegex()
    return regex.replace(f, "")
}
