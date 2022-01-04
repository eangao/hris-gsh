package com.hrisgsh.app.repository;

import com.hrisgsh.app.domain.TrainingHistory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TrainingHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TrainingHistoryRepository extends JpaRepository<TrainingHistory, Long>, JpaSpecificationExecutor<TrainingHistory> {}
