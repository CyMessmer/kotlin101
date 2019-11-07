package co.cy.kotlin101;

import co.cy.kotin101.Person;
import org.jetbrains.annotations.NotNull;

public class Demo {
    // Easily create a Person from the kotlin code
    Person person = new Person("Cy", "Messmer");

    // Access to the companion object
    int age = person.Companion.getAge();

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void helloWorld(){
        // call kotlin function
        person.greet();
    }
}
