package org.hanihome.hanihomebe.item.application;

import org.hanihome.hanihomebe.item.domain.OptionItem;
import org.hanihome.hanihomebe.item.web.dto.OptionItemResponseDTO;

import java.util.List;

public interface OptionItemConverter<T> {
    List<OptionItemResponseDTO> toResponseDTO(List<T> entityOptionItems);
}
