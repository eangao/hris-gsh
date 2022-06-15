package com.hrisgsh.app.service;

import com.hrisgsh.app.service.dto.LeaveDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.hrisgsh.app.domain.Leave}.
 */
public interface LeaveService {
    /**
     * Save a leave.
     *
     * @param leaveDTO the entity to save.
     * @return the persisted entity.
     */
    LeaveDTO save(LeaveDTO leaveDTO);

    /**
     * Partially updates a leave.
     *
     * @param leaveDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<LeaveDTO> partialUpdate(LeaveDTO leaveDTO);

    /**
     * Get all the leaves.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LeaveDTO> findAll(Pageable pageable);

    /**
     * Get the "id" leave.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LeaveDTO> findOne(Long id);

    /**
     * Delete the "id" leave.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
