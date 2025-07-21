package org.hanihome.hanihomebe.viewing.application.service;

import org.hanihome.hanihomebe.global.exception.CustomException;
import org.hanihome.hanihomebe.global.response.domain.ServiceCode;
import org.hanihome.hanihomebe.global.utility.SecurityContextUtils;
import org.hanihome.hanihomebe.item.application.converter.OptionItemConverterForViewing;
import org.hanihome.hanihomebe.item.web.dto.OptionItemResponseDTO;
import org.hanihome.hanihomebe.viewing.domain.Viewing;
import org.hanihome.hanihomebe.viewing.web.converter.ViewingConverter;
import org.hanihome.hanihomebe.viewing.web.converter.context.ViewingConvertContext;
import org.hanihome.hanihomebe.viewing.web.enums.ViewingViewType;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ViewingConversionService {
    private Map<ViewingViewType, ViewingConverter<?>> converterMap = new HashMap<>();
    private final OptionItemConverterForViewing optionItemConverter;

    public ViewingConversionService(List<ViewingConverter<?>> converters,
                                    OptionItemConverterForViewing optionItemConverter
    ) {
        converters.forEach(converter ->
                converterMap.put(converter.supports(), converter));
        this.optionItemConverter = optionItemConverter;
    }

    public <T> T convert(Viewing viewing, ViewingViewType view) {
        Long requesterId = SecurityContextUtils.getHttpRequesterId()
                .orElseThrow(()->new CustomException(ServiceCode.NEED_TO_AUTHENTICATED));
        List<OptionItemResponseDTO> optionItemResponseDTOs = optionItemConverter.toOptionItemResponseDTO(List.copyOf(viewing.getViewingOptionItems()));

        ViewingConverter<T> converter = getConverter(view);
        ViewingConvertContext viewingConvertContext = ViewingConvertContext.create(viewing, requesterId, optionItemResponseDTOs);
        return converter.convert(viewingConvertContext);
    }

    public <T> List<T> convert(List<Viewing> viewings, ViewingViewType view) {
        return convertViewingListToDTO(viewings, view);
    }

    private <T> List<T> convertViewingListToDTO(List<Viewing> viewings, ViewingViewType view) {
        Long requesterId = SecurityContextUtils.getHttpRequesterId()
                .orElseThrow(()->new CustomException(ServiceCode.NEED_TO_AUTHENTICATED));
        ViewingConverter<T> converter = getConverter(view);

        return viewings.stream()
                .map(viewing -> {
                    List<OptionItemResponseDTO> optionItemResponseDTOs = optionItemConverter.toOptionItemResponseDTO(List.copyOf(viewing.getViewingOptionItems()));
                    return converter.convert(ViewingConvertContext.create(viewing, requesterId, optionItemResponseDTOs));
                })
                .toList();
    }

    private <T> ViewingConverter<T> getConverter(ViewingViewType type) {
        return (ViewingConverter<T>) converterMap.getOrDefault(type,
                converterMap.get(ViewingViewType.DEFAULT));
    }
}
