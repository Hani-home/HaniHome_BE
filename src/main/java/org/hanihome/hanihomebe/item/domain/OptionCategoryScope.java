package org.hanihome.hanihomebe.item.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class OptionCategoryScope {
    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "option_category_id")
    private OptionCategory optionCategory;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "scope_type_id")
    private ScopeType scopeType;

    public static OptionCategoryScope create(OptionCategory optionCategory, ScopeType scopeType) {
        return OptionCategoryScope.builder()
                .optionCategory(optionCategory)
                .scopeType(scopeType)
                .build();
    }
}
