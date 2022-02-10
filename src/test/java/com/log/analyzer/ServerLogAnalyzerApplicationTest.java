/*  © 2022 */
package com.log.analyzer;

import static com.log.analyzer.ServerLogAnalyzerApplication.main;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.log.analyzer.config.AppConfig;
import com.log.analyzer.services.LogService;
import java.io.File;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

@SpringBootTest(classes = ServerLogAnalyzerApplication.class)
@TestPropertySource("classpath:application.properties")
@ContextConfiguration(classes = AppConfig.class, loader = AnnotationConfigContextLoader.class)
class ServerLogAnalyzerApplicationTest {

  @Mock LogService logService;

  @Test
  void testMain_Success() {
    File file = new File("src/test/resources/logfile.txt");
    String absolutePath = file.getAbsolutePath();

    main(new String[] {absolutePath});

    verify(logService, never()).instigate(absolutePath); // TODO Check this
  }

  @Test
  void testMain_Fail() {
    main(new String[] {});
    verify(logService, never()).instigate(null);
  }

  @Test
  void testMain_Unknown() {
    main(new String[] {"something"});
    verify(logService, never()).instigate(null);
  }
}
