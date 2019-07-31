package testutil

import java.nio.file.*

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


val BWAINDEXFILE= testInputDir.resolve("GRCh38_no_alt_analysis_set_GCA_000001405.15.fasta.tar")!!


