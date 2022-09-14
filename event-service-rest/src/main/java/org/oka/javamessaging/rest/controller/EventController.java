package org.oka.javamessaging.rest.controller;

import lombok.RequiredArgsConstructor;
import org.oka.javamessaging.model.Event;
import org.oka.javamessaging.service.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @GetMapping(value = "/events/{id}")
    public ResponseEntity<Event> getEvent(@PathVariable("id") final Long id) {
        Event byId = eventService.getEvent(id);

        return new ResponseEntity<>(byId, OK);
    }

    @GetMapping(value = "/events")
    public ResponseEntity<List<Event>> getEvents() {

        return new ResponseEntity<>(eventService.getAllEvents(), OK);
    }

    @PostMapping(value = "/events")
    public ResponseEntity<Void> createEvent(@RequestBody Event event) {
        eventService.createEvent(event);

        return new ResponseEntity<>(OK);
    }

    @PutMapping(value = "/events/{id}")
    public ResponseEntity<Void> updateEvent(@PathVariable("id") final Long id, @RequestBody Event event) {
        eventService.updateEvent(id, event);

        return new ResponseEntity<>(OK);
    }

    @DeleteMapping(value = "/events/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable("id") final Long id) {
        eventService.deleteEvent(id);

        return new ResponseEntity<>(OK);
    }
}
