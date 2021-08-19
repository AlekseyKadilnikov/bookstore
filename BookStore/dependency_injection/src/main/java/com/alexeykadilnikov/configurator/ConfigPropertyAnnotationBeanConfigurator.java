package com.alexeykadilnikov.configurator;

import com.alexeykadilnikov.annotation.ConfigProperty;
import com.alexeykadilnikov.context.ApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Properties;

public class ConfigPropertyAnnotationBeanConfigurator implements BeanConfigurator {
    private static final Logger logger = LoggerFactory.getLogger(ConfigPropertyAnnotationBeanConfigurator.class);

    Properties property = new Properties();

    public ConfigPropertyAnnotationBeanConfigurator() {
        try {
            FileInputStream fis = new FileInputStream("properties\\bookstore.yml");
            property.load(fis);
            fis.close();
        } catch (FileNotFoundException e) {
            logger.error("FileNotFoundException");
        } catch (IOException e) {
            logger.error("IOException");
        }
    }

    @Override
    public void configure(Object t, ApplicationContext context) {
        Class<?> implClass = t.getClass();
        try {
            for (Field field : implClass.getDeclaredFields()) {
                ConfigProperty annotation = field.getAnnotation(ConfigProperty.class);

                if (annotation != null) {
                    String value = annotation.value().isEmpty() ? property.getProperty(field.getName()) : property.getProperty(annotation.value());
                    field.setAccessible(true);
                    field.set(t, value);
                }
            }
        } catch (IllegalAccessException e) {
            logger.error("IllegalAccessException");
        }
    }
}
