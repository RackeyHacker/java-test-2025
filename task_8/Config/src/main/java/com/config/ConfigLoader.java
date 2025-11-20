package com.config;

import com.config.annotations.ConfigProperty;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Properties;

public class ConfigLoader {

    public static void loadConfiguration(Object obj) {

        Class<?> clazz = obj.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            ConfigProperty annotation = field.getAnnotation(ConfigProperty.class);
            if (annotation == null) continue;

            Properties properties = new Properties();

            String configFileName = annotation.configFileName();

            try {
                ClassLoader cl = obj.getClass().getClassLoader();
                properties.load(cl.getResourceAsStream(configFileName));
            } catch (Exception e) {
                throw new RuntimeException("Can't load configuration file", e);
            }

            String propertyName = annotation.propertyName();
            String propertyNameValue = properties.getProperty(propertyName);
            if (propertyNameValue == null) {
                throw new RuntimeException("The property not found: " + propertyName);
            }

            Class<?> type = field.getType();

            Object value = convertValue(type, propertyNameValue);

            field.setAccessible(true);
            try {
                field.set(obj, value);
            } catch (Exception e) {
                throw new RuntimeException("Can't set " + field.getName(), e);
            }

        }
    }

    private static Object convertValue(Class<?> type, String value) {
        if (type == String.class) return value;
        if (type == Integer.class || type == int.class) return Integer.valueOf(value);
        if (type == Long.class || type == long.class) return Long.valueOf(value);
        if (type == Double.class || type == double.class) return Double.valueOf(value);
        if (type == Boolean.class || type == boolean.class) return Boolean.valueOf(value);
        if (type == BigDecimal.class) return new BigDecimal(value);
        throw new RuntimeException("Unsupported type: " + type);
    }

}
