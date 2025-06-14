package org.hanihome.hanihomebe.item.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hanihome.hanihomebe.property.domain.enums.PropertySuperType;

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

//    private PropertySuperType kind; // 어느 매물 유형이 가지는 옵션들
    // TODO: 윗줄 처럼 어느 엔티티/종류 와 연관된 것인지 판별하는 데이터를 추가할 것:
    //  enum이나 다른 매물/뷰잉같은 구체 엔티티가 아니라 별도의 엔티티로 사용

    public static OptionCategory create(CategoryCode categoryCode) {
        return OptionCategory.builder()
                .categoryCode(categoryCode)
                .name(categoryCode.getName())
                .build();
    }


}
