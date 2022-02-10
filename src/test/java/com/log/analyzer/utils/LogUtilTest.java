/*  © 2022 */
package com.log.analyzer.utils;

import static com.log.analyzer.beans.State.STARTED;
import static com.log.analyzer.utils.LogUtil.convert;
import static com.log.analyzer.utils.LogUtil.writeValueAsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.log.analyzer.beans.LogEvent;
import java.util.Optional;
import org.junit.jupiter.api.Test;

/** @author jitendrabhadouriya */
class LogUtilTest {

  @Test
  void testConvert_Failure() {
    final Optional<LogEvent> obj =
        convert(
            " {\"id\" \"scsmbstgra\", \"state\": \"STARTED\", \"type\": \"APPLICATION_LOG\", \"host\": \"12345\", \"timestamp\": 1491377495212}",
            LogEvent.class);
    assertEquals(obj, Optional.empty());
  }

  @Test
  void testConvert_Success() {
    final Optional<LogEvent> obj =
        convert(
            " {\"id\": \"scsmbstgra\", \"state\": \"STARTED\", \"type\": \"APPLICATION_LOG\", \"host\": \"12345\", \"timestamp\": 1491377495212}",
            LogEvent.class);
    assertNotNull(obj, "Unable to serialising");
  }

  @Test
  void testWriteValueAsString_Failure() {
    assertNotNull(writeValueAsString(null, false), "Unable to serialising");
  }

  @Test
  void testWriteValueAsString_Success() {
    final LogEvent event = new LogEvent("1", STARTED, 1234546474, null, null);
    assertNotNull(writeValueAsString(event, false), "Unable to serialising");
  }
}
