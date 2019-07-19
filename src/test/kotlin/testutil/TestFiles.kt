package testutil

import java.nio.file.*
interface  Junk

fun getResourcePath(relativePath: String): Path {

    val url = TestCmdRunner::class.java.classLoader.getResource(relativePath)
     return Paths.get(url.toURI())
}


// Resource Directories
val testInputResourcesDir = getResourcePath("test-input-files")
val testOutputResourcesDir = getResourcePath("test-output-files")

// Test Working Directories
val testDir = Paths.get("/tmp/chipseq-test")!!
val testInputDir = testDir.resolve("input")!!
val testOutputDir = testDir.resolve("output")!!

// Input Files
val REPS= testInputDir.resolve("rep1.subsampled.25.fastq.gz")!!

val REP1= testInputDir.resolve("rep1-R1.subsampled.67.fastq.gz")!!
val REP2= testInputDir.resolve("rep1-R2.subsampled.67.fastq.gz")!!

val CTL= testInputDir.resolve("ctl1.subsampled.25.fastq.gz")!!

val CONTROL= testInputDir.resolve("ctl1.subsampled.25.fastq.gz")!!

val BWAINDEXFILE= testInputDir.resolve("GRCh38_no_alt_analysis_set_GCA_000001405.15.fasta.tar")!!
// Output File Names
val REPS_OUTPUT= "rep_align_output"
val CONTROL_OUTPUT= "control_align_output"
//val tr = getResourcePath1("resources")
val tdr= Paths.get("/home/nishiphalke/chipseq-bwa-task/src/test/resources/test-input-files")
val CNTRL =  tdr.resolve("ctl1.subsampled.25.fastq.gz")//getResourcePath1("rep1.subsampled.25.fastq.gz")

val REPS1 =  tdr.resolve("rep1.subsampled.25.fastq.gz")//getResourcePath1("rep1.subsampled.25.fastq.gz")
val BWAINDEX1 = tdr.resolve("GRCh38_no_alt_analysis_set_GCA_000001405.15.fasta.tar")
val OUTDIR =  Paths.get("/home/nishiphalke/chipseq-bwa-task/src/test/resources/test-output-files")

val SE1= testInputDir.resolve("ENCFF000ASP.fastq.gz")
val CONTROLREP= testInputDir.resolve("ENCFF000ARK.fastq.gz")