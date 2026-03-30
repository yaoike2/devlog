package com.qianlispace.backend.controller;

import com.qianlispace.backend.dto.LogEntryCreateRequest;
import com.qianlispace.backend.dto.LogEntryResponse;
import com.qianlispace.backend.service.LogEntryService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LogEntryController.class)
class LogEntryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private LogEntryService logEntryService;

    // --- GET /logs (list) ---

    @Test
    void list_defaultParams_returnsPagedResult() throws Exception {
        List<LogEntryResponse> items = List.of(
                buildResponse(2L, "第二条", "tag2"),
                buildResponse(1L, "第一条", "tag1")
        );
        Page<LogEntryResponse> page = new PageImpl<>(items, org.springframework.data.domain.PageRequest.of(0, 10), 2);
        when(logEntryService.list(eq(0), eq(10))).thenReturn(page);

        mockMvc.perform(get("/logs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.content.length()").value(2))
                .andExpect(jsonPath("$.data.totalElements").value(2))
                .andExpect(jsonPath("$.data.totalPages").value(1))
                .andExpect(jsonPath("$.data.number").value(0));

        verify(logEntryService).list(0, 10);
    }

    @Test
    void list_customParams_usesProvidedValues() throws Exception {
        Page<LogEntryResponse> page = new PageImpl<>(Collections.emptyList(), org.springframework.data.domain.PageRequest.of(1, 5), 0);
        when(logEntryService.list(eq(1), eq(5))).thenReturn(page);

        mockMvc.perform(get("/logs").param("page", "1").param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        verify(logEntryService).list(1, 5);
    }

    @Test
    void list_emptyResult_returnsEmptyPage() throws Exception {
        Page<LogEntryResponse> page = new PageImpl<>(Collections.emptyList(), org.springframework.data.domain.PageRequest.of(0, 10), 0);
        when(logEntryService.list(eq(0), eq(10))).thenReturn(page);

        mockMvc.perform(get("/logs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.content.length()").value(0))
                .andExpect(jsonPath("$.data.totalElements").value(0))
                .andExpect(jsonPath("$.data.totalPages").value(0));
    }

    // --- POST /logs ---

    @Test
    void create_validRequest_returnsSuccess() throws Exception {
        LogEntryCreateRequest request = new LogEntryCreateRequest();
        request.setContent("今天完成了登录功能");
        request.setTags("backend,auth");

        LogEntryResponse response = buildResponse(1L, "今天完成了登录功能", "backend,auth");
        when(logEntryService.create(any())).thenReturn(response);

        mockMvc.perform(post("/logs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.content").value("今天完成了登录功能"))
                .andExpect(jsonPath("$.data.tags").value("backend,auth"));
    }

    @Test
    void create_blankContent_returnsBadRequest() throws Exception {
        LogEntryCreateRequest request = new LogEntryCreateRequest();
        request.setContent("");

        mockMvc.perform(post("/logs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_missingContent_returnsBadRequest() throws Exception {
        mockMvc.perform(post("/logs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    // --- GET /logs/{id} ---

    @Test
    void getById_existingId_returnsSuccess() throws Exception {
        LogEntryResponse response = buildResponse(42L, "记录内容", "tag1");
        when(logEntryService.getById(eq(42L))).thenReturn(response);

        mockMvc.perform(get("/logs/42"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.id").value(42))
                .andExpect(jsonPath("$.data.content").value("记录内容"));
    }

    @Test
    void getById_nonExistingId_returnsNotFoundInBody() throws Exception {
        when(logEntryService.getById(eq(999L)))
                .thenThrow(new EntityNotFoundException("日志条目不存在，id=999"));

        mockMvc.perform(get("/logs/999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    private LogEntryResponse buildResponse(Long id, String content, String tags) {
        try {
            LogEntryResponse resp = new LogEntryResponse();
            setField(resp, "id", id);
            setField(resp, "content", content);
            setField(resp, "tags", tags);
            setField(resp, "createdAt", LocalDateTime.now());
            setField(resp, "updatedAt", LocalDateTime.now());
            return resp;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setField(Object target, String fieldName, Object value) throws Exception {
        var field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}
