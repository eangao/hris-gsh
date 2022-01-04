package com.hrisgsh.app.repository;

import com.hrisgsh.app.domain.Cluster;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Cluster entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClusterRepository extends JpaRepository<Cluster, Long>, JpaSpecificationExecutor<Cluster> {}
