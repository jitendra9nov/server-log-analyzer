/*  © 2022 */
package com.log.analyzer;

import static org.springframework.boot.WebApplicationType.NONE;
import static org.springframework.util.StringUtils.hasLength;

import com.log.analyzer.services.LogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * This is Spring Boot Application class which will start the application using Command Line
 * Arguments
 *
 * @author jitendrabhadouriya
 */
@SpringBootApplication
public class ServerLogAnalyzerApplication implements CommandLineRunner {

  private static final Logger LOGGER = LoggerFactory.getLogger(ServerLogAnalyzerApplication.class);

  /**
   * This is main method to which will be called to start the Spring Boot Application.
   *
   * @param args
   */
  public static void main(final String[] args) {
    /*
     * Since this is not the web application, disabling all the web related
     * components.
     */
    final SpringApplication application = new SpringApplication(ServerLogAnalyzerApplication.class);
    application.setWebApplicationType(NONE);
    application.setHeadless(false);
    application.run(args);
  }

  @Autowired private LogService logService;

  /**
   * This is overridden method of the {@code CommandLineRunner} interface, and takes arguments
   * passed while running the application.
   */
  @Override
  public void run(final String... args) throws Exception {

    String filePath = null;
    try {
      filePath = args[0];
    } catch (final ArrayIndexOutOfBoundsException e) {
      LOGGER.error("File path parameter is missing");
    }
    /* check if file path passed is not empty or null */
    if (hasLength(filePath)) {

      LOGGER.info("Starting processing the Log File for provided path: {}", filePath);

      /*
       * starting the log analysis process by calling instigate method of the
       * LogService
       */
      logService.instigate(filePath);
    }
  }
}
