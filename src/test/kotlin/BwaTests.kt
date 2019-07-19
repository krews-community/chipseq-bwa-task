import org.junit.jupiter.api.*
import step.*
import testutil.*
import mu.KotlinLogging
import testutil.cmdRunner
import testutil.setupTest

private val log = KotlinLogging.logger {}

class BwaTests {
    @BeforeEach fun setup() = setupTest()
    @AfterEach fun cleanup() = CopyOpt()

     @Test fun `run bwa step`() {
        //TODO: get test files from gc bucket as files are large in size
       // cmdRunner.bwa(SE1,BWAINDEXFILE,false,false,testOutputDir.resolve(REPS_OUTPUT),null)
      //  cmdRunner.bwa(CONTROLREP,BWAINDEXFILE,false,false,4,testOutputDir.resolve("ENCFF000ARK"),null)
    }
}