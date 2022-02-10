/*  © 2022 */
package com.log.analyzer.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * This bean contains information about the log record i.e. combination of log starting as well
 * ending event. This also contains utility method to calculate the duration and event slowness
 *
 * @author jitendrabhadouriya
 */
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class LogRecord {

  private static final int LOG_THRESHOLD = 4;

  private final LogEvent startEvent;
  private final LogEvent endEvent;

  /**
   * @param startEvent
   * @param endEvent
   */
  public LogRecord(final LogEvent startEvent, final LogEvent endEvent) {
    this.startEvent = startEvent;
    this.endEvent = endEvent;
  }

  /**
   * This method
   *
   * @return
   */
  public long getDuration() {

    return null != endEvent && null != startEvent
        ? endEvent.getTimestamp() - startEvent.getTimestamp()
        : 0;
  }

  /** @return the endEvent */
  public LogEvent getEndEvent() {
    return endEvent;
  }

  /** @return the startEvent */
  public LogEvent getStartEvent() {
    return startEvent;
  }

  public boolean isSlow() {
    return getDuration() > LOG_THRESHOLD;
  }
}
