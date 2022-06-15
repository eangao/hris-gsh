package com.hrisgsh.app.repository;

import com.hrisgsh.app.domain.HolidayType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the HolidayType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HolidayTypeRepository extends JpaRepository<HolidayType, Long>, JpaSpecificationExecutor<HolidayType> {}
