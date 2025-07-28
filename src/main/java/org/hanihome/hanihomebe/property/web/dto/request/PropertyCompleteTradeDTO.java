package org.hanihome.hanihomebe.property.web.dto.request;

public record PropertyCompleteTradeDTO(
        Long requesterId,
        Long viewingId,
        Long propertyId,
        Boolean dealWithOutsider
)
{
    public static PropertyCompleteTradeDTO create(Long requesterId, Long viewingId, Long propertyId, Boolean dealWithOutsider) {
        return new PropertyCompleteTradeDTO(requesterId, viewingId, propertyId, dealWithOutsider);
    }
}
