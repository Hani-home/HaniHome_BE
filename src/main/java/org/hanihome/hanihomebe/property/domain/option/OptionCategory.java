package org.hanihome.hanihomebe.property.domain.option;

import jakarta.persistence.*;
import lombok.*;
import org.hanihome.hanihomebe.property.domain.enums.PropertySuperType;

import javax.swing.text.html.Option;

import java.util.Collection;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
@Builder(access = PRIVATE)
@Getter
@Entity
public class OptionCategory {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "option_category_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private CategoryCode categoryCode;

    private String name;

    private PropertySuperType kind; // 어느 매물 유형이 가지는 옵션들

    public static OptionCategory create(CategoryCode categoryCode) {
        return OptionCategory.builder()
                .categoryCode(categoryCode)
                .name(categoryCode.getName())
                .build();
    }


}
