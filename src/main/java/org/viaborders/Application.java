package org.viaborders;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * Java and Spring Boot main application class.
 */
@SpringBootApplication
public class Application {

  /**
   * Spring Boot application starts here.
   *
   * <p><em>External properties, logging, and other features of Spring Boot
   * are installed in the context by default only if you use SpringApplication to create it.</em>
   *
   * @param args Java command-line arguments.
   */

  static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

}
