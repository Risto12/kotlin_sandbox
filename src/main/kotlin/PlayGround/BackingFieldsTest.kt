package PlayGround

class BackingFieldsTest {
    private var _a = 2
    var b = _a
        get() { return field }
        set(value) {
            // Field is the keyword to point to the value.
            // Otherwise, would create endless loop
            field = value
        }
}
