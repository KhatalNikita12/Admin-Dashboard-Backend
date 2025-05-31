package com.example.demo.config;

import com.example.demo.entity.SystemSettings;
import com.example.demo.repository.SystemSettingsRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SystemSettingsInitializer {

    private final SystemSettingsRepository repository;

    @PostConstruct
    public void init() {
        if (!repository.existsById(1L)) {
            SystemSettings settings = new SystemSettings();
            settings.setAllowRegistrations(true);  // default as required
            settings.setFreezeEdits(false);        // optional
            repository.save(settings);
        }
    }
}
