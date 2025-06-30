package org.hanihome.hanihomebe.property.domain.item;

import jakarta.persistence.*;
import lombok.*;
import org.hanihome.hanihomebe.item.domain.OptionItem;
import org.hanihome.hanihomebe.property.domain.Property;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Entity
public class PropertyOptionItem {
    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY) @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    @ManyToOne(fetch = LAZY) @JoinColumn(name = "option_item_id", nullable = false)
    private OptionItem optionItem;

    private String optionItemName;

    public void setProperty(Property property) {
        this.property = property;
    }
}
