/*  © 2022 */
package com.log.analyzer.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is Utility class containing reusable utility methods to process log analysis.
 *
 * @author jitendrabhadouriya
 */
public class LogUtil {

  private static final Logger LOGGER = LoggerFactory.getLogger(LogUtil.class);

  private static final ObjectMapper MAPPER = new ObjectMapper();

  private LogUtil() {

    throw new IllegalStateException("Utility class should not be instanciated");
  }

  /**
   * This method converts Java Beans into Json String by serialising the object
   *
   * @param obj - object to be converted into json string
   * @param isPretty - true if want to format json; else false
   * @return
   */
  public static String writeValueAsString(final Object obj, final boolean isPretty) {

    String stringJson = null;

    try {
      final ObjectWriter objWriter =
          isPretty ? MAPPER.writerWithDefaultPrettyPrinter() : MAPPER.writer();

      stringJson = objWriter.writeValueAsString(obj);
    } catch (final JsonProcessingException e) {
      LOGGER.warn("Failed while serializing given Java value as a String", e);
    }
    return stringJson;
  }

  /**
   * This method deserializes the Json Content from given JSON Object into the Passed reference
   * object
   *
   * @param obj - object containing json string
   * @param type - type of Java Bean in which Json object needs to be converted
   * @return
   */
  public static <T> T convert(final Object obj, final Class<T> type) {

    T objBean = null;

    try {
      objBean = MAPPER.readValue(writeValueAsString(obj, false), type);

    } catch (final JsonProcessingException e) {
      LOGGER.warn("Failed while deserializing JSON content from given JSON content String", e);
    }
    return objBean;
  }

  public static ObjectMapper getObjectMapper() {
    return MAPPER;
  }

  /**
   * This method deserializes the Json Content from given JSON Object into the Passed reference
   * object
   *
   * @param obj - json string
   * @param type - type of Java Bean in which Json object needs to be converted
   * @return
   */
  public static <T> Optional<T> convert(final String jsonString, final Class<T> type) {

    Optional<T> objBean = Optional.empty();

    try {
      objBean = Optional.of(MAPPER.readValue(jsonString, type));

    } catch (final JsonProcessingException e) {
      LOGGER.warn("Failed while deserializing JSON content from given JSON content String", e);
    }
    return objBean;
  }
}
