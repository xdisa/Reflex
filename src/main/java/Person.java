import java.util.Objects;

public class Person {
    private String lastName;
    private String firstName;
    private int id;

    public Person(String lastName, String firstName, int id) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return id == person.id && Objects.equals(lastName, person.lastName) && Objects.equals(firstName, person.firstName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lastName, firstName, id);
    }

    @Override
    public String toString() {
        return "Person{" +
            "lastName='" + lastName + '\'' +
            ", firstName='" + firstName + '\'' +
            ", id=" + id +
            '}';
    }

    public Person() {
    }
}
