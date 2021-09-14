package com.alexeykadilnikov.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:bookstore.yml")
@ComponentScan("com.alexeykadilnikov")
public class SpringConfig {
}
