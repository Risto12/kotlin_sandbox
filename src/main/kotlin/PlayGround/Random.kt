package PlayGround

fun safeCasting1(a: Int): String? {
    // will always return null because this can't work but doesn't throw exception
    return a as? String
}

//generic types are erased at runtime.
fun <T> safeCasting2(from: Any): T? = from as? T

fun sequenceTest() {
    val seq = sequence {
        var a = 1
        do {
            yield(a)
            a++
        } while(true)
    }
    val iterator = seq.iterator()
    println(iterator.next())
    println(iterator.next())
}