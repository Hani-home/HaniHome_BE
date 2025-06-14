package org.hanihome.hanihomebe.item.application;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemManager implements ApplicationRunner {
    private final PropertyItemInitializer propertyItemInitializer;
    private final ViewingItemInitializer viewingItemInitializer;
    private final ScopeInitializer scopeInitializer;

    @Transactional
    @Override
    public void run(ApplicationArguments args) throws Exception {
        /// initialize optionItems
        propertyItemInitializer.initialize();
        viewingItemInitializer.initialize();

        /// initialize scope and OptionCategoryScope
        scopeInitializer.initialize();
    }
}