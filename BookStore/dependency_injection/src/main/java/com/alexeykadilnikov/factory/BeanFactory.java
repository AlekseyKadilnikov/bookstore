package com.alexeykadilnikov.factory;

import com.alexeykadilnikov.configurator.BeanConfigurator;
import com.alexeykadilnikov.context.ApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class BeanFactory {
    private static final Logger logger = LoggerFactory.getLogger(BeanFactory.class);

    private final List<BeanConfigurator> configurators = new ArrayList<>();
    private final ApplicationContext context;

    public BeanFactory(ApplicationContext context) {
        this.context = context;
        try {
            for (Class<? extends BeanConfigurator> aClass : context.getConfig().getScanner().getSubTypesOf(BeanConfigurator.class)) {
                configurators.add(aClass.getDeclaredConstructor().newInstance());
            }
        } catch (InstantiationException e) {
            logger.error("InstantiationException");
        } catch (IllegalAccessException e) {
            logger.error("IllegalAccessException");
        } catch (InvocationTargetException e) {
            logger.error("InvocationTargetException");
        } catch (NoSuchMethodException e) {
            logger.error("NoSuchMethodException");
        }
    }

    public <T> T createBean(Class<T> implClass) {

        try {
            T bean = implClass.getDeclaredConstructor().newInstance();

            configurators.forEach(objectConfigurator -> objectConfigurator.configure(bean, context));

            return bean;

        } catch (InstantiationException e) {
            logger.error("InstantiationException");
        } catch (IllegalAccessException e) {
            logger.error("IllegalAccessException");
        } catch (InvocationTargetException e) {
            logger.error("InvocationTargetException");
        } catch (NoSuchMethodException e) {
            logger.error("NoSuchMethodException");
        }
        return null;
    }
}
