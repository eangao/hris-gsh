package com.hrisgsh.app.service;

import com.hrisgsh.app.service.dto.HolidayTypeDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.hrisgsh.app.domain.HolidayType}.
 */
public interface HolidayTypeService {
    /**
     * Save a holidayType.
     *
     * @param holidayTypeDTO the entity to save.
     * @return the persisted entity.
     */
    HolidayTypeDTO save(HolidayTypeDTO holidayTypeDTO);

    /**
     * Partially updates a holidayType.
     *
     * @param holidayTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<HolidayTypeDTO> partialUpdate(HolidayTypeDTO holidayTypeDTO);

    /**
     * Get all the holidayTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<HolidayTypeDTO> findAll(Pageable pageable);

    /**
     * Get the "id" holidayType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<HolidayTypeDTO> findOne(Long id);

    /**
     * Delete the "id" holidayType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
