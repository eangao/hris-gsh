package com.hrisgsh.app.service;

import com.hrisgsh.app.service.dto.LeaveTypeDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.hrisgsh.app.domain.LeaveType}.
 */
public interface LeaveTypeService {
    /**
     * Save a leaveType.
     *
     * @param leaveTypeDTO the entity to save.
     * @return the persisted entity.
     */
    LeaveTypeDTO save(LeaveTypeDTO leaveTypeDTO);

    /**
     * Partially updates a leaveType.
     *
     * @param leaveTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<LeaveTypeDTO> partialUpdate(LeaveTypeDTO leaveTypeDTO);

    /**
     * Get all the leaveTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LeaveTypeDTO> findAll(Pageable pageable);

    /**
     * Get the "id" leaveType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LeaveTypeDTO> findOne(Long id);

    /**
     * Delete the "id" leaveType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
