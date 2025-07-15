package org.hanihome.hanihomebe.metro.repository;

import org.hanihome.hanihomebe.metro.domain.NearestMetroStop;
import org.hanihome.hanihomebe.property.domain.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface NearestMetroStopRepository extends JpaRepository<NearestMetroStop, Long> {
    Optional<NearestMetroStop> findByProperty(Property property);
}
