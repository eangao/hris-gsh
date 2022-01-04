package com.hrisgsh.app.service.impl;

import com.hrisgsh.app.domain.DailyTimeRecord;
import com.hrisgsh.app.repository.DailyTimeRecordRepository;
import com.hrisgsh.app.service.DailyTimeRecordService;
import com.hrisgsh.app.service.dto.DailyTimeRecordDTO;
import com.hrisgsh.app.service.mapper.DailyTimeRecordMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link DailyTimeRecord}.
 */
@Service
@Transactional
public class DailyTimeRecordServiceImpl implements DailyTimeRecordService {

    private final Logger log = LoggerFactory.getLogger(DailyTimeRecordServiceImpl.class);

    private final DailyTimeRecordRepository dailyTimeRecordRepository;

    private final DailyTimeRecordMapper dailyTimeRecordMapper;

    public DailyTimeRecordServiceImpl(DailyTimeRecordRepository dailyTimeRecordRepository, DailyTimeRecordMapper dailyTimeRecordMapper) {
        this.dailyTimeRecordRepository = dailyTimeRecordRepository;
        this.dailyTimeRecordMapper = dailyTimeRecordMapper;
    }

    @Override
    public DailyTimeRecordDTO save(DailyTimeRecordDTO dailyTimeRecordDTO) {
        log.debug("Request to save DailyTimeRecord : {}", dailyTimeRecordDTO);
        DailyTimeRecord dailyTimeRecord = dailyTimeRecordMapper.toEntity(dailyTimeRecordDTO);
        dailyTimeRecord = dailyTimeRecordRepository.save(dailyTimeRecord);
        return dailyTimeRecordMapper.toDto(dailyTimeRecord);
    }

    @Override
    public Optional<DailyTimeRecordDTO> partialUpdate(DailyTimeRecordDTO dailyTimeRecordDTO) {
        log.debug("Request to partially update DailyTimeRecord : {}", dailyTimeRecordDTO);

        return dailyTimeRecordRepository
            .findById(dailyTimeRecordDTO.getId())
            .map(existingDailyTimeRecord -> {
                dailyTimeRecordMapper.partialUpdate(existingDailyTimeRecord, dailyTimeRecordDTO);

                return existingDailyTimeRecord;
            })
            .map(dailyTimeRecordRepository::save)
            .map(dailyTimeRecordMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DailyTimeRecordDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DailyTimeRecords");
        return dailyTimeRecordRepository.findAll(pageable).map(dailyTimeRecordMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DailyTimeRecordDTO> findOne(Long id) {
        log.debug("Request to get DailyTimeRecord : {}", id);
        return dailyTimeRecordRepository.findById(id).map(dailyTimeRecordMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete DailyTimeRecord : {}", id);
        dailyTimeRecordRepository.deleteById(id);
    }
}
