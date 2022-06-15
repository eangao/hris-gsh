package com.hrisgsh.app.service.impl;

import com.hrisgsh.app.domain.Leave;
import com.hrisgsh.app.repository.LeaveRepository;
import com.hrisgsh.app.service.LeaveService;
import com.hrisgsh.app.service.dto.LeaveDTO;
import com.hrisgsh.app.service.mapper.LeaveMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Leave}.
 */
@Service
@Transactional
public class LeaveServiceImpl implements LeaveService {

    private final Logger log = LoggerFactory.getLogger(LeaveServiceImpl.class);

    private final LeaveRepository leaveRepository;

    private final LeaveMapper leaveMapper;

    public LeaveServiceImpl(LeaveRepository leaveRepository, LeaveMapper leaveMapper) {
        this.leaveRepository = leaveRepository;
        this.leaveMapper = leaveMapper;
    }

    @Override
    public LeaveDTO save(LeaveDTO leaveDTO) {
        log.debug("Request to save Leave : {}", leaveDTO);
        Leave leave = leaveMapper.toEntity(leaveDTO);
        leave = leaveRepository.save(leave);
        return leaveMapper.toDto(leave);
    }

    @Override
    public Optional<LeaveDTO> partialUpdate(LeaveDTO leaveDTO) {
        log.debug("Request to partially update Leave : {}", leaveDTO);

        return leaveRepository
            .findById(leaveDTO.getId())
            .map(existingLeave -> {
                leaveMapper.partialUpdate(existingLeave, leaveDTO);

                return existingLeave;
            })
            .map(leaveRepository::save)
            .map(leaveMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeaveDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Leaves");
        return leaveRepository.findAll(pageable).map(leaveMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LeaveDTO> findOne(Long id) {
        log.debug("Request to get Leave : {}", id);
        return leaveRepository.findById(id).map(leaveMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Leave : {}", id);
        leaveRepository.deleteById(id);
    }
}
