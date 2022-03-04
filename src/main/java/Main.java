public class Main {
    public static void main(String[] args) throws Exception {
Person person = new Person("Disa","OG",1);
Password password = new Password("Qwerty14");
        Password password1 = new Password("Qwerty124");
UserData userData = new UserData(9,"ugyi","iutgfy");


userData.copyMatchingFields(person,userData);//копируем поля из персоны
userData.copyMatchingFields(password,userData);//копируем поля из пассворда


        System.out.println(userData);


    }
}
