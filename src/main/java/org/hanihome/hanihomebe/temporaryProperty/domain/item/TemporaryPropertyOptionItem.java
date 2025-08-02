package org.hanihome.hanihomebe.temporaryProperty.domain.item;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hanihome.hanihomebe.item.domain.OptionItem;
import org.hanihome.hanihomebe.temporaryProperty.domain.TemporaryProperty;

import java.util.Objects;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Entity
public class TemporaryPropertyOptionItem {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "temporary_property_id", nullable = false)
    private TemporaryProperty temporaryProperty;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "option_item_id", nullable = false)
    private OptionItem optionItem;

    private String optionItemName;

    public void setTemporaryProperty(TemporaryProperty temporaryProperty) {
        this.temporaryProperty = temporaryProperty;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (o == null || !(o instanceof TemporaryPropertyOptionItem that)) return false;
        return this.temporaryProperty == that.temporaryProperty &&
                this.optionItem == that.optionItem;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                System.identityHashCode(temporaryProperty),
                System.identityHashCode(optionItem)
        );
    }
}
