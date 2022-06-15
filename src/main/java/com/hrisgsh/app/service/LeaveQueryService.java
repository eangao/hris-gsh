package com.hrisgsh.app.service;

import com.hrisgsh.app.domain.*; // for static metamodels
import com.hrisgsh.app.domain.Leave;
import com.hrisgsh.app.repository.LeaveRepository;
import com.hrisgsh.app.service.criteria.LeaveCriteria;
import com.hrisgsh.app.service.dto.LeaveDTO;
import com.hrisgsh.app.service.mapper.LeaveMapper;
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
 * Service for executing complex queries for {@link Leave} entities in the database.
 * The main input is a {@link LeaveCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LeaveDTO} or a {@link Page} of {@link LeaveDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LeaveQueryService extends QueryService<Leave> {

    private final Logger log = LoggerFactory.getLogger(LeaveQueryService.class);

    private final LeaveRepository leaveRepository;

    private final LeaveMapper leaveMapper;

    public LeaveQueryService(LeaveRepository leaveRepository, LeaveMapper leaveMapper) {
        this.leaveRepository = leaveRepository;
        this.leaveMapper = leaveMapper;
    }

    /**
     * Return a {@link List} of {@link LeaveDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LeaveDTO> findByCriteria(LeaveCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Leave> specification = createSpecification(criteria);
        return leaveMapper.toDto(leaveRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link LeaveDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LeaveDTO> findByCriteria(LeaveCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Leave> specification = createSpecification(criteria);
        return leaveRepository.findAll(specification, page).map(leaveMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LeaveCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Leave> specification = createSpecification(criteria);
        return leaveRepository.count(specification);
    }

    /**
     * Function to convert {@link LeaveCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Leave> createSpecification(LeaveCriteria criteria) {
        Specification<Leave> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Leave_.id));
            }
            if (criteria.getDateApply() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateApply(), Leave_.dateApply));
            }
            if (criteria.getDateStart() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateStart(), Leave_.dateStart));
            }
            if (criteria.getDateEnd() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateEnd(), Leave_.dateEnd));
            }
            if (criteria.getDateReturn() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateReturn(), Leave_.dateReturn));
            }
            if (criteria.getCheckupDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCheckupDate(), Leave_.checkupDate));
            }
            if (criteria.getConvalescingPeriod() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getConvalescingPeriod(), Leave_.convalescingPeriod));
            }
            if (criteria.getDiagnosis() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDiagnosis(), Leave_.diagnosis));
            }
            if (criteria.getEmployeeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getEmployeeId(), root -> root.join(Leave_.employee, JoinType.LEFT).get(Employee_.id))
                    );
            }
            if (criteria.getLeaveTypeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getLeaveTypeId(), root -> root.join(Leave_.leaveType, JoinType.LEFT).get(LeaveType_.id))
                    );
            }
        }
        return specification;
    }
}
