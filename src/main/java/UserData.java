import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class UserData {
    private int id;
    private String password;
    private String lastName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public UserData(int id, String password, String lastName) {
        this.id = id;
        this.password = password;
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserData userData = (UserData) o;
        return id == userData.id && Objects.equals(password, userData.password) && Objects.equals(lastName, userData.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, password, lastName);
    }

    @Override
    public String toString() {
        return "UserData{" +
            "id=" + id +
            ", password='" + password + '\'' +
            ", lastName='" + lastName + '\'' +
            '}';
    }

    public static <T> T combine2Objects(T a, T b) throws InstantiationException,IllegalAccessException{
        T result = (T) a.getClass().newInstance();
        Object[] fields = Arrays.stream(a.getClass().getDeclaredFields()).filter(f -> !f.getName().equals("serialVersionUID")).collect(Collectors.toList()).toArray();
        for (Object fieldobj : fields) {
            Field field = (Field) fieldobj;
            field.set(result, field.get(b) != null ? field.get(b) : field.get(a));
        }
        return result;
    }

    public UserData() {
    }

}
