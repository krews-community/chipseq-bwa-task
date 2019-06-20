import org.junit.jupiter.api.*
import step.*
import util.*
import mu.KotlinLogging
private val log = KotlinLogging.logger {}

class BwaTests {
    @BeforeEach fun setup() = setupTest()
   // @AfterEach fun cleanup() = cleanupTest()

    @Test fun `run bwa step`() {

      cmdRunner.bwa(REPS,BWAINDEXFILE,testOutputDir.resolve(REPS_OUTPUT))
      cmdRunner.bwa(CONTROL,BWAINDEXFILE,testOutputDir.resolve(CONTROL_OUTPUT))
     // assertOutputMatches(REPS_OUTPUT)
    }
}