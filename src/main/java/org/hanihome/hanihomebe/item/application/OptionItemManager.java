package org.hanihome.hanihomebe.item.application;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OptionItemManager {
    private final PropertyItemInitializer propertyItemInitializer;
    private final ViewingItemInitializer viewingItemInitializer;

    @Transactional
    @PostConstruct
    public void init() {
        propertyItemInitializer.initialize();
        viewingItemInitializer.initialize();
    }
}