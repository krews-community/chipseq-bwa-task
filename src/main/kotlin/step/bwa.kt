package step

import mu.KotlinLogging
import java.nio.file.*
import java.util.zip.GZIPInputStream
import util.CmdRunner
private val log = KotlinLogging.logger {}


fun CmdRunner.bwa(rep: Path,bwaIndexFile:Path,output: Path) {

    Files.createDirectories(output.parent)
    log.info { bwaIndexFile }
    log.info { output }
   // this.run("tar xvf $bwaIndexFile -C $output")
    val rawInputStream = Files.newInputStream(rep)
    val inputStream = if (rep.toString().endsWith(".gz")) GZIPInputStream(rawInputStream) else rawInputStream

    inputStream.reader().forEachLine { line ->
        Files.createDirectories(output.parent)
        Files.newBufferedWriter(output).use { writer ->
                writer.write(line)
        }
    }


}


