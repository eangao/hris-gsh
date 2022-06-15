package com.hrisgsh.app.service;

import com.hrisgsh.app.service.dto.DesignationDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.hrisgsh.app.domain.Designation}.
 */
public interface DesignationService {
    /**
     * Save a designation.
     *
     * @param designationDTO the entity to save.
     * @return the persisted entity.
     */
    DesignationDTO save(DesignationDTO designationDTO);

    /**
     * Partially updates a designation.
     *
     * @param designationDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DesignationDTO> partialUpdate(DesignationDTO designationDTO);

    /**
     * Get all the designations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DesignationDTO> findAll(Pageable pageable);

    /**
     * Get the "id" designation.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DesignationDTO> findOne(Long id);

    /**
     * Delete the "id" designation.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
