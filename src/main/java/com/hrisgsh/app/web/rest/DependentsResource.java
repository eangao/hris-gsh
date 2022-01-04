package com.hrisgsh.app.web.rest;

import com.hrisgsh.app.repository.DependentsRepository;
import com.hrisgsh.app.service.DependentsQueryService;
import com.hrisgsh.app.service.DependentsService;
import com.hrisgsh.app.service.criteria.DependentsCriteria;
import com.hrisgsh.app.service.dto.DependentsDTO;
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
 * REST controller for managing {@link com.hrisgsh.app.domain.Dependents}.
 */
@RestController
@RequestMapping("/api")
public class DependentsResource {

    private final Logger log = LoggerFactory.getLogger(DependentsResource.class);

    private static final String ENTITY_NAME = "dependents";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DependentsService dependentsService;

    private final DependentsRepository dependentsRepository;

    private final DependentsQueryService dependentsQueryService;

    public DependentsResource(
        DependentsService dependentsService,
        DependentsRepository dependentsRepository,
        DependentsQueryService dependentsQueryService
    ) {
        this.dependentsService = dependentsService;
        this.dependentsRepository = dependentsRepository;
        this.dependentsQueryService = dependentsQueryService;
    }

    /**
     * {@code POST  /dependents} : Create a new dependents.
     *
     * @param dependentsDTO the dependentsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dependentsDTO, or with status {@code 400 (Bad Request)} if the dependents has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/dependents")
    public ResponseEntity<DependentsDTO> createDependents(@Valid @RequestBody DependentsDTO dependentsDTO) throws URISyntaxException {
        log.debug("REST request to save Dependents : {}", dependentsDTO);
        if (dependentsDTO.getId() != null) {
            throw new BadRequestAlertException("A new dependents cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DependentsDTO result = dependentsService.save(dependentsDTO);
        return ResponseEntity
            .created(new URI("/api/dependents/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /dependents/:id} : Updates an existing dependents.
     *
     * @param id the id of the dependentsDTO to save.
     * @param dependentsDTO the dependentsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dependentsDTO,
     * or with status {@code 400 (Bad Request)} if the dependentsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dependentsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/dependents/{id}")
    public ResponseEntity<DependentsDTO> updateDependents(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DependentsDTO dependentsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Dependents : {}, {}", id, dependentsDTO);
        if (dependentsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dependentsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dependentsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DependentsDTO result = dependentsService.save(dependentsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dependentsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /dependents/:id} : Partial updates given fields of an existing dependents, field will ignore if it is null
     *
     * @param id the id of the dependentsDTO to save.
     * @param dependentsDTO the dependentsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dependentsDTO,
     * or with status {@code 400 (Bad Request)} if the dependentsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the dependentsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the dependentsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/dependents/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DependentsDTO> partialUpdateDependents(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DependentsDTO dependentsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Dependents partially : {}, {}", id, dependentsDTO);
        if (dependentsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dependentsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dependentsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DependentsDTO> result = dependentsService.partialUpdate(dependentsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dependentsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /dependents} : get all the dependents.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dependents in body.
     */
    @GetMapping("/dependents")
    public ResponseEntity<List<DependentsDTO>> getAllDependents(DependentsCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Dependents by criteria: {}", criteria);
        Page<DependentsDTO> page = dependentsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /dependents/count} : count all the dependents.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/dependents/count")
    public ResponseEntity<Long> countDependents(DependentsCriteria criteria) {
        log.debug("REST request to count Dependents by criteria: {}", criteria);
        return ResponseEntity.ok().body(dependentsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /dependents/:id} : get the "id" dependents.
     *
     * @param id the id of the dependentsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dependentsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/dependents/{id}")
    public ResponseEntity<DependentsDTO> getDependents(@PathVariable Long id) {
        log.debug("REST request to get Dependents : {}", id);
        Optional<DependentsDTO> dependentsDTO = dependentsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dependentsDTO);
    }

    /**
     * {@code DELETE  /dependents/:id} : delete the "id" dependents.
     *
     * @param id the id of the dependentsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/dependents/{id}")
    public ResponseEntity<Void> deleteDependents(@PathVariable Long id) {
        log.debug("REST request to delete Dependents : {}", id);
        dependentsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
