package com.alexeykadilnikov.controller;

import com.alexeykadilnikov.dto.RequestDto;
import com.alexeykadilnikov.service.IRequestService;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@NoArgsConstructor
@RequestMapping("requests")
public class RequestController {
    private static final Logger logger = LoggerFactory.getLogger(RequestController.class);

    private IRequestService requestService;

    @Autowired
    public RequestController(IRequestService requestService) {
        this.requestService = requestService;
    }

    @GetMapping("requestsForBook/{id}/{sortBy}")
    @PreAuthorize("hasAuthority('requests:read')")
    public List<RequestDto> getRequestsForBookSortedByCount(@PathVariable("id") long bookId,
                                                            @PathVariable("sortBy") String sortBy,
                                                            @RequestParam("mode") String direction) {
        return requestService.getRequestsForBook(bookId, sortBy, direction);
    }

    @GetMapping()
    @PreAuthorize("hasAuthority('requests:read')")
    public List<RequestDto> getAll() {
        return requestService.getAll();
    }

    @PostMapping()
    @PreAuthorize("hasAuthority('requests:write')")
    public RequestDto save(@RequestBody RequestDto request) {
        return requestService.save(request);
    }
}
