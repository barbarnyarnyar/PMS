package com.tolimoli.pms.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tolimoli.pms.dto.response.ApiResponse;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * HelloWorld Controller for testing API endpoints
 * 
 * Provides basic endpoints to verify the PMS application is running
 */
@RestController
@RequestMapping("/api/hello")
@CrossOrigin(origins = "*")
public class HelloWorldController {

    /**
     * Simple hello world endpoint
     */
    @GetMapping
    public ResponseEntity<ApiResponse<String>> hello() {
        String message = "Hello World from Tolimoli PMS!";
        ApiResponse<String> response = new ApiResponse<>("success", "Hello World message retrieved successfully",
                message);
        return ResponseEntity.ok(response);
    }

    /**
     * Hello with custom name parameter
     */
    @GetMapping("/{name}")
    public ResponseEntity<ApiResponse<String>> helloWithName(@PathVariable String name) {
        String message = "Hello " + name + " from Tolimoli PMS!";
        ApiResponse<String> response = new ApiResponse<>("success", "Personalized hello message retrieved successfully",
                message);
        return ResponseEntity.ok(response);
    }

    /**
     * Hello with query parameter
     */
    @GetMapping("/greet")
    public ResponseEntity<ApiResponse<String>> greetWithQuery(
            @RequestParam(value = "name", defaultValue = "Guest") String name,
            @RequestParam(value = "greeting", defaultValue = "Hello") String greeting) {
        String message = greeting + " " + name + "! Welcome to Tolimoli PMS.";
        ApiResponse<String> response = new ApiResponse<>("success", "Custom greeting message retrieved successfully",
                message);
        return ResponseEntity.ok(response);
    }

    /**
     * POST endpoint for hello with request body
     */
    @PostMapping("/message")
    public ResponseEntity<ApiResponse<Map<String, Object>>> createHelloMessage(
            @RequestBody HelloRequest request) {
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("originalMessage", request.getMessage());
        responseData.put("response", "Hello! Your message was: " + request.getMessage());
        responseData.put("timestamp", LocalDateTime.now());
        responseData.put("from", "Tolimoli PMS System");

        ApiResponse<Map<String, Object>> response = new ApiResponse<>("success", "Hello message processed successfully",
                responseData);
        return ResponseEntity.ok(response);
    }

    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<ApiResponse<Map<String, Object>>> healthCheck() {
        Map<String, Object> healthData = new HashMap<>();
        healthData.put("status", "UP");
        healthData.put("service", "Tolimoli PMS");
        healthData.put("version", "1.0.0");
        healthData.put("timestamp", LocalDateTime.now());
        healthData.put("message", "Service is running properly");

        ApiResponse<Map<String, Object>> response = new ApiResponse<>("success", "Health check completed successfully",
                healthData);
        return ResponseEntity.ok(response);
    }

    /**
     * Request DTO for POST endpoint
     */
    public static class HelloRequest {
        private String message;
        private String sender;

        public HelloRequest() {
        }

        public HelloRequest(String message, String sender) {
            this.message = message;
            this.sender = sender;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getSender() {
            return sender;
        }

        public void setSender(String sender) {
            this.sender = sender;
        }
    }
}