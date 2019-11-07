package co.cy.kotin101

import co.cy.kotlin101.Demo
import kotlin.random.Random

// Underscores can be used to make numbers more readable
const val billion = 1_000_000_000

private fun String.helloWorld() {
    println("Hello world")
}

private fun String.hello(name: String) {
    println("Hello $name")
}

fun main() {
    // extension functions to add new functionality to String class
    "".helloWorld()
    "this will be lost to the ether".hello("Cy")


    // val makes defaultData immutable
    // type can be inferred
    val defaultData: Data = Data(1, 3.3f)
    println("Data arg3 ${defaultData.arg3}")

    // default data is immutable however arg1 and arg2 are not
    // arguments are accessed directly, not get/set
    defaultData.arg1++

    // will call toString() by default
    println(defaultData)
    println(Identity.iAm)


    // create a java class, type is inferred
    val demo = Demo()

    // call a java function, calling a kotlin function
    demo.helloWorld()

    // person may be null because demo.person comes from java
    val uniquePeople = mutableSetOf(
        demo.person,
        Person("Cy", "Young"),
        demo.person,
        Person("John", "Smith"),
        Person("Jane", "Doe")
    )
    println(uniquePeople)

    val newDemo = Demo()
    newDemo.person.lastName = "Smith"
    // add a new person
    uniquePeople += newDemo.person
    println(uniquePeople)

    // convert to list, filter out null people
    val people = uniquePeople.toList()//.filterNotNull()

    // I'm sure there is a Cy so !! states this can't be null
    val lastCy = people.findLast { it.firstName == "Cy" }!!
    val allCys = people.filter { it.firstName == "Cy" }
    // print  a string literal and remove the extra whitespace
    println(
        """Last Cy $lastCy
           Sorted Cys ${allCys.sortedBy { person -> person.lastName }}.
        """.replace(" ", "")
    )

    val numbers = listOf(1, 2, 3, null, 5)
    // fold numbers to get total value, and when a null is encountered replace with a 0
    // .plus is the same as +
    val total = numbers.fold(0, {total, next-> total.plus(next ?: 0) })
    println(total)
}

// This creates a singleton
object Identity {

    // immutable. Lazy evaluation when first accessed
    val iAm: String by lazy { whoAmI() }

    // Can be an expression body
    private fun whoAmI(): String {
        return if (Random(System.currentTimeMillis()).nextInt() % 2 == 1) {
            "Cy"
        } else {
            "Unknown"
        }
    }
}