package com.alexeykadilnikov.context;

import com.alexeykadilnikov.Singleton;
import com.alexeykadilnikov.configurator.Config;
import com.alexeykadilnikov.factory.BeanFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ApplicationContext {
    private BeanFactory factory;
    private final Map<Class, Object> cache = new ConcurrentHashMap<>();
    private final Config config;

    public ApplicationContext(Config config) {
        this.config = config;

    }

    public Config getConfig() {
        return config;
    }

    public void setFactory(BeanFactory factory) {
        this.factory = factory;
    }

    public <T> T getBean(Class<T> type) {
        Class<? extends  T> implClass = type;
        if(cache.containsKey(type)) {
            return (T) cache.get(type);
        }

        if(implClass.isInterface()) {
            implClass = config.getImplClass(implClass);
        }
        T t = factory.createBean(implClass);

        if(implClass.isAnnotationPresent(Singleton.class)) {
            cache.put(type, t);
        }

        return t;
    }
}
