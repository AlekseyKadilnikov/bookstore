package com.alexeykadilnikov.configurator;

import com.alexeykadilnikov.context.ApplicationContext;

public interface BeanConfigurator {
    void configure(Object t, ApplicationContext context);
}
