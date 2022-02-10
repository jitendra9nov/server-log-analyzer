/*  © 2022 */
package com.log.analyzer.entities;

import static org.springframework.util.Assert.notNull;

import com.log.analyzer.beans.LogEvent;
import com.log.analyzer.beans.LogRecord;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.io.Serializable;

@Entity
@Table(name = "EVENTS")
public class Event implements Serializable {

  /** */
  private static final long serialVersionUID = 5679712301155210345L;

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

  @Transient private transient String dateOfBirthString;

  protected Event() {}

  public Event(LogRecord logRecord) {

    notNull(logRecord, "Trying to create Event with null parameter (logRecord)");

    final LogEvent startEvent = logRecord.getStartEvent();
    notNull(startEvent, "Trying to create Event with null eventLog (startEvent)");

    this.id = startEvent.getId();
    this.alert = logRecord.isSlow();
    this.duration = logRecord.getDuration();
    this.type = startEvent.getType();
    this.host = startEvent.getHost();
  }

  /** @return the id */
  public String getId() {
    return id;
  }

  /** @return the alert */
  public boolean isAlert() {
    return alert;
  }

  /** @return the duration */
  public long getDuration() {
    return duration;
  }

  /** @return the type */
  public String getType() {
    return type;
  }

  /** @return the host */
  public String getHost() {
    return host;
  }

  /** @param id the id to set */
  public void setId(String id) {
    this.id = id;
  }

  /** @param alert the alert to set */
  public void setAlert(boolean alert) {
    this.alert = alert;
  }

  /** @param duration the duration to set */
  public void setDuration(long duration) {
    this.duration = duration;
  }

  /** @param type the type to set */
  public void setType(String type) {
    this.type = type;
  }

  /** @param host the host to set */
  public void setHost(String host) {
    this.host = host;
  }
}
