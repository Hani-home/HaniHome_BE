package org.hanihome.hanihomebe.item.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.mapstruct.ap.internal.model.GeneratedType;

import static jakarta.persistence.GenerationType.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class ScopeType {
    @Column(name = "scope_type_id")
    @Id @GeneratedValue(strategy = IDENTITY)
    public Long id;

    @Enumerated(EnumType.STRING)
    private ScopeCode scopeCode;

    public static ScopeType create(ScopeCode scopeCode) {
        return ScopeType.builder()
                .scopeCode(scopeCode)
                .build();
    }

}
