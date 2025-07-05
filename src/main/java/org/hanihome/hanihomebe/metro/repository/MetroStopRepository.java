package org.hanihome.hanihomebe.metro.repository;

import org.hanihome.hanihomebe.metro.domain.MetroStop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MetroStopRepository extends JpaRepository<MetroStop, Long> {
    Optional<MetroStop> findByBusinessStopId(String businessStopId);
}
