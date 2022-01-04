package com.hrisgsh.app.service.impl;

import com.hrisgsh.app.domain.LeaveType;
import com.hrisgsh.app.repository.LeaveTypeRepository;
import com.hrisgsh.app.service.LeaveTypeService;
import com.hrisgsh.app.service.dto.LeaveTypeDTO;
import com.hrisgsh.app.service.mapper.LeaveTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link LeaveType}.
 */
@Service
@Transactional
public class LeaveTypeServiceImpl implements LeaveTypeService {

    private final Logger log = LoggerFactory.getLogger(LeaveTypeServiceImpl.class);

    private final LeaveTypeRepository leaveTypeRepository;

    private final LeaveTypeMapper leaveTypeMapper;

    public LeaveTypeServiceImpl(LeaveTypeRepository leaveTypeRepository, LeaveTypeMapper leaveTypeMapper) {
        this.leaveTypeRepository = leaveTypeRepository;
        this.leaveTypeMapper = leaveTypeMapper;
    }

    @Override
    public LeaveTypeDTO save(LeaveTypeDTO leaveTypeDTO) {
        log.debug("Request to save LeaveType : {}", leaveTypeDTO);
        LeaveType leaveType = leaveTypeMapper.toEntity(leaveTypeDTO);
        leaveType = leaveTypeRepository.save(leaveType);
        return leaveTypeMapper.toDto(leaveType);
    }

    @Override
    public Optional<LeaveTypeDTO> partialUpdate(LeaveTypeDTO leaveTypeDTO) {
        log.debug("Request to partially update LeaveType : {}", leaveTypeDTO);

        return leaveTypeRepository
            .findById(leaveTypeDTO.getId())
            .map(existingLeaveType -> {
                leaveTypeMapper.partialUpdate(existingLeaveType, leaveTypeDTO);

                return existingLeaveType;
            })
            .map(leaveTypeRepository::save)
            .map(leaveTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeaveTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LeaveTypes");
        return leaveTypeRepository.findAll(pageable).map(leaveTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LeaveTypeDTO> findOne(Long id) {
        log.debug("Request to get LeaveType : {}", id);
        return leaveTypeRepository.findById(id).map(leaveTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete LeaveType : {}", id);
        leaveTypeRepository.deleteById(id);
    }
}
