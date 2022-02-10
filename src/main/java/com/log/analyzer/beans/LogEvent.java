/*  © 2022 */
package com.log.analyzer.beans;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This bean contains information about the log event i.e. id, state, time etc. This represent one
 * line item in the log file
 *
 * @author jitendrabhadouriya
 */
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class LogEvent {

  private final String id;
  private final State state;
  private final long timestamp;

  private final String type;
  private final String host;

  @JsonCreator
  public LogEvent(
      @JsonProperty("id") final String id,
      @JsonProperty("state") final State state,
      @JsonProperty("timestamp") final long timestamp,
      @JsonProperty("type") final String type,
      @JsonProperty("host") final String host) {

    this.id = id;
    this.state = state;
    this.timestamp = timestamp;
    this.type = type;
    this.host = host;
  }

  /** @return the host */
  public String getHost() {
    return host;
  }

  /** @return the id */
  public String getId() {
    return id;
  }

  /** @return the state */
  public State getState() {
    return state;
  }

  /** @return the timestamp */
  public long getTimestamp() {
    return timestamp;
  }

  /** @return the type */
  public String getType() {
    return type;
  }
}
