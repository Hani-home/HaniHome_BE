package org.hanihome.hanihomebe.item.application;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ItemManager implements ApplicationRunner {
    private final PropertyItemInitializer propertyItemInitializer;
    private final ViewingItemInitializer viewingItemInitializer;
    private final ScopeInitializer scopeInitializer;

    @Transactional
    @Override
    public void run(ApplicationArguments args) throws Exception {
        /// initialize optionItems and Categories
        log.info("ItemManager 초기화 시작");
        propertyItemInitializer.initialize();
        viewingItemInitializer.initialize();

        /// initialize scope and OptionCategoryScope
        scopeInitializer.initialize();
        log.info("ItemManger 초기화 종료");
    }
}