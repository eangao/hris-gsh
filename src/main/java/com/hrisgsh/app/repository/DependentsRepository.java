package com.hrisgsh.app.repository;

import com.hrisgsh.app.domain.Dependents;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Dependents entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DependentsRepository extends JpaRepository<Dependents, Long>, JpaSpecificationExecutor<Dependents> {}
