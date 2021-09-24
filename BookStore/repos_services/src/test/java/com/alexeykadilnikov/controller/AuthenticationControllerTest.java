package com.alexeykadilnikov.controller;

import com.alexeykadilnikov.config.TestConfig;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
public class AuthenticationControllerTest {

}
