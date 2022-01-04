package com.hrisgsh.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.hrisgsh.app.IntegrationTest;
import com.hrisgsh.app.domain.Employee;
import com.hrisgsh.app.domain.Leave;
import com.hrisgsh.app.domain.LeaveType;
import com.hrisgsh.app.repository.LeaveRepository;
import com.hrisgsh.app.service.criteria.LeaveCriteria;
import com.hrisgsh.app.service.dto.LeaveDTO;
import com.hrisgsh.app.service.mapper.LeaveMapper;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link LeaveResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LeaveResourceIT {

    private static final LocalDate DEFAULT_DATE_APPLY = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_APPLY = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_APPLY = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_DATE_START = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_START = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_START = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_DATE_END = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_END = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_END = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_DATE_RETURN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_RETURN = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_RETURN = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_CHECKUP_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CHECKUP_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CHECKUP_DATE = LocalDate.ofEpochDay(-1L);

    private static final Integer DEFAULT_CONVALESCING_PERIOD = 1;
    private static final Integer UPDATED_CONVALESCING_PERIOD = 2;
    private static final Integer SMALLER_CONVALESCING_PERIOD = 1 - 1;

    private static final String DEFAULT_DIAGNOSIS = "AAAAAAAAAA";
    private static final String UPDATED_DIAGNOSIS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/leaves";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LeaveRepository leaveRepository;

    @Autowired
    private LeaveMapper leaveMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLeaveMockMvc;

    private Leave leave;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Leave createEntity(EntityManager em) {
        Leave leave = new Leave()
            .dateApply(DEFAULT_DATE_APPLY)
            .dateStart(DEFAULT_DATE_START)
            .dateEnd(DEFAULT_DATE_END)
            .dateReturn(DEFAULT_DATE_RETURN)
            .checkupDate(DEFAULT_CHECKUP_DATE)
            .convalescingPeriod(DEFAULT_CONVALESCING_PERIOD)
            .diagnosis(DEFAULT_DIAGNOSIS);
        // Add required entity
        Employee employee;
        if (TestUtil.findAll(em, Employee.class).isEmpty()) {
            employee = EmployeeResourceIT.createEntity(em);
            em.persist(employee);
            em.flush();
        } else {
            employee = TestUtil.findAll(em, Employee.class).get(0);
        }
        leave.setEmployee(employee);
        // Add required entity
        LeaveType leaveType;
        if (TestUtil.findAll(em, LeaveType.class).isEmpty()) {
            leaveType = LeaveTypeResourceIT.createEntity(em);
            em.persist(leaveType);
            em.flush();
        } else {
            leaveType = TestUtil.findAll(em, LeaveType.class).get(0);
        }
        leave.setLeaveType(leaveType);
        return leave;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Leave createUpdatedEntity(EntityManager em) {
        Leave leave = new Leave()
            .dateApply(UPDATED_DATE_APPLY)
            .dateStart(UPDATED_DATE_START)
            .dateEnd(UPDATED_DATE_END)
            .dateReturn(UPDATED_DATE_RETURN)
            .checkupDate(UPDATED_CHECKUP_DATE)
            .convalescingPeriod(UPDATED_CONVALESCING_PERIOD)
            .diagnosis(UPDATED_DIAGNOSIS);
        // Add required entity
        Employee employee;
        if (TestUtil.findAll(em, Employee.class).isEmpty()) {
            employee = EmployeeResourceIT.createUpdatedEntity(em);
            em.persist(employee);
            em.flush();
        } else {
            employee = TestUtil.findAll(em, Employee.class).get(0);
        }
        leave.setEmployee(employee);
        // Add required entity
        LeaveType leaveType;
        if (TestUtil.findAll(em, LeaveType.class).isEmpty()) {
            leaveType = LeaveTypeResourceIT.createUpdatedEntity(em);
            em.persist(leaveType);
            em.flush();
        } else {
            leaveType = TestUtil.findAll(em, LeaveType.class).get(0);
        }
        leave.setLeaveType(leaveType);
        return leave;
    }

    @BeforeEach
    public void initTest() {
        leave = createEntity(em);
    }

    @Test
    @Transactional
    void createLeave() throws Exception {
        int databaseSizeBeforeCreate = leaveRepository.findAll().size();
        // Create the Leave
        LeaveDTO leaveDTO = leaveMapper.toDto(leave);
        restLeaveMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(leaveDTO)))
            .andExpect(status().isCreated());

        // Validate the Leave in the database
        List<Leave> leaveList = leaveRepository.findAll();
        assertThat(leaveList).hasSize(databaseSizeBeforeCreate + 1);
        Leave testLeave = leaveList.get(leaveList.size() - 1);
        assertThat(testLeave.getDateApply()).isEqualTo(DEFAULT_DATE_APPLY);
        assertThat(testLeave.getDateStart()).isEqualTo(DEFAULT_DATE_START);
        assertThat(testLeave.getDateEnd()).isEqualTo(DEFAULT_DATE_END);
        assertThat(testLeave.getDateReturn()).isEqualTo(DEFAULT_DATE_RETURN);
        assertThat(testLeave.getCheckupDate()).isEqualTo(DEFAULT_CHECKUP_DATE);
        assertThat(testLeave.getConvalescingPeriod()).isEqualTo(DEFAULT_CONVALESCING_PERIOD);
        assertThat(testLeave.getDiagnosis()).isEqualTo(DEFAULT_DIAGNOSIS);
    }

    @Test
    @Transactional
    void createLeaveWithExistingId() throws Exception {
        // Create the Leave with an existing ID
        leave.setId(1L);
        LeaveDTO leaveDTO = leaveMapper.toDto(leave);

        int databaseSizeBeforeCreate = leaveRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLeaveMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(leaveDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Leave in the database
        List<Leave> leaveList = leaveRepository.findAll();
        assertThat(leaveList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllLeaves() throws Exception {
        // Initialize the database
        leaveRepository.saveAndFlush(leave);

        // Get all the leaveList
        restLeaveMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leave.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateApply").value(hasItem(DEFAULT_DATE_APPLY.toString())))
            .andExpect(jsonPath("$.[*].dateStart").value(hasItem(DEFAULT_DATE_START.toString())))
            .andExpect(jsonPath("$.[*].dateEnd").value(hasItem(DEFAULT_DATE_END.toString())))
            .andExpect(jsonPath("$.[*].dateReturn").value(hasItem(DEFAULT_DATE_RETURN.toString())))
            .andExpect(jsonPath("$.[*].checkupDate").value(hasItem(DEFAULT_CHECKUP_DATE.toString())))
            .andExpect(jsonPath("$.[*].convalescingPeriod").value(hasItem(DEFAULT_CONVALESCING_PERIOD)))
            .andExpect(jsonPath("$.[*].diagnosis").value(hasItem(DEFAULT_DIAGNOSIS)));
    }

    @Test
    @Transactional
    void getLeave() throws Exception {
        // Initialize the database
        leaveRepository.saveAndFlush(leave);

        // Get the leave
        restLeaveMockMvc
            .perform(get(ENTITY_API_URL_ID, leave.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(leave.getId().intValue()))
            .andExpect(jsonPath("$.dateApply").value(DEFAULT_DATE_APPLY.toString()))
            .andExpect(jsonPath("$.dateStart").value(DEFAULT_DATE_START.toString()))
            .andExpect(jsonPath("$.dateEnd").value(DEFAULT_DATE_END.toString()))
            .andExpect(jsonPath("$.dateReturn").value(DEFAULT_DATE_RETURN.toString()))
            .andExpect(jsonPath("$.checkupDate").value(DEFAULT_CHECKUP_DATE.toString()))
            .andExpect(jsonPath("$.convalescingPeriod").value(DEFAULT_CONVALESCING_PERIOD))
            .andExpect(jsonPath("$.diagnosis").value(DEFAULT_DIAGNOSIS));
    }

    @Test
    @Transactional
    void getLeavesByIdFiltering() throws Exception {
        // Initialize the database
        leaveRepository.saveAndFlush(leave);

        Long id = leave.getId();

        defaultLeaveShouldBeFound("id.equals=" + id);
        defaultLeaveShouldNotBeFound("id.notEquals=" + id);

        defaultLeaveShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLeaveShouldNotBeFound("id.greaterThan=" + id);

        defaultLeaveShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLeaveShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllLeavesByDateApplyIsEqualToSomething() throws Exception {
        // Initialize the database
        leaveRepository.saveAndFlush(leave);

        // Get all the leaveList where dateApply equals to DEFAULT_DATE_APPLY
        defaultLeaveShouldBeFound("dateApply.equals=" + DEFAULT_DATE_APPLY);

        // Get all the leaveList where dateApply equals to UPDATED_DATE_APPLY
        defaultLeaveShouldNotBeFound("dateApply.equals=" + UPDATED_DATE_APPLY);
    }

    @Test
    @Transactional
    void getAllLeavesByDateApplyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaveRepository.saveAndFlush(leave);

        // Get all the leaveList where dateApply not equals to DEFAULT_DATE_APPLY
        defaultLeaveShouldNotBeFound("dateApply.notEquals=" + DEFAULT_DATE_APPLY);

        // Get all the leaveList where dateApply not equals to UPDATED_DATE_APPLY
        defaultLeaveShouldBeFound("dateApply.notEquals=" + UPDATED_DATE_APPLY);
    }

    @Test
    @Transactional
    void getAllLeavesByDateApplyIsInShouldWork() throws Exception {
        // Initialize the database
        leaveRepository.saveAndFlush(leave);

        // Get all the leaveList where dateApply in DEFAULT_DATE_APPLY or UPDATED_DATE_APPLY
        defaultLeaveShouldBeFound("dateApply.in=" + DEFAULT_DATE_APPLY + "," + UPDATED_DATE_APPLY);

        // Get all the leaveList where dateApply equals to UPDATED_DATE_APPLY
        defaultLeaveShouldNotBeFound("dateApply.in=" + UPDATED_DATE_APPLY);
    }

    @Test
    @Transactional
    void getAllLeavesByDateApplyIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaveRepository.saveAndFlush(leave);

        // Get all the leaveList where dateApply is not null
        defaultLeaveShouldBeFound("dateApply.specified=true");

        // Get all the leaveList where dateApply is null
        defaultLeaveShouldNotBeFound("dateApply.specified=false");
    }

    @Test
    @Transactional
    void getAllLeavesByDateApplyIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaveRepository.saveAndFlush(leave);

        // Get all the leaveList where dateApply is greater than or equal to DEFAULT_DATE_APPLY
        defaultLeaveShouldBeFound("dateApply.greaterThanOrEqual=" + DEFAULT_DATE_APPLY);

        // Get all the leaveList where dateApply is greater than or equal to UPDATED_DATE_APPLY
        defaultLeaveShouldNotBeFound("dateApply.greaterThanOrEqual=" + UPDATED_DATE_APPLY);
    }

    @Test
    @Transactional
    void getAllLeavesByDateApplyIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaveRepository.saveAndFlush(leave);

        // Get all the leaveList where dateApply is less than or equal to DEFAULT_DATE_APPLY
        defaultLeaveShouldBeFound("dateApply.lessThanOrEqual=" + DEFAULT_DATE_APPLY);

        // Get all the leaveList where dateApply is less than or equal to SMALLER_DATE_APPLY
        defaultLeaveShouldNotBeFound("dateApply.lessThanOrEqual=" + SMALLER_DATE_APPLY);
    }

    @Test
    @Transactional
    void getAllLeavesByDateApplyIsLessThanSomething() throws Exception {
        // Initialize the database
        leaveRepository.saveAndFlush(leave);

        // Get all the leaveList where dateApply is less than DEFAULT_DATE_APPLY
        defaultLeaveShouldNotBeFound("dateApply.lessThan=" + DEFAULT_DATE_APPLY);

        // Get all the leaveList where dateApply is less than UPDATED_DATE_APPLY
        defaultLeaveShouldBeFound("dateApply.lessThan=" + UPDATED_DATE_APPLY);
    }

    @Test
    @Transactional
    void getAllLeavesByDateApplyIsGreaterThanSomething() throws Exception {
        // Initialize the database
        leaveRepository.saveAndFlush(leave);

        // Get all the leaveList where dateApply is greater than DEFAULT_DATE_APPLY
        defaultLeaveShouldNotBeFound("dateApply.greaterThan=" + DEFAULT_DATE_APPLY);

        // Get all the leaveList where dateApply is greater than SMALLER_DATE_APPLY
        defaultLeaveShouldBeFound("dateApply.greaterThan=" + SMALLER_DATE_APPLY);
    }

    @Test
    @Transactional
    void getAllLeavesByDateStartIsEqualToSomething() throws Exception {
        // Initialize the database
        leaveRepository.saveAndFlush(leave);

        // Get all the leaveList where dateStart equals to DEFAULT_DATE_START
        defaultLeaveShouldBeFound("dateStart.equals=" + DEFAULT_DATE_START);

        // Get all the leaveList where dateStart equals to UPDATED_DATE_START
        defaultLeaveShouldNotBeFound("dateStart.equals=" + UPDATED_DATE_START);
    }

    @Test
    @Transactional
    void getAllLeavesByDateStartIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaveRepository.saveAndFlush(leave);

        // Get all the leaveList where dateStart not equals to DEFAULT_DATE_START
        defaultLeaveShouldNotBeFound("dateStart.notEquals=" + DEFAULT_DATE_START);

        // Get all the leaveList where dateStart not equals to UPDATED_DATE_START
        defaultLeaveShouldBeFound("dateStart.notEquals=" + UPDATED_DATE_START);
    }

    @Test
    @Transactional
    void getAllLeavesByDateStartIsInShouldWork() throws Exception {
        // Initialize the database
        leaveRepository.saveAndFlush(leave);

        // Get all the leaveList where dateStart in DEFAULT_DATE_START or UPDATED_DATE_START
        defaultLeaveShouldBeFound("dateStart.in=" + DEFAULT_DATE_START + "," + UPDATED_DATE_START);

        // Get all the leaveList where dateStart equals to UPDATED_DATE_START
        defaultLeaveShouldNotBeFound("dateStart.in=" + UPDATED_DATE_START);
    }

    @Test
    @Transactional
    void getAllLeavesByDateStartIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaveRepository.saveAndFlush(leave);

        // Get all the leaveList where dateStart is not null
        defaultLeaveShouldBeFound("dateStart.specified=true");

        // Get all the leaveList where dateStart is null
        defaultLeaveShouldNotBeFound("dateStart.specified=false");
    }

    @Test
    @Transactional
    void getAllLeavesByDateStartIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaveRepository.saveAndFlush(leave);

        // Get all the leaveList where dateStart is greater than or equal to DEFAULT_DATE_START
        defaultLeaveShouldBeFound("dateStart.greaterThanOrEqual=" + DEFAULT_DATE_START);

        // Get all the leaveList where dateStart is greater than or equal to UPDATED_DATE_START
        defaultLeaveShouldNotBeFound("dateStart.greaterThanOrEqual=" + UPDATED_DATE_START);
    }

    @Test
    @Transactional
    void getAllLeavesByDateStartIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaveRepository.saveAndFlush(leave);

        // Get all the leaveList where dateStart is less than or equal to DEFAULT_DATE_START
        defaultLeaveShouldBeFound("dateStart.lessThanOrEqual=" + DEFAULT_DATE_START);

        // Get all the leaveList where dateStart is less than or equal to SMALLER_DATE_START
        defaultLeaveShouldNotBeFound("dateStart.lessThanOrEqual=" + SMALLER_DATE_START);
    }

    @Test
    @Transactional
    void getAllLeavesByDateStartIsLessThanSomething() throws Exception {
        // Initialize the database
        leaveRepository.saveAndFlush(leave);

        // Get all the leaveList where dateStart is less than DEFAULT_DATE_START
        defaultLeaveShouldNotBeFound("dateStart.lessThan=" + DEFAULT_DATE_START);

        // Get all the leaveList where dateStart is less than UPDATED_DATE_START
        defaultLeaveShouldBeFound("dateStart.lessThan=" + UPDATED_DATE_START);
    }

    @Test
    @Transactional
    void getAllLeavesByDateStartIsGreaterThanSomething() throws Exception {
        // Initialize the database
        leaveRepository.saveAndFlush(leave);

        // Get all the leaveList where dateStart is greater than DEFAULT_DATE_START
        defaultLeaveShouldNotBeFound("dateStart.greaterThan=" + DEFAULT_DATE_START);

        // Get all the leaveList where dateStart is greater than SMALLER_DATE_START
        defaultLeaveShouldBeFound("dateStart.greaterThan=" + SMALLER_DATE_START);
    }

    @Test
    @Transactional
    void getAllLeavesByDateEndIsEqualToSomething() throws Exception {
        // Initialize the database
        leaveRepository.saveAndFlush(leave);

        // Get all the leaveList where dateEnd equals to DEFAULT_DATE_END
        defaultLeaveShouldBeFound("dateEnd.equals=" + DEFAULT_DATE_END);

        // Get all the leaveList where dateEnd equals to UPDATED_DATE_END
        defaultLeaveShouldNotBeFound("dateEnd.equals=" + UPDATED_DATE_END);
    }

    @Test
    @Transactional
    void getAllLeavesByDateEndIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaveRepository.saveAndFlush(leave);

        // Get all the leaveList where dateEnd not equals to DEFAULT_DATE_END
        defaultLeaveShouldNotBeFound("dateEnd.notEquals=" + DEFAULT_DATE_END);

        // Get all the leaveList where dateEnd not equals to UPDATED_DATE_END
        defaultLeaveShouldBeFound("dateEnd.notEquals=" + UPDATED_DATE_END);
    }

    @Test
    @Transactional
    void getAllLeavesByDateEndIsInShouldWork() throws Exception {
        // Initialize the database
        leaveRepository.saveAndFlush(leave);

        // Get all the leaveList where dateEnd in DEFAULT_DATE_END or UPDATED_DATE_END
        defaultLeaveShouldBeFound("dateEnd.in=" + DEFAULT_DATE_END + "," + UPDATED_DATE_END);

        // Get all the leaveList where dateEnd equals to UPDATED_DATE_END
        defaultLeaveShouldNotBeFound("dateEnd.in=" + UPDATED_DATE_END);
    }

    @Test
    @Transactional
    void getAllLeavesByDateEndIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaveRepository.saveAndFlush(leave);

        // Get all the leaveList where dateEnd is not null
        defaultLeaveShouldBeFound("dateEnd.specified=true");

        // Get all the leaveList where dateEnd is null
        defaultLeaveShouldNotBeFound("dateEnd.specified=false");
    }

    @Test
    @Transactional
    void getAllLeavesByDateEndIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaveRepository.saveAndFlush(leave);

        // Get all the leaveList where dateEnd is greater than or equal to DEFAULT_DATE_END
        defaultLeaveShouldBeFound("dateEnd.greaterThanOrEqual=" + DEFAULT_DATE_END);

        // Get all the leaveList where dateEnd is greater than or equal to UPDATED_DATE_END
        defaultLeaveShouldNotBeFound("dateEnd.greaterThanOrEqual=" + UPDATED_DATE_END);
    }

    @Test
    @Transactional
    void getAllLeavesByDateEndIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaveRepository.saveAndFlush(leave);

        // Get all the leaveList where dateEnd is less than or equal to DEFAULT_DATE_END
        defaultLeaveShouldBeFound("dateEnd.lessThanOrEqual=" + DEFAULT_DATE_END);

        // Get all the leaveList where dateEnd is less than or equal to SMALLER_DATE_END
        defaultLeaveShouldNotBeFound("dateEnd.lessThanOrEqual=" + SMALLER_DATE_END);
    }

    @Test
    @Transactional
    void getAllLeavesByDateEndIsLessThanSomething() throws Exception {
        // Initialize the database
        leaveRepository.saveAndFlush(leave);

        // Get all the leaveList where dateEnd is less than DEFAULT_DATE_END
        defaultLeaveShouldNotBeFound("dateEnd.lessThan=" + DEFAULT_DATE_END);

        // Get all the leaveList where dateEnd is less than UPDATED_DATE_END
        defaultLeaveShouldBeFound("dateEnd.lessThan=" + UPDATED_DATE_END);
    }

    @Test
    @Transactional
    void getAllLeavesByDateEndIsGreaterThanSomething() throws Exception {
        // Initialize the database
        leaveRepository.saveAndFlush(leave);

        // Get all the leaveList where dateEnd is greater than DEFAULT_DATE_END
        defaultLeaveShouldNotBeFound("dateEnd.greaterThan=" + DEFAULT_DATE_END);

        // Get all the leaveList where dateEnd is greater than SMALLER_DATE_END
        defaultLeaveShouldBeFound("dateEnd.greaterThan=" + SMALLER_DATE_END);
    }

    @Test
    @Transactional
    void getAllLeavesByDateReturnIsEqualToSomething() throws Exception {
        // Initialize the database
        leaveRepository.saveAndFlush(leave);

        // Get all the leaveList where dateReturn equals to DEFAULT_DATE_RETURN
        defaultLeaveShouldBeFound("dateReturn.equals=" + DEFAULT_DATE_RETURN);

        // Get all the leaveList where dateReturn equals to UPDATED_DATE_RETURN
        defaultLeaveShouldNotBeFound("dateReturn.equals=" + UPDATED_DATE_RETURN);
    }

    @Test
    @Transactional
    void getAllLeavesByDateReturnIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaveRepository.saveAndFlush(leave);

        // Get all the leaveList where dateReturn not equals to DEFAULT_DATE_RETURN
        defaultLeaveShouldNotBeFound("dateReturn.notEquals=" + DEFAULT_DATE_RETURN);

        // Get all the leaveList where dateReturn not equals to UPDATED_DATE_RETURN
        defaultLeaveShouldBeFound("dateReturn.notEquals=" + UPDATED_DATE_RETURN);
    }

    @Test
    @Transactional
    void getAllLeavesByDateReturnIsInShouldWork() throws Exception {
        // Initialize the database
        leaveRepository.saveAndFlush(leave);

        // Get all the leaveList where dateReturn in DEFAULT_DATE_RETURN or UPDATED_DATE_RETURN
        defaultLeaveShouldBeFound("dateReturn.in=" + DEFAULT_DATE_RETURN + "," + UPDATED_DATE_RETURN);

        // Get all the leaveList where dateReturn equals to UPDATED_DATE_RETURN
        defaultLeaveShouldNotBeFound("dateReturn.in=" + UPDATED_DATE_RETURN);
    }

    @Test
    @Transactional
    void getAllLeavesByDateReturnIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaveRepository.saveAndFlush(leave);

        // Get all the leaveList where dateReturn is not null
        defaultLeaveShouldBeFound("dateReturn.specified=true");

        // Get all the leaveList where dateReturn is null
        defaultLeaveShouldNotBeFound("dateReturn.specified=false");
    }

    @Test
    @Transactional
    void getAllLeavesByDateReturnIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaveRepository.saveAndFlush(leave);

        // Get all the leaveList where dateReturn is greater than or equal to DEFAULT_DATE_RETURN
        defaultLeaveShouldBeFound("dateReturn.greaterThanOrEqual=" + DEFAULT_DATE_RETURN);

        // Get all the leaveList where dateReturn is greater than or equal to UPDATED_DATE_RETURN
        defaultLeaveShouldNotBeFound("dateReturn.greaterThanOrEqual=" + UPDATED_DATE_RETURN);
    }

    @Test
    @Transactional
    void getAllLeavesByDateReturnIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaveRepository.saveAndFlush(leave);

        // Get all the leaveList where dateReturn is less than or equal to DEFAULT_DATE_RETURN
        defaultLeaveShouldBeFound("dateReturn.lessThanOrEqual=" + DEFAULT_DATE_RETURN);

        // Get all the leaveList where dateReturn is less than or equal to SMALLER_DATE_RETURN
        defaultLeaveShouldNotBeFound("dateReturn.lessThanOrEqual=" + SMALLER_DATE_RETURN);
    }

    @Test
    @Transactional
    void getAllLeavesByDateReturnIsLessThanSomething() throws Exception {
        // Initialize the database
        leaveRepository.saveAndFlush(leave);

        // Get all the leaveList where dateReturn is less than DEFAULT_DATE_RETURN
        defaultLeaveShouldNotBeFound("dateReturn.lessThan=" + DEFAULT_DATE_RETURN);

        // Get all the leaveList where dateReturn is less than UPDATED_DATE_RETURN
        defaultLeaveShouldBeFound("dateReturn.lessThan=" + UPDATED_DATE_RETURN);
    }

    @Test
    @Transactional
    void getAllLeavesByDateReturnIsGreaterThanSomething() throws Exception {
        // Initialize the database
        leaveRepository.saveAndFlush(leave);

        // Get all the leaveList where dateReturn is greater than DEFAULT_DATE_RETURN
        defaultLeaveShouldNotBeFound("dateReturn.greaterThan=" + DEFAULT_DATE_RETURN);

        // Get all the leaveList where dateReturn is greater than SMALLER_DATE_RETURN
        defaultLeaveShouldBeFound("dateReturn.greaterThan=" + SMALLER_DATE_RETURN);
    }

    @Test
    @Transactional
    void getAllLeavesByCheckupDateIsEqualToSomething() throws Exception {
        // Initialize the database
        leaveRepository.saveAndFlush(leave);

        // Get all the leaveList where checkupDate equals to DEFAULT_CHECKUP_DATE
        defaultLeaveShouldBeFound("checkupDate.equals=" + DEFAULT_CHECKUP_DATE);

        // Get all the leaveList where checkupDate equals to UPDATED_CHECKUP_DATE
        defaultLeaveShouldNotBeFound("checkupDate.equals=" + UPDATED_CHECKUP_DATE);
    }

    @Test
    @Transactional
    void getAllLeavesByCheckupDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaveRepository.saveAndFlush(leave);

        // Get all the leaveList where checkupDate not equals to DEFAULT_CHECKUP_DATE
        defaultLeaveShouldNotBeFound("checkupDate.notEquals=" + DEFAULT_CHECKUP_DATE);

        // Get all the leaveList where checkupDate not equals to UPDATED_CHECKUP_DATE
        defaultLeaveShouldBeFound("checkupDate.notEquals=" + UPDATED_CHECKUP_DATE);
    }

    @Test
    @Transactional
    void getAllLeavesByCheckupDateIsInShouldWork() throws Exception {
        // Initialize the database
        leaveRepository.saveAndFlush(leave);

        // Get all the leaveList where checkupDate in DEFAULT_CHECKUP_DATE or UPDATED_CHECKUP_DATE
        defaultLeaveShouldBeFound("checkupDate.in=" + DEFAULT_CHECKUP_DATE + "," + UPDATED_CHECKUP_DATE);

        // Get all the leaveList where checkupDate equals to UPDATED_CHECKUP_DATE
        defaultLeaveShouldNotBeFound("checkupDate.in=" + UPDATED_CHECKUP_DATE);
    }

    @Test
    @Transactional
    void getAllLeavesByCheckupDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaveRepository.saveAndFlush(leave);

        // Get all the leaveList where checkupDate is not null
        defaultLeaveShouldBeFound("checkupDate.specified=true");

        // Get all the leaveList where checkupDate is null
        defaultLeaveShouldNotBeFound("checkupDate.specified=false");
    }

    @Test
    @Transactional
    void getAllLeavesByCheckupDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaveRepository.saveAndFlush(leave);

        // Get all the leaveList where checkupDate is greater than or equal to DEFAULT_CHECKUP_DATE
        defaultLeaveShouldBeFound("checkupDate.greaterThanOrEqual=" + DEFAULT_CHECKUP_DATE);

        // Get all the leaveList where checkupDate is greater than or equal to UPDATED_CHECKUP_DATE
        defaultLeaveShouldNotBeFound("checkupDate.greaterThanOrEqual=" + UPDATED_CHECKUP_DATE);
    }

    @Test
    @Transactional
    void getAllLeavesByCheckupDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaveRepository.saveAndFlush(leave);

        // Get all the leaveList where checkupDate is less than or equal to DEFAULT_CHECKUP_DATE
        defaultLeaveShouldBeFound("checkupDate.lessThanOrEqual=" + DEFAULT_CHECKUP_DATE);

        // Get all the leaveList where checkupDate is less than or equal to SMALLER_CHECKUP_DATE
        defaultLeaveShouldNotBeFound("checkupDate.lessThanOrEqual=" + SMALLER_CHECKUP_DATE);
    }

    @Test
    @Transactional
    void getAllLeavesByCheckupDateIsLessThanSomething() throws Exception {
        // Initialize the database
        leaveRepository.saveAndFlush(leave);

        // Get all the leaveList where checkupDate is less than DEFAULT_CHECKUP_DATE
        defaultLeaveShouldNotBeFound("checkupDate.lessThan=" + DEFAULT_CHECKUP_DATE);

        // Get all the leaveList where checkupDate is less than UPDATED_CHECKUP_DATE
        defaultLeaveShouldBeFound("checkupDate.lessThan=" + UPDATED_CHECKUP_DATE);
    }

    @Test
    @Transactional
    void getAllLeavesByCheckupDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        leaveRepository.saveAndFlush(leave);

        // Get all the leaveList where checkupDate is greater than DEFAULT_CHECKUP_DATE
        defaultLeaveShouldNotBeFound("checkupDate.greaterThan=" + DEFAULT_CHECKUP_DATE);

        // Get all the leaveList where checkupDate is greater than SMALLER_CHECKUP_DATE
        defaultLeaveShouldBeFound("checkupDate.greaterThan=" + SMALLER_CHECKUP_DATE);
    }

    @Test
    @Transactional
    void getAllLeavesByConvalescingPeriodIsEqualToSomething() throws Exception {
        // Initialize the database
        leaveRepository.saveAndFlush(leave);

        // Get all the leaveList where convalescingPeriod equals to DEFAULT_CONVALESCING_PERIOD
        defaultLeaveShouldBeFound("convalescingPeriod.equals=" + DEFAULT_CONVALESCING_PERIOD);

        // Get all the leaveList where convalescingPeriod equals to UPDATED_CONVALESCING_PERIOD
        defaultLeaveShouldNotBeFound("convalescingPeriod.equals=" + UPDATED_CONVALESCING_PERIOD);
    }

    @Test
    @Transactional
    void getAllLeavesByConvalescingPeriodIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaveRepository.saveAndFlush(leave);

        // Get all the leaveList where convalescingPeriod not equals to DEFAULT_CONVALESCING_PERIOD
        defaultLeaveShouldNotBeFound("convalescingPeriod.notEquals=" + DEFAULT_CONVALESCING_PERIOD);

        // Get all the leaveList where convalescingPeriod not equals to UPDATED_CONVALESCING_PERIOD
        defaultLeaveShouldBeFound("convalescingPeriod.notEquals=" + UPDATED_CONVALESCING_PERIOD);
    }

    @Test
    @Transactional
    void getAllLeavesByConvalescingPeriodIsInShouldWork() throws Exception {
        // Initialize the database
        leaveRepository.saveAndFlush(leave);

        // Get all the leaveList where convalescingPeriod in DEFAULT_CONVALESCING_PERIOD or UPDATED_CONVALESCING_PERIOD
        defaultLeaveShouldBeFound("convalescingPeriod.in=" + DEFAULT_CONVALESCING_PERIOD + "," + UPDATED_CONVALESCING_PERIOD);

        // Get all the leaveList where convalescingPeriod equals to UPDATED_CONVALESCING_PERIOD
        defaultLeaveShouldNotBeFound("convalescingPeriod.in=" + UPDATED_CONVALESCING_PERIOD);
    }

    @Test
    @Transactional
    void getAllLeavesByConvalescingPeriodIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaveRepository.saveAndFlush(leave);

        // Get all the leaveList where convalescingPeriod is not null
        defaultLeaveShouldBeFound("convalescingPeriod.specified=true");

        // Get all the leaveList where convalescingPeriod is null
        defaultLeaveShouldNotBeFound("convalescingPeriod.specified=false");
    }

    @Test
    @Transactional
    void getAllLeavesByConvalescingPeriodIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaveRepository.saveAndFlush(leave);

        // Get all the leaveList where convalescingPeriod is greater than or equal to DEFAULT_CONVALESCING_PERIOD
        defaultLeaveShouldBeFound("convalescingPeriod.greaterThanOrEqual=" + DEFAULT_CONVALESCING_PERIOD);

        // Get all the leaveList where convalescingPeriod is greater than or equal to UPDATED_CONVALESCING_PERIOD
        defaultLeaveShouldNotBeFound("convalescingPeriod.greaterThanOrEqual=" + UPDATED_CONVALESCING_PERIOD);
    }

    @Test
    @Transactional
    void getAllLeavesByConvalescingPeriodIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaveRepository.saveAndFlush(leave);

        // Get all the leaveList where convalescingPeriod is less than or equal to DEFAULT_CONVALESCING_PERIOD
        defaultLeaveShouldBeFound("convalescingPeriod.lessThanOrEqual=" + DEFAULT_CONVALESCING_PERIOD);

        // Get all the leaveList where convalescingPeriod is less than or equal to SMALLER_CONVALESCING_PERIOD
        defaultLeaveShouldNotBeFound("convalescingPeriod.lessThanOrEqual=" + SMALLER_CONVALESCING_PERIOD);
    }

    @Test
    @Transactional
    void getAllLeavesByConvalescingPeriodIsLessThanSomething() throws Exception {
        // Initialize the database
        leaveRepository.saveAndFlush(leave);

        // Get all the leaveList where convalescingPeriod is less than DEFAULT_CONVALESCING_PERIOD
        defaultLeaveShouldNotBeFound("convalescingPeriod.lessThan=" + DEFAULT_CONVALESCING_PERIOD);

        // Get all the leaveList where convalescingPeriod is less than UPDATED_CONVALESCING_PERIOD
        defaultLeaveShouldBeFound("convalescingPeriod.lessThan=" + UPDATED_CONVALESCING_PERIOD);
    }

    @Test
    @Transactional
    void getAllLeavesByConvalescingPeriodIsGreaterThanSomething() throws Exception {
        // Initialize the database
        leaveRepository.saveAndFlush(leave);

        // Get all the leaveList where convalescingPeriod is greater than DEFAULT_CONVALESCING_PERIOD
        defaultLeaveShouldNotBeFound("convalescingPeriod.greaterThan=" + DEFAULT_CONVALESCING_PERIOD);

        // Get all the leaveList where convalescingPeriod is greater than SMALLER_CONVALESCING_PERIOD
        defaultLeaveShouldBeFound("convalescingPeriod.greaterThan=" + SMALLER_CONVALESCING_PERIOD);
    }

    @Test
    @Transactional
    void getAllLeavesByDiagnosisIsEqualToSomething() throws Exception {
        // Initialize the database
        leaveRepository.saveAndFlush(leave);

        // Get all the leaveList where diagnosis equals to DEFAULT_DIAGNOSIS
        defaultLeaveShouldBeFound("diagnosis.equals=" + DEFAULT_DIAGNOSIS);

        // Get all the leaveList where diagnosis equals to UPDATED_DIAGNOSIS
        defaultLeaveShouldNotBeFound("diagnosis.equals=" + UPDATED_DIAGNOSIS);
    }

    @Test
    @Transactional
    void getAllLeavesByDiagnosisIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaveRepository.saveAndFlush(leave);

        // Get all the leaveList where diagnosis not equals to DEFAULT_DIAGNOSIS
        defaultLeaveShouldNotBeFound("diagnosis.notEquals=" + DEFAULT_DIAGNOSIS);

        // Get all the leaveList where diagnosis not equals to UPDATED_DIAGNOSIS
        defaultLeaveShouldBeFound("diagnosis.notEquals=" + UPDATED_DIAGNOSIS);
    }

    @Test
    @Transactional
    void getAllLeavesByDiagnosisIsInShouldWork() throws Exception {
        // Initialize the database
        leaveRepository.saveAndFlush(leave);

        // Get all the leaveList where diagnosis in DEFAULT_DIAGNOSIS or UPDATED_DIAGNOSIS
        defaultLeaveShouldBeFound("diagnosis.in=" + DEFAULT_DIAGNOSIS + "," + UPDATED_DIAGNOSIS);

        // Get all the leaveList where diagnosis equals to UPDATED_DIAGNOSIS
        defaultLeaveShouldNotBeFound("diagnosis.in=" + UPDATED_DIAGNOSIS);
    }

    @Test
    @Transactional
    void getAllLeavesByDiagnosisIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaveRepository.saveAndFlush(leave);

        // Get all the leaveList where diagnosis is not null
        defaultLeaveShouldBeFound("diagnosis.specified=true");

        // Get all the leaveList where diagnosis is null
        defaultLeaveShouldNotBeFound("diagnosis.specified=false");
    }

    @Test
    @Transactional
    void getAllLeavesByDiagnosisContainsSomething() throws Exception {
        // Initialize the database
        leaveRepository.saveAndFlush(leave);

        // Get all the leaveList where diagnosis contains DEFAULT_DIAGNOSIS
        defaultLeaveShouldBeFound("diagnosis.contains=" + DEFAULT_DIAGNOSIS);

        // Get all the leaveList where diagnosis contains UPDATED_DIAGNOSIS
        defaultLeaveShouldNotBeFound("diagnosis.contains=" + UPDATED_DIAGNOSIS);
    }

    @Test
    @Transactional
    void getAllLeavesByDiagnosisNotContainsSomething() throws Exception {
        // Initialize the database
        leaveRepository.saveAndFlush(leave);

        // Get all the leaveList where diagnosis does not contain DEFAULT_DIAGNOSIS
        defaultLeaveShouldNotBeFound("diagnosis.doesNotContain=" + DEFAULT_DIAGNOSIS);

        // Get all the leaveList where diagnosis does not contain UPDATED_DIAGNOSIS
        defaultLeaveShouldBeFound("diagnosis.doesNotContain=" + UPDATED_DIAGNOSIS);
    }

    @Test
    @Transactional
    void getAllLeavesByEmployeeIsEqualToSomething() throws Exception {
        // Initialize the database
        leaveRepository.saveAndFlush(leave);
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
        leave.setEmployee(employee);
        leaveRepository.saveAndFlush(leave);
        Long employeeId = employee.getId();

        // Get all the leaveList where employee equals to employeeId
        defaultLeaveShouldBeFound("employeeId.equals=" + employeeId);

        // Get all the leaveList where employee equals to (employeeId + 1)
        defaultLeaveShouldNotBeFound("employeeId.equals=" + (employeeId + 1));
    }

    @Test
    @Transactional
    void getAllLeavesByLeaveTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        leaveRepository.saveAndFlush(leave);
        LeaveType leaveType;
        if (TestUtil.findAll(em, LeaveType.class).isEmpty()) {
            leaveType = LeaveTypeResourceIT.createEntity(em);
            em.persist(leaveType);
            em.flush();
        } else {
            leaveType = TestUtil.findAll(em, LeaveType.class).get(0);
        }
        em.persist(leaveType);
        em.flush();
        leave.setLeaveType(leaveType);
        leaveRepository.saveAndFlush(leave);
        Long leaveTypeId = leaveType.getId();

        // Get all the leaveList where leaveType equals to leaveTypeId
        defaultLeaveShouldBeFound("leaveTypeId.equals=" + leaveTypeId);

        // Get all the leaveList where leaveType equals to (leaveTypeId + 1)
        defaultLeaveShouldNotBeFound("leaveTypeId.equals=" + (leaveTypeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLeaveShouldBeFound(String filter) throws Exception {
        restLeaveMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leave.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateApply").value(hasItem(DEFAULT_DATE_APPLY.toString())))
            .andExpect(jsonPath("$.[*].dateStart").value(hasItem(DEFAULT_DATE_START.toString())))
            .andExpect(jsonPath("$.[*].dateEnd").value(hasItem(DEFAULT_DATE_END.toString())))
            .andExpect(jsonPath("$.[*].dateReturn").value(hasItem(DEFAULT_DATE_RETURN.toString())))
            .andExpect(jsonPath("$.[*].checkupDate").value(hasItem(DEFAULT_CHECKUP_DATE.toString())))
            .andExpect(jsonPath("$.[*].convalescingPeriod").value(hasItem(DEFAULT_CONVALESCING_PERIOD)))
            .andExpect(jsonPath("$.[*].diagnosis").value(hasItem(DEFAULT_DIAGNOSIS)));

        // Check, that the count call also returns 1
        restLeaveMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLeaveShouldNotBeFound(String filter) throws Exception {
        restLeaveMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLeaveMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingLeave() throws Exception {
        // Get the leave
        restLeaveMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLeave() throws Exception {
        // Initialize the database
        leaveRepository.saveAndFlush(leave);

        int databaseSizeBeforeUpdate = leaveRepository.findAll().size();

        // Update the leave
        Leave updatedLeave = leaveRepository.findById(leave.getId()).get();
        // Disconnect from session so that the updates on updatedLeave are not directly saved in db
        em.detach(updatedLeave);
        updatedLeave
            .dateApply(UPDATED_DATE_APPLY)
            .dateStart(UPDATED_DATE_START)
            .dateEnd(UPDATED_DATE_END)
            .dateReturn(UPDATED_DATE_RETURN)
            .checkupDate(UPDATED_CHECKUP_DATE)
            .convalescingPeriod(UPDATED_CONVALESCING_PERIOD)
            .diagnosis(UPDATED_DIAGNOSIS);
        LeaveDTO leaveDTO = leaveMapper.toDto(updatedLeave);

        restLeaveMockMvc
            .perform(
                put(ENTITY_API_URL_ID, leaveDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaveDTO))
            )
            .andExpect(status().isOk());

        // Validate the Leave in the database
        List<Leave> leaveList = leaveRepository.findAll();
        assertThat(leaveList).hasSize(databaseSizeBeforeUpdate);
        Leave testLeave = leaveList.get(leaveList.size() - 1);
        assertThat(testLeave.getDateApply()).isEqualTo(UPDATED_DATE_APPLY);
        assertThat(testLeave.getDateStart()).isEqualTo(UPDATED_DATE_START);
        assertThat(testLeave.getDateEnd()).isEqualTo(UPDATED_DATE_END);
        assertThat(testLeave.getDateReturn()).isEqualTo(UPDATED_DATE_RETURN);
        assertThat(testLeave.getCheckupDate()).isEqualTo(UPDATED_CHECKUP_DATE);
        assertThat(testLeave.getConvalescingPeriod()).isEqualTo(UPDATED_CONVALESCING_PERIOD);
        assertThat(testLeave.getDiagnosis()).isEqualTo(UPDATED_DIAGNOSIS);
    }

    @Test
    @Transactional
    void putNonExistingLeave() throws Exception {
        int databaseSizeBeforeUpdate = leaveRepository.findAll().size();
        leave.setId(count.incrementAndGet());

        // Create the Leave
        LeaveDTO leaveDTO = leaveMapper.toDto(leave);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLeaveMockMvc
            .perform(
                put(ENTITY_API_URL_ID, leaveDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaveDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Leave in the database
        List<Leave> leaveList = leaveRepository.findAll();
        assertThat(leaveList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLeave() throws Exception {
        int databaseSizeBeforeUpdate = leaveRepository.findAll().size();
        leave.setId(count.incrementAndGet());

        // Create the Leave
        LeaveDTO leaveDTO = leaveMapper.toDto(leave);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeaveMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaveDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Leave in the database
        List<Leave> leaveList = leaveRepository.findAll();
        assertThat(leaveList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLeave() throws Exception {
        int databaseSizeBeforeUpdate = leaveRepository.findAll().size();
        leave.setId(count.incrementAndGet());

        // Create the Leave
        LeaveDTO leaveDTO = leaveMapper.toDto(leave);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeaveMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(leaveDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Leave in the database
        List<Leave> leaveList = leaveRepository.findAll();
        assertThat(leaveList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLeaveWithPatch() throws Exception {
        // Initialize the database
        leaveRepository.saveAndFlush(leave);

        int databaseSizeBeforeUpdate = leaveRepository.findAll().size();

        // Update the leave using partial update
        Leave partialUpdatedLeave = new Leave();
        partialUpdatedLeave.setId(leave.getId());

        partialUpdatedLeave
            .dateApply(UPDATED_DATE_APPLY)
            .dateStart(UPDATED_DATE_START)
            .dateEnd(UPDATED_DATE_END)
            .checkupDate(UPDATED_CHECKUP_DATE)
            .convalescingPeriod(UPDATED_CONVALESCING_PERIOD)
            .diagnosis(UPDATED_DIAGNOSIS);

        restLeaveMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLeave.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLeave))
            )
            .andExpect(status().isOk());

        // Validate the Leave in the database
        List<Leave> leaveList = leaveRepository.findAll();
        assertThat(leaveList).hasSize(databaseSizeBeforeUpdate);
        Leave testLeave = leaveList.get(leaveList.size() - 1);
        assertThat(testLeave.getDateApply()).isEqualTo(UPDATED_DATE_APPLY);
        assertThat(testLeave.getDateStart()).isEqualTo(UPDATED_DATE_START);
        assertThat(testLeave.getDateEnd()).isEqualTo(UPDATED_DATE_END);
        assertThat(testLeave.getDateReturn()).isEqualTo(DEFAULT_DATE_RETURN);
        assertThat(testLeave.getCheckupDate()).isEqualTo(UPDATED_CHECKUP_DATE);
        assertThat(testLeave.getConvalescingPeriod()).isEqualTo(UPDATED_CONVALESCING_PERIOD);
        assertThat(testLeave.getDiagnosis()).isEqualTo(UPDATED_DIAGNOSIS);
    }

    @Test
    @Transactional
    void fullUpdateLeaveWithPatch() throws Exception {
        // Initialize the database
        leaveRepository.saveAndFlush(leave);

        int databaseSizeBeforeUpdate = leaveRepository.findAll().size();

        // Update the leave using partial update
        Leave partialUpdatedLeave = new Leave();
        partialUpdatedLeave.setId(leave.getId());

        partialUpdatedLeave
            .dateApply(UPDATED_DATE_APPLY)
            .dateStart(UPDATED_DATE_START)
            .dateEnd(UPDATED_DATE_END)
            .dateReturn(UPDATED_DATE_RETURN)
            .checkupDate(UPDATED_CHECKUP_DATE)
            .convalescingPeriod(UPDATED_CONVALESCING_PERIOD)
            .diagnosis(UPDATED_DIAGNOSIS);

        restLeaveMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLeave.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLeave))
            )
            .andExpect(status().isOk());

        // Validate the Leave in the database
        List<Leave> leaveList = leaveRepository.findAll();
        assertThat(leaveList).hasSize(databaseSizeBeforeUpdate);
        Leave testLeave = leaveList.get(leaveList.size() - 1);
        assertThat(testLeave.getDateApply()).isEqualTo(UPDATED_DATE_APPLY);
        assertThat(testLeave.getDateStart()).isEqualTo(UPDATED_DATE_START);
        assertThat(testLeave.getDateEnd()).isEqualTo(UPDATED_DATE_END);
        assertThat(testLeave.getDateReturn()).isEqualTo(UPDATED_DATE_RETURN);
        assertThat(testLeave.getCheckupDate()).isEqualTo(UPDATED_CHECKUP_DATE);
        assertThat(testLeave.getConvalescingPeriod()).isEqualTo(UPDATED_CONVALESCING_PERIOD);
        assertThat(testLeave.getDiagnosis()).isEqualTo(UPDATED_DIAGNOSIS);
    }

    @Test
    @Transactional
    void patchNonExistingLeave() throws Exception {
        int databaseSizeBeforeUpdate = leaveRepository.findAll().size();
        leave.setId(count.incrementAndGet());

        // Create the Leave
        LeaveDTO leaveDTO = leaveMapper.toDto(leave);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLeaveMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, leaveDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(leaveDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Leave in the database
        List<Leave> leaveList = leaveRepository.findAll();
        assertThat(leaveList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLeave() throws Exception {
        int databaseSizeBeforeUpdate = leaveRepository.findAll().size();
        leave.setId(count.incrementAndGet());

        // Create the Leave
        LeaveDTO leaveDTO = leaveMapper.toDto(leave);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeaveMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(leaveDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Leave in the database
        List<Leave> leaveList = leaveRepository.findAll();
        assertThat(leaveList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLeave() throws Exception {
        int databaseSizeBeforeUpdate = leaveRepository.findAll().size();
        leave.setId(count.incrementAndGet());

        // Create the Leave
        LeaveDTO leaveDTO = leaveMapper.toDto(leave);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeaveMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(leaveDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Leave in the database
        List<Leave> leaveList = leaveRepository.findAll();
        assertThat(leaveList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLeave() throws Exception {
        // Initialize the database
        leaveRepository.saveAndFlush(leave);

        int databaseSizeBeforeDelete = leaveRepository.findAll().size();

        // Delete the leave
        restLeaveMockMvc
            .perform(delete(ENTITY_API_URL_ID, leave.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Leave> leaveList = leaveRepository.findAll();
        assertThat(leaveList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
