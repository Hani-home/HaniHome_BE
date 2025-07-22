package org.hanihome.hanihomebe.deal.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hanihome.hanihomebe.member.domain.Member;
import org.hanihome.hanihomebe.property.domain.Property;
import org.hanihome.hanihomebe.viewing.domain.Viewing;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = PRIVATE)
@Builder(access = PRIVATE)
@Entity
public class Deal {
    @Id @GeneratedValue
    private Long id;

    @OneToOne(fetch = LAZY) @JoinColumn(name = "viewing_id")
    private Viewing viewing;

    @OneToOne(fetch = LAZY) @JoinColumn(name = "property_id")
    private Property property;

    @ManyToOne(fetch = LAZY) @JoinColumn(name = "host_id")
    private Member host;

    @ManyToOne(fetch = LAZY) @JoinColumn(name = "guest_id")
    private Member guest;

    public static Deal create(Viewing viewing,
                              Property property,
                              Member host,
                              Member guest
    ) {
        return Deal.builder()
                .viewing(viewing)
                .property(property)
                .host(host)
                .guest(guest)
                .build();
    }


}
