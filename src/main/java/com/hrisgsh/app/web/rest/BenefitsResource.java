package com.hrisgsh.app.web.rest;

import com.hrisgsh.app.repository.BenefitsRepository;
import com.hrisgsh.app.service.BenefitsQueryService;
import com.hrisgsh.app.service.BenefitsService;
import com.hrisgsh.app.service.criteria.BenefitsCriteria;
import com.hrisgsh.app.service.dto.BenefitsDTO;
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
 * REST controller for managing {@link com.hrisgsh.app.domain.Benefits}.
 */
@RestController
@RequestMapping("/api")
public class BenefitsResource {

    private final Logger log = LoggerFactory.getLogger(BenefitsResource.class);

    private static final String ENTITY_NAME = "benefits";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BenefitsService benefitsService;

    private final BenefitsRepository benefitsRepository;

    private final BenefitsQueryService benefitsQueryService;

    public BenefitsResource(
        BenefitsService benefitsService,
        BenefitsRepository benefitsRepository,
        BenefitsQueryService benefitsQueryService
    ) {
        this.benefitsService = benefitsService;
        this.benefitsRepository = benefitsRepository;
        this.benefitsQueryService = benefitsQueryService;
    }

    /**
     * {@code POST  /benefits} : Create a new benefits.
     *
     * @param benefitsDTO the benefitsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new benefitsDTO, or with status {@code 400 (Bad Request)} if the benefits has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/benefits")
    public ResponseEntity<BenefitsDTO> createBenefits(@Valid @RequestBody BenefitsDTO benefitsDTO) throws URISyntaxException {
        log.debug("REST request to save Benefits : {}", benefitsDTO);
        if (benefitsDTO.getId() != null) {
            throw new BadRequestAlertException("A new benefits cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BenefitsDTO result = benefitsService.save(benefitsDTO);
        return ResponseEntity
            .created(new URI("/api/benefits/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /benefits/:id} : Updates an existing benefits.
     *
     * @param id the id of the benefitsDTO to save.
     * @param benefitsDTO the benefitsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated benefitsDTO,
     * or with status {@code 400 (Bad Request)} if the benefitsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the benefitsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/benefits/{id}")
    public ResponseEntity<BenefitsDTO> updateBenefits(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BenefitsDTO benefitsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Benefits : {}, {}", id, benefitsDTO);
        if (benefitsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, benefitsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!benefitsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BenefitsDTO result = benefitsService.save(benefitsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, benefitsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /benefits/:id} : Partial updates given fields of an existing benefits, field will ignore if it is null
     *
     * @param id the id of the benefitsDTO to save.
     * @param benefitsDTO the benefitsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated benefitsDTO,
     * or with status {@code 400 (Bad Request)} if the benefitsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the benefitsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the benefitsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/benefits/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BenefitsDTO> partialUpdateBenefits(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BenefitsDTO benefitsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Benefits partially : {}, {}", id, benefitsDTO);
        if (benefitsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, benefitsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!benefitsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BenefitsDTO> result = benefitsService.partialUpdate(benefitsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, benefitsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /benefits} : get all the benefits.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of benefits in body.
     */
    @GetMapping("/benefits")
    public ResponseEntity<List<BenefitsDTO>> getAllBenefits(BenefitsCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Benefits by criteria: {}", criteria);
        Page<BenefitsDTO> page = benefitsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /benefits/count} : count all the benefits.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/benefits/count")
    public ResponseEntity<Long> countBenefits(BenefitsCriteria criteria) {
        log.debug("REST request to count Benefits by criteria: {}", criteria);
        return ResponseEntity.ok().body(benefitsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /benefits/:id} : get the "id" benefits.
     *
     * @param id the id of the benefitsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the benefitsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/benefits/{id}")
    public ResponseEntity<BenefitsDTO> getBenefits(@PathVariable Long id) {
        log.debug("REST request to get Benefits : {}", id);
        Optional<BenefitsDTO> benefitsDTO = benefitsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(benefitsDTO);
    }

    /**
     * {@code DELETE  /benefits/:id} : delete the "id" benefits.
     *
     * @param id the id of the benefitsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/benefits/{id}")
    public ResponseEntity<Void> deleteBenefits(@PathVariable Long id) {
        log.debug("REST request to delete Benefits : {}", id);
        benefitsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
