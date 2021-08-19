package com.alexeykadilnikov.configurator;

import com.alexeykadilnikov.annotation.InjectBean;
import com.alexeykadilnikov.context.ApplicationContext;
import com.alexeykadilnikov.factory.BeanFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

public class InjectBeanAnnotationBeanConfigurator implements BeanConfigurator {
    private static final Logger logger = LoggerFactory.getLogger(InjectBeanAnnotationBeanConfigurator.class);

    @Override
    public void configure(Object t, ApplicationContext context) {
        try {
            for(Field field : t.getClass().getDeclaredFields()) {
                if(field.isAnnotationPresent(InjectBean.class)) {
                    field.setAccessible(true);
                    Object bean = context.getBean(field.getType());
                    field.set(t, bean);
                }
            }
        } catch (IllegalAccessException e) {
            logger.error("IllegalAccessException");
        }
    }
}
