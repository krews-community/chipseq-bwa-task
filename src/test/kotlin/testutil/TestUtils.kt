package testutil

import java.nio.file.*
import mu.KotlinLogging
import util.CmdRunner
import util.exec

private val log = KotlinLogging.logger {}
val cmdRunner = TestCmdRunner()

class TestCmdRunner : CmdRunner {
    override fun run(cmd: String) = exec("docker", "exec", "chipseq-bwa-base", "sh", "-c", cmd)
}

fun copyDirectory(fromDir: Path, toDir: Path) {
    Files.walk(fromDir).forEach { fromFile ->
        if (Files.isRegularFile(fromFile)) {
            val toFile = toDir.resolve(fromDir.relativize(fromFile))
            Files.createDirectories(toFile.parent)
            Files.copy(fromFile, toFile)
        }
    }
}

fun setupTest() {
    cleanupTest()
    // Copy all resource files from "test-input-files" and "test-output-files" dirs into
    // docker mounted working /tmp dir
    copyDirectory(testInputResourcesDir, testInputDir)
  //  copyDirectory(testOutputResourcesDir, testOutputDir)
}

fun cleanupTest() {
    log.info { testInputDir }
    if (Files.exists(testInputDir)) {

        Files.walk(testInputDir).sorted(Comparator.reverseOrder()).forEach { Files.delete(it) }
    }
    if (Files.exists(testOutputDir)) {
        Files.walk(testOutputDir).sorted(Comparator.reverseOrder()).forEach { Files.delete(it) }
    }
}

fun CopyOpt() {
    copyDirectory(testOutputDir, testOutputResourcesDir)
}