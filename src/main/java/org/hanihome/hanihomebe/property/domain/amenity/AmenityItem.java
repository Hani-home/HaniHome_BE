/*
package org.hanihome.hanihomebe.property.domain.amenity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class AmenityItem {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "amenity_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "amenity_category_id", nullable = false)
    private AmenityCategory amenityCategory;

    @Column(nullable = false, unique = true)
    private String name;

}
*/
