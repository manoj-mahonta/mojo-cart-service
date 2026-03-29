package com.mojo.cart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@RestController
@RequestMapping("/api/health")
public class HealthController {

    @Autowired
    private DataSource dataSource;

    @GetMapping
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Application is running");
    }

    @GetMapping("/database")
    public ResponseEntity<String> databaseHealth() {
        try (Connection connection = dataSource.getConnection()) {
            if (connection.isValid(2)) {
                return ResponseEntity.ok("Database connection is healthy");
            } else {
                return ResponseEntity.status(503).body("Database connection is invalid");
            }
        } catch (SQLException e) {
            return ResponseEntity.status(503).body("Database connection failed: " + e.getMessage());
        }
    }
}
