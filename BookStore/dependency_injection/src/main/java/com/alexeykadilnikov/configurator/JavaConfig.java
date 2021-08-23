package com.alexeykadilnikov.configurator;

import org.reflections.Reflections;

import java.util.Set;

public class JavaConfig implements Config {
    private final Reflections scanner;

    public JavaConfig(String packageToScan) {
        this.scanner = new Reflections(packageToScan);
    }

    @Override
    public <T> Class<? extends T> getImplClass(Class<T> ifc) {
        Set<Class<? extends T>> classes = scanner.getSubTypesOf(ifc);
        if(classes.size() != 1) {
            throw new RuntimeException(ifc + " has 0 or more than 1 impl!");
        }

        return classes.iterator().next();
    }

    @Override
    public Reflections getScanner() {
        return scanner;
    }
}
