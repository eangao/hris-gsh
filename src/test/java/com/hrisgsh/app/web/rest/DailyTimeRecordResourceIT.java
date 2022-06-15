package com.hrisgsh.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.hrisgsh.app.IntegrationTest;
import com.hrisgsh.app.domain.DailyTimeRecord;
import com.hrisgsh.app.domain.Employee;
import com.hrisgsh.app.repository.DailyTimeRecordRepository;
import com.hrisgsh.app.service.criteria.DailyTimeRecordCriteria;
import com.hrisgsh.app.service.dto.DailyTimeRecordDTO;
import com.hrisgsh.app.service.mapper.DailyTimeRecordMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link DailyTimeRecordResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DailyTimeRecordResourceIT {

    private static final Long DEFAULT_EMPLOYEE_BIOMETRIC_ID = 1L;
    private static final Long UPDATED_EMPLOYEE_BIOMETRIC_ID = 2L;
    private static final Long SMALLER_EMPLOYEE_BIOMETRIC_ID = 1L - 1L;

    private static final String DEFAULT_INPUT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_INPUT_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_ATTENDANCE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_ATTENDANCE_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_TEMPERATURE = "AAAAAAAAAA";
    private static final String UPDATED_TEMPERATURE = "BBBBBBBBBB";

    private static final String DEFAULT_LOG_DATE = "AAAAAAAAAA";
    private static final String UPDATED_LOG_DATE = "BBBBBBBBBB";

    private static final String DEFAULT_LOG_TIME = "AAAAAAAAAA";
    private static final String UPDATED_LOG_TIME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/daily-time-records";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DailyTimeRecordRepository dailyTimeRecordRepository;

    @Autowired
    private DailyTimeRecordMapper dailyTimeRecordMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDailyTimeRecordMockMvc;

    private DailyTimeRecord dailyTimeRecord;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DailyTimeRecord createEntity(EntityManager em) {
        DailyTimeRecord dailyTimeRecord = new DailyTimeRecord()
            .employeeBiometricId(DEFAULT_EMPLOYEE_BIOMETRIC_ID)
            .inputType(DEFAULT_INPUT_TYPE)
            .attendanceType(DEFAULT_ATTENDANCE_TYPE)
            .temperature(DEFAULT_TEMPERATURE)
            .logDate(DEFAULT_LOG_DATE)
            .logTime(DEFAULT_LOG_TIME);
        // Add required entity
        Employee employee;
        if (TestUtil.findAll(em, Employee.class).isEmpty()) {
            employee = EmployeeResourceIT.createEntity(em);
            em.persist(employee);
            em.flush();
        } else {
            employee = TestUtil.findAll(em, Employee.class).get(0);
        }
        dailyTimeRecord.setEmployee(employee);
        return dailyTimeRecord;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DailyTimeRecord createUpdatedEntity(EntityManager em) {
        DailyTimeRecord dailyTimeRecord = new DailyTimeRecord()
            .employeeBiometricId(UPDATED_EMPLOYEE_BIOMETRIC_ID)
            .inputType(UPDATED_INPUT_TYPE)
            .attendanceType(UPDATED_ATTENDANCE_TYPE)
            .temperature(UPDATED_TEMPERATURE)
            .logDate(UPDATED_LOG_DATE)
            .logTime(UPDATED_LOG_TIME);
        // Add required entity
        Employee employee;
        if (TestUtil.findAll(em, Employee.class).isEmpty()) {
            employee = EmployeeResourceIT.createUpdatedEntity(em);
            em.persist(employee);
            em.flush();
        } else {
            employee = TestUtil.findAll(em, Employee.class).get(0);
        }
        dailyTimeRecord.setEmployee(employee);
        return dailyTimeRecord;
    }

    @BeforeEach
    public void initTest() {
        dailyTimeRecord = createEntity(em);
    }

    @Test
    @Transactional
    void createDailyTimeRecord() throws Exception {
        int databaseSizeBeforeCreate = dailyTimeRecordRepository.findAll().size();
        // Create the DailyTimeRecord
        DailyTimeRecordDTO dailyTimeRecordDTO = dailyTimeRecordMapper.toDto(dailyTimeRecord);
        restDailyTimeRecordMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dailyTimeRecordDTO))
            )
            .andExpect(status().isCreated());

        // Validate the DailyTimeRecord in the database
        List<DailyTimeRecord> dailyTimeRecordList = dailyTimeRecordRepository.findAll();
        assertThat(dailyTimeRecordList).hasSize(databaseSizeBeforeCreate + 1);
        DailyTimeRecord testDailyTimeRecord = dailyTimeRecordList.get(dailyTimeRecordList.size() - 1);
        assertThat(testDailyTimeRecord.getEmployeeBiometricId()).isEqualTo(DEFAULT_EMPLOYEE_BIOMETRIC_ID);
        assertThat(testDailyTimeRecord.getInputType()).isEqualTo(DEFAULT_INPUT_TYPE);
        assertThat(testDailyTimeRecord.getAttendanceType()).isEqualTo(DEFAULT_ATTENDANCE_TYPE);
        assertThat(testDailyTimeRecord.getTemperature()).isEqualTo(DEFAULT_TEMPERATURE);
        assertThat(testDailyTimeRecord.getLogDate()).isEqualTo(DEFAULT_LOG_DATE);
        assertThat(testDailyTimeRecord.getLogTime()).isEqualTo(DEFAULT_LOG_TIME);
    }

    @Test
    @Transactional
    void createDailyTimeRecordWithExistingId() throws Exception {
        // Create the DailyTimeRecord with an existing ID
        dailyTimeRecord.setId(1L);
        DailyTimeRecordDTO dailyTimeRecordDTO = dailyTimeRecordMapper.toDto(dailyTimeRecord);

        int databaseSizeBeforeCreate = dailyTimeRecordRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDailyTimeRecordMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dailyTimeRecordDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DailyTimeRecord in the database
        List<DailyTimeRecord> dailyTimeRecordList = dailyTimeRecordRepository.findAll();
        assertThat(dailyTimeRecordList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDailyTimeRecords() throws Exception {
        // Initialize the database
        dailyTimeRecordRepository.saveAndFlush(dailyTimeRecord);

        // Get all the dailyTimeRecordList
        restDailyTimeRecordMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dailyTimeRecord.getId().intValue())))
            .andExpect(jsonPath("$.[*].employeeBiometricId").value(hasItem(DEFAULT_EMPLOYEE_BIOMETRIC_ID.intValue())))
            .andExpect(jsonPath("$.[*].inputType").value(hasItem(DEFAULT_INPUT_TYPE)))
            .andExpect(jsonPath("$.[*].attendanceType").value(hasItem(DEFAULT_ATTENDANCE_TYPE)))
            .andExpect(jsonPath("$.[*].temperature").value(hasItem(DEFAULT_TEMPERATURE)))
            .andExpect(jsonPath("$.[*].logDate").value(hasItem(DEFAULT_LOG_DATE)))
            .andExpect(jsonPath("$.[*].logTime").value(hasItem(DEFAULT_LOG_TIME)));
    }

    @Test
    @Transactional
    void getDailyTimeRecord() throws Exception {
        // Initialize the database
        dailyTimeRecordRepository.saveAndFlush(dailyTimeRecord);

        // Get the dailyTimeRecord
        restDailyTimeRecordMockMvc
            .perform(get(ENTITY_API_URL_ID, dailyTimeRecord.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dailyTimeRecord.getId().intValue()))
            .andExpect(jsonPath("$.employeeBiometricId").value(DEFAULT_EMPLOYEE_BIOMETRIC_ID.intValue()))
            .andExpect(jsonPath("$.inputType").value(DEFAULT_INPUT_TYPE))
            .andExpect(jsonPath("$.attendanceType").value(DEFAULT_ATTENDANCE_TYPE))
            .andExpect(jsonPath("$.temperature").value(DEFAULT_TEMPERATURE))
            .andExpect(jsonPath("$.logDate").value(DEFAULT_LOG_DATE))
            .andExpect(jsonPath("$.logTime").value(DEFAULT_LOG_TIME));
    }

    @Test
    @Transactional
    void getDailyTimeRecordsByIdFiltering() throws Exception {
        // Initialize the database
        dailyTimeRecordRepository.saveAndFlush(dailyTimeRecord);

        Long id = dailyTimeRecord.getId();

        defaultDailyTimeRecordShouldBeFound("id.equals=" + id);
        defaultDailyTimeRecordShouldNotBeFound("id.notEquals=" + id);

        defaultDailyTimeRecordShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDailyTimeRecordShouldNotBeFound("id.greaterThan=" + id);

        defaultDailyTimeRecordShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDailyTimeRecordShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDailyTimeRecordsByEmployeeBiometricIdIsEqualToSomething() throws Exception {
        // Initialize the database
        dailyTimeRecordRepository.saveAndFlush(dailyTimeRecord);

        // Get all the dailyTimeRecordList where employeeBiometricId equals to DEFAULT_EMPLOYEE_BIOMETRIC_ID
        defaultDailyTimeRecordShouldBeFound("employeeBiometricId.equals=" + DEFAULT_EMPLOYEE_BIOMETRIC_ID);

        // Get all the dailyTimeRecordList where employeeBiometricId equals to UPDATED_EMPLOYEE_BIOMETRIC_ID
        defaultDailyTimeRecordShouldNotBeFound("employeeBiometricId.equals=" + UPDATED_EMPLOYEE_BIOMETRIC_ID);
    }

    @Test
    @Transactional
    void getAllDailyTimeRecordsByEmployeeBiometricIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dailyTimeRecordRepository.saveAndFlush(dailyTimeRecord);

        // Get all the dailyTimeRecordList where employeeBiometricId not equals to DEFAULT_EMPLOYEE_BIOMETRIC_ID
        defaultDailyTimeRecordShouldNotBeFound("employeeBiometricId.notEquals=" + DEFAULT_EMPLOYEE_BIOMETRIC_ID);

        // Get all the dailyTimeRecordList where employeeBiometricId not equals to UPDATED_EMPLOYEE_BIOMETRIC_ID
        defaultDailyTimeRecordShouldBeFound("employeeBiometricId.notEquals=" + UPDATED_EMPLOYEE_BIOMETRIC_ID);
    }

    @Test
    @Transactional
    void getAllDailyTimeRecordsByEmployeeBiometricIdIsInShouldWork() throws Exception {
        // Initialize the database
        dailyTimeRecordRepository.saveAndFlush(dailyTimeRecord);

        // Get all the dailyTimeRecordList where employeeBiometricId in DEFAULT_EMPLOYEE_BIOMETRIC_ID or UPDATED_EMPLOYEE_BIOMETRIC_ID
        defaultDailyTimeRecordShouldBeFound(
            "employeeBiometricId.in=" + DEFAULT_EMPLOYEE_BIOMETRIC_ID + "," + UPDATED_EMPLOYEE_BIOMETRIC_ID
        );

        // Get all the dailyTimeRecordList where employeeBiometricId equals to UPDATED_EMPLOYEE_BIOMETRIC_ID
        defaultDailyTimeRecordShouldNotBeFound("employeeBiometricId.in=" + UPDATED_EMPLOYEE_BIOMETRIC_ID);
    }

    @Test
    @Transactional
    void getAllDailyTimeRecordsByEmployeeBiometricIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        dailyTimeRecordRepository.saveAndFlush(dailyTimeRecord);

        // Get all the dailyTimeRecordList where employeeBiometricId is not null
        defaultDailyTimeRecordShouldBeFound("employeeBiometricId.specified=true");

        // Get all the dailyTimeRecordList where employeeBiometricId is null
        defaultDailyTimeRecordShouldNotBeFound("employeeBiometricId.specified=false");
    }

    @Test
    @Transactional
    void getAllDailyTimeRecordsByEmployeeBiometricIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        dailyTimeRecordRepository.saveAndFlush(dailyTimeRecord);

        // Get all the dailyTimeRecordList where employeeBiometricId is greater than or equal to DEFAULT_EMPLOYEE_BIOMETRIC_ID
        defaultDailyTimeRecordShouldBeFound("employeeBiometricId.greaterThanOrEqual=" + DEFAULT_EMPLOYEE_BIOMETRIC_ID);

        // Get all the dailyTimeRecordList where employeeBiometricId is greater than or equal to UPDATED_EMPLOYEE_BIOMETRIC_ID
        defaultDailyTimeRecordShouldNotBeFound("employeeBiometricId.greaterThanOrEqual=" + UPDATED_EMPLOYEE_BIOMETRIC_ID);
    }

    @Test
    @Transactional
    void getAllDailyTimeRecordsByEmployeeBiometricIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        dailyTimeRecordRepository.saveAndFlush(dailyTimeRecord);

        // Get all the dailyTimeRecordList where employeeBiometricId is less than or equal to DEFAULT_EMPLOYEE_BIOMETRIC_ID
        defaultDailyTimeRecordShouldBeFound("employeeBiometricId.lessThanOrEqual=" + DEFAULT_EMPLOYEE_BIOMETRIC_ID);

        // Get all the dailyTimeRecordList where employeeBiometricId is less than or equal to SMALLER_EMPLOYEE_BIOMETRIC_ID
        defaultDailyTimeRecordShouldNotBeFound("employeeBiometricId.lessThanOrEqual=" + SMALLER_EMPLOYEE_BIOMETRIC_ID);
    }

    @Test
    @Transactional
    void getAllDailyTimeRecordsByEmployeeBiometricIdIsLessThanSomething() throws Exception {
        // Initialize the database
        dailyTimeRecordRepository.saveAndFlush(dailyTimeRecord);

        // Get all the dailyTimeRecordList where employeeBiometricId is less than DEFAULT_EMPLOYEE_BIOMETRIC_ID
        defaultDailyTimeRecordShouldNotBeFound("employeeBiometricId.lessThan=" + DEFAULT_EMPLOYEE_BIOMETRIC_ID);

        // Get all the dailyTimeRecordList where employeeBiometricId is less than UPDATED_EMPLOYEE_BIOMETRIC_ID
        defaultDailyTimeRecordShouldBeFound("employeeBiometricId.lessThan=" + UPDATED_EMPLOYEE_BIOMETRIC_ID);
    }

    @Test
    @Transactional
    void getAllDailyTimeRecordsByEmployeeBiometricIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        dailyTimeRecordRepository.saveAndFlush(dailyTimeRecord);

        // Get all the dailyTimeRecordList where employeeBiometricId is greater than DEFAULT_EMPLOYEE_BIOMETRIC_ID
        defaultDailyTimeRecordShouldNotBeFound("employeeBiometricId.greaterThan=" + DEFAULT_EMPLOYEE_BIOMETRIC_ID);

        // Get all the dailyTimeRecordList where employeeBiometricId is greater than SMALLER_EMPLOYEE_BIOMETRIC_ID
        defaultDailyTimeRecordShouldBeFound("employeeBiometricId.greaterThan=" + SMALLER_EMPLOYEE_BIOMETRIC_ID);
    }

    @Test
    @Transactional
    void getAllDailyTimeRecordsByInputTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        dailyTimeRecordRepository.saveAndFlush(dailyTimeRecord);

        // Get all the dailyTimeRecordList where inputType equals to DEFAULT_INPUT_TYPE
        defaultDailyTimeRecordShouldBeFound("inputType.equals=" + DEFAULT_INPUT_TYPE);

        // Get all the dailyTimeRecordList where inputType equals to UPDATED_INPUT_TYPE
        defaultDailyTimeRecordShouldNotBeFound("inputType.equals=" + UPDATED_INPUT_TYPE);
    }

    @Test
    @Transactional
    void getAllDailyTimeRecordsByInputTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dailyTimeRecordRepository.saveAndFlush(dailyTimeRecord);

        // Get all the dailyTimeRecordList where inputType not equals to DEFAULT_INPUT_TYPE
        defaultDailyTimeRecordShouldNotBeFound("inputType.notEquals=" + DEFAULT_INPUT_TYPE);

        // Get all the dailyTimeRecordList where inputType not equals to UPDATED_INPUT_TYPE
        defaultDailyTimeRecordShouldBeFound("inputType.notEquals=" + UPDATED_INPUT_TYPE);
    }

    @Test
    @Transactional
    void getAllDailyTimeRecordsByInputTypeIsInShouldWork() throws Exception {
        // Initialize the database
        dailyTimeRecordRepository.saveAndFlush(dailyTimeRecord);

        // Get all the dailyTimeRecordList where inputType in DEFAULT_INPUT_TYPE or UPDATED_INPUT_TYPE
        defaultDailyTimeRecordShouldBeFound("inputType.in=" + DEFAULT_INPUT_TYPE + "," + UPDATED_INPUT_TYPE);

        // Get all the dailyTimeRecordList where inputType equals to UPDATED_INPUT_TYPE
        defaultDailyTimeRecordShouldNotBeFound("inputType.in=" + UPDATED_INPUT_TYPE);
    }

    @Test
    @Transactional
    void getAllDailyTimeRecordsByInputTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        dailyTimeRecordRepository.saveAndFlush(dailyTimeRecord);

        // Get all the dailyTimeRecordList where inputType is not null
        defaultDailyTimeRecordShouldBeFound("inputType.specified=true");

        // Get all the dailyTimeRecordList where inputType is null
        defaultDailyTimeRecordShouldNotBeFound("inputType.specified=false");
    }

    @Test
    @Transactional
    void getAllDailyTimeRecordsByInputTypeContainsSomething() throws Exception {
        // Initialize the database
        dailyTimeRecordRepository.saveAndFlush(dailyTimeRecord);

        // Get all the dailyTimeRecordList where inputType contains DEFAULT_INPUT_TYPE
        defaultDailyTimeRecordShouldBeFound("inputType.contains=" + DEFAULT_INPUT_TYPE);

        // Get all the dailyTimeRecordList where inputType contains UPDATED_INPUT_TYPE
        defaultDailyTimeRecordShouldNotBeFound("inputType.contains=" + UPDATED_INPUT_TYPE);
    }

    @Test
    @Transactional
    void getAllDailyTimeRecordsByInputTypeNotContainsSomething() throws Exception {
        // Initialize the database
        dailyTimeRecordRepository.saveAndFlush(dailyTimeRecord);

        // Get all the dailyTimeRecordList where inputType does not contain DEFAULT_INPUT_TYPE
        defaultDailyTimeRecordShouldNotBeFound("inputType.doesNotContain=" + DEFAULT_INPUT_TYPE);

        // Get all the dailyTimeRecordList where inputType does not contain UPDATED_INPUT_TYPE
        defaultDailyTimeRecordShouldBeFound("inputType.doesNotContain=" + UPDATED_INPUT_TYPE);
    }

    @Test
    @Transactional
    void getAllDailyTimeRecordsByAttendanceTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        dailyTimeRecordRepository.saveAndFlush(dailyTimeRecord);

        // Get all the dailyTimeRecordList where attendanceType equals to DEFAULT_ATTENDANCE_TYPE
        defaultDailyTimeRecordShouldBeFound("attendanceType.equals=" + DEFAULT_ATTENDANCE_TYPE);

        // Get all the dailyTimeRecordList where attendanceType equals to UPDATED_ATTENDANCE_TYPE
        defaultDailyTimeRecordShouldNotBeFound("attendanceType.equals=" + UPDATED_ATTENDANCE_TYPE);
    }

    @Test
    @Transactional
    void getAllDailyTimeRecordsByAttendanceTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dailyTimeRecordRepository.saveAndFlush(dailyTimeRecord);

        // Get all the dailyTimeRecordList where attendanceType not equals to DEFAULT_ATTENDANCE_TYPE
        defaultDailyTimeRecordShouldNotBeFound("attendanceType.notEquals=" + DEFAULT_ATTENDANCE_TYPE);

        // Get all the dailyTimeRecordList where attendanceType not equals to UPDATED_ATTENDANCE_TYPE
        defaultDailyTimeRecordShouldBeFound("attendanceType.notEquals=" + UPDATED_ATTENDANCE_TYPE);
    }

    @Test
    @Transactional
    void getAllDailyTimeRecordsByAttendanceTypeIsInShouldWork() throws Exception {
        // Initialize the database
        dailyTimeRecordRepository.saveAndFlush(dailyTimeRecord);

        // Get all the dailyTimeRecordList where attendanceType in DEFAULT_ATTENDANCE_TYPE or UPDATED_ATTENDANCE_TYPE
        defaultDailyTimeRecordShouldBeFound("attendanceType.in=" + DEFAULT_ATTENDANCE_TYPE + "," + UPDATED_ATTENDANCE_TYPE);

        // Get all the dailyTimeRecordList where attendanceType equals to UPDATED_ATTENDANCE_TYPE
        defaultDailyTimeRecordShouldNotBeFound("attendanceType.in=" + UPDATED_ATTENDANCE_TYPE);
    }

    @Test
    @Transactional
    void getAllDailyTimeRecordsByAttendanceTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        dailyTimeRecordRepository.saveAndFlush(dailyTimeRecord);

        // Get all the dailyTimeRecordList where attendanceType is not null
        defaultDailyTimeRecordShouldBeFound("attendanceType.specified=true");

        // Get all the dailyTimeRecordList where attendanceType is null
        defaultDailyTimeRecordShouldNotBeFound("attendanceType.specified=false");
    }

    @Test
    @Transactional
    void getAllDailyTimeRecordsByAttendanceTypeContainsSomething() throws Exception {
        // Initialize the database
        dailyTimeRecordRepository.saveAndFlush(dailyTimeRecord);

        // Get all the dailyTimeRecordList where attendanceType contains DEFAULT_ATTENDANCE_TYPE
        defaultDailyTimeRecordShouldBeFound("attendanceType.contains=" + DEFAULT_ATTENDANCE_TYPE);

        // Get all the dailyTimeRecordList where attendanceType contains UPDATED_ATTENDANCE_TYPE
        defaultDailyTimeRecordShouldNotBeFound("attendanceType.contains=" + UPDATED_ATTENDANCE_TYPE);
    }

    @Test
    @Transactional
    void getAllDailyTimeRecordsByAttendanceTypeNotContainsSomething() throws Exception {
        // Initialize the database
        dailyTimeRecordRepository.saveAndFlush(dailyTimeRecord);

        // Get all the dailyTimeRecordList where attendanceType does not contain DEFAULT_ATTENDANCE_TYPE
        defaultDailyTimeRecordShouldNotBeFound("attendanceType.doesNotContain=" + DEFAULT_ATTENDANCE_TYPE);

        // Get all the dailyTimeRecordList where attendanceType does not contain UPDATED_ATTENDANCE_TYPE
        defaultDailyTimeRecordShouldBeFound("attendanceType.doesNotContain=" + UPDATED_ATTENDANCE_TYPE);
    }

    @Test
    @Transactional
    void getAllDailyTimeRecordsByTemperatureIsEqualToSomething() throws Exception {
        // Initialize the database
        dailyTimeRecordRepository.saveAndFlush(dailyTimeRecord);

        // Get all the dailyTimeRecordList where temperature equals to DEFAULT_TEMPERATURE
        defaultDailyTimeRecordShouldBeFound("temperature.equals=" + DEFAULT_TEMPERATURE);

        // Get all the dailyTimeRecordList where temperature equals to UPDATED_TEMPERATURE
        defaultDailyTimeRecordShouldNotBeFound("temperature.equals=" + UPDATED_TEMPERATURE);
    }

    @Test
    @Transactional
    void getAllDailyTimeRecordsByTemperatureIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dailyTimeRecordRepository.saveAndFlush(dailyTimeRecord);

        // Get all the dailyTimeRecordList where temperature not equals to DEFAULT_TEMPERATURE
        defaultDailyTimeRecordShouldNotBeFound("temperature.notEquals=" + DEFAULT_TEMPERATURE);

        // Get all the dailyTimeRecordList where temperature not equals to UPDATED_TEMPERATURE
        defaultDailyTimeRecordShouldBeFound("temperature.notEquals=" + UPDATED_TEMPERATURE);
    }

    @Test
    @Transactional
    void getAllDailyTimeRecordsByTemperatureIsInShouldWork() throws Exception {
        // Initialize the database
        dailyTimeRecordRepository.saveAndFlush(dailyTimeRecord);

        // Get all the dailyTimeRecordList where temperature in DEFAULT_TEMPERATURE or UPDATED_TEMPERATURE
        defaultDailyTimeRecordShouldBeFound("temperature.in=" + DEFAULT_TEMPERATURE + "," + UPDATED_TEMPERATURE);

        // Get all the dailyTimeRecordList where temperature equals to UPDATED_TEMPERATURE
        defaultDailyTimeRecordShouldNotBeFound("temperature.in=" + UPDATED_TEMPERATURE);
    }

    @Test
    @Transactional
    void getAllDailyTimeRecordsByTemperatureIsNullOrNotNull() throws Exception {
        // Initialize the database
        dailyTimeRecordRepository.saveAndFlush(dailyTimeRecord);

        // Get all the dailyTimeRecordList where temperature is not null
        defaultDailyTimeRecordShouldBeFound("temperature.specified=true");

        // Get all the dailyTimeRecordList where temperature is null
        defaultDailyTimeRecordShouldNotBeFound("temperature.specified=false");
    }

    @Test
    @Transactional
    void getAllDailyTimeRecordsByTemperatureContainsSomething() throws Exception {
        // Initialize the database
        dailyTimeRecordRepository.saveAndFlush(dailyTimeRecord);

        // Get all the dailyTimeRecordList where temperature contains DEFAULT_TEMPERATURE
        defaultDailyTimeRecordShouldBeFound("temperature.contains=" + DEFAULT_TEMPERATURE);

        // Get all the dailyTimeRecordList where temperature contains UPDATED_TEMPERATURE
        defaultDailyTimeRecordShouldNotBeFound("temperature.contains=" + UPDATED_TEMPERATURE);
    }

    @Test
    @Transactional
    void getAllDailyTimeRecordsByTemperatureNotContainsSomething() throws Exception {
        // Initialize the database
        dailyTimeRecordRepository.saveAndFlush(dailyTimeRecord);

        // Get all the dailyTimeRecordList where temperature does not contain DEFAULT_TEMPERATURE
        defaultDailyTimeRecordShouldNotBeFound("temperature.doesNotContain=" + DEFAULT_TEMPERATURE);

        // Get all the dailyTimeRecordList where temperature does not contain UPDATED_TEMPERATURE
        defaultDailyTimeRecordShouldBeFound("temperature.doesNotContain=" + UPDATED_TEMPERATURE);
    }

    @Test
    @Transactional
    void getAllDailyTimeRecordsByLogDateIsEqualToSomething() throws Exception {
        // Initialize the database
        dailyTimeRecordRepository.saveAndFlush(dailyTimeRecord);

        // Get all the dailyTimeRecordList where logDate equals to DEFAULT_LOG_DATE
        defaultDailyTimeRecordShouldBeFound("logDate.equals=" + DEFAULT_LOG_DATE);

        // Get all the dailyTimeRecordList where logDate equals to UPDATED_LOG_DATE
        defaultDailyTimeRecordShouldNotBeFound("logDate.equals=" + UPDATED_LOG_DATE);
    }

    @Test
    @Transactional
    void getAllDailyTimeRecordsByLogDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dailyTimeRecordRepository.saveAndFlush(dailyTimeRecord);

        // Get all the dailyTimeRecordList where logDate not equals to DEFAULT_LOG_DATE
        defaultDailyTimeRecordShouldNotBeFound("logDate.notEquals=" + DEFAULT_LOG_DATE);

        // Get all the dailyTimeRecordList where logDate not equals to UPDATED_LOG_DATE
        defaultDailyTimeRecordShouldBeFound("logDate.notEquals=" + UPDATED_LOG_DATE);
    }

    @Test
    @Transactional
    void getAllDailyTimeRecordsByLogDateIsInShouldWork() throws Exception {
        // Initialize the database
        dailyTimeRecordRepository.saveAndFlush(dailyTimeRecord);

        // Get all the dailyTimeRecordList where logDate in DEFAULT_LOG_DATE or UPDATED_LOG_DATE
        defaultDailyTimeRecordShouldBeFound("logDate.in=" + DEFAULT_LOG_DATE + "," + UPDATED_LOG_DATE);

        // Get all the dailyTimeRecordList where logDate equals to UPDATED_LOG_DATE
        defaultDailyTimeRecordShouldNotBeFound("logDate.in=" + UPDATED_LOG_DATE);
    }

    @Test
    @Transactional
    void getAllDailyTimeRecordsByLogDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        dailyTimeRecordRepository.saveAndFlush(dailyTimeRecord);

        // Get all the dailyTimeRecordList where logDate is not null
        defaultDailyTimeRecordShouldBeFound("logDate.specified=true");

        // Get all the dailyTimeRecordList where logDate is null
        defaultDailyTimeRecordShouldNotBeFound("logDate.specified=false");
    }

    @Test
    @Transactional
    void getAllDailyTimeRecordsByLogDateContainsSomething() throws Exception {
        // Initialize the database
        dailyTimeRecordRepository.saveAndFlush(dailyTimeRecord);

        // Get all the dailyTimeRecordList where logDate contains DEFAULT_LOG_DATE
        defaultDailyTimeRecordShouldBeFound("logDate.contains=" + DEFAULT_LOG_DATE);

        // Get all the dailyTimeRecordList where logDate contains UPDATED_LOG_DATE
        defaultDailyTimeRecordShouldNotBeFound("logDate.contains=" + UPDATED_LOG_DATE);
    }

    @Test
    @Transactional
    void getAllDailyTimeRecordsByLogDateNotContainsSomething() throws Exception {
        // Initialize the database
        dailyTimeRecordRepository.saveAndFlush(dailyTimeRecord);

        // Get all the dailyTimeRecordList where logDate does not contain DEFAULT_LOG_DATE
        defaultDailyTimeRecordShouldNotBeFound("logDate.doesNotContain=" + DEFAULT_LOG_DATE);

        // Get all the dailyTimeRecordList where logDate does not contain UPDATED_LOG_DATE
        defaultDailyTimeRecordShouldBeFound("logDate.doesNotContain=" + UPDATED_LOG_DATE);
    }

    @Test
    @Transactional
    void getAllDailyTimeRecordsByLogTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        dailyTimeRecordRepository.saveAndFlush(dailyTimeRecord);

        // Get all the dailyTimeRecordList where logTime equals to DEFAULT_LOG_TIME
        defaultDailyTimeRecordShouldBeFound("logTime.equals=" + DEFAULT_LOG_TIME);

        // Get all the dailyTimeRecordList where logTime equals to UPDATED_LOG_TIME
        defaultDailyTimeRecordShouldNotBeFound("logTime.equals=" + UPDATED_LOG_TIME);
    }

    @Test
    @Transactional
    void getAllDailyTimeRecordsByLogTimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dailyTimeRecordRepository.saveAndFlush(dailyTimeRecord);

        // Get all the dailyTimeRecordList where logTime not equals to DEFAULT_LOG_TIME
        defaultDailyTimeRecordShouldNotBeFound("logTime.notEquals=" + DEFAULT_LOG_TIME);

        // Get all the dailyTimeRecordList where logTime not equals to UPDATED_LOG_TIME
        defaultDailyTimeRecordShouldBeFound("logTime.notEquals=" + UPDATED_LOG_TIME);
    }

    @Test
    @Transactional
    void getAllDailyTimeRecordsByLogTimeIsInShouldWork() throws Exception {
        // Initialize the database
        dailyTimeRecordRepository.saveAndFlush(dailyTimeRecord);

        // Get all the dailyTimeRecordList where logTime in DEFAULT_LOG_TIME or UPDATED_LOG_TIME
        defaultDailyTimeRecordShouldBeFound("logTime.in=" + DEFAULT_LOG_TIME + "," + UPDATED_LOG_TIME);

        // Get all the dailyTimeRecordList where logTime equals to UPDATED_LOG_TIME
        defaultDailyTimeRecordShouldNotBeFound("logTime.in=" + UPDATED_LOG_TIME);
    }

    @Test
    @Transactional
    void getAllDailyTimeRecordsByLogTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        dailyTimeRecordRepository.saveAndFlush(dailyTimeRecord);

        // Get all the dailyTimeRecordList where logTime is not null
        defaultDailyTimeRecordShouldBeFound("logTime.specified=true");

        // Get all the dailyTimeRecordList where logTime is null
        defaultDailyTimeRecordShouldNotBeFound("logTime.specified=false");
    }

    @Test
    @Transactional
    void getAllDailyTimeRecordsByLogTimeContainsSomething() throws Exception {
        // Initialize the database
        dailyTimeRecordRepository.saveAndFlush(dailyTimeRecord);

        // Get all the dailyTimeRecordList where logTime contains DEFAULT_LOG_TIME
        defaultDailyTimeRecordShouldBeFound("logTime.contains=" + DEFAULT_LOG_TIME);

        // Get all the dailyTimeRecordList where logTime contains UPDATED_LOG_TIME
        defaultDailyTimeRecordShouldNotBeFound("logTime.contains=" + UPDATED_LOG_TIME);
    }

    @Test
    @Transactional
    void getAllDailyTimeRecordsByLogTimeNotContainsSomething() throws Exception {
        // Initialize the database
        dailyTimeRecordRepository.saveAndFlush(dailyTimeRecord);

        // Get all the dailyTimeRecordList where logTime does not contain DEFAULT_LOG_TIME
        defaultDailyTimeRecordShouldNotBeFound("logTime.doesNotContain=" + DEFAULT_LOG_TIME);

        // Get all the dailyTimeRecordList where logTime does not contain UPDATED_LOG_TIME
        defaultDailyTimeRecordShouldBeFound("logTime.doesNotContain=" + UPDATED_LOG_TIME);
    }

    @Test
    @Transactional
    void getAllDailyTimeRecordsByEmployeeIsEqualToSomething() throws Exception {
        // Initialize the database
        dailyTimeRecordRepository.saveAndFlush(dailyTimeRecord);
        Employee employee;
        if (TestUtil.findAll(em, Employee.class).isEmpty()) {
            employee = EmployeeResourceIT.createEntity(em);
            em.persist(employee);
            em.flush();
        } else {
            employee = TestUtil.findAll(em, Employee.class).get(0);
        }
        em.persist(employee);
        em.flush();
        dailyTimeRecord.setEmployee(employee);
        dailyTimeRecordRepository.saveAndFlush(dailyTimeRecord);
        Long employeeId = employee.getId();

        // Get all the dailyTimeRecordList where employee equals to employeeId
        defaultDailyTimeRecordShouldBeFound("employeeId.equals=" + employeeId);

        // Get all the dailyTimeRecordList where employee equals to (employeeId + 1)
        defaultDailyTimeRecordShouldNotBeFound("employeeId.equals=" + (employeeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDailyTimeRecordShouldBeFound(String filter) throws Exception {
        restDailyTimeRecordMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dailyTimeRecord.getId().intValue())))
            .andExpect(jsonPath("$.[*].employeeBiometricId").value(hasItem(DEFAULT_EMPLOYEE_BIOMETRIC_ID.intValue())))
            .andExpect(jsonPath("$.[*].inputType").value(hasItem(DEFAULT_INPUT_TYPE)))
            .andExpect(jsonPath("$.[*].attendanceType").value(hasItem(DEFAULT_ATTENDANCE_TYPE)))
            .andExpect(jsonPath("$.[*].temperature").value(hasItem(DEFAULT_TEMPERATURE)))
            .andExpect(jsonPath("$.[*].logDate").value(hasItem(DEFAULT_LOG_DATE)))
            .andExpect(jsonPath("$.[*].logTime").value(hasItem(DEFAULT_LOG_TIME)));

        // Check, that the count call also returns 1
        restDailyTimeRecordMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDailyTimeRecordShouldNotBeFound(String filter) throws Exception {
        restDailyTimeRecordMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDailyTimeRecordMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDailyTimeRecord() throws Exception {
        // Get the dailyTimeRecord
        restDailyTimeRecordMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDailyTimeRecord() throws Exception {
        // Initialize the database
        dailyTimeRecordRepository.saveAndFlush(dailyTimeRecord);

        int databaseSizeBeforeUpdate = dailyTimeRecordRepository.findAll().size();

        // Update the dailyTimeRecord
        DailyTimeRecord updatedDailyTimeRecord = dailyTimeRecordRepository.findById(dailyTimeRecord.getId()).get();
        // Disconnect from session so that the updates on updatedDailyTimeRecord are not directly saved in db
        em.detach(updatedDailyTimeRecord);
        updatedDailyTimeRecord
            .employeeBiometricId(UPDATED_EMPLOYEE_BIOMETRIC_ID)
            .inputType(UPDATED_INPUT_TYPE)
            .attendanceType(UPDATED_ATTENDANCE_TYPE)
            .temperature(UPDATED_TEMPERATURE)
            .logDate(UPDATED_LOG_DATE)
            .logTime(UPDATED_LOG_TIME);
        DailyTimeRecordDTO dailyTimeRecordDTO = dailyTimeRecordMapper.toDto(updatedDailyTimeRecord);

        restDailyTimeRecordMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dailyTimeRecordDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dailyTimeRecordDTO))
            )
            .andExpect(status().isOk());

        // Validate the DailyTimeRecord in the database
        List<DailyTimeRecord> dailyTimeRecordList = dailyTimeRecordRepository.findAll();
        assertThat(dailyTimeRecordList).hasSize(databaseSizeBeforeUpdate);
        DailyTimeRecord testDailyTimeRecord = dailyTimeRecordList.get(dailyTimeRecordList.size() - 1);
        assertThat(testDailyTimeRecord.getEmployeeBiometricId()).isEqualTo(UPDATED_EMPLOYEE_BIOMETRIC_ID);
        assertThat(testDailyTimeRecord.getInputType()).isEqualTo(UPDATED_INPUT_TYPE);
        assertThat(testDailyTimeRecord.getAttendanceType()).isEqualTo(UPDATED_ATTENDANCE_TYPE);
        assertThat(testDailyTimeRecord.getTemperature()).isEqualTo(UPDATED_TEMPERATURE);
        assertThat(testDailyTimeRecord.getLogDate()).isEqualTo(UPDATED_LOG_DATE);
        assertThat(testDailyTimeRecord.getLogTime()).isEqualTo(UPDATED_LOG_TIME);
    }

    @Test
    @Transactional
    void putNonExistingDailyTimeRecord() throws Exception {
        int databaseSizeBeforeUpdate = dailyTimeRecordRepository.findAll().size();
        dailyTimeRecord.setId(count.incrementAndGet());

        // Create the DailyTimeRecord
        DailyTimeRecordDTO dailyTimeRecordDTO = dailyTimeRecordMapper.toDto(dailyTimeRecord);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDailyTimeRecordMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dailyTimeRecordDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dailyTimeRecordDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DailyTimeRecord in the database
        List<DailyTimeRecord> dailyTimeRecordList = dailyTimeRecordRepository.findAll();
        assertThat(dailyTimeRecordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDailyTimeRecord() throws Exception {
        int databaseSizeBeforeUpdate = dailyTimeRecordRepository.findAll().size();
        dailyTimeRecord.setId(count.incrementAndGet());

        // Create the DailyTimeRecord
        DailyTimeRecordDTO dailyTimeRecordDTO = dailyTimeRecordMapper.toDto(dailyTimeRecord);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDailyTimeRecordMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dailyTimeRecordDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DailyTimeRecord in the database
        List<DailyTimeRecord> dailyTimeRecordList = dailyTimeRecordRepository.findAll();
        assertThat(dailyTimeRecordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDailyTimeRecord() throws Exception {
        int databaseSizeBeforeUpdate = dailyTimeRecordRepository.findAll().size();
        dailyTimeRecord.setId(count.incrementAndGet());

        // Create the DailyTimeRecord
        DailyTimeRecordDTO dailyTimeRecordDTO = dailyTimeRecordMapper.toDto(dailyTimeRecord);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDailyTimeRecordMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dailyTimeRecordDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DailyTimeRecord in the database
        List<DailyTimeRecord> dailyTimeRecordList = dailyTimeRecordRepository.findAll();
        assertThat(dailyTimeRecordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDailyTimeRecordWithPatch() throws Exception {
        // Initialize the database
        dailyTimeRecordRepository.saveAndFlush(dailyTimeRecord);

        int databaseSizeBeforeUpdate = dailyTimeRecordRepository.findAll().size();

        // Update the dailyTimeRecord using partial update
        DailyTimeRecord partialUpdatedDailyTimeRecord = new DailyTimeRecord();
        partialUpdatedDailyTimeRecord.setId(dailyTimeRecord.getId());

        partialUpdatedDailyTimeRecord
            .employeeBiometricId(UPDATED_EMPLOYEE_BIOMETRIC_ID)
            .inputType(UPDATED_INPUT_TYPE)
            .attendanceType(UPDATED_ATTENDANCE_TYPE)
            .logDate(UPDATED_LOG_DATE)
            .logTime(UPDATED_LOG_TIME);

        restDailyTimeRecordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDailyTimeRecord.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDailyTimeRecord))
            )
            .andExpect(status().isOk());

        // Validate the DailyTimeRecord in the database
        List<DailyTimeRecord> dailyTimeRecordList = dailyTimeRecordRepository.findAll();
        assertThat(dailyTimeRecordList).hasSize(databaseSizeBeforeUpdate);
        DailyTimeRecord testDailyTimeRecord = dailyTimeRecordList.get(dailyTimeRecordList.size() - 1);
        assertThat(testDailyTimeRecord.getEmployeeBiometricId()).isEqualTo(UPDATED_EMPLOYEE_BIOMETRIC_ID);
        assertThat(testDailyTimeRecord.getInputType()).isEqualTo(UPDATED_INPUT_TYPE);
        assertThat(testDailyTimeRecord.getAttendanceType()).isEqualTo(UPDATED_ATTENDANCE_TYPE);
        assertThat(testDailyTimeRecord.getTemperature()).isEqualTo(DEFAULT_TEMPERATURE);
        assertThat(testDailyTimeRecord.getLogDate()).isEqualTo(UPDATED_LOG_DATE);
        assertThat(testDailyTimeRecord.getLogTime()).isEqualTo(UPDATED_LOG_TIME);
    }

    @Test
    @Transactional
    void fullUpdateDailyTimeRecordWithPatch() throws Exception {
        // Initialize the database
        dailyTimeRecordRepository.saveAndFlush(dailyTimeRecord);

        int databaseSizeBeforeUpdate = dailyTimeRecordRepository.findAll().size();

        // Update the dailyTimeRecord using partial update
        DailyTimeRecord partialUpdatedDailyTimeRecord = new DailyTimeRecord();
        partialUpdatedDailyTimeRecord.setId(dailyTimeRecord.getId());

        partialUpdatedDailyTimeRecord
            .employeeBiometricId(UPDATED_EMPLOYEE_BIOMETRIC_ID)
            .inputType(UPDATED_INPUT_TYPE)
            .attendanceType(UPDATED_ATTENDANCE_TYPE)
            .temperature(UPDATED_TEMPERATURE)
            .logDate(UPDATED_LOG_DATE)
            .logTime(UPDATED_LOG_TIME);

        restDailyTimeRecordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDailyTimeRecord.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDailyTimeRecord))
            )
            .andExpect(status().isOk());

        // Validate the DailyTimeRecord in the database
        List<DailyTimeRecord> dailyTimeRecordList = dailyTimeRecordRepository.findAll();
        assertThat(dailyTimeRecordList).hasSize(databaseSizeBeforeUpdate);
        DailyTimeRecord testDailyTimeRecord = dailyTimeRecordList.get(dailyTimeRecordList.size() - 1);
        assertThat(testDailyTimeRecord.getEmployeeBiometricId()).isEqualTo(UPDATED_EMPLOYEE_BIOMETRIC_ID);
        assertThat(testDailyTimeRecord.getInputType()).isEqualTo(UPDATED_INPUT_TYPE);
        assertThat(testDailyTimeRecord.getAttendanceType()).isEqualTo(UPDATED_ATTENDANCE_TYPE);
        assertThat(testDailyTimeRecord.getTemperature()).isEqualTo(UPDATED_TEMPERATURE);
        assertThat(testDailyTimeRecord.getLogDate()).isEqualTo(UPDATED_LOG_DATE);
        assertThat(testDailyTimeRecord.getLogTime()).isEqualTo(UPDATED_LOG_TIME);
    }

    @Test
    @Transactional
    void patchNonExistingDailyTimeRecord() throws Exception {
        int databaseSizeBeforeUpdate = dailyTimeRecordRepository.findAll().size();
        dailyTimeRecord.setId(count.incrementAndGet());

        // Create the DailyTimeRecord
        DailyTimeRecordDTO dailyTimeRecordDTO = dailyTimeRecordMapper.toDto(dailyTimeRecord);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDailyTimeRecordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, dailyTimeRecordDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dailyTimeRecordDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DailyTimeRecord in the database
        List<DailyTimeRecord> dailyTimeRecordList = dailyTimeRecordRepository.findAll();
        assertThat(dailyTimeRecordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDailyTimeRecord() throws Exception {
        int databaseSizeBeforeUpdate = dailyTimeRecordRepository.findAll().size();
        dailyTimeRecord.setId(count.incrementAndGet());

        // Create the DailyTimeRecord
        DailyTimeRecordDTO dailyTimeRecordDTO = dailyTimeRecordMapper.toDto(dailyTimeRecord);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDailyTimeRecordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dailyTimeRecordDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DailyTimeRecord in the database
        List<DailyTimeRecord> dailyTimeRecordList = dailyTimeRecordRepository.findAll();
        assertThat(dailyTimeRecordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDailyTimeRecord() throws Exception {
        int databaseSizeBeforeUpdate = dailyTimeRecordRepository.findAll().size();
        dailyTimeRecord.setId(count.incrementAndGet());

        // Create the DailyTimeRecord
        DailyTimeRecordDTO dailyTimeRecordDTO = dailyTimeRecordMapper.toDto(dailyTimeRecord);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDailyTimeRecordMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dailyTimeRecordDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DailyTimeRecord in the database
        List<DailyTimeRecord> dailyTimeRecordList = dailyTimeRecordRepository.findAll();
        assertThat(dailyTimeRecordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDailyTimeRecord() throws Exception {
        // Initialize the database
        dailyTimeRecordRepository.saveAndFlush(dailyTimeRecord);

        int databaseSizeBeforeDelete = dailyTimeRecordRepository.findAll().size();

        // Delete the dailyTimeRecord
        restDailyTimeRecordMockMvc
            .perform(delete(ENTITY_API_URL_ID, dailyTimeRecord.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DailyTimeRecord> dailyTimeRecordList = dailyTimeRecordRepository.findAll();
        assertThat(dailyTimeRecordList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
