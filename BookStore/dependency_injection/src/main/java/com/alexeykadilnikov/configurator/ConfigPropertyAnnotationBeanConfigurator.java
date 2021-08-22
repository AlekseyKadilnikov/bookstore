package com.alexeykadilnikov.configurator;

import com.alexeykadilnikov.ConfigProperty;
import com.alexeykadilnikov.context.ApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;

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

                    String[] props = propertyName.split(",");

                    Object[] values = new Object[props.length];

                    Class<?> type = annotation.type();

                    Class<?> contentClass = null;
                    if(props.length > 1 && type != Class.class) {
                        if(field.getType().isArray()) {
                            type = field.getType().getComponentType();
                        } else {
                            ParameterizedType listType= (ParameterizedType) field.getGenericType();
                            contentClass = (Class<?>) listType.getActualTypeArguments()[0];
                            type = contentClass;
                        }
                    }

                    for(int i = 0; i < props.length; i++) {
                        if(type == Class.class) {
                            if(field.getType() != Class.class) {
                                values[i] = toObject(field.getType(), props[i]);
                            }
                        } else {
                            values[i] = toObject(type, props[i]);
                        }
                    }

                    field.setAccessible(true);
                    if(values.length == 1) {
                        field.set(t, values[0]);
                    } else if (Collection.class.isAssignableFrom(field.getType())) {
                        if(field.getType().equals(Set.class)) {
                            field.set(t, Set.of(values));
                        } else {
                            field.set(t, Arrays.asList(values));
                        }
                    } else {
                        if (Integer.class.equals(type)) {
                            Integer[] arr = new Integer[values.length];
                            for(int i = 0; i < values.length; i++)
                            {
                                arr[i] = (Integer) values[i];
                            }
                            field.set(t, arr);
                        } else if (Boolean.class.equals(type)) {
                            Boolean[] arr = new Boolean[values.length];
                            for(int i = 0; i < values.length; i++)
                            {
                                arr[i] = (Boolean) values[i];
                            }
                            field.set(t, arr);
                        } else if (Byte.class.equals(type)) {
                            Byte[] arr = new Byte[values.length];
                            for(int i = 0; i < values.length; i++)
                            {
                                arr[i] = (Byte) values[i];
                            }
                            field.set(t, arr);
                        } else if (Short.class.equals(type)) {
                            Short[] arr = new Short[values.length];
                            for(int i = 0; i < values.length; i++)
                            {
                                arr[i] = (Short) values[i];
                            }
                            field.set(t, arr);
                        } else if (Float.class.equals(type)) {
                            Float[] arr = new Float[values.length];
                            for(int i = 0; i < values.length; i++)
                            {
                                arr[i] = (Float) values[i];
                            }
                            field.set(t, arr);
                        } else if (Double.class.equals(type)) {
                            Double[] arr = new Double[values.length];
                            for(int i = 0; i < values.length; i++)
                            {
                                arr[i] = (Double) values[i];
                            }
                            field.set(t, arr);
                        } else if (Long.class.equals(type)) {
                            Long[] arr = new Long[values.length];
                            for(int i = 0; i < values.length; i++)
                            {
                                arr[i] = (Long) values[i];
                            }
                            field.set(t, arr);
                        } else {
                            String[] arr = new String[values.length];
                            for(int i = 0; i < values.length; i++)
                            {
                                arr[i] = (String) values[i];
                            }
                            field.set(t, arr);
                        }
                    }
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
