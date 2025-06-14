package org.hanihome.hanihomebe.viewing.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hanihome.hanihomebe.item.domain.OptionItem;

import static jakarta.persistence.GenerationType.*;

@NoArgsConstructor
@Getter
@Entity
public class ViewingOptionItem {
    @Column(name = "viewing_option_item_id")
    @Id @GeneratedValue(strategy = IDENTITY)
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
}
