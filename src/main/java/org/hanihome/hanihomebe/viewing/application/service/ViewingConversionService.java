package org.hanihome.hanihomebe.viewing.application.service;

import org.hanihome.hanihomebe.HaniHomeBeApplication;
import org.hanihome.hanihomebe.item.application.converter.OptionItemConverterForViewing;
import org.hanihome.hanihomebe.item.web.dto.OptionItemResponseDTO;
import org.hanihome.hanihomebe.security.auth.user.detail.CustomUserDetails;
import org.hanihome.hanihomebe.viewing.domain.Viewing;
import org.hanihome.hanihomebe.viewing.web.converter.ViewingConverter;
import org.hanihome.hanihomebe.viewing.web.converter.context.ViewingConvertContext;
import org.hanihome.hanihomebe.viewing.web.enums.ViewingViewType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
        Long requesterId = getHttpRequesterId();
        List<OptionItemResponseDTO> optionItemResponseDTOs = optionItemConverter.toOptionItemResponseDTO(List.copyOf(viewing.getViewingOptionItems()));

        ViewingConverter<T> converter = getConverter(view);
        return converter.convert(ViewingConvertContext.create(viewing, requesterId, optionItemResponseDTOs));
    }

    public <T> List<T> convert(List<Viewing> viewings, ViewingViewType view) {
        return convertViewingListToDTO(viewings, view);
    }

    private <T> List<T> convertViewingListToDTO(List<Viewing> viewings, ViewingViewType view) {
        Long requesterId = getHttpRequesterId();
        ViewingConverter<T> converter = getConverter(view);

        return viewings.stream()
                .map(viewing -> {
                    List<OptionItemResponseDTO> optionItemResponseDTOs = optionItemConverter.toOptionItemResponseDTO(List.copyOf(viewing.getViewingOptionItems()));
                    return converter.convert(ViewingConvertContext.create(viewing, requesterId, optionItemResponseDTOs));
                })
                .toList();
    }

    private static Long getHttpRequesterId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = getCustomUserDetails(auth);
        Long requesterId = userDetails.getUserId();
        return requesterId;
    }

    private static CustomUserDetails getCustomUserDetails(Authentication auth) {
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        return userDetails;
    }

    private <T> ViewingConverter<T> getConverter(ViewingViewType type) {
        return (ViewingConverter<T>) converterMap.getOrDefault(type,
                converterMap.get(ViewingViewType.DEFAULT));
    }
}
