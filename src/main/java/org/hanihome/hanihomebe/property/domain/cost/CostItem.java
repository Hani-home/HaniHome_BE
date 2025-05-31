/*
package org.hanihome.hanihomebe.property.domain.cost;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;
@Getter
@NoArgsConstructor
@Entity
public class CostItem {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "cost_item_id")
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    // 직접 입력한 포함된 비용
    private boolean isCustom;

    private Long memberId;


}
*/
