package com.hrisgsh.app.service;

import com.hrisgsh.app.domain.*; // for static metamodels
import com.hrisgsh.app.domain.DutySchedule;
import com.hrisgsh.app.repository.DutyScheduleRepository;
import com.hrisgsh.app.service.criteria.DutyScheduleCriteria;
import com.hrisgsh.app.service.dto.DutyScheduleDTO;
import com.hrisgsh.app.service.mapper.DutyScheduleMapper;
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
 * Service for executing complex queries for {@link DutySchedule} entities in the database.
 * The main input is a {@link DutyScheduleCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DutyScheduleDTO} or a {@link Page} of {@link DutyScheduleDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DutyScheduleQueryService extends QueryService<DutySchedule> {

    private final Logger log = LoggerFactory.getLogger(DutyScheduleQueryService.class);

    private final DutyScheduleRepository dutyScheduleRepository;

    private final DutyScheduleMapper dutyScheduleMapper;

    public DutyScheduleQueryService(DutyScheduleRepository dutyScheduleRepository, DutyScheduleMapper dutyScheduleMapper) {
        this.dutyScheduleRepository = dutyScheduleRepository;
        this.dutyScheduleMapper = dutyScheduleMapper;
    }

    /**
     * Return a {@link List} of {@link DutyScheduleDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DutyScheduleDTO> findByCriteria(DutyScheduleCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<DutySchedule> specification = createSpecification(criteria);
        return dutyScheduleMapper.toDto(dutyScheduleRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link DutyScheduleDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DutyScheduleDTO> findByCriteria(DutyScheduleCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DutySchedule> specification = createSpecification(criteria);
        return dutyScheduleRepository.findAll(specification, page).map(dutyScheduleMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DutyScheduleCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<DutySchedule> specification = createSpecification(criteria);
        return dutyScheduleRepository.count(specification);
    }

    /**
     * Function to convert {@link DutyScheduleCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<DutySchedule> createSpecification(DutyScheduleCriteria criteria) {
        Specification<DutySchedule> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), DutySchedule_.id));
            }
            if (criteria.getDateTimeIn() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateTimeIn(), DutySchedule_.dateTimeIn));
            }
            if (criteria.getDateTimeOut() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateTimeOut(), DutySchedule_.dateTimeOut));
            }
            if (criteria.getEmployeeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEmployeeId(),
                            root -> root.join(DutySchedule_.employee, JoinType.LEFT).get(Employee_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
