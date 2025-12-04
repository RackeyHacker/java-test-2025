package com;

import com.annotations.*;
import org.reflections.Reflections;
import com.config.ConfigLoader;

import java.lang.reflect.Field;
import java.util.*;

public class DI {

    private static DI instance;
    private Map<Class<?>, Object> container = new HashMap<>();
    private Set<Class<?>> beans = new HashSet<>();

    public static DI getInstance() {
        if (instance == null) {
            instance = new DI();
            instance.injectDependencies();
        }
        return instance;
    }

    private void injectDependencies() {

        Reflections reflections = new Reflections("com");
        beans.addAll(reflections.getTypesAnnotatedWith(Repository.class));
        beans.addAll(reflections.getTypesAnnotatedWith(Service.class));
        beans.addAll(reflections.getTypesAnnotatedWith(Controller.class));
        beans.addAll(reflections.getTypesAnnotatedWith(Configuration.class));

        List<Class<?>> list = new ArrayList<>(beans);
        list.sort(Comparator.comparing(Class::getName));

        for (Class<?> bean : list) {
            makeInstance(bean);
        }

        for (Class<?> bean : list) {
            injectFields(bean);
        }

    }

    private void makeInstance(Class<?> bean) {
        try {

            Object instance = bean.getDeclaredConstructor().newInstance();

            if (bean.isAnnotationPresent(Configuration.class)) {
                ConfigLoader.loadConfiguration(instance);
            }

            container.put(bean, instance);

            for (Class<?> iface : bean.getInterfaces()) {
                container.put(iface, instance);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void injectFields(Class<?> bean) {

        Object instance = container.get(bean);

        for (Field field : getAllFields(bean)) {
            if (!field.isAnnotationPresent(Autowired.class)) continue;

            Class<?> fieldType = field.getType();
            Object dependency = container.get(fieldType);

            if (dependency == null) throw new RuntimeException("dependency object is null");

            field.setAccessible(true);
            try {
                field.set(instance, dependency);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private List<Field> getAllFields(Class<?> type) {
        List<Field> fields = new ArrayList<>();
        while (type != null) {
            fields.addAll(Arrays.asList(type.getDeclaredFields()));
            type = type.getSuperclass();
        }
        return fields;
    }

    public <T> T get(Class<T> type) {
        return (T) container.get(type);
    }

}
