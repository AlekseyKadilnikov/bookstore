package com.alexeykadilnikov.context;

import com.alexeykadilnikov.configurator.JavaConfig;
import com.alexeykadilnikov.factory.BeanFactory;

public class Application {
    public static ApplicationContext run(String packageToScan) {
        JavaConfig config = new JavaConfig(packageToScan);
        ApplicationContext context = new ApplicationContext(config);
        BeanFactory beanFactory = new BeanFactory(context);
        context.setFactory(beanFactory);
        return context;
    }
}
