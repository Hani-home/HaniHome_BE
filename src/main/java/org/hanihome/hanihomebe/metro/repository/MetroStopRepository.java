package org.hanihome.hanihomebe.metro.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.hanihome.hanihomebe.metro.domain.MetroStop;
import org.hanihome.hanihomebe.metro.web.dto.nearest.NearestMetroStopProjectionDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface MetroStopRepository extends JpaRepository<MetroStop, Long> {
    Optional<MetroStop> findByBusinessStopId(String businessStopId);

    @Query(value = "select ms.metro_stop_id as id, " +
            "ST_Distance_Sphere(" +
            "ST_GeomFromText(CONCAT('POINT(', ms.stop_latitude, ' ', ms.stop_longitude, ')'), 4326)," +
            "ST_GeomFromText(CONCAT('POINT(', :latitude, ' ', :longitude, ')'), 4326)" +
            ") / 1000 as distance " +
            "from metro_stop ms " +
            "order by distance " +
            "limit 1",
            nativeQuery = true)
    NearestMetroStopProjectionDTO findNearestMetroAndDistance(@Param("latitude") BigDecimal latitude, @Param("longitude") BigDecimal longitude);
}
