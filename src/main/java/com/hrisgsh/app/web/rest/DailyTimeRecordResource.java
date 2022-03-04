package com.hrisgsh.app.web.rest;

import static org.springframework.http.HttpStatus.OK;

import com.hrisgsh.app.repository.DailyTimeRecordRepository;
import com.hrisgsh.app.service.DailyTimeRecordQueryService;
import com.hrisgsh.app.service.DailyTimeRecordService;
import com.hrisgsh.app.service.criteria.DailyTimeRecordCriteria;
import com.hrisgsh.app.service.dto.DailyTimeRecordDTO;
import com.hrisgsh.app.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
/////////////////////////////////////////////
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
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
 * REST controller for managing {@link com.hrisgsh.app.domain.DailyTimeRecord}.
 */
@RestController
@RequestMapping("/api")
public class DailyTimeRecordResource {

    private final Logger log = LoggerFactory.getLogger(DailyTimeRecordResource.class);

    private static final String ENTITY_NAME = "dailyTimeRecord";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DailyTimeRecordService dailyTimeRecordService;

    private final DailyTimeRecordRepository dailyTimeRecordRepository;

    private final DailyTimeRecordQueryService dailyTimeRecordQueryService;

    public DailyTimeRecordResource(
        DailyTimeRecordService dailyTimeRecordService,
        DailyTimeRecordRepository dailyTimeRecordRepository,
        DailyTimeRecordQueryService dailyTimeRecordQueryService
    ) {
        this.dailyTimeRecordService = dailyTimeRecordService;
        this.dailyTimeRecordRepository = dailyTimeRecordRepository;
        this.dailyTimeRecordQueryService = dailyTimeRecordQueryService;
    }

    /**
     * {@code POST  /daily-time-records} : Create a new dailyTimeRecord.
     *
     * @param dailyTimeRecordDTO the dailyTimeRecordDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dailyTimeRecordDTO, or with status {@code 400 (Bad Request)} if the dailyTimeRecord has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/daily-time-records")
    public ResponseEntity<DailyTimeRecordDTO> createDailyTimeRecord(@Valid @RequestBody DailyTimeRecordDTO dailyTimeRecordDTO)
        throws URISyntaxException {
        log.debug("REST request to save DailyTimeRecord : {}", dailyTimeRecordDTO);
        if (dailyTimeRecordDTO.getId() != null) {
            throw new BadRequestAlertException("A new dailyTimeRecord cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DailyTimeRecordDTO result = dailyTimeRecordService.save(dailyTimeRecordDTO);
        return ResponseEntity
            .created(new URI("/api/daily-time-records/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PostMapping("/biometric-punch-log")
    public ResponseEntity<String> addBiometricPunchLog(@RequestBody String biometricData)
        throws ParseException, java.text.ParseException, Exception {
        Logger logger = LoggerFactory.getLogger((this.getClass()));

        //https://stackoverflow.com/questions/36549315/how-to-access-nested-elements-of-json-data-in-java
        //tutorial
        //Using the JSON simple library parse the string into a json object
        JSONParser parse = new JSONParser();
        JSONObject biometricDataObject = (JSONObject) parse.parse(biometricData);
        JSONObject realTime = (JSONObject) parse.parse(biometricDataObject.get("RealTime").toString());

        //camsunit api return sent data. if has punchlog or UserUpdated for adding/editing user information.
        //We will capture only the punchLog data for the meantime
        //https://camsunit.com/application/biometric-web-api-sample-request-response.html?operation=RealTimePunchLog
        //status: done to comply to camsunit biometric time attendance
        if (realTime.get("PunchLog") == null) {
            return new ResponseEntity<>("done", HttpStatus.OK);
        }

        JSONObject punchLog = (JSONObject) parse.parse(realTime.get("PunchLog").toString());

        Long employeeId = Long.parseLong(punchLog.get("UserId").toString());
        String inputType = punchLog.get("InputType").toString();

        String temperature = "";
        //cams biometric return temperature even without using face.
        //Use this code while this bug not yet solve
        if (inputType.equalsIgnoreCase("Face")) {
            temperature = punchLog.get("Temperature").toString();
        }

        //NOTE   camsunit jsn sometimes return AttendanceType sometimes return Type
        String attendanceType = "";
        if (punchLog.get("AttendanceType") != null) {
            attendanceType = punchLog.get("AttendanceType").toString();
        } else if (punchLog.get("Type") != null) {
            attendanceType = punchLog.get("Type").toString();
        }

        //Change the value of attendance type
        //From CheckIn/CheckOut   to   In/Out
        if (attendanceType.equalsIgnoreCase("CheckOut")) {
            attendanceType = "Out";
        } else if (attendanceType.equalsIgnoreCase("CheckIn")) {
            attendanceType = "In";
        }

        //Experience challenges in saving time save in the database not same value
        //Temporarily save date and time as String data type
        //If solution is found, date and time will be save as Date data type
        // ============================================================================================ ==========================
        //https://stackoverflow.com/questions/18915075/java-convert-string-to-timestamp
        //        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //        // you can change format of date
        //        Date logTime = dateFormat.parse(punchLog.get("LogTime").toString().substring(0, 19));//
        //        String dateInString = punchLog.get("LogTime").toString().substring(0, 19);
        //        Date date = DateUtils.parseDate(dateInString, "yyyy-MM-dd HH:mm:ss");
        //        logger.info("date {}", date);
        //        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        //        formatter.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        //        String dateInString = punchLog.get("LogTime").toString().substring(0, 19);
        //        Date date = formatter.parse(dateInString);
        //        String formattedDateString = formatter.format(date);
        //=========================================================================================================================

        String logDate = punchLog.get("LogTime").toString().substring(0, 10);
        String logTime = punchLog.get("LogTime").toString().substring(11, 19);

        DailyTimeRecordDTO dailyTimeRecordDTO = new DailyTimeRecordDTO();

        dailyTimeRecordDTO.setEmployeeBiometricId(employeeId);
        dailyTimeRecordDTO.setInputType(inputType);
        dailyTimeRecordDTO.setAttendanceType(attendanceType);
        dailyTimeRecordDTO.setTemperature(temperature);
        dailyTimeRecordDTO.setLogDate(logDate);
        dailyTimeRecordDTO.setLogTime(logTime);

        dailyTimeRecordService.save(dailyTimeRecordDTO);

        //https://camsunit.com/application/biometric-web-api-sample-request-response.html?operation=RealTimePunchLog
        //status: done to comply to camsunit biometric time attendance
        return new ResponseEntity<>("done", HttpStatus.OK);
    }

    /**
     * {@code PUT  /daily-time-records/:id} : Updates an existing dailyTimeRecord.
     *
     * @param id the id of the dailyTimeRecordDTO to save.
     * @param dailyTimeRecordDTO the dailyTimeRecordDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dailyTimeRecordDTO,
     * or with status {@code 400 (Bad Request)} if the dailyTimeRecordDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dailyTimeRecordDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/daily-time-records/{id}")
    public ResponseEntity<DailyTimeRecordDTO> updateDailyTimeRecord(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DailyTimeRecordDTO dailyTimeRecordDTO
    ) throws URISyntaxException {
        log.debug("REST request to update DailyTimeRecord : {}, {}", id, dailyTimeRecordDTO);
        if (dailyTimeRecordDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dailyTimeRecordDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dailyTimeRecordRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DailyTimeRecordDTO result = dailyTimeRecordService.save(dailyTimeRecordDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dailyTimeRecordDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /daily-time-records/:id} : Partial updates given fields of an existing dailyTimeRecord, field will ignore if it is null
     *
     * @param id the id of the dailyTimeRecordDTO to save.
     * @param dailyTimeRecordDTO the dailyTimeRecordDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dailyTimeRecordDTO,
     * or with status {@code 400 (Bad Request)} if the dailyTimeRecordDTO is not valid,
     * or with status {@code 404 (Not Found)} if the dailyTimeRecordDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the dailyTimeRecordDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/daily-time-records/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DailyTimeRecordDTO> partialUpdateDailyTimeRecord(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DailyTimeRecordDTO dailyTimeRecordDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update DailyTimeRecord partially : {}, {}", id, dailyTimeRecordDTO);
        if (dailyTimeRecordDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dailyTimeRecordDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dailyTimeRecordRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DailyTimeRecordDTO> result = dailyTimeRecordService.partialUpdate(dailyTimeRecordDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dailyTimeRecordDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /daily-time-records} : get all the dailyTimeRecords.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dailyTimeRecords in body.
     */
    @GetMapping("/daily-time-records")
    public ResponseEntity<List<DailyTimeRecordDTO>> getAllDailyTimeRecords(DailyTimeRecordCriteria criteria, Pageable pageable) {
        log.debug("REST request to get DailyTimeRecords by criteria: {}", criteria);
        Page<DailyTimeRecordDTO> page = dailyTimeRecordQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /daily-time-records/count} : count all the dailyTimeRecords.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/daily-time-records/count")
    public ResponseEntity<Long> countDailyTimeRecords(DailyTimeRecordCriteria criteria) {
        log.debug("REST request to count DailyTimeRecords by criteria: {}", criteria);
        return ResponseEntity.ok().body(dailyTimeRecordQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /daily-time-records/:id} : get the "id" dailyTimeRecord.
     *
     * @param id the id of the dailyTimeRecordDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dailyTimeRecordDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/daily-time-records/{id}")
    public ResponseEntity<DailyTimeRecordDTO> getDailyTimeRecord(@PathVariable Long id) {
        log.debug("REST request to get DailyTimeRecord : {}", id);
        Optional<DailyTimeRecordDTO> dailyTimeRecordDTO = dailyTimeRecordService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dailyTimeRecordDTO);
    }

    /**
     * {@code DELETE  /daily-time-records/:id} : delete the "id" dailyTimeRecord.
     *
     * @param id the id of the dailyTimeRecordDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/daily-time-records/{id}")
    public ResponseEntity<Void> deleteDailyTimeRecord(@PathVariable Long id) {
        log.debug("REST request to delete DailyTimeRecord : {}", id);
        dailyTimeRecordService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
