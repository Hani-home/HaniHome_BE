package org.hanihome.hanihomebe.viewing.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hanihome.hanihomebe.item.domain.OptionItem;

import java.util.Objects;

import static jakarta.persistence.GenerationType.*;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Getter
@Table(
        name = "viewing_option_item",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"viewing_id", "option_item_id"}),
        }
)
@Entity
public class ViewingOptionItem {
    @Column(name = "viewing_option_item_id")
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "viewing_id", nullable = false)
    private Viewing viewing;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_item_id", nullable = false)
    private OptionItem optionItem;

    private String optionItemName;

    public void setViewing(Viewing viewing) {
        this.viewing = viewing;
    }

    public static ViewingOptionItem create(OptionItem optionItem) {
        return ViewingOptionItem.builder()
                .optionItem(optionItem)
                .optionItemName(optionItem.getItemName())
                .build();
    }

    /// Viewing.addViewingOptionItems() 에서 동등성 검사가 내포되므로 구현
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ViewingOptionItem that)) return false;
        // 같은 Viewing 인스턴스이고, 같은 OptionItem 인스턴스일 때만 동일하다고 판단
        return this.viewing == that.viewing
                && this.optionItem == that.optionItem;
    }

    /**
     * 영속화 전·후 일관된 값 보장
     * 보통 엔티티의 id 필드를 해시코드에 포함시키면,
     * 영속화 이전 (id == null) 와
     * 영속화 이후 (id 가 DB에서 생성된 값)
     * 가 달라져 버려서, 같은 객체인데도 해시코드가 바뀌는 문제가 생길 수 있습니다.
     * 반면 identityHashCode 는 객체 인스턴스 자체 에 기반하므로, 영속화 전후에도 값이 변하지 않습니다.
     */
    //해시코드도 같고 equals() 도 true 여야 “중복”으로 판단해서 추가를 막거나 contains 결과를 true 로 돌려줌
    //해시코드가 다르면 equals() 도 안 부르고 바로 “없다”고 결론
    @Override
    public int hashCode() {
        // identityHashCode 를 써서, 영속화 전후 모두 일관된 해시코드를 보장
        return Objects.hash(
                System.identityHashCode(viewing),
                System.identityHashCode(optionItem)
        );
    }

}
