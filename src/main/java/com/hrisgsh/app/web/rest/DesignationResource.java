package com.hrisgsh.app.web.rest;

import com.hrisgsh.app.repository.DesignationRepository;
import com.hrisgsh.app.service.DesignationQueryService;
import com.hrisgsh.app.service.DesignationService;
import com.hrisgsh.app.service.criteria.DesignationCriteria;
import com.hrisgsh.app.service.dto.DesignationDTO;
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
 * REST controller for managing {@link com.hrisgsh.app.domain.Designation}.
 */
@RestController
@RequestMapping("/api")
public class DesignationResource {

    private final Logger log = LoggerFactory.getLogger(DesignationResource.class);

    private static final String ENTITY_NAME = "designation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DesignationService designationService;

    private final DesignationRepository designationRepository;

    private final DesignationQueryService designationQueryService;

    public DesignationResource(
        DesignationService designationService,
        DesignationRepository designationRepository,
        DesignationQueryService designationQueryService
    ) {
        this.designationService = designationService;
        this.designationRepository = designationRepository;
        this.designationQueryService = designationQueryService;
    }

    /**
     * {@code POST  /designations} : Create a new designation.
     *
     * @param designationDTO the designationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new designationDTO, or with status {@code 400 (Bad Request)} if the designation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/designations")
    public ResponseEntity<DesignationDTO> createDesignation(@Valid @RequestBody DesignationDTO designationDTO) throws URISyntaxException {
        log.debug("REST request to save Designation : {}", designationDTO);
        if (designationDTO.getId() != null) {
            throw new BadRequestAlertException("A new designation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DesignationDTO result = designationService.save(designationDTO);
        return ResponseEntity
            .created(new URI("/api/designations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /designations/:id} : Updates an existing designation.
     *
     * @param id the id of the designationDTO to save.
     * @param designationDTO the designationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated designationDTO,
     * or with status {@code 400 (Bad Request)} if the designationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the designationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/designations/{id}")
    public ResponseEntity<DesignationDTO> updateDesignation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DesignationDTO designationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Designation : {}, {}", id, designationDTO);
        if (designationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, designationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!designationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DesignationDTO result = designationService.save(designationDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, designationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /designations/:id} : Partial updates given fields of an existing designation, field will ignore if it is null
     *
     * @param id the id of the designationDTO to save.
     * @param designationDTO the designationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated designationDTO,
     * or with status {@code 400 (Bad Request)} if the designationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the designationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the designationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/designations/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DesignationDTO> partialUpdateDesignation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DesignationDTO designationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Designation partially : {}, {}", id, designationDTO);
        if (designationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, designationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!designationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DesignationDTO> result = designationService.partialUpdate(designationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, designationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /designations} : get all the designations.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of designations in body.
     */
    @GetMapping("/designations")
    public ResponseEntity<List<DesignationDTO>> getAllDesignations(DesignationCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Designations by criteria: {}", criteria);
        Page<DesignationDTO> page = designationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /designations/count} : count all the designations.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/designations/count")
    public ResponseEntity<Long> countDesignations(DesignationCriteria criteria) {
        log.debug("REST request to count Designations by criteria: {}", criteria);
        return ResponseEntity.ok().body(designationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /designations/:id} : get the "id" designation.
     *
     * @param id the id of the designationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the designationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/designations/{id}")
    public ResponseEntity<DesignationDTO> getDesignation(@PathVariable Long id) {
        log.debug("REST request to get Designation : {}", id);
        Optional<DesignationDTO> designationDTO = designationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(designationDTO);
    }

    /**
     * {@code DELETE  /designations/:id} : delete the "id" designation.
     *
     * @param id the id of the designationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/designations/{id}")
    public ResponseEntity<Void> deleteDesignation(@PathVariable Long id) {
        log.debug("REST request to delete Designation : {}", id);
        designationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
