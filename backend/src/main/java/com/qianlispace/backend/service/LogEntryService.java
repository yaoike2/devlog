package com.qianlispace.backend.service;

import com.qianlispace.backend.dto.LogEntryCreateRequest;
import com.qianlispace.backend.dto.LogEntryResponse;
import org.springframework.data.domain.Page;

public interface LogEntryService {

    LogEntryResponse create(LogEntryCreateRequest request);

    LogEntryResponse getById(Long id);

    Page<LogEntryResponse> list(int page, int size);
}
