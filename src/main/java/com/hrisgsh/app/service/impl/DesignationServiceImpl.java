package com.hrisgsh.app.service.impl;

import com.hrisgsh.app.domain.Designation;
import com.hrisgsh.app.repository.DesignationRepository;
import com.hrisgsh.app.service.DesignationService;
import com.hrisgsh.app.service.dto.DesignationDTO;
import com.hrisgsh.app.service.mapper.DesignationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Designation}.
 */
@Service
@Transactional
public class DesignationServiceImpl implements DesignationService {

    private final Logger log = LoggerFactory.getLogger(DesignationServiceImpl.class);

    private final DesignationRepository designationRepository;

    private final DesignationMapper designationMapper;

    public DesignationServiceImpl(DesignationRepository designationRepository, DesignationMapper designationMapper) {
        this.designationRepository = designationRepository;
        this.designationMapper = designationMapper;
    }

    @Override
    public DesignationDTO save(DesignationDTO designationDTO) {
        log.debug("Request to save Designation : {}", designationDTO);
        Designation designation = designationMapper.toEntity(designationDTO);
        designation = designationRepository.save(designation);
        return designationMapper.toDto(designation);
    }

    @Override
    public Optional<DesignationDTO> partialUpdate(DesignationDTO designationDTO) {
        log.debug("Request to partially update Designation : {}", designationDTO);

        return designationRepository
            .findById(designationDTO.getId())
            .map(existingDesignation -> {
                designationMapper.partialUpdate(existingDesignation, designationDTO);

                return existingDesignation;
            })
            .map(designationRepository::save)
            .map(designationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DesignationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Designations");
        return designationRepository.findAll(pageable).map(designationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DesignationDTO> findOne(Long id) {
        log.debug("Request to get Designation : {}", id);
        return designationRepository.findById(id).map(designationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Designation : {}", id);
        designationRepository.deleteById(id);
    }
}
