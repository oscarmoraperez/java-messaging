package org.oka.javamessaging.model;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventRepository extends PagingAndSortingRepository<Event, Long> {
    Optional<Event> findByTitle(final String title);
}
