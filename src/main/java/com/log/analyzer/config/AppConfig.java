/*  © 2022 */
package com.log.analyzer.config;

import static java.util.concurrent.Executors.newFixedThreadPool;

import java.util.concurrent.ExecutorService;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * This class provides spring configurations that a class declares one or more @Bean methods and may
 * be processed by the Spring container to generate bean definitions and service requests for those
 * beans at runtime
 *
 * @author jitendrabhadouriya
 */
@Configuration
public class AppConfig {

  @Value("${executor.thread.number:5}")
  private int numberOfThreads;

  @Value("${spring.datasource.driver-class-name:#{null}}")
  private String driverClass;

  @Value("${spring.datasource.url:#{null}}")
  private String url;

  @Value("${spring.datasource.username:#{null}}")
  private String userName;

  @Value("${spring.datasource.password:#{null}}")
  private String pwd;

  /**
   * This is custom bean for {@code DataSource} class
   *
   * @return
   */
  @Bean
  public DataSource dataSource() {

    return DataSourceBuilder.create()
        .driverClassName(driverClass)
        .url(url)
        .username(userName)
        .password(pwd)
        .build();
  }

  /**
   * This provides bean for {@code ExecutorService} class
   *
   * @return
   */
  @Bean
  public ExecutorService executorService() {
    return newFixedThreadPool(numberOfThreads);
  }
}
