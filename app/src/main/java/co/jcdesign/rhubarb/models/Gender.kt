package co.jcdesign.rhubarb.models

enum class Gender(val value: Int) {

    FEMALE(0),
    MALE(1),
    NON_BINARY(2);

    override fun toString(): String {
        return when (this) {
            FEMALE -> "Female"
            MALE -> "Male"
            NON_BINARY -> "Non-binary"
        }
    }
}