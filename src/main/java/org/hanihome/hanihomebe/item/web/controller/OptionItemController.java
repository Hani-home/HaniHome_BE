package org.hanihome.hanihomebe.item.web.controller;

import org.hanihome.hanihomebe.item.domain.CategoryCode;
import org.hanihome.hanihomebe.item.web.dto.OptionCategoryResponseDTO;
import org.hanihome.hanihomebe.item.web.dto.OptionItemCreateDTO;
import org.hanihome.hanihomebe.item.web.dto.OptionItemPatchDTO;
import org.hanihome.hanihomebe.item.web.dto.OptionItemResponseDTO;
import org.hanihome.hanihomebe.property.domain.enums.PropertySuperType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/option-items")
public interface OptionItemController {

    /**
     * 카테고리별 옵션아이템 조회
     */
    List<OptionItemResponseDTO> getOptionItems(@PathVariable CategoryCode categoryCode);

    /**
     * 특정 카테고리에 속한 옵션아이템 생성
     */
    Long createOptionItem(@PathVariable CategoryCode categoryCode,
                          @RequestBody OptionItemCreateDTO dto);

    /**
     * 옵션아이템 수정
     */
    OptionItemResponseDTO modifyOptionItem(@PathVariable Long optionItemId,
                                           @RequestBody OptionItemPatchDTO dto);
}
