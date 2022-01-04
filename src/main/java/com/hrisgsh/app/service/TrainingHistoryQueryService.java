package com.hrisgsh.app.service;

import com.hrisgsh.app.domain.*; // for static metamodels
import com.hrisgsh.app.domain.TrainingHistory;
import com.hrisgsh.app.repository.TrainingHistoryRepository;
import com.hrisgsh.app.service.criteria.TrainingHistoryCriteria;
import com.hrisgsh.app.service.dto.TrainingHistoryDTO;
import com.hrisgsh.app.service.mapper.TrainingHistoryMapper;
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
 * Service for executing complex queries for {@link TrainingHistory} entities in the database.
 * The main input is a {@link TrainingHistoryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TrainingHistoryDTO} or a {@link Page} of {@link TrainingHistoryDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TrainingHistoryQueryService extends QueryService<TrainingHistory> {

    private final Logger log = LoggerFactory.getLogger(TrainingHistoryQueryService.class);

    private final TrainingHistoryRepository trainingHistoryRepository;

    private final TrainingHistoryMapper trainingHistoryMapper;

    public TrainingHistoryQueryService(TrainingHistoryRepository trainingHistoryRepository, TrainingHistoryMapper trainingHistoryMapper) {
        this.trainingHistoryRepository = trainingHistoryRepository;
        this.trainingHistoryMapper = trainingHistoryMapper;
    }

    /**
     * Return a {@link List} of {@link TrainingHistoryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TrainingHistoryDTO> findByCriteria(TrainingHistoryCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TrainingHistory> specification = createSpecification(criteria);
        return trainingHistoryMapper.toDto(trainingHistoryRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TrainingHistoryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TrainingHistoryDTO> findByCriteria(TrainingHistoryCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TrainingHistory> specification = createSpecification(criteria);
        return trainingHistoryRepository.findAll(specification, page).map(trainingHistoryMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TrainingHistoryCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TrainingHistory> specification = createSpecification(criteria);
        return trainingHistoryRepository.count(specification);
    }

    /**
     * Function to convert {@link TrainingHistoryCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TrainingHistory> createSpecification(TrainingHistoryCriteria criteria) {
        Specification<TrainingHistory> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TrainingHistory_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), TrainingHistory_.name));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate(), TrainingHistory_.date));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), TrainingHistory_.description));
            }
            if (criteria.getEmployeeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEmployeeId(),
                            root -> root.join(TrainingHistory_.employee, JoinType.LEFT).get(Employee_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
