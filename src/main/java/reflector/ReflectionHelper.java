package reflector;

import java.lang.reflect.Field;

/**
 * Created by mihanik
 * 04.04.15 0:48
 * Package: reflector
 */
class ReflectionHelper {
    public static Object generateObject(String className) {
        Object obj = null;
        try {
           obj = Class.forName(className).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    @SuppressWarnings("UnusedReturnValue")
    public static Object setObjectField(Object obj, String fieldName, String value) {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);

            if (field.getType().equals(String.class)) {
                field.set(obj, value);
            } else if (field.getType().equals(Integer.class)) {
                field.set(obj, Integer.decode(value));
            }

            field.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }
}
