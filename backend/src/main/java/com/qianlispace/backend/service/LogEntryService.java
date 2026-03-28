package com.qianlispace.backend.service;

import com.qianlispace.backend.dto.LogEntryCreateRequest;
import com.qianlispace.backend.dto.LogEntryResponse;

public interface LogEntryService {

    LogEntryResponse create(LogEntryCreateRequest request);

    LogEntryResponse getById(Long id);
}
