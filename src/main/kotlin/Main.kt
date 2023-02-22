import PlayGround.testChannelProducerConsumer1
import kotlinx.coroutines.coroutineScope

/**
 * Sandbox for playing around with Kotlin
 */
/*suspend fun main() = coroutineScope {
    testJobCancelingJob3()
    Unit
}
*/
suspend fun main() = coroutineScope {
    testChannelProducerConsumer1()

}