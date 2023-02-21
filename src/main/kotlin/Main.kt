import CoroutinePlayGround.*
import PlayGround.safeCasting1
import PlayGround.safeCasting2
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay

/**
 * Sandbox for playing around with Kotlin
 */
/*suspend fun main() = coroutineScope {
    testJobCancelingJob3()
    Unit
}
*/
suspend fun main() = coroutineScope {
    testCancellationException(true)
}