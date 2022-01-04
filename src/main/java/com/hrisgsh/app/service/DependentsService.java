package com.hrisgsh.app.service;

import com.hrisgsh.app.service.dto.DependentsDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.hrisgsh.app.domain.Dependents}.
 */
public interface DependentsService {
    /**
     * Save a dependents.
     *
     * @param dependentsDTO the entity to save.
     * @return the persisted entity.
     */
    DependentsDTO save(DependentsDTO dependentsDTO);

    /**
     * Partially updates a dependents.
     *
     * @param dependentsDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DependentsDTO> partialUpdate(DependentsDTO dependentsDTO);

    /**
     * Get all the dependents.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DependentsDTO> findAll(Pageable pageable);

    /**
     * Get the "id" dependents.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DependentsDTO> findOne(Long id);

    /**
     * Delete the "id" dependents.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
