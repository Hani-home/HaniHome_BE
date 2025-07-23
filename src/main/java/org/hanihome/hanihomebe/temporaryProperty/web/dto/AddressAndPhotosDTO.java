package org.hanihome.hanihomebe.temporaryProperty.web.dto;

import org.hanihome.hanihomebe.interest.region.Region;

import java.util.List;

public record AddressAndPhotosDTO(
        Region region,
        List<String> imageUrls
){}
