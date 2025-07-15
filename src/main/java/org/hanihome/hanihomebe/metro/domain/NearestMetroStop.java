package org.hanihome.hanihomebe.metro.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hanihome.hanihomebe.property.domain.Property;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Entity
public class NearestMetroStop {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "metro_stop_id")
    private MetroStop metroStop;

    @OneToOne(fetch = FetchType.LAZY) @JoinColumn(name = "property_id")
    private Property property;

    private Double distance;

    public static NearestMetroStop create(MetroStop nearestMetroStop, Property property, Double distance) {
        return NearestMetroStop.builder()
                .metroStop(nearestMetroStop)
                .property(property)
                .distance(distance)
                .build();
    }
}
