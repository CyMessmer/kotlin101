package co.cy.kotin101

/**
 * Getters, setters, equals(), hashCode(), toString(), and copy()
 * are all included for free
 * Type must be defined in data class
 */
data class Data(var arg1: Int, var arg2: Float, val arg3: String = "default")

data class Person(var firstName: String, var lastName: String) {
    // Equivalent of static types in java
    companion object {
        val age = 29
    }

    fun greet(){
        println("Hello fellow human")
    }
}