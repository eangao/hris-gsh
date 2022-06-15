package com.hrisgsh.app.service.impl;

import com.hrisgsh.app.domain.Cluster;
import com.hrisgsh.app.repository.ClusterRepository;
import com.hrisgsh.app.service.ClusterService;
import com.hrisgsh.app.service.dto.ClusterDTO;
import com.hrisgsh.app.service.mapper.ClusterMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Cluster}.
 */
@Service
@Transactional
public class ClusterServiceImpl implements ClusterService {

    private final Logger log = LoggerFactory.getLogger(ClusterServiceImpl.class);

    private final ClusterRepository clusterRepository;

    private final ClusterMapper clusterMapper;

    public ClusterServiceImpl(ClusterRepository clusterRepository, ClusterMapper clusterMapper) {
        this.clusterRepository = clusterRepository;
        this.clusterMapper = clusterMapper;
    }

    @Override
    public ClusterDTO save(ClusterDTO clusterDTO) {
        log.debug("Request to save Cluster : {}", clusterDTO);
        Cluster cluster = clusterMapper.toEntity(clusterDTO);
        cluster = clusterRepository.save(cluster);
        return clusterMapper.toDto(cluster);
    }

    @Override
    public Optional<ClusterDTO> partialUpdate(ClusterDTO clusterDTO) {
        log.debug("Request to partially update Cluster : {}", clusterDTO);

        return clusterRepository
            .findById(clusterDTO.getId())
            .map(existingCluster -> {
                clusterMapper.partialUpdate(existingCluster, clusterDTO);

                return existingCluster;
            })
            .map(clusterRepository::save)
            .map(clusterMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ClusterDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Clusters");
        return clusterRepository.findAll(pageable).map(clusterMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ClusterDTO> findOne(Long id) {
        log.debug("Request to get Cluster : {}", id);
        return clusterRepository.findById(id).map(clusterMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Cluster : {}", id);
        clusterRepository.deleteById(id);
    }
}
