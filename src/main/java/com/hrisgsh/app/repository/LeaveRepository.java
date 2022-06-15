package com.hrisgsh.app.repository;

import com.hrisgsh.app.domain.Leave;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Leave entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LeaveRepository extends JpaRepository<Leave, Long>, JpaSpecificationExecutor<Leave> {}
