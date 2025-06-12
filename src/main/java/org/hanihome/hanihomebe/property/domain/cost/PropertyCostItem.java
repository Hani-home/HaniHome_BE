/*
package org.hanihome.hanihomebe.property.domain.cost;

import jakarta.persistence.*;
import lombok.*;
import org.hanihome.hanihomebe.property.domain.Property;

import java.security.PrivateKey;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor (access = PRIVATE)
@Builder(access = PRIVATE)
@Getter
@Entity
public class PropertyCostItem {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "property_cost_item_id")
    private Long id;

    @ManyToOne(fetch = LAZY) @JoinColumn(name = "property_id")
    private Property property;

    @ManyToOne(fetch = LAZY) @JoinColumn(name = "cost_item_id")
    private CostItem costItem;

    private String userWriteItem;

    public static PropertyCostItem createDefaultCostItem(Property property, CostItem costItem) {
        return PropertyCostItem.builder()
                .property(property)
                .costItem(costItem)
                .build();
    }

    public static PropertyCostItem createCustomCostItem(Property property, CostItem costItem, String userWriteItem) {
        return PropertyCostItem.builder()
                .property(property)
                .costItem(costItem)
                .userWriteItem(userWriteItem)
                .build();
    }


}
*/
