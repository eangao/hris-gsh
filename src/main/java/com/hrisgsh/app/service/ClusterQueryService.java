package com.hrisgsh.app.service;

import com.hrisgsh.app.domain.*; // for static metamodels
import com.hrisgsh.app.domain.Cluster;
import com.hrisgsh.app.repository.ClusterRepository;
import com.hrisgsh.app.service.criteria.ClusterCriteria;
import com.hrisgsh.app.service.dto.ClusterDTO;
import com.hrisgsh.app.service.mapper.ClusterMapper;
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
 * Service for executing complex queries for {@link Cluster} entities in the database.
 * The main input is a {@link ClusterCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ClusterDTO} or a {@link Page} of {@link ClusterDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ClusterQueryService extends QueryService<Cluster> {

    private final Logger log = LoggerFactory.getLogger(ClusterQueryService.class);

    private final ClusterRepository clusterRepository;

    private final ClusterMapper clusterMapper;

    public ClusterQueryService(ClusterRepository clusterRepository, ClusterMapper clusterMapper) {
        this.clusterRepository = clusterRepository;
        this.clusterMapper = clusterMapper;
    }

    /**
     * Return a {@link List} of {@link ClusterDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ClusterDTO> findByCriteria(ClusterCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Cluster> specification = createSpecification(criteria);
        return clusterMapper.toDto(clusterRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ClusterDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ClusterDTO> findByCriteria(ClusterCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Cluster> specification = createSpecification(criteria);
        return clusterRepository.findAll(specification, page).map(clusterMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ClusterCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Cluster> specification = createSpecification(criteria);
        return clusterRepository.count(specification);
    }

    /**
     * Function to convert {@link ClusterCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Cluster> createSpecification(ClusterCriteria criteria) {
        Specification<Cluster> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Cluster_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Cluster_.name));
            }
            if (criteria.getDepartmentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDepartmentId(),
                            root -> root.join(Cluster_.departments, JoinType.LEFT).get(Department_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
