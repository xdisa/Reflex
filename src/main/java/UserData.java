import com.sun.istack.internal.NotNull;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;
import java.lang.reflect.Modifier;
import java.util.List;

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
///////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////NOT WORK////////////////////////////////////////////////
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

    public static void copy(Object from, Object to) throws Exception {
        UserData.copy(from, to, Object.class);
    }

    public static UserData copy(Object from, Object to, Class depth) throws Exception {
        Class fromClass = from.getClass();
        Class toClass = to.getClass();
        List<Field> fromFields = collectFields(fromClass, depth);
        List<Field> toFields = collectFields(toClass, depth);
        Field target;
        for (Field source : fromFields) {
            if ((target = findAndRemove(source, toFields)) != null) {
                target.set(to, source.get(from));
            }
        }
        return null;
    }

    private static List<Field> collectFields(Class c, Class depth) {
        List<Field> accessibleFields = new ArrayList<>();
        do {
            int modifiers;
            for (Field field : c.getDeclaredFields()) {
                modifiers = field.getModifiers();
                if (!Modifier.isStatic(modifiers) && Modifier.isPublic(modifiers)) {
                    accessibleFields.add(field);
                }
            }
            c = c.getSuperclass();
        } while (c != null && c != depth);
        return accessibleFields;
    }

    private static Field findAndRemove(Field field, List<Field> fields) {
        Field actual;
        for (Iterator<Field> i = fields.iterator(); i.hasNext();) {
            actual = i.next();
            if (field.getName().equals(actual.getName())
                    && field.getType().equals(actual.getType())) {
                i.remove();
                return actual;
            }
        }
        return null;
    }



    //////////////////////////////////////////////////////////////////////////////////////////////////


    public static <T > void copyAllFields(T to, T from) {
        Class<T> clazz = (Class<T>) from.getClass();
        // OR:
        // Class<T> clazz = (Class<T>) to.getClass();
        List<Field> fields = getAllModelFields(clazz);

        if (fields != null) {
            for (Field field : fields) {
                try {
                    field.setAccessible(true);
                    field.set(to,field.get(from));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static List<Field> getAllModelFields(Class aClass) {
        List<Field> fields = new ArrayList<>();
        do {
            Collections.addAll(fields, aClass.getDeclaredFields());
            aClass = aClass.getSuperclass();
        } while (aClass != null);
        return fields;
    }
//////////////////////////////////////////////////////////////////////////////////////////

    public static <T> void copyAvalableFields(@NotNull T source, @NotNull T target) throws IllegalAccessException {
        Field[] fields = source.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (!Modifier.isStatic(field.getModifiers())
                    && !Modifier.isFinal(field.getModifiers())) {
                field.set(target, field.get(source));
            }
        }
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////


    ////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////WORKING//////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////

    public static void copyMatchingFields( Object fromObj, Object toObj ) {
        if ( fromObj == null || toObj == null )
            throw new NullPointerException("Source and destination objects must be non-null");

        Class fromClass = fromObj.getClass();
        Class toClass = toObj.getClass();

        Field[] fields = fromClass.getDeclaredFields();
        for ( Field f : fields ) {
            try {
                Field t = toClass.getDeclaredField( f.getName() );

                if ( t.getType() == f.getType() ) {
                    // extend this if to copy more immutable types if interested
                    if ( t.getType() == String.class
                            || t.getType() == int.class || t.getType() == Integer.class
                            || t.getType() == char.class || t.getType() == Character.class) {
                        f.setAccessible(true);
                        t.setAccessible(true);
                        t.set( toObj, f.get(fromObj) );
                    } else if ( t.getType() == Date.class  ) {
                        // dates are not immutable, so clone non-null dates into the destination object
                        Date d = (Date)f.get(fromObj);
                        f.setAccessible(true);
                        t.setAccessible(true);
                        t.set( toObj, d != null ? d.clone() : null );
                    }
                }
            } catch (NoSuchFieldException ex) {
                // skip it
            } catch (IllegalAccessException ex) {
                System.out.println("error");
            }
        }
    }
}
