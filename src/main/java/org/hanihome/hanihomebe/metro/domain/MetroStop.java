package org.hanihome.hanihomebe.metro.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hanihome.hanihomebe.metro.web.dto.MetroStopPatchDTO;

import java.math.BigDecimal;

/**
 * 지하철 정거장
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "metro_stop")
@Entity
public class MetroStop {

    /**
     * GTFS 기준 stop_id (예: "200030")
     */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "metro_stop_id")
    private Long id;

    @Column(name = "business_stop_id", length = 50, nullable = false)
    private String businessStopId;

    /** 역 이름 (예: "Martin Place Station") */
    @Column(name = "stop_name", length = 255, nullable = false)
    private String stopName;

    /** 위도 (precision=9, scale=6 권장) */
    @Column(name = "stop_latitude", precision = 9, scale = 6, nullable = false)
    private BigDecimal stopLatitude;

    /** 경도 (precision=9, scale=6 권장) */
    @Column(name = "stop_longitude", precision = 9, scale = 6, nullable = false)
    private BigDecimal stopLongitude;

    /**
     * location_type
     * 0 = single stop
     * 1 = parent stop
     * 2 = 플랫폼 등
     */
    @Column(name = "location_type", nullable = false)
    private String locationType;

    /**
     * parent_stop(station)
     * 부모 정거장
     * 해당 엔티티가 자식 정거장으로 부모와 같은 정거장이지만 다른 플랫폼일 때
     * 없으면 NULL.
     */
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "parent_stop")
    private MetroStop parentStop;

    /**
     * wheelchair_boarding
     * 0 = 정보 없음
     * 1 = 휠체어 탑승 가능
     */
    @Column(name = "wheelchair_boarding", nullable = false)
    private Boolean wheelchairBoarding;

    /** platform_code (예: "1", "2" "3") */
    @Column(name = "platform_code", length = 50)
    private String platformCode;

    public static MetroStop createParent(String businessStopId, String stopName, BigDecimal stopLatitude, BigDecimal stopLongitude, String locationType, Boolean wheelchairBoarding, String platformCode) {
        return MetroStop.builder()
                .businessStopId(businessStopId)
                .stopName(stopName)
                .stopLatitude(stopLatitude)
                .stopLongitude(stopLongitude)
                .locationType(locationType)
                .wheelchairBoarding(wheelchairBoarding)
                .platformCode(platformCode)
                .build();
    }

    public static MetroStop createChild(String businessStopId, String stopName, BigDecimal stopLatitude, BigDecimal stopLongitude, String locationType, MetroStop parentStop, Boolean wheelchairBoarding, String platformCode) {
        return MetroStop.builder()
                .businessStopId(businessStopId)
                .stopName(stopName)
                .stopLatitude(stopLatitude)
                .stopLongitude(stopLongitude)
                .locationType(locationType)
                .parentStop(parentStop)
                .wheelchairBoarding(wheelchairBoarding)
                .platformCode(platformCode)
                .build();
    }

    public void update(MetroStopPatchDTO dto) {
        if (dto.stopName() != null) {
            this.stopName = dto.stopName();
        }
        if (dto.stopLatitude() != null) {
            this.stopLatitude = dto.stopLatitude();
        }
        if (dto.stopLongitude() != null) {
            this.stopLongitude = dto.stopLongitude();
        }
        if (dto.locationType() != null) {
            this.locationType = dto.locationType();
        }
        if (dto.wheelchairBoarding() != null) {
            this.wheelchairBoarding = dto.wheelchairBoarding();
        }
        if (dto.platformCode() != null) {
            this.platformCode = dto.platformCode();
        }
    }

    public void updateParent(MetroStop parentStop) {
        this.parentStop = parentStop;
    }


}