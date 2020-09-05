package com.hassan.islandrank.cleaner;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.hassan.islandrank.Main;


public class Cleaner {

    private HashMap<Class<?>, Set<Field>> toClean = new HashMap<>();

    private Cleaner() {

        //Init clean method onDisable
        // Main.getInstance().onDisable(this::clean);

    }

    public static Cleaner init() {
        return new Cleaner();
    }

    public void registerClass(Class<?> clazz) {

        Set<Field> fields = toClean.putIfAbsent(clazz, new HashSet<>());

        //Loop through fields
        for(Field field : clazz.getFields()) {
            if(Modifier.isStatic(field.getModifiers())) {

                field.setAccessible(true);
                fields.add(field);

            }
        }

    }

    public void clean() {

        toClean.values().forEach(fields -> fields.forEach(field -> {

            try {
                field.set(null, null);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }));

    }


}
