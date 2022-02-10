/*  © 2022 */
package com.log.analyzer.repository;

import com.log.analyzer.entities.Event;
import org.springframework.data.repository.CrudRepository;

/**
 * This is Repository class which provides CRUD operations for {@code Event} entity
 *
 * @author jitendrabhadouriya
 */
public interface EventRepository extends CrudRepository<Event, String> {}
