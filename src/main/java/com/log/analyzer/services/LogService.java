/*  © 2022 */
package com.log.analyzer.services;

import static com.log.analyzer.utils.LogUtil.convert;
import static com.log.analyzer.utils.LogUtil.writeValueAsString;

import com.log.analyzer.beans.LogEvent;
import com.log.analyzer.beans.LogRecord;
import com.log.analyzer.beans.State;
import com.log.analyzer.entities.Event;
import com.log.analyzer.repository.EventRepository;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class LogService {

  private static final Logger LOGGER = LoggerFactory.getLogger(LogService.class);

  @Value("${executor.thread.number:5}")
  private int numberOfThreads;

  private Map<String, LogEvent> eventLogsMap;

  @Autowired private ExecutorService executorService;

  @Autowired private EventRepository eventRepository;

  /** @param filePath */
  public void instigate(String filePath) {
    try {
      eventLogsMap = new HashMap<>(100);

      processLogsFile(filePath);
      sleepThread();
      executorService.shutdown();
    } catch (IOException e) {
      LOGGER.error("There was an error processing event logs file");
    }
  }

  /** */
  private void sleepThread() {
    try {
      // This is needed because it seems HSQLDB needs some time to commit the changes
      // to the
      // file before the program finishes.
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      LOGGER.error("There was an error trying to sleep the main thread");
      // Restore interrupted state...
      Thread.currentThread().interrupt();
    }
  }

  /**
   * @param filePath
   * @throws IOException
   */
  private void processLogsFile(String filePath) throws IOException {

    try (LineIterator it =
        FileUtils.lineIterator(new File(filePath), StandardCharsets.UTF_8.name())) {
      while (it.hasNext()) {
        processEventLogLine(it.nextLine());
      }
    }
  }

  private void processEventLogLine(String eventLogLine) {
    Optional<LogEvent> eventLogOptional = convert(eventLogLine, LogEvent.class);
    eventLogOptional.ifPresent(this::processEventLog);
  }

  private void processEventLog(LogEvent eventLog) {
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("Processing LogEvent:: {}", writeValueAsString(eventLog, false));
    }

    if (isIdAlreadyPresent(eventLog.getId())) {
      processEvent(eventLog);
    } else {
      addNewEventId(eventLog);
    }
  }

  /**
   * @param eventId
   * @return
   */
  private boolean isIdAlreadyPresent(String eventId) {
    return eventLogsMap.containsKey(eventId);
  }

  private void addNewEventId(LogEvent eventLog) {
    eventLogsMap.put(eventLog.getId(), eventLog);
  }

  private void processEvent(LogEvent currentEventLog) {
    CompletableFuture.runAsync(
        () -> {
          LogRecord logRecord = buildLogRecord(currentEventLog);

          if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Building LogRecord:: {}", writeValueAsString(logRecord, false));
          }
          if (logRecord.isSlow()) {
            LOGGER.warn(
                "ALERT::: A slow event has been identified with details: {}",
                writeValueAsString(logRecord, false));
          }

          final Event event = buildEvent(logRecord);

          if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Saving Event in the database:: {}", writeValueAsString(event, false));
          }

          eventRepository.save(event);

          if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Event Saved in the database:: {}", writeValueAsString(event, false));
          }

          removeEventId(currentEventLog);
        },
        executorService);
  }

  private Event buildEvent(LogRecord logRecord) {
    return new Event(logRecord);
  }

  /** @param currentEventLog */
  private void removeEventId(LogEvent currentEventLog) {
    eventLogsMap.remove(currentEventLog.getId());
  }

  private LogRecord buildLogRecord(LogEvent currentLogEvent) {
    LogRecord recordLog;

    if (currentLogEvent.getState() == State.STARTED) {
      recordLog = new LogRecord(currentLogEvent, eventLogsMap.get(currentLogEvent.getId()));
    } else {
      recordLog = new LogRecord(eventLogsMap.get(currentLogEvent.getId()), currentLogEvent);
    }

    return recordLog;
  }
}
