package org.hanihome.hanihomebe.item.application.converter;

import org.hanihome.hanihomebe.item.web.dto.OptionItemResponseDTO;

import java.util.List;

public interface OptionItemConverter<T> {
    List<OptionItemResponseDTO> toOptionItemResponseDTO(List<T> entityOptionItems);
}
