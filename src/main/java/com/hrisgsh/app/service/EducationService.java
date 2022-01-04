package com.hrisgsh.app.service;

import com.hrisgsh.app.service.dto.EducationDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.hrisgsh.app.domain.Education}.
 */
public interface EducationService {
    /**
     * Save a education.
     *
     * @param educationDTO the entity to save.
     * @return the persisted entity.
     */
    EducationDTO save(EducationDTO educationDTO);

    /**
     * Partially updates a education.
     *
     * @param educationDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EducationDTO> partialUpdate(EducationDTO educationDTO);

    /**
     * Get all the educations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EducationDTO> findAll(Pageable pageable);

    /**
     * Get the "id" education.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EducationDTO> findOne(Long id);

    /**
     * Delete the "id" education.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
