package org.hanihome.hanihomebe.temporaryProperty.domain.repository;

import org.hanihome.hanihomebe.temporaryProperty.domain.TemporaryRentProperty;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemporaryRentPropertyRepository extends JpaRepository<TemporaryRentProperty, Long> {
}
