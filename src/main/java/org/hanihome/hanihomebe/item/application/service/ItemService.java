package org.hanihome.hanihomebe.item.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hanihome.hanihomebe.global.exception.CustomException;
import org.hanihome.hanihomebe.global.response.domain.ServiceCode;
import org.hanihome.hanihomebe.item.domain.CategoryCode;
import org.hanihome.hanihomebe.item.domain.OptionCategory;
import org.hanihome.hanihomebe.item.domain.OptionItem;
import org.hanihome.hanihomebe.item.domain.ScopeCode;
import org.hanihome.hanihomebe.item.repository.OptionCategoryRepository;
import org.hanihome.hanihomebe.item.repository.OptionItemRepository;
import org.hanihome.hanihomebe.item.web.dto.OptionCategoryResponseDTO;
import org.hanihome.hanihomebe.item.web.dto.OptionItemCreateDTO;
import org.hanihome.hanihomebe.item.web.dto.OptionItemPatchDTO;
import org.hanihome.hanihomebe.item.web.dto.OptionItemResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ItemService {
    private final OptionCategoryRepository categoryRepository;
    private final OptionItemRepository optionItemRepository;

    // Scope별 카테고리 조회
    public List<OptionCategoryResponseDTO> getOptionCategoriesByScopeCode(ScopeCode scopeCode) {
        List<OptionCategoryResponseDTO> dtos = categoryRepository.findAllByScope(scopeCode)
                .stream()
                .map(category -> OptionCategoryResponseDTO.from(category))
                .toList();
        return dtos;
    }

    // 카테고리별 옵션아이템 조회
    public List<OptionItemResponseDTO> getOptionItemsByCategoryCode(CategoryCode categoryCode) {
        List<OptionItemResponseDTO> dtos = optionItemRepository.findAllByOptionCategory_CategoryCode(categoryCode)
                .stream()
                .map(optionItem -> OptionItemResponseDTO.from(optionItem))
                .toList();
        return dtos;
    }

    // create OptionItem
    @Transactional
    public Long createOptionItem(OptionItemCreateDTO dto) {
        OptionCategory category = categoryRepository.findByCategoryCode(dto.categoryCode())
                .orElseThrow(() -> new CustomException(ServiceCode.OPTION_CATEGORY_NOT_EXISTS));
        OptionItem parentItem = dto.parentItemId() == null ?
                null
                : optionItemRepository.findById(dto.parentItemId())
                .orElseThrow(() -> new CustomException(ServiceCode.OPTION_ITEM_NOT_EXISTS));

        OptionItem newOptionItem = OptionItem.createDefault(category, parentItem, dto.itemName());
        return optionItemRepository.save(newOptionItem).getId();
    }

    @Transactional
    public OptionItemResponseDTO modifyOptionItem(Long optionItemId, OptionItemPatchDTO dto) {
        OptionItem findOptionItem = optionItemRepository.findById(optionItemId)
                .orElseThrow(() -> new CustomException(ServiceCode.OPTION_ITEM_NOT_EXISTS));
        OptionItem parentItem = dto.parentItemId() == null ?
                null :
                optionItemRepository.findById(dto.parentItemId())
                        .orElseThrow(() -> new CustomException(ServiceCode.OPTION_ITEM_NOT_EXISTS));
        OptionCategory category = categoryRepository.findByCategoryCode(dto.categoryCode())
                .orElseThrow(() -> new CustomException(ServiceCode.OPTION_CATEGORY_NOT_EXISTS));

        findOptionItem.modify(category, parentItem, dto.itemName());
        optionItemRepository.save(findOptionItem);
        return OptionItemResponseDTO.from(findOptionItem);
    }
}
