package com.example.inventorymanagement.service;

import com.example.inventorymanagement.model.entities.LogEntity;
import com.example.inventorymanagement.repository.LogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LogService {

    private final LogRepository logRepository;

    public void logInfo(String message) {
        log.info(message);
        saveLog("INFO", message);
    }

    public void logError(String message, Throwable throwable) {
        log.error(message, throwable);
        saveLog("ERROR", message);
    }

    private void saveLog(String logLevel, String logMessage) {
        LogEntity logEntity = new LogEntity();
        logEntity.setLogLevel(logLevel);
        logEntity.setLogMessage(logMessage);

        logRepository.save(logEntity);
    }

}
