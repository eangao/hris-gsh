package com.hrisgsh.app.repository;

import com.hrisgsh.app.domain.DailyTimeRecord;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the DailyTimeRecord entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DailyTimeRecordRepository extends JpaRepository<DailyTimeRecord, Long>, JpaSpecificationExecutor<DailyTimeRecord> {}
