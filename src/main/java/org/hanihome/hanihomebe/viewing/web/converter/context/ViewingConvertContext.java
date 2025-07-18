package org.hanihome.hanihomebe.viewing.web.converter.context;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hanihome.hanihomebe.item.web.dto.OptionItemResponseDTO;
import org.hanihome.hanihomebe.viewing.domain.Viewing;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class ViewingConvertContext {
    private final Viewing viewing;
    private final Long requesterId;
    private final List<OptionItemResponseDTO> optionItems;

    public static ViewingConvertContext create(Viewing viewing, Long requesterId, List<OptionItemResponseDTO> optionItems) {
        return new ViewingConvertContext(viewing, requesterId, optionItems);
    }
}
