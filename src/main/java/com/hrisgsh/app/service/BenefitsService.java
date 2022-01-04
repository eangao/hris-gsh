package com.hrisgsh.app.service;

import com.hrisgsh.app.service.dto.BenefitsDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.hrisgsh.app.domain.Benefits}.
 */
public interface BenefitsService {
    /**
     * Save a benefits.
     *
     * @param benefitsDTO the entity to save.
     * @return the persisted entity.
     */
    BenefitsDTO save(BenefitsDTO benefitsDTO);

    /**
     * Partially updates a benefits.
     *
     * @param benefitsDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<BenefitsDTO> partialUpdate(BenefitsDTO benefitsDTO);

    /**
     * Get all the benefits.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BenefitsDTO> findAll(Pageable pageable);

    /**
     * Get the "id" benefits.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BenefitsDTO> findOne(Long id);

    /**
     * Delete the "id" benefits.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
