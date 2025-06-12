/*
package org.hanihome.hanihomebe.property.domain.amenity;

import jakarta.persistence.*;
import lombok.Getter;
import org.hanihome.hanihomebe.property.domain.Property;

@Getter
@Entity
public class PropertyAmenityItem {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "property_id")
    private Property property;

    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "amenity_item_id")
    private AmenityItem amenityItem;
}
*/
