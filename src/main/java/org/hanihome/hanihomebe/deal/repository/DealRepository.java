package org.hanihome.hanihomebe.deal.repository;

import org.hanihome.hanihomebe.deal.domain.Deal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DealRepository extends JpaRepository<Deal, Long> {
    List<Deal> findByGuest_Id(Long guestId);

    List<Deal> findByHost_Id(Long hostId);
}
