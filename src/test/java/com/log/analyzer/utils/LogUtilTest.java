/*  © 2022 */
package com.log.analyzer.utils;

import static com.log.analyzer.beans.State.STARTED;
import static com.log.analyzer.utils.LogUtil.writeValueAsString;
import static org.junit.jupiter.api.Assertions.*;

import com.log.analyzer.beans.LogEvent;
import org.junit.jupiter.api.Test;

/** @author jitendrabhadouriya */
class LogUtilTest {

  @Test
  void testWriteValueAsString_Success() {
    final LogEvent event = new LogEvent("1", STARTED, 1234546474, null, null);
    assertNotNull(writeValueAsString(event, false), "Unable to serialising");
  }

  @Test
  void testWriteValueAsString_Failure() {
    assertNotNull(writeValueAsString(null, false), "Unable to serialising");
  }
}
