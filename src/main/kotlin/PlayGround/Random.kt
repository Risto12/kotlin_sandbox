package PlayGround

fun safeCasting1(a: Int): String? {
    // will always return null because this can't work but doesn't throw exception
    return a as? String
}

//generic types are erased at runtime.
fun <T> safeCasting2(from: Any): T? = from as? T