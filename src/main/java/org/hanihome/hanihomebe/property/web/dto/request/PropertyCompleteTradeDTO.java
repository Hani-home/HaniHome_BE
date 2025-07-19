package org.hanihome.hanihomebe.property.web.dto.request;

public record PropertyCompleteTradeDTO(
        Long requesterId,
        Long viewingId,
        Long propertyId
)
{
    public static PropertyCompleteTradeDTO create(Long requesterId, Long viewingId, Long propertyId) {
        return new PropertyCompleteTradeDTO(requesterId, viewingId, propertyId);
    }
}
