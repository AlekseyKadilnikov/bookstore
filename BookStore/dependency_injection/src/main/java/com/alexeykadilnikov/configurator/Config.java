package com.alexeykadilnikov.configurator;

import org.reflections.Reflections;

public interface Config {
    <T> Class<? extends T> getImplClass(Class<T> interfaceClass);

    Reflections getScanner();
}
