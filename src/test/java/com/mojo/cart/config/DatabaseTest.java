package com.mojo.cart.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class DatabaseTest {

    @Test
    public void contextLoads() {
        // This test will verify that the Spring context loads successfully
        // including the database connection
    }
}
