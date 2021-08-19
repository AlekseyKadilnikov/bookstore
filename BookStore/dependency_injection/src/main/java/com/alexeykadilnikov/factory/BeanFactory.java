package com.alexeykadilnikov.factory;

import com.alexeykadilnikov.annotation.InjectBean;
import com.alexeykadilnikov.configurator.BeanConfigurator;
import com.alexeykadilnikov.configurator.JavaBeanConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.stream.Collectors;

public class BeanFactory {
    private static final Logger logger = LoggerFactory.getLogger(BeanFactory.class);

    private static BeanFactory instance;
    private final BeanConfigurator beanConfigurator;

    private BeanFactory() {
        this.beanConfigurator = new JavaBeanConfigurator("com.alexeykadilnikov");
    }

    public static BeanFactory getInstance() {
        if(instance == null) {
            instance = new BeanFactory();
        }
        return instance;
    }


    public <T> T getBean(Class<T> clazz) {
        Class<? extends  T> implementationClass = clazz;
        if(implementationClass.isInterface()) {
            implementationClass = beanConfigurator.getImplementationClass(implementationClass);
        }

        try {
            T bean = implementationClass.getDeclaredConstructor().newInstance();

            for(Field field : Arrays.stream(implementationClass.getDeclaredFields())
                    .filter(field -> field.isAnnotationPresent(InjectBean.class))
                    .collect(Collectors.toList())) {
                field.setAccessible(true);
                field.set(bean, instance.getBean(field.getType()));
            }

            return bean;

        } catch (InstantiationException e) {
            logger.error("InstantiationException");
        } catch (IllegalAccessException e) {
            logger.error("IllegalAccessException");
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            logger.error("InvocationTargetException");
        } catch (NoSuchMethodException e) {
            logger.error("NoSuchMethodException");
            e.printStackTrace();
        }
        return null;
    }
}
