package com.hrisgsh.app.service;

import com.hrisgsh.app.domain.*; // for static metamodels
import com.hrisgsh.app.domain.Dependents;
import com.hrisgsh.app.repository.DependentsRepository;
import com.hrisgsh.app.service.criteria.DependentsCriteria;
import com.hrisgsh.app.service.dto.DependentsDTO;
import com.hrisgsh.app.service.mapper.DependentsMapper;
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
 * Service for executing complex queries for {@link Dependents} entities in the database.
 * The main input is a {@link DependentsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DependentsDTO} or a {@link Page} of {@link DependentsDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DependentsQueryService extends QueryService<Dependents> {

    private final Logger log = LoggerFactory.getLogger(DependentsQueryService.class);

    private final DependentsRepository dependentsRepository;

    private final DependentsMapper dependentsMapper;

    public DependentsQueryService(DependentsRepository dependentsRepository, DependentsMapper dependentsMapper) {
        this.dependentsRepository = dependentsRepository;
        this.dependentsMapper = dependentsMapper;
    }

    /**
     * Return a {@link List} of {@link DependentsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DependentsDTO> findByCriteria(DependentsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Dependents> specification = createSpecification(criteria);
        return dependentsMapper.toDto(dependentsRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link DependentsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DependentsDTO> findByCriteria(DependentsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Dependents> specification = createSpecification(criteria);
        return dependentsRepository.findAll(specification, page).map(dependentsMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DependentsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Dependents> specification = createSpecification(criteria);
        return dependentsRepository.count(specification);
    }

    /**
     * Function to convert {@link DependentsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Dependents> createSpecification(DependentsCriteria criteria) {
        Specification<Dependents> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Dependents_.id));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), Dependents_.firstName));
            }
            if (criteria.getMiddleName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMiddleName(), Dependents_.middleName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), Dependents_.lastName));
            }
            if (criteria.getRelation() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRelation(), Dependents_.relation));
            }
            if (criteria.getEmployeeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEmployeeId(),
                            root -> root.join(Dependents_.employee, JoinType.LEFT).get(Employee_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
