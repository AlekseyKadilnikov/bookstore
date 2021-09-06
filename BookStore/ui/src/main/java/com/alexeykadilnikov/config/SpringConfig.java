package com.alexeykadilnikov.config;

import com.alexeykadilnikov.BookStore;
import com.alexeykadilnikov.controller.ControllerUtils;
import com.alexeykadilnikov.view.menu.MenuController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:bookstore.yml")
public class SpringConfig {
    @Bean
    public ControllerUtils controllerUtils() {
        return new ControllerUtils();
    }
    @Bean
    public MenuController mc() {
        return new MenuController();
    }
    @Bean
    public BookStore bookStore() {
        return new BookStore(controllerUtils(), mc());
    }
}
