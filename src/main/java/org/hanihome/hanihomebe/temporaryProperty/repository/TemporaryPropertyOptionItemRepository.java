package org.hanihome.hanihomebe.temporaryProperty.repository;

import org.hanihome.hanihomebe.temporaryProperty.domain.TemporaryProperty;
import org.hanihome.hanihomebe.temporaryProperty.domain.item.TemporaryPropertyOptionItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemporaryPropertyOptionItemRepository extends JpaRepository<TemporaryPropertyOptionItem, Long> {

    void deleteByTemporaryProperty(TemporaryProperty temporaryProperty);
}