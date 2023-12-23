package org.lab;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Properties;

/**
 * The {@code Injector} class is responsible for injecting dependencies into fields
 * annotated with {@code AutoInjectable}.
 * <p>
 * It uses reflection to inspect the fields of an object, identifies those annotated with
 * {@code AutoInjectable}, and then creates and injects instances of the required dependencies
 * at runtime.
 * </p>
 *
 * @see AutoInjectable
 */
public class Injector {

    /**
     * Injects dependencies into the fields of the given object.
     *
     * @param object the object into which dependencies should be injected
     * @param <T>    the type of the object
     * @return the object with injected dependencies
     */
    public <T> T inject(T object) {
        Class<?> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(AutoInjectable.class)) {
                Class<?> fieldType = field.getType();
                Object instance = createInstance(fieldType);
                field.setAccessible(true);
                try {
                    field.set(object, instance);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        return object;
    }

    /**
     * Creates an instance of the specified class using the class name obtained from
     * the properties file.
     *
     * @param clazz the class for which an instance should be created
     * @return an instance of the specified class
     * @throws RuntimeException if an instance cannot be created
     */
    private Object createInstance(Class<?> clazz) {
        String className = getClassNameFromProperties(clazz.getName());
        try {
            return Class.forName(className).newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to create an instance for " + clazz.getName());
        }
    }

    /**
     * Retrieves the class name corresponding to the given interface or class name
     * from the properties file.
     *
     * @param interfaceName the name of the interface or class
     * @return the fully qualified class name
     * @throws RuntimeException if the properties file cannot be loaded
     */
    private String getClassNameFromProperties(String interfaceName) {
        Properties properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("injector.properties")) {
            properties.load(input);
            return properties.getProperty(interfaceName);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load properties file");
        }
    }
}
