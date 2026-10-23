package com.example.tasktracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the k8s-task-tracker application.
 *
 * @SpringBootApplication combines three annotations:
 *   - @Configuration:       Marks this class as a source of bean definitions.
 *   - @EnableAutoConfiguration: Tells Spring Boot to auto-configure beans based on
 *                               the dependencies on the classpath (e.g., embedded Tomcat).
 *   - @ComponentScan:       Scans this package and sub-packages for @Component,
 *                           @Service, @Controller, etc.
 */
@SpringBootApplication
public class K8sTaskTrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(K8sTaskTrackerApplication.class, args);
    }
}
