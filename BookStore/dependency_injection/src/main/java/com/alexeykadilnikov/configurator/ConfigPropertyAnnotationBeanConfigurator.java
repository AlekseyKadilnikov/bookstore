package com.alexeykadilnikov.configurator;

import com.alexeykadilnikov.ConfigProperty;
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

    @Override
    public void configure(Object t, ApplicationContext context) {
        Class<?> implClass = t.getClass();
        try {
            for (Field field : implClass.getDeclaredFields()) {
                ConfigProperty annotation = field.getAnnotation(ConfigProperty.class);

                FileInputStream fis;
                if (annotation != null) {
                    String configName = annotation.configName();
                    if(configName.isEmpty()) {
                        fis = new FileInputStream("properties\\application.yml");
                        property.load(fis);
                        configName = property.getProperty("configPath");
                    }
                    else {
                        configName = annotation.configName();
                    }
                    fis = new FileInputStream(configName);
                    property.load(fis);
                    fis.close();

                    String propertyName = annotation.propertyName().isEmpty()
                            ? property.getProperty(field.getDeclaringClass().getSimpleName() + "." + field.getName())
                            : property.getProperty(annotation.propertyName());

                    Class<?> type = annotation.type();

                    Object value = propertyName;

                    if(type == Class.class) {
                        if(field.getType() != Class.class) {
                            value = toObject(field.getType(), propertyName);
                        }
                    } else {
                        value = toObject(type, propertyName);
                    }

                    field.setAccessible(true);
                    field.set(t, value);
                }
            }
        } catch (IllegalAccessException e) {
            logger.error("IllegalAccessException");
        } catch (FileNotFoundException e) {
            logger.error("FileNotFoundException");
        } catch (IOException e) {
            logger.error("IOException");
        } catch (NumberFormatException e) {
            logger.error("NumberFormatException");
        } catch (IllegalArgumentException e) {
            logger.error("IllegalArgumentException");
        }
    }

    private Object toObject( Class<?> clazz, String value ) throws NumberFormatException {
        if( Boolean.class == clazz || boolean.class == clazz ) return Boolean.parseBoolean( value );
        if( Byte.class == clazz || byte.class == clazz ) return Byte.parseByte( value );
        if( Short.class == clazz || short.class == clazz ) return Short.parseShort( value );
        if( Integer.class == clazz || int.class == clazz ) return Integer.parseInt( value );
        if( Long.class == clazz || long.class == clazz ) return Long.parseLong( value );
        if( Float.class == clazz || float.class == clazz ) return Float.parseFloat( value );
        if( Double.class == clazz || double.class == clazz ) return Double.parseDouble( value );
        return value;
    }
}
