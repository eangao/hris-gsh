package com.hrisgsh.app.web.rest;

import com.hrisgsh.app.repository.TrainingHistoryRepository;
import com.hrisgsh.app.service.TrainingHistoryQueryService;
import com.hrisgsh.app.service.TrainingHistoryService;
import com.hrisgsh.app.service.criteria.TrainingHistoryCriteria;
import com.hrisgsh.app.service.dto.TrainingHistoryDTO;
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
 * REST controller for managing {@link com.hrisgsh.app.domain.TrainingHistory}.
 */
@RestController
@RequestMapping("/api")
public class TrainingHistoryResource {

    private final Logger log = LoggerFactory.getLogger(TrainingHistoryResource.class);

    private static final String ENTITY_NAME = "trainingHistory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TrainingHistoryService trainingHistoryService;

    private final TrainingHistoryRepository trainingHistoryRepository;

    private final TrainingHistoryQueryService trainingHistoryQueryService;

    public TrainingHistoryResource(
        TrainingHistoryService trainingHistoryService,
        TrainingHistoryRepository trainingHistoryRepository,
        TrainingHistoryQueryService trainingHistoryQueryService
    ) {
        this.trainingHistoryService = trainingHistoryService;
        this.trainingHistoryRepository = trainingHistoryRepository;
        this.trainingHistoryQueryService = trainingHistoryQueryService;
    }

    /**
     * {@code POST  /training-histories} : Create a new trainingHistory.
     *
     * @param trainingHistoryDTO the trainingHistoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new trainingHistoryDTO, or with status {@code 400 (Bad Request)} if the trainingHistory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/training-histories")
    public ResponseEntity<TrainingHistoryDTO> createTrainingHistory(@Valid @RequestBody TrainingHistoryDTO trainingHistoryDTO)
        throws URISyntaxException {
        log.debug("REST request to save TrainingHistory : {}", trainingHistoryDTO);
        if (trainingHistoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new trainingHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TrainingHistoryDTO result = trainingHistoryService.save(trainingHistoryDTO);
        return ResponseEntity
            .created(new URI("/api/training-histories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /training-histories/:id} : Updates an existing trainingHistory.
     *
     * @param id the id of the trainingHistoryDTO to save.
     * @param trainingHistoryDTO the trainingHistoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated trainingHistoryDTO,
     * or with status {@code 400 (Bad Request)} if the trainingHistoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the trainingHistoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/training-histories/{id}")
    public ResponseEntity<TrainingHistoryDTO> updateTrainingHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TrainingHistoryDTO trainingHistoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TrainingHistory : {}, {}", id, trainingHistoryDTO);
        if (trainingHistoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, trainingHistoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!trainingHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TrainingHistoryDTO result = trainingHistoryService.save(trainingHistoryDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, trainingHistoryDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /training-histories/:id} : Partial updates given fields of an existing trainingHistory, field will ignore if it is null
     *
     * @param id the id of the trainingHistoryDTO to save.
     * @param trainingHistoryDTO the trainingHistoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated trainingHistoryDTO,
     * or with status {@code 400 (Bad Request)} if the trainingHistoryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the trainingHistoryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the trainingHistoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/training-histories/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TrainingHistoryDTO> partialUpdateTrainingHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TrainingHistoryDTO trainingHistoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TrainingHistory partially : {}, {}", id, trainingHistoryDTO);
        if (trainingHistoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, trainingHistoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!trainingHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TrainingHistoryDTO> result = trainingHistoryService.partialUpdate(trainingHistoryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, trainingHistoryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /training-histories} : get all the trainingHistories.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of trainingHistories in body.
     */
    @GetMapping("/training-histories")
    public ResponseEntity<List<TrainingHistoryDTO>> getAllTrainingHistories(TrainingHistoryCriteria criteria, Pageable pageable) {
        log.debug("REST request to get TrainingHistories by criteria: {}", criteria);
        Page<TrainingHistoryDTO> page = trainingHistoryQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /training-histories/count} : count all the trainingHistories.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/training-histories/count")
    public ResponseEntity<Long> countTrainingHistories(TrainingHistoryCriteria criteria) {
        log.debug("REST request to count TrainingHistories by criteria: {}", criteria);
        return ResponseEntity.ok().body(trainingHistoryQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /training-histories/:id} : get the "id" trainingHistory.
     *
     * @param id the id of the trainingHistoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the trainingHistoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/training-histories/{id}")
    public ResponseEntity<TrainingHistoryDTO> getTrainingHistory(@PathVariable Long id) {
        log.debug("REST request to get TrainingHistory : {}", id);
        Optional<TrainingHistoryDTO> trainingHistoryDTO = trainingHistoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(trainingHistoryDTO);
    }

    /**
     * {@code DELETE  /training-histories/:id} : delete the "id" trainingHistory.
     *
     * @param id the id of the trainingHistoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/training-histories/{id}")
    public ResponseEntity<Void> deleteTrainingHistory(@PathVariable Long id) {
        log.debug("REST request to delete TrainingHistory : {}", id);
        trainingHistoryService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
