package com.qianlispace.backend.controller;

import com.qianlispace.backend.common.Result;
import com.qianlispace.backend.dto.LogEntryCreateRequest;
import com.qianlispace.backend.dto.LogEntryResponse;
import com.qianlispace.backend.service.LogEntryService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/logs")
public class LogEntryController {

    private final LogEntryService service;

    public LogEntryController(LogEntryService service) {
        this.service = service;
    }

    @PostMapping
    public Result<LogEntryResponse> create(@Valid @RequestBody LogEntryCreateRequest request) {
        return Result.success(service.create(request));
    }

    @GetMapping("/{id}")
    public Result<LogEntryResponse> getById(@PathVariable Long id) {
        try {
            return Result.success(service.getById(id));
        } catch (EntityNotFoundException e) {
            return Result.notFound(e.getMessage());
        }
    }
}
