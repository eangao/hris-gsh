package com.hrisgsh.app.service.impl;

import com.hrisgsh.app.domain.DutySchedule;
import com.hrisgsh.app.repository.DutyScheduleRepository;
import com.hrisgsh.app.service.DutyScheduleService;
import com.hrisgsh.app.service.dto.DutyScheduleDTO;
import com.hrisgsh.app.service.mapper.DutyScheduleMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link DutySchedule}.
 */
@Service
@Transactional
public class DutyScheduleServiceImpl implements DutyScheduleService {

    private final Logger log = LoggerFactory.getLogger(DutyScheduleServiceImpl.class);

    private final DutyScheduleRepository dutyScheduleRepository;

    private final DutyScheduleMapper dutyScheduleMapper;

    public DutyScheduleServiceImpl(DutyScheduleRepository dutyScheduleRepository, DutyScheduleMapper dutyScheduleMapper) {
        this.dutyScheduleRepository = dutyScheduleRepository;
        this.dutyScheduleMapper = dutyScheduleMapper;
    }

    @Override
    public DutyScheduleDTO save(DutyScheduleDTO dutyScheduleDTO) {
        log.debug("Request to save DutySchedule : {}", dutyScheduleDTO);
        DutySchedule dutySchedule = dutyScheduleMapper.toEntity(dutyScheduleDTO);
        dutySchedule = dutyScheduleRepository.save(dutySchedule);
        return dutyScheduleMapper.toDto(dutySchedule);
    }

    @Override
    public Optional<DutyScheduleDTO> partialUpdate(DutyScheduleDTO dutyScheduleDTO) {
        log.debug("Request to partially update DutySchedule : {}", dutyScheduleDTO);

        return dutyScheduleRepository
            .findById(dutyScheduleDTO.getId())
            .map(existingDutySchedule -> {
                dutyScheduleMapper.partialUpdate(existingDutySchedule, dutyScheduleDTO);

                return existingDutySchedule;
            })
            .map(dutyScheduleRepository::save)
            .map(dutyScheduleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DutyScheduleDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DutySchedules");
        return dutyScheduleRepository.findAll(pageable).map(dutyScheduleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DutyScheduleDTO> findOne(Long id) {
        log.debug("Request to get DutySchedule : {}", id);
        return dutyScheduleRepository.findById(id).map(dutyScheduleMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete DutySchedule : {}", id);
        dutyScheduleRepository.deleteById(id);
    }
}
