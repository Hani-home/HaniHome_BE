package org.hanihome.hanihomebe.property.web.controller;

import lombok.RequiredArgsConstructor;
import org.hanihome.hanihomebe.item.application.service.ItemService;
import org.hanihome.hanihomebe.item.domain.CategoryCode;
import org.hanihome.hanihomebe.item.domain.ScopeCode;
import org.hanihome.hanihomebe.item.web.dto.OptionCategoryResponseDTO;
import org.hanihome.hanihomebe.item.web.dto.OptionItemCreateDTO;
import org.hanihome.hanihomebe.item.web.dto.OptionItemPatchDTO;
import org.hanihome.hanihomebe.item.web.dto.OptionItemResponseDTO;
import org.hanihome.hanihomebe.property.domain.enums.PropertySuperType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/properties")
@RestController
public class PropertyOptionItemController {
    private final ItemService itemService;

    /**
     * read
     */
    // 매물 페이지에 속한 카테고리 조회
    @GetMapping("/categories")
    public List<OptionCategoryResponseDTO> getCategories(@RequestParam PropertySuperType kind) {
        ScopeCode scopeCode = kind.equals(PropertySuperType.SHARE) ?
                ScopeCode.SCOPE_SHARE
                : ScopeCode.SCOPE_RENT;

        return itemService.getOptionCategoriesByScopeCode(scopeCode);
    }

    // 카테고리별 옵션아이템 조회
    @GetMapping("/categories/{categoryCode}/option-items")
    public List<OptionItemResponseDTO> getOptionItems(@PathVariable CategoryCode categoryCode) {
        return itemService.getOptionItemsByCategoryCode(categoryCode);
    }

    /**
     * 아이템 추가
     */

    // 특정 카테고리에 속한 옵션아이템 생성
    @PostMapping("/categories/{categoryCode}/option-items")
    public Long createOptionItem(@PathVariable CategoryCode categoryCode, @RequestBody OptionItemCreateDTO dto) {
        return itemService.createOptionItem(dto);
    }

    /**
     * 아이템 수정
     */
    @PutMapping("/categories/{categoryCode}/option-items/{optionItemId}")
    public OptionItemResponseDTO modifyOptionItem(@PathVariable Long optionItemId,
                                                  @RequestBody OptionItemPatchDTO dto) {
        return itemService.modifyOptionItem(optionItemId, dto);
    }
}
