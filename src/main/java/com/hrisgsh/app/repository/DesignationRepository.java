package com.hrisgsh.app.repository;

import com.hrisgsh.app.domain.Designation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Designation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DesignationRepository extends JpaRepository<Designation, Long>, JpaSpecificationExecutor<Designation> {}
