/*  © 2022 */
package com.log.analyzer.entities;

import static org.springframework.util.Assert.notNull;

import com.log.analyzer.beans.LogEvent;
import com.log.analyzer.beans.LogRecord;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "EVENTS")
public class Event {

  @Id
  @Column(name = "ID")
  private String id;

  @Column(name = "ALERT")
  private boolean alert;

  @Column(name = "DURATION")
  private long duration;

  @Column(name = "LOG_TYPE")
  private String type;

  @Column(name = "HOST")
  private String host;

  protected Event() {}

  public Event(final LogRecord logRecord) {

    notNull(logRecord, "Trying to create Event with null parameter (logRecord)");

    final LogEvent startEvent = logRecord.getStartEvent();
    notNull(startEvent, "Trying to create Event with null eventLog (startEvent)");

    id = startEvent.getId();
    alert = logRecord.isSlow();
    duration = logRecord.getDuration();
    type = startEvent.getType();
    host = startEvent.getHost();
  }

  /** @return the duration */
  public long getDuration() {
    return duration;
  }

  /** @return the host */
  public String getHost() {
    return host;
  }

  /** @return the id */
  public String getId() {
    return id;
  }

  /** @return the type */
  public String getType() {
    return type;
  }

  /** @return the alert */
  public boolean isAlert() {
    return alert;
  }

  /** @param alert the alert to set */
  public void setAlert(final boolean alert) {
    this.alert = alert;
  }

  /** @param duration the duration to set */
  public void setDuration(final long duration) {
    this.duration = duration;
  }

  /** @param host the host to set */
  public void setHost(final String host) {
    this.host = host;
  }

  /** @param id the id to set */
  public void setId(final String id) {
    this.id = id;
  }

  /** @param type the type to set */
  public void setType(final String type) {
    this.type = type;
  }
}
