package PlayGround

fun <T> T.applyThenReturn1(f: (T) -> Unit): T {
    f(this)
    return this
}

fun <T> T.applyThenReturn2(o: Int, f: T.(Int, Int) -> Unit): T {
    f(o, 2)
    return this
}
