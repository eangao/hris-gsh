package com.hrisgsh.app.web.rest;

import com.hrisgsh.app.repository.DutyScheduleRepository;
import com.hrisgsh.app.service.DutyScheduleQueryService;
import com.hrisgsh.app.service.DutyScheduleService;
import com.hrisgsh.app.service.criteria.DutyScheduleCriteria;
import com.hrisgsh.app.service.dto.DutyScheduleDTO;
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
 * REST controller for managing {@link com.hrisgsh.app.domain.DutySchedule}.
 */
@RestController
@RequestMapping("/api")
public class DutyScheduleResource {

    private final Logger log = LoggerFactory.getLogger(DutyScheduleResource.class);

    private static final String ENTITY_NAME = "dutySchedule";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DutyScheduleService dutyScheduleService;

    private final DutyScheduleRepository dutyScheduleRepository;

    private final DutyScheduleQueryService dutyScheduleQueryService;

    public DutyScheduleResource(
        DutyScheduleService dutyScheduleService,
        DutyScheduleRepository dutyScheduleRepository,
        DutyScheduleQueryService dutyScheduleQueryService
    ) {
        this.dutyScheduleService = dutyScheduleService;
        this.dutyScheduleRepository = dutyScheduleRepository;
        this.dutyScheduleQueryService = dutyScheduleQueryService;
    }

    /**
     * {@code POST  /duty-schedules} : Create a new dutySchedule.
     *
     * @param dutyScheduleDTO the dutyScheduleDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dutyScheduleDTO, or with status {@code 400 (Bad Request)} if the dutySchedule has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/duty-schedules")
    public ResponseEntity<DutyScheduleDTO> createDutySchedule(@Valid @RequestBody DutyScheduleDTO dutyScheduleDTO)
        throws URISyntaxException {
        log.debug("REST request to save DutySchedule : {}", dutyScheduleDTO);
        if (dutyScheduleDTO.getId() != null) {
            throw new BadRequestAlertException("A new dutySchedule cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DutyScheduleDTO result = dutyScheduleService.save(dutyScheduleDTO);
        return ResponseEntity
            .created(new URI("/api/duty-schedules/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /duty-schedules/:id} : Updates an existing dutySchedule.
     *
     * @param id the id of the dutyScheduleDTO to save.
     * @param dutyScheduleDTO the dutyScheduleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dutyScheduleDTO,
     * or with status {@code 400 (Bad Request)} if the dutyScheduleDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dutyScheduleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/duty-schedules/{id}")
    public ResponseEntity<DutyScheduleDTO> updateDutySchedule(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DutyScheduleDTO dutyScheduleDTO
    ) throws URISyntaxException {
        log.debug("REST request to update DutySchedule : {}, {}", id, dutyScheduleDTO);
        if (dutyScheduleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dutyScheduleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dutyScheduleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DutyScheduleDTO result = dutyScheduleService.save(dutyScheduleDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dutyScheduleDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /duty-schedules/:id} : Partial updates given fields of an existing dutySchedule, field will ignore if it is null
     *
     * @param id the id of the dutyScheduleDTO to save.
     * @param dutyScheduleDTO the dutyScheduleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dutyScheduleDTO,
     * or with status {@code 400 (Bad Request)} if the dutyScheduleDTO is not valid,
     * or with status {@code 404 (Not Found)} if the dutyScheduleDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the dutyScheduleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/duty-schedules/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DutyScheduleDTO> partialUpdateDutySchedule(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DutyScheduleDTO dutyScheduleDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update DutySchedule partially : {}, {}", id, dutyScheduleDTO);
        if (dutyScheduleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dutyScheduleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dutyScheduleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DutyScheduleDTO> result = dutyScheduleService.partialUpdate(dutyScheduleDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dutyScheduleDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /duty-schedules} : get all the dutySchedules.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dutySchedules in body.
     */
    @GetMapping("/duty-schedules")
    public ResponseEntity<List<DutyScheduleDTO>> getAllDutySchedules(DutyScheduleCriteria criteria, Pageable pageable) {
        log.debug("REST request to get DutySchedules by criteria: {}", criteria);
        Page<DutyScheduleDTO> page = dutyScheduleQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /duty-schedules/count} : count all the dutySchedules.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/duty-schedules/count")
    public ResponseEntity<Long> countDutySchedules(DutyScheduleCriteria criteria) {
        log.debug("REST request to count DutySchedules by criteria: {}", criteria);
        return ResponseEntity.ok().body(dutyScheduleQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /duty-schedules/:id} : get the "id" dutySchedule.
     *
     * @param id the id of the dutyScheduleDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dutyScheduleDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/duty-schedules/{id}")
    public ResponseEntity<DutyScheduleDTO> getDutySchedule(@PathVariable Long id) {
        log.debug("REST request to get DutySchedule : {}", id);
        Optional<DutyScheduleDTO> dutyScheduleDTO = dutyScheduleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dutyScheduleDTO);
    }

    /**
     * {@code DELETE  /duty-schedules/:id} : delete the "id" dutySchedule.
     *
     * @param id the id of the dutyScheduleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/duty-schedules/{id}")
    public ResponseEntity<Void> deleteDutySchedule(@PathVariable Long id) {
        log.debug("REST request to delete DutySchedule : {}", id);
        dutyScheduleService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
