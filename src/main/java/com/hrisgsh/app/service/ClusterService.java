package com.hrisgsh.app.service;

import com.hrisgsh.app.service.dto.ClusterDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.hrisgsh.app.domain.Cluster}.
 */
public interface ClusterService {
    /**
     * Save a cluster.
     *
     * @param clusterDTO the entity to save.
     * @return the persisted entity.
     */
    ClusterDTO save(ClusterDTO clusterDTO);

    /**
     * Partially updates a cluster.
     *
     * @param clusterDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ClusterDTO> partialUpdate(ClusterDTO clusterDTO);

    /**
     * Get all the clusters.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ClusterDTO> findAll(Pageable pageable);

    /**
     * Get the "id" cluster.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ClusterDTO> findOne(Long id);

    /**
     * Delete the "id" cluster.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
