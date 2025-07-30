package org.hanihome.hanihomebe.viewing.repository;

import org.hanihome.hanihomebe.property.domain.Property;
import org.hanihome.hanihomebe.viewing.domain.Viewing;
import org.hanihome.hanihomebe.viewing.domain.ViewingStatus;

import java.util.List;

public interface CustomViewingRepository {
    List<Viewing> findByPropertyAndStatusList(Long propertyId, List<ViewingStatus> statusList);
}
