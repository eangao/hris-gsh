package com.hrisgsh.app.web.rest;

import com.hrisgsh.app.repository.HolidayTypeRepository;
import com.hrisgsh.app.service.HolidayTypeQueryService;
import com.hrisgsh.app.service.HolidayTypeService;
import com.hrisgsh.app.service.criteria.HolidayTypeCriteria;
import com.hrisgsh.app.service.dto.HolidayTypeDTO;
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
 * REST controller for managing {@link com.hrisgsh.app.domain.HolidayType}.
 */
@RestController
@RequestMapping("/api")
public class HolidayTypeResource {

    private final Logger log = LoggerFactory.getLogger(HolidayTypeResource.class);

    private static final String ENTITY_NAME = "holidayType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HolidayTypeService holidayTypeService;

    private final HolidayTypeRepository holidayTypeRepository;

    private final HolidayTypeQueryService holidayTypeQueryService;

    public HolidayTypeResource(
        HolidayTypeService holidayTypeService,
        HolidayTypeRepository holidayTypeRepository,
        HolidayTypeQueryService holidayTypeQueryService
    ) {
        this.holidayTypeService = holidayTypeService;
        this.holidayTypeRepository = holidayTypeRepository;
        this.holidayTypeQueryService = holidayTypeQueryService;
    }

    /**
     * {@code POST  /holiday-types} : Create a new holidayType.
     *
     * @param holidayTypeDTO the holidayTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new holidayTypeDTO, or with status {@code 400 (Bad Request)} if the holidayType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/holiday-types")
    public ResponseEntity<HolidayTypeDTO> createHolidayType(@Valid @RequestBody HolidayTypeDTO holidayTypeDTO) throws URISyntaxException {
        log.debug("REST request to save HolidayType : {}", holidayTypeDTO);
        if (holidayTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new holidayType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HolidayTypeDTO result = holidayTypeService.save(holidayTypeDTO);
        return ResponseEntity
            .created(new URI("/api/holiday-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /holiday-types/:id} : Updates an existing holidayType.
     *
     * @param id the id of the holidayTypeDTO to save.
     * @param holidayTypeDTO the holidayTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated holidayTypeDTO,
     * or with status {@code 400 (Bad Request)} if the holidayTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the holidayTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/holiday-types/{id}")
    public ResponseEntity<HolidayTypeDTO> updateHolidayType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody HolidayTypeDTO holidayTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update HolidayType : {}, {}", id, holidayTypeDTO);
        if (holidayTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, holidayTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!holidayTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        HolidayTypeDTO result = holidayTypeService.save(holidayTypeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, holidayTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /holiday-types/:id} : Partial updates given fields of an existing holidayType, field will ignore if it is null
     *
     * @param id the id of the holidayTypeDTO to save.
     * @param holidayTypeDTO the holidayTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated holidayTypeDTO,
     * or with status {@code 400 (Bad Request)} if the holidayTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the holidayTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the holidayTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/holiday-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<HolidayTypeDTO> partialUpdateHolidayType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody HolidayTypeDTO holidayTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update HolidayType partially : {}, {}", id, holidayTypeDTO);
        if (holidayTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, holidayTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!holidayTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HolidayTypeDTO> result = holidayTypeService.partialUpdate(holidayTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, holidayTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /holiday-types} : get all the holidayTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of holidayTypes in body.
     */
    @GetMapping("/holiday-types")
    public ResponseEntity<List<HolidayTypeDTO>> getAllHolidayTypes(HolidayTypeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get HolidayTypes by criteria: {}", criteria);
        Page<HolidayTypeDTO> page = holidayTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /holiday-types/count} : count all the holidayTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/holiday-types/count")
    public ResponseEntity<Long> countHolidayTypes(HolidayTypeCriteria criteria) {
        log.debug("REST request to count HolidayTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(holidayTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /holiday-types/:id} : get the "id" holidayType.
     *
     * @param id the id of the holidayTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the holidayTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/holiday-types/{id}")
    public ResponseEntity<HolidayTypeDTO> getHolidayType(@PathVariable Long id) {
        log.debug("REST request to get HolidayType : {}", id);
        Optional<HolidayTypeDTO> holidayTypeDTO = holidayTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(holidayTypeDTO);
    }

    /**
     * {@code DELETE  /holiday-types/:id} : delete the "id" holidayType.
     *
     * @param id the id of the holidayTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/holiday-types/{id}")
    public ResponseEntity<Void> deleteHolidayType(@PathVariable Long id) {
        log.debug("REST request to delete HolidayType : {}", id);
        holidayTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
