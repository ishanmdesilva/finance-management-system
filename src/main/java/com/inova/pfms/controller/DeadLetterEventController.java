package com.inova.pfms.controller;

import com.inova.pfms.service.DeadLetterEventService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller for managing dead letter events.
 */
@RestController
@RequestMapping("/api/dead-letters")
@AllArgsConstructor(onConstructor_ = @__(@Autowired))
public class DeadLetterEventController {

    private final DeadLetterEventService deadLetterEventService;

    /**
     * find all event types
     *
     * @return        -   ResponseEntity
     */
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/event-types")
    public ResponseEntity<Object> findAllEventTypes() {
        return ResponseEntity.status(HttpStatus.OK).body(deadLetterEventService.findAllEventTypes());
    }

    /**
     * search dead letter events
     *
     * @param  all       -   no filtration
     * @param eventType  -   the type of event to filter by (optional)
     * @param startDate  -   the start date for the search range (optional)
     * @param endDate    -   the end date for the search range (optional)
     * @return           -   ResponseEntity
     */
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/search")
    public ResponseEntity<Object> searchEvents(
            @RequestParam Boolean all,
            @RequestParam(required = false) String eventType,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate
    ) {
        Map<String, Object> map = new HashMap<>();
        map.put("all", all);
        map.put("eventType", eventType);
        map.put("startDate", startDate);
        map.put("endDate", endDate);
        return ResponseEntity.status(HttpStatus.OK).body(deadLetterEventService.searchEvent(map));
    }



}
