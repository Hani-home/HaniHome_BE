package org.hanihome.hanihomebe.property.domain.option;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hanihome.hanihomebe.property.domain.Property;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
public class PropertyOptionItem {
    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY) @JoinColumn(name = "property_id")
    private Property property;

    @ManyToOne(fetch = LAZY) @JoinColumn(name = "option_item_id")
    private OptionItem optionItem;

}
