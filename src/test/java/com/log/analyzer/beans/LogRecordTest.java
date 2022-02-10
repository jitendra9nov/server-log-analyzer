/*  © 2022 */
package com.log.analyzer.beans;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/** @author jitendrabhadouriya */
class LogRecordTest {

  @Test
  void testLogRecord_Duration() {
    LogEvent startEvent = new LogEvent("first", State.STARTED, 1234567, "1234", "LOG");
    LogEvent endEvent = new LogEvent("first", State.FINISHED, 1234570, "1234", "LOG");

    LogRecord record = new LogRecord(startEvent, endEvent);

    assertEquals(3, record.getDuration());
  }

  @Test
  void testLogRecord_Slow() {
    LogEvent startEvent = new LogEvent("first", State.STARTED, 1234567, "1234", "LOG");
    LogEvent endEvent = new LogEvent("first", State.FINISHED, 1234572, "1234", "LOG");

    LogRecord record = new LogRecord(startEvent, endEvent);

    assertTrue(record.isSlow());
  }

  @Test
  void testLogRecord_Fast() {
    LogEvent startEvent = new LogEvent("first", State.STARTED, 1234567, "1234", "LOG");
    LogEvent endEvent = new LogEvent("first", State.FINISHED, 1234566, "1234", "LOG");

    LogRecord record = new LogRecord(startEvent, endEvent);

    assertFalse(record.isSlow());
  }

  @Test
  void testLogRecord_Partial_Duration() {
    LogEvent endEvent = new LogEvent("first", State.FINISHED, 1234570, "1234", "LOG");

    LogRecord record = new LogRecord(null, endEvent);

    assertEquals(0, record.getDuration());
  }

  @Test
  void testLogRecord_Partial_State() {
    LogEvent startEvent = new LogEvent("first", State.STARTED, 1234567, "1234", "LOG");

    LogRecord record = new LogRecord(startEvent, null);

    assertFalse(record.isSlow());
  }
}
