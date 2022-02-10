/*  © 2022 */
package com.log.analyzer.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/** @author jitendrabhadouriya */
@SpringBootTest
class LogServiceTest {

  @Autowired LogService logService;

  @Test
  void testInstigate() {
    logService.instigate(
        "/Users/jitendrabhadouriya/git/server-log-analyzer/src/test/resources/logfile.txt");
  }
}
