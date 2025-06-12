package org.hanihome.hanihomebe.property.domain.option;

import jakarta.persistence.*;
import lombok.*;

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

    public static OptionCategory create(CategoryCode categoryCode) {
        return OptionCategory.builder()
                .categoryCode(categoryCode)
                .name(categoryCode.getName())
                .build();
    }


}
