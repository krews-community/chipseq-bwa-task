package util

import org.assertj.core.api.Assertions.*
import java.nio.file.*

fun getResourcePath(relativePath: String): Path {
    val url = TestCmdRunner::class.java.classLoader.getResource(relativePath)
    return Paths.get(url.toURI())
}

fun assertOutputMatches(filename: String) =
    assertThat(testOutputDir.resolve(filename)).hasSameContentAs(testOutputResourcesDir.resolve(filename))!!

// Resource Directories
val testInputResourcesDir = getResourcePath("test-input-files")
val testOutputResourcesDir = getResourcePath("test-output-files")

// Test Working Directories
val testDir = Paths.get("/tmp/chipseq-test")!!
val testInputDir = testDir.resolve("input")!!
val testOutputDir = testDir.resolve("output")!!

// Input Files
val REPS= testInputDir.resolve("rep1.subsampled.25.fastq.gz")!!
val CONTROL= testInputDir.resolve("ctl1.subsampled.25.fastq.gz")!!

val BWAINDEXFILE= testInputDir.resolve("GRCh38_no_alt_analysis_set_GCA_000001405.15.fasta.tar")!!
// Output File Names
val REPS_OUTPUT= "rep_align_output"
val CONTROL_OUTPUT= "control_align_output"