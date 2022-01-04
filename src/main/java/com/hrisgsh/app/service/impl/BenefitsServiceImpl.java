package com.hrisgsh.app.service.impl;

import com.hrisgsh.app.domain.Benefits;
import com.hrisgsh.app.repository.BenefitsRepository;
import com.hrisgsh.app.service.BenefitsService;
import com.hrisgsh.app.service.dto.BenefitsDTO;
import com.hrisgsh.app.service.mapper.BenefitsMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Benefits}.
 */
@Service
@Transactional
public class BenefitsServiceImpl implements BenefitsService {

    private final Logger log = LoggerFactory.getLogger(BenefitsServiceImpl.class);

    private final BenefitsRepository benefitsRepository;

    private final BenefitsMapper benefitsMapper;

    public BenefitsServiceImpl(BenefitsRepository benefitsRepository, BenefitsMapper benefitsMapper) {
        this.benefitsRepository = benefitsRepository;
        this.benefitsMapper = benefitsMapper;
    }

    @Override
    public BenefitsDTO save(BenefitsDTO benefitsDTO) {
        log.debug("Request to save Benefits : {}", benefitsDTO);
        Benefits benefits = benefitsMapper.toEntity(benefitsDTO);
        benefits = benefitsRepository.save(benefits);
        return benefitsMapper.toDto(benefits);
    }

    @Override
    public Optional<BenefitsDTO> partialUpdate(BenefitsDTO benefitsDTO) {
        log.debug("Request to partially update Benefits : {}", benefitsDTO);

        return benefitsRepository
            .findById(benefitsDTO.getId())
            .map(existingBenefits -> {
                benefitsMapper.partialUpdate(existingBenefits, benefitsDTO);

                return existingBenefits;
            })
            .map(benefitsRepository::save)
            .map(benefitsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BenefitsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Benefits");
        return benefitsRepository.findAll(pageable).map(benefitsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BenefitsDTO> findOne(Long id) {
        log.debug("Request to get Benefits : {}", id);
        return benefitsRepository.findById(id).map(benefitsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Benefits : {}", id);
        benefitsRepository.deleteById(id);
    }
}
