package com.hrisgsh.app.service;

import com.hrisgsh.app.domain.*; // for static metamodels
import com.hrisgsh.app.domain.Benefits;
import com.hrisgsh.app.repository.BenefitsRepository;
import com.hrisgsh.app.service.criteria.BenefitsCriteria;
import com.hrisgsh.app.service.dto.BenefitsDTO;
import com.hrisgsh.app.service.mapper.BenefitsMapper;
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
 * Service for executing complex queries for {@link Benefits} entities in the database.
 * The main input is a {@link BenefitsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link BenefitsDTO} or a {@link Page} of {@link BenefitsDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BenefitsQueryService extends QueryService<Benefits> {

    private final Logger log = LoggerFactory.getLogger(BenefitsQueryService.class);

    private final BenefitsRepository benefitsRepository;

    private final BenefitsMapper benefitsMapper;

    public BenefitsQueryService(BenefitsRepository benefitsRepository, BenefitsMapper benefitsMapper) {
        this.benefitsRepository = benefitsRepository;
        this.benefitsMapper = benefitsMapper;
    }

    /**
     * Return a {@link List} of {@link BenefitsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<BenefitsDTO> findByCriteria(BenefitsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Benefits> specification = createSpecification(criteria);
        return benefitsMapper.toDto(benefitsRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link BenefitsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BenefitsDTO> findByCriteria(BenefitsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Benefits> specification = createSpecification(criteria);
        return benefitsRepository.findAll(specification, page).map(benefitsMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BenefitsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Benefits> specification = createSpecification(criteria);
        return benefitsRepository.count(specification);
    }

    /**
     * Function to convert {@link BenefitsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Benefits> createSpecification(BenefitsCriteria criteria) {
        Specification<Benefits> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Benefits_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Benefits_.name));
            }
            if (criteria.getEmployeeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEmployeeId(),
                            root -> root.join(Benefits_.employees, JoinType.LEFT).get(Employee_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
