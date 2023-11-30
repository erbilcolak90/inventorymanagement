package com.example.inventorymanagement.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.inventorymanagement.model.entities.LogEntity;
import com.example.inventorymanagement.repository.LogRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class LogServiceTest {

    @Mock
    private LogRepository logRepositoryMock;
    @InjectMocks
    private LogService logService;

    @BeforeEach
    void setUp() {

    }

    @Test
    void testLogInfo() {

        String message = "Test info message";

        logService.logInfo(message);

        verify(logRepositoryMock, times(1)).save(any(LogEntity.class));

    }

    @Test
    void testLogError() {

        String errorMessage = "Test error message";
        Throwable throwable = new RuntimeException("Test exception");

        logService.logError(errorMessage, throwable);

        verify(logRepositoryMock, times(1)).save(any(LogEntity.class));
    }


    @AfterEach
    void tearDown() {
    }
}
