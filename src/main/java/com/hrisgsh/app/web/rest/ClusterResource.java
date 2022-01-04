package com.hrisgsh.app.web.rest;

import com.hrisgsh.app.repository.ClusterRepository;
import com.hrisgsh.app.service.ClusterQueryService;
import com.hrisgsh.app.service.ClusterService;
import com.hrisgsh.app.service.criteria.ClusterCriteria;
import com.hrisgsh.app.service.dto.ClusterDTO;
import com.hrisgsh.app.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.hrisgsh.app.domain.Cluster}.
 */
@RestController
@RequestMapping("/api")
public class ClusterResource {

    private final Logger log = LoggerFactory.getLogger(ClusterResource.class);

    private static final String ENTITY_NAME = "cluster";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ClusterService clusterService;

    private final ClusterRepository clusterRepository;

    private final ClusterQueryService clusterQueryService;

    public ClusterResource(ClusterService clusterService, ClusterRepository clusterRepository, ClusterQueryService clusterQueryService) {
        this.clusterService = clusterService;
        this.clusterRepository = clusterRepository;
        this.clusterQueryService = clusterQueryService;
    }

    /**
     * {@code POST  /clusters} : Create a new cluster.
     *
     * @param clusterDTO the clusterDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new clusterDTO, or with status {@code 400 (Bad Request)} if the cluster has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/clusters")
    public ResponseEntity<ClusterDTO> createCluster(@Valid @RequestBody ClusterDTO clusterDTO) throws URISyntaxException {
        log.debug("REST request to save Cluster : {}", clusterDTO);
        if (clusterDTO.getId() != null) {
            throw new BadRequestAlertException("A new cluster cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ClusterDTO result = clusterService.save(clusterDTO);
        return ResponseEntity
            .created(new URI("/api/clusters/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /clusters/:id} : Updates an existing cluster.
     *
     * @param id the id of the clusterDTO to save.
     * @param clusterDTO the clusterDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated clusterDTO,
     * or with status {@code 400 (Bad Request)} if the clusterDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the clusterDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/clusters/{id}")
    public ResponseEntity<ClusterDTO> updateCluster(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ClusterDTO clusterDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Cluster : {}, {}", id, clusterDTO);
        if (clusterDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, clusterDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!clusterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ClusterDTO result = clusterService.save(clusterDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, clusterDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /clusters/:id} : Partial updates given fields of an existing cluster, field will ignore if it is null
     *
     * @param id the id of the clusterDTO to save.
     * @param clusterDTO the clusterDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated clusterDTO,
     * or with status {@code 400 (Bad Request)} if the clusterDTO is not valid,
     * or with status {@code 404 (Not Found)} if the clusterDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the clusterDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/clusters/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ClusterDTO> partialUpdateCluster(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ClusterDTO clusterDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Cluster partially : {}, {}", id, clusterDTO);
        if (clusterDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, clusterDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!clusterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ClusterDTO> result = clusterService.partialUpdate(clusterDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, clusterDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /clusters} : get all the clusters.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of clusters in body.
     */
    @GetMapping("/clusters")
    public ResponseEntity<List<ClusterDTO>> getAllClusters(ClusterCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Clusters by criteria: {}", criteria);
        Page<ClusterDTO> page = clusterQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /clusters/count} : count all the clusters.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/clusters/count")
    public ResponseEntity<Long> countClusters(ClusterCriteria criteria) {
        log.debug("REST request to count Clusters by criteria: {}", criteria);
        return ResponseEntity.ok().body(clusterQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /clusters/:id} : get the "id" cluster.
     *
     * @param id the id of the clusterDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the clusterDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/clusters/{id}")
    public ResponseEntity<ClusterDTO> getCluster(@PathVariable Long id) {
        log.debug("REST request to get Cluster : {}", id);
        Optional<ClusterDTO> clusterDTO = clusterService.findOne(id);
        return ResponseUtil.wrapOrNotFound(clusterDTO);
    }

    /**
     * {@code DELETE  /clusters/:id} : delete the "id" cluster.
     *
     * @param id the id of the clusterDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/clusters/{id}")
    public ResponseEntity<Void> deleteCluster(@PathVariable Long id) {
        log.debug("REST request to delete Cluster : {}", id);
        clusterService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
