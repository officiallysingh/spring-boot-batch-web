package com.ksoot.batch;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
        info =
        @Info(
                title = "Statement API",
                version = "0.0.1"))
public class StatementJobApplication {

  public static void main(String[] args) {
    SpringApplication.run(StatementJobApplication.class, args);
  }
}
