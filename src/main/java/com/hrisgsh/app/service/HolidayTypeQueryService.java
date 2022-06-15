package com.hrisgsh.app.service;

import com.hrisgsh.app.domain.*; // for static metamodels
import com.hrisgsh.app.domain.HolidayType;
import com.hrisgsh.app.repository.HolidayTypeRepository;
import com.hrisgsh.app.service.criteria.HolidayTypeCriteria;
import com.hrisgsh.app.service.dto.HolidayTypeDTO;
import com.hrisgsh.app.service.mapper.HolidayTypeMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link HolidayType} entities in the database.
 * The main input is a {@link HolidayTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link HolidayTypeDTO} or a {@link Page} of {@link HolidayTypeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class HolidayTypeQueryService extends QueryService<HolidayType> {

    private final Logger log = LoggerFactory.getLogger(HolidayTypeQueryService.class);

    private final HolidayTypeRepository holidayTypeRepository;

    private final HolidayTypeMapper holidayTypeMapper;

    public HolidayTypeQueryService(HolidayTypeRepository holidayTypeRepository, HolidayTypeMapper holidayTypeMapper) {
        this.holidayTypeRepository = holidayTypeRepository;
        this.holidayTypeMapper = holidayTypeMapper;
    }

    /**
     * Return a {@link List} of {@link HolidayTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<HolidayTypeDTO> findByCriteria(HolidayTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<HolidayType> specification = createSpecification(criteria);
        return holidayTypeMapper.toDto(holidayTypeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link HolidayTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<HolidayTypeDTO> findByCriteria(HolidayTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<HolidayType> specification = createSpecification(criteria);
        return holidayTypeRepository.findAll(specification, page).map(holidayTypeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(HolidayTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<HolidayType> specification = createSpecification(criteria);
        return holidayTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link HolidayTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<HolidayType> createSpecification(HolidayTypeCriteria criteria) {
        Specification<HolidayType> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), HolidayType_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), HolidayType_.name));
            }
        }
        return specification;
    }
}
