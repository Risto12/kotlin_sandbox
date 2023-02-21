import CoroutinePlayGround.testContextPassing
import CoroutinePlayGround.testJobCancellation1
import CoroutinePlayGround.testJobCancellation2
import CoroutinePlayGround.testingCoroutineJoin
import kotlinx.coroutines.coroutineScope


suspend fun main() = coroutineScope {
    testingCoroutineJoin()
    Unit
}