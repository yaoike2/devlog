package com.qianlispace.backend.service.impl;

import com.qianlispace.backend.dto.LogEntryCreateRequest;
import com.qianlispace.backend.dto.LogEntryResponse;
import com.qianlispace.backend.entity.LogEntry;
import com.qianlispace.backend.repository.LogEntryRepository;
import com.qianlispace.backend.service.LogEntryService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class LogEntryServiceImpl implements LogEntryService {

    private final LogEntryRepository repository;

    public LogEntryServiceImpl(LogEntryRepository repository) {
        this.repository = repository;
    }

    @Override
    public LogEntryResponse create(LogEntryCreateRequest request) {
        LogEntry entry = new LogEntry();
        entry.setContent(request.getContent());
        entry.setTags(request.getTags());
        LogEntry saved = repository.save(entry);
        return LogEntryResponse.from(saved);
    }

    @Override
    public LogEntryResponse getById(Long id) {
        LogEntry entry = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("日志条目不存在，id=" + id));
        return LogEntryResponse.from(entry);
    }
}
