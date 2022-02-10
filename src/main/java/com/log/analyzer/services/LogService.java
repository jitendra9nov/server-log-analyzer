/*  © 2022 */
package com.log.analyzer.services;

import static com.log.analyzer.utils.LogUtil.convert;
import static com.log.analyzer.utils.LogUtil.writeValueAsString;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.io.FileUtils.lineIterator;

import com.log.analyzer.beans.LogEvent;
import com.log.analyzer.beans.LogRecord;
import com.log.analyzer.beans.State;
import com.log.analyzer.entities.Event;
import com.log.analyzer.repository.EventRepository;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import org.apache.commons.io.LineIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * This service provides methods to read log file, analyze them and then alert the slow events in
 * the logs. After that this connects to the database in order to store the log record with specific
 * information in the database
 *
 * @author jitendrabhadouriya
 */
@Service
public class LogService {

  private static final Logger LOGGER = LoggerFactory.getLogger(LogService.class);

  @Value("${executor.thread.number:5}")
  private int numberOfThreads;

  private Map<String, LogEvent> eventLogsMap;

  @Autowired private ExecutorService executorService;

  @Autowired private EventRepository eventRepository;

  /**
   * This method is entry for the log analysis. This takes the log file as input, analyzes the logs
   * and then alerts the slow logs and store log record in database.
   *
   * @param filePath - path of the text file containing the log information
   */
  public void instigate(final String filePath) {
    try {
      eventLogsMap = new HashMap<>(100);

      /* starting process to read and process the log file */

      processLogsFile(filePath);

      executorService.shutdown();

    } catch (final IOException e) {
      LOGGER.error("There was an error processing event logs file");
    }
  }

  /** @param eventLog */
  private void addNewEventId(final LogEvent eventLog) {
    eventLogsMap.put(eventLog.getId(), eventLog);
  }

  /**
   * This method opens a future task and waits till both the events arrive. After that it will check
   * for the slow event and flag the alert in the log. As a final step it will store the Log record
   * in the database with duration and slow flag
   *
   * @param currentEventLog- LogEvent instance
   */
  private void analyzeAndProcessLogAsyn(final LogEvent currentEventLog) {
    CompletableFuture.runAsync(
        () -> {
          final LogRecord logRecord = buildLogRecord(currentEventLog);

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

  /**
   * This method builds {@code Event} entity from {@code LogRecord}
   *
   * @param logRecord- LogRecord instance
   * @return
   */
  private Event buildEvent(final LogRecord logRecord) {
    return new Event(logRecord);
  }

  /**
   * This method builds log record by combining both the start and end events
   *
   * @param currentLogEvent-LogEvent instance
   * @return
   */
  private LogRecord buildLogRecord(final LogEvent currentLogEvent) {
    LogRecord recordLog;

    if (currentLogEvent.getState() == State.STARTED) {
      recordLog = new LogRecord(currentLogEvent, eventLogsMap.get(currentLogEvent.getId()));
    } else {
      recordLog = new LogRecord(eventLogsMap.get(currentLogEvent.getId()), currentLogEvent);
    }

    return recordLog;
  }

  /**
   * this method checks if the event id is already present in the database
   *
   * @param eventId
   * @return
   */
  private boolean isIdAlreadyPresent(final String eventId) {
    return eventLogsMap.containsKey(eventId);
  }

  /**
   * This method collects both the log events i.e. start and finish. After that it will analyse the
   * slowness to flag the alert before storing in database
   *
   * @param eventLog - LogEvent instance
   */
  private void processEventLog(final LogEvent eventLog) {
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("Processing LogEvent:: {}", writeValueAsString(eventLog, false));
    }

    if (isIdAlreadyPresent(eventLog.getId())) {
      analyzeAndProcessLogAsyn(eventLog);
    } else {
      addNewEventId(eventLog);
    }
  }

  /**
   * This method processes the Log Event by converting log string into the java object
   *
   * @param logLine - string containing log event in json format
   */
  private void processLogLine(final String logLine) {
    /* convert the string Json into LogEvent object and process that if available */
    final Optional<LogEvent> eventLogOptional = convert(logLine, LogEvent.class);
    eventLogOptional.ifPresent(this::processEventLog);
  }

  /**
   * This method loads log file line by line and then sends processes the event log one by one
   *
   * @param filePath- path of the text file containing the log information
   * @throws IOException
   */
  private void processLogsFile(final String filePath) throws IOException {

    /* starting process to read and process the log file */
    try (LineIterator it = lineIterator(new File(filePath), UTF_8.name())) {
      while (it.hasNext()) {
        processLogLine(it.nextLine());
      }
    }
  }

  /** @param currentEventLog */
  private void removeEventId(final LogEvent currentEventLog) {
    eventLogsMap.remove(currentEventLog.getId());
  }
}
