/*  © 2022 */
package com.log.analyzer.services;

import static org.mockito.Mockito.verify;

import com.log.analyzer.beans.LogEvent;
import com.log.analyzer.repository.EventRepository;
import java.io.File;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/** @author jitendrabhadouriya */
class LogServiceTest {

  @InjectMocks LogService logService;

  @Mock EventRepository eventRepository;

  @Mock private ExecutorService executorService;

  @Mock Map<String, LogEvent> eventLogsMap;

  @BeforeEach
  void initService() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testInstigate() {

    File file = new File("src/test/resources/logfile.txt");
    String absolutePath = file.getAbsolutePath();
    logService.instigate(absolutePath);

    verify(executorService).shutdown();
  }

  @Test
  void testInstigate_Exception() {

    logService.instigate("src/test/resources/logfile.txt");

    verify(executorService).shutdown();
  }
}
