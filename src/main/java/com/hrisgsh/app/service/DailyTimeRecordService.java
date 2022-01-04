package com.hrisgsh.app.service;

import com.hrisgsh.app.service.dto.DailyTimeRecordDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.hrisgsh.app.domain.DailyTimeRecord}.
 */
public interface DailyTimeRecordService {
    /**
     * Save a dailyTimeRecord.
     *
     * @param dailyTimeRecordDTO the entity to save.
     * @return the persisted entity.
     */
    DailyTimeRecordDTO save(DailyTimeRecordDTO dailyTimeRecordDTO);

    /**
     * Partially updates a dailyTimeRecord.
     *
     * @param dailyTimeRecordDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DailyTimeRecordDTO> partialUpdate(DailyTimeRecordDTO dailyTimeRecordDTO);

    /**
     * Get all the dailyTimeRecords.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DailyTimeRecordDTO> findAll(Pageable pageable);

    /**
     * Get the "id" dailyTimeRecord.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DailyTimeRecordDTO> findOne(Long id);

    /**
     * Delete the "id" dailyTimeRecord.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
