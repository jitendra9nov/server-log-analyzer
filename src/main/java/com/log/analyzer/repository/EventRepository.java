/*  © 2022 */
package com.log.analyzer.repository;

import com.log.analyzer.entities.Event;
import org.springframework.data.repository.CrudRepository;

public interface EventRepository extends CrudRepository<Event, String> {}
