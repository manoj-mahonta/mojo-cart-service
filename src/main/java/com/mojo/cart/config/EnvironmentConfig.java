package com.mojo.cart.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource(value = "file:.env", ignoreResourceNotFound = false)
@PropertySource(value = "file:.env.local", ignoreResourceNotFound = true)
@ConfigurationProperties
public class EnvironmentConfig {

    public EnvironmentConfig(Environment environment) {
        // This constructor ensures the environment is available
        // The @PropertySource annotations will load the .env files
    }
}
