package com.hrisgsh.app.service;

import com.hrisgsh.app.domain.*; // for static metamodels
import com.hrisgsh.app.domain.Holiday;
import com.hrisgsh.app.repository.HolidayRepository;
import com.hrisgsh.app.service.criteria.HolidayCriteria;
import com.hrisgsh.app.service.dto.HolidayDTO;
import com.hrisgsh.app.service.mapper.HolidayMapper;
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
 * Service for executing complex queries for {@link Holiday} entities in the database.
 * The main input is a {@link HolidayCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link HolidayDTO} or a {@link Page} of {@link HolidayDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class HolidayQueryService extends QueryService<Holiday> {

    private final Logger log = LoggerFactory.getLogger(HolidayQueryService.class);

    private final HolidayRepository holidayRepository;

    private final HolidayMapper holidayMapper;

    public HolidayQueryService(HolidayRepository holidayRepository, HolidayMapper holidayMapper) {
        this.holidayRepository = holidayRepository;
        this.holidayMapper = holidayMapper;
    }

    /**
     * Return a {@link List} of {@link HolidayDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<HolidayDTO> findByCriteria(HolidayCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Holiday> specification = createSpecification(criteria);
        return holidayMapper.toDto(holidayRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link HolidayDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<HolidayDTO> findByCriteria(HolidayCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Holiday> specification = createSpecification(criteria);
        return holidayRepository.findAll(specification, page).map(holidayMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(HolidayCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Holiday> specification = createSpecification(criteria);
        return holidayRepository.count(specification);
    }

    /**
     * Function to convert {@link HolidayCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Holiday> createSpecification(HolidayCriteria criteria) {
        Specification<Holiday> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Holiday_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Holiday_.name));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate(), Holiday_.date));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Holiday_.description));
            }
        }
        return specification;
    }
}
