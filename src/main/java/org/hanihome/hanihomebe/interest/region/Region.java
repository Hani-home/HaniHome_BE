package org.hanihome.hanihomebe.interest.region;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/** ver1: 값타입 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Embeddable
public class Region {
    // Australia
    @Column(nullable = false)
    private String country;
    // 2067
    @Column(nullable = false)
    private String postCode;
    // NSW
    @Column(nullable = false)
    private String state;
    // Chatswood
    @Column(nullable = false)
    private String suburb;
    // Smith St
    @Column(nullable = false)
    private String streetName;
    // 25
    @Column(nullable = false)
    private String streetNumber;

    // 1203, 몇동 몇호
    @Column(nullable = false)
    private String unit;

    // Chatswood Central Apartments
    @Column(nullable = true)
    private String buildingName;
}


/** ver2: DB 테이블 */
/*
@Entity
@Table(name = "regions")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Region extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    *//** 지역명 (예: 서울특별시, 강남구, 논현동 등) *//*
    @Column(nullable = false, length = 100)
    private String name;

    *//** (선택) ISO 코드나 자체 코드가 필요하면 *//*
    @Column(length = 20, unique = true)
    private String code;

    *//** 중앙 지점 위도 *//*
    @Column(nullable = false)
    private Double latitude;

    *//** 중앙 지점 경도 *//*
    @Column(nullable = false)
    private Double longitude;

    *//** 상위 행정구역 (없으면 최상위) *//*
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Region parent;

    *//** 하위 행정구역들 *//*
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Region> children = new HashSet<>();


    *//**
     * 팩토리 메서드: 최상위(부모 없는) 지역 생성
     *//*
    public static Region createRoot(String name,
                                    String code,
                                    Double latitude,
                                    Double longitude) {
        return Region.builder()
                .name(name)
                .code(code)
                .latitude(latitude)
                .longitude(longitude)
                .build();
    }

    *//**
     * 팩토리 메서드: 하위 지역 생성
     *//*
    public static Region createChild(Region parent,
                                     String name,
                                     String code,
                                     Double latitude,
                                     Double longitude) {
        Region child = Region.builder()
                .parent(parent)
                .name(name)
                .code(code)
                .latitude(latitude)
                .longitude(longitude)
                .build();
        parent.getChildren().add(child);
        return child;
    }
}
*/
