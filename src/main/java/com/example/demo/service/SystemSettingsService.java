package com.example.demo.service;

import com.example.demo.entity.SystemSettings;
import com.example.demo.repository.SystemSettingsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SystemSettingsService {

    private final SystemSettingsRepository repo;

    /**
     * Always returns the single SystemSettings row.
     * Creates it if it doesn't exist yet.
     */
    @Transactional(readOnly = true)
    public SystemSettings getSettings() {
        return repo.findById(1L)
                   .orElseGet(() -> repo.save(new SystemSettings()));
    }

    @Transactional
    public SystemSettings updateSettings(SystemSettings updated) {
        SystemSettings existing = getSettings();
        existing.setAllowRegistrations(updated.isAllowRegistrations());
        existing.setFreezeEdits(updated.isFreezeEdits());
        return repo.save(existing);
    }

    /**
     * Returns true if user registrations are allowed.
     */
    @Transactional(readOnly = true)
    public boolean canRegisterUsers() {
        return getSettings().isAllowRegistrations();
    }

    /**
     * Returns true if edits are frozen.
     */
    @Transactional(readOnly = true)
    public boolean isFreezeEditsEnabled() {
        return getSettings().isFreezeEdits();
    }
}
