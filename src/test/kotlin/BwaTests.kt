import org.junit.jupiter.api.*
import step.*
import testutil.*
import mu.KotlinLogging
import testutil.cmdRunner
import testutil.setupTest

private val log = KotlinLogging.logger {}

class BwaTests {
    @BeforeEach fun setup() = setupTest()
    @AfterEach fun cleanup() = cleanupTest()

    @Test fun `run bwa step`() {
        //TODO: get test files from gc bucket as files are large in size
        //cmdRunner.bwa(CTL,BWAINDEXFILE,testOutputDir.resolve(REPS_OUTPUT),null)
    }
}