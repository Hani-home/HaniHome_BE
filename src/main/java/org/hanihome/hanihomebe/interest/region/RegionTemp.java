/*
package org.hanihome.hanihomebe.interest.region;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "regions")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    */
/** 지역명 *//*

    @Column(nullable = false, length = 100)
    private String name;

    */
/** 경도 *//*

    @Column(nullable = false, length = 20)
    private String lng;

    */
/** 위도 *//*

    @Column(nullable = false, length = 20)
    private String lat;

    */
/** 지도의 가로 길이(픽셀 등 단위) *//*

    @Column(nullable = false)
    private Double width;

    */
/** 지도의 세로 길이(픽셀 등 단위) *//*

    @Column(nullable = false)
    private Double height;

    private Region(String name, String lng, String lat, Double width, Double height) {
        this.name = name;
        this.lng = lng;
        this.lat = lat;
        this.width = width;
        this.height = height;
    }

    */
/**
     * 팩토리 메서드: 새로운 Region 생성
     *//*

    public static Region create(String name, String lng, String lat, Double width, Double height) {
        return new Region(name, lng, lat, width, height);
    }
}
*/
