public class Main {
    public static void main(String[] args) throws InstantiationException, IllegalAccessException {
Person person = new Person("Disa","OG",1);
Password password = new Password("Qwerty14");
UserData userData = new UserData();
userData.combine2Objects(person,password);
        System.out.println(userData);


    }
}
