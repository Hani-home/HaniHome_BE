package org.hanihome.hanihomebe.item.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Table(name = "option_items",
        uniqueConstraints = @UniqueConstraint(columnNames = {"option_category_id", "parent_id", "item_name"}) // 유니크 제약조건
)
@Entity
public class OptionItem {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "option_item_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "option_category_id")
    private OptionCategory optionCategory;


    // 계층구조 - 부모
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_id")
    private OptionItem parent;

/*
    // 계층구조 - 자식
    @Builder.Default
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true, fetch = LAZY)
    private List<OptionItem> child = new ArrayList<>();
*/

    @Builder.Default
    private boolean isActive = true;

    private String itemName;

    // 사용자 직접 입력한 아이템
    @Builder.Default
    private boolean isCustom = false;


    public static OptionItem createDefault(OptionCategory optionCategory, String itemName) {
        return OptionItem.builder()
                .optionCategory(optionCategory)
                .itemName(itemName)
                .build();
    }

    public static OptionItem createDefault(OptionCategory optionCategory, OptionItem parent, String itemName) {
        return OptionItem.builder()
                .optionCategory(optionCategory)
                .parent(parent)
                .itemName(itemName)
                .build();
    }

    public static OptionItem createCustom(OptionCategory optionCategory, OptionItem parent, String itemName) {
        return OptionItem.builder()
                .optionCategory(optionCategory)
                .parent(parent)
                .isCustom(true)
                .itemName(itemName)
                .build();
    }

    public void modify(OptionCategory category, OptionItem parentItem, String itemName) {
        this.optionCategory = category;
        this.parent = parentItem;
        this.itemName = itemName;
    }

/*
    // 연관관계 편의 메서드
    public void addChild(OptionItem child) {
        this.child.add(child);
    }
    */

}
