package org.hanihome.hanihomebe.viewing.web.controller;

import lombok.RequiredArgsConstructor;
import org.hanihome.hanihomebe.item.application.service.ItemService;
import org.hanihome.hanihomebe.item.domain.CategoryCode;
import org.hanihome.hanihomebe.item.domain.ScopeCode;
import org.hanihome.hanihomebe.item.web.controller.OptionItemController;
import org.hanihome.hanihomebe.item.web.dto.OptionCategoryResponseDTO;
import org.hanihome.hanihomebe.item.web.dto.OptionItemCreateDTO;
import org.hanihome.hanihomebe.item.web.dto.OptionItemPatchDTO;
import org.hanihome.hanihomebe.item.web.dto.OptionItemResponseDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/viewings/categories")
@RestController
public class ViewingOptionItemController implements OptionItemController {

    private final ItemService itemService;

    /**
     * 뷰잉 페이지에 속한 카테고리 조회
     */
    @GetMapping
    public List<OptionCategoryResponseDTO> getCategories() {
        ScopeCode scopeCode = ScopeCode.SCOPE_VIEWING;
        return itemService.getOptionCategoriesByScopeCode(scopeCode);
    }

    /**
     * 카테고리별 옵션아이템 조회
     */
    @Override
    @GetMapping("/{categoryCode}/option-items")
    public List<OptionItemResponseDTO> getOptionItems(@PathVariable CategoryCode categoryCode) {
        return itemService.getOptionItemsByCategoryCode(categoryCode);
    }

    /**
     * 특정 카테고리에 속한 옵션아이템 생성
     */
    @Override
    @PostMapping("/{categoryCode}/option-items")
    public Long createOptionItem(@PathVariable CategoryCode categoryCode,
                                 @RequestBody OptionItemCreateDTO dto) {
        // dto 안에 categoryCode를 세팅하지 않는다면, 필요시 dto에 포함시키거나 service 호출 시 추가 인자로 넘겨주세요.
        return itemService.createOptionItem(dto);
    }

    /**
     * 옵션아이템 수정
     */
    @Override
    @PutMapping("/{categoryCode}/option-items/{optionItemId}")
    public OptionItemResponseDTO modifyOptionItem(@PathVariable Long optionItemId,
                                                  @RequestBody OptionItemPatchDTO dto) {
        return itemService.modifyOptionItem(optionItemId, dto);
    }
}
