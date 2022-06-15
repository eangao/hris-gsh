package com.hrisgsh.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.hrisgsh.app.IntegrationTest;
import com.hrisgsh.app.domain.Employee;
import com.hrisgsh.app.domain.TrainingHistory;
import com.hrisgsh.app.repository.TrainingHistoryRepository;
import com.hrisgsh.app.service.criteria.TrainingHistoryCriteria;
import com.hrisgsh.app.service.dto.TrainingHistoryDTO;
import com.hrisgsh.app.service.mapper.TrainingHistoryMapper;
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
 * Integration tests for the {@link TrainingHistoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TrainingHistoryResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/training-histories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TrainingHistoryRepository trainingHistoryRepository;

    @Autowired
    private TrainingHistoryMapper trainingHistoryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTrainingHistoryMockMvc;

    private TrainingHistory trainingHistory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TrainingHistory createEntity(EntityManager em) {
        TrainingHistory trainingHistory = new TrainingHistory().name(DEFAULT_NAME).date(DEFAULT_DATE).description(DEFAULT_DESCRIPTION);
        // Add required entity
        Employee employee;
        if (TestUtil.findAll(em, Employee.class).isEmpty()) {
            employee = EmployeeResourceIT.createEntity(em);
            em.persist(employee);
            em.flush();
        } else {
            employee = TestUtil.findAll(em, Employee.class).get(0);
        }
        trainingHistory.setEmployee(employee);
        return trainingHistory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TrainingHistory createUpdatedEntity(EntityManager em) {
        TrainingHistory trainingHistory = new TrainingHistory().name(UPDATED_NAME).date(UPDATED_DATE).description(UPDATED_DESCRIPTION);
        // Add required entity
        Employee employee;
        if (TestUtil.findAll(em, Employee.class).isEmpty()) {
            employee = EmployeeResourceIT.createUpdatedEntity(em);
            em.persist(employee);
            em.flush();
        } else {
            employee = TestUtil.findAll(em, Employee.class).get(0);
        }
        trainingHistory.setEmployee(employee);
        return trainingHistory;
    }

    @BeforeEach
    public void initTest() {
        trainingHistory = createEntity(em);
    }

    @Test
    @Transactional
    void createTrainingHistory() throws Exception {
        int databaseSizeBeforeCreate = trainingHistoryRepository.findAll().size();
        // Create the TrainingHistory
        TrainingHistoryDTO trainingHistoryDTO = trainingHistoryMapper.toDto(trainingHistory);
        restTrainingHistoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trainingHistoryDTO))
            )
            .andExpect(status().isCreated());

        // Validate the TrainingHistory in the database
        List<TrainingHistory> trainingHistoryList = trainingHistoryRepository.findAll();
        assertThat(trainingHistoryList).hasSize(databaseSizeBeforeCreate + 1);
        TrainingHistory testTrainingHistory = trainingHistoryList.get(trainingHistoryList.size() - 1);
        assertThat(testTrainingHistory.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTrainingHistory.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testTrainingHistory.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createTrainingHistoryWithExistingId() throws Exception {
        // Create the TrainingHistory with an existing ID
        trainingHistory.setId(1L);
        TrainingHistoryDTO trainingHistoryDTO = trainingHistoryMapper.toDto(trainingHistory);

        int databaseSizeBeforeCreate = trainingHistoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTrainingHistoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trainingHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrainingHistory in the database
        List<TrainingHistory> trainingHistoryList = trainingHistoryRepository.findAll();
        assertThat(trainingHistoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = trainingHistoryRepository.findAll().size();
        // set the field null
        trainingHistory.setName(null);

        // Create the TrainingHistory, which fails.
        TrainingHistoryDTO trainingHistoryDTO = trainingHistoryMapper.toDto(trainingHistory);

        restTrainingHistoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trainingHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        List<TrainingHistory> trainingHistoryList = trainingHistoryRepository.findAll();
        assertThat(trainingHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = trainingHistoryRepository.findAll().size();
        // set the field null
        trainingHistory.setDate(null);

        // Create the TrainingHistory, which fails.
        TrainingHistoryDTO trainingHistoryDTO = trainingHistoryMapper.toDto(trainingHistory);

        restTrainingHistoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trainingHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        List<TrainingHistory> trainingHistoryList = trainingHistoryRepository.findAll();
        assertThat(trainingHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTrainingHistories() throws Exception {
        // Initialize the database
        trainingHistoryRepository.saveAndFlush(trainingHistory);

        // Get all the trainingHistoryList
        restTrainingHistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trainingHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getTrainingHistory() throws Exception {
        // Initialize the database
        trainingHistoryRepository.saveAndFlush(trainingHistory);

        // Get the trainingHistory
        restTrainingHistoryMockMvc
            .perform(get(ENTITY_API_URL_ID, trainingHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(trainingHistory.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getTrainingHistoriesByIdFiltering() throws Exception {
        // Initialize the database
        trainingHistoryRepository.saveAndFlush(trainingHistory);

        Long id = trainingHistory.getId();

        defaultTrainingHistoryShouldBeFound("id.equals=" + id);
        defaultTrainingHistoryShouldNotBeFound("id.notEquals=" + id);

        defaultTrainingHistoryShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTrainingHistoryShouldNotBeFound("id.greaterThan=" + id);

        defaultTrainingHistoryShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTrainingHistoryShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTrainingHistoriesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        trainingHistoryRepository.saveAndFlush(trainingHistory);

        // Get all the trainingHistoryList where name equals to DEFAULT_NAME
        defaultTrainingHistoryShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the trainingHistoryList where name equals to UPDATED_NAME
        defaultTrainingHistoryShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTrainingHistoriesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        trainingHistoryRepository.saveAndFlush(trainingHistory);

        // Get all the trainingHistoryList where name not equals to DEFAULT_NAME
        defaultTrainingHistoryShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the trainingHistoryList where name not equals to UPDATED_NAME
        defaultTrainingHistoryShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTrainingHistoriesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        trainingHistoryRepository.saveAndFlush(trainingHistory);

        // Get all the trainingHistoryList where name in DEFAULT_NAME or UPDATED_NAME
        defaultTrainingHistoryShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the trainingHistoryList where name equals to UPDATED_NAME
        defaultTrainingHistoryShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTrainingHistoriesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        trainingHistoryRepository.saveAndFlush(trainingHistory);

        // Get all the trainingHistoryList where name is not null
        defaultTrainingHistoryShouldBeFound("name.specified=true");

        // Get all the trainingHistoryList where name is null
        defaultTrainingHistoryShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllTrainingHistoriesByNameContainsSomething() throws Exception {
        // Initialize the database
        trainingHistoryRepository.saveAndFlush(trainingHistory);

        // Get all the trainingHistoryList where name contains DEFAULT_NAME
        defaultTrainingHistoryShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the trainingHistoryList where name contains UPDATED_NAME
        defaultTrainingHistoryShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTrainingHistoriesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        trainingHistoryRepository.saveAndFlush(trainingHistory);

        // Get all the trainingHistoryList where name does not contain DEFAULT_NAME
        defaultTrainingHistoryShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the trainingHistoryList where name does not contain UPDATED_NAME
        defaultTrainingHistoryShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTrainingHistoriesByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        trainingHistoryRepository.saveAndFlush(trainingHistory);

        // Get all the trainingHistoryList where date equals to DEFAULT_DATE
        defaultTrainingHistoryShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the trainingHistoryList where date equals to UPDATED_DATE
        defaultTrainingHistoryShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllTrainingHistoriesByDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        trainingHistoryRepository.saveAndFlush(trainingHistory);

        // Get all the trainingHistoryList where date not equals to DEFAULT_DATE
        defaultTrainingHistoryShouldNotBeFound("date.notEquals=" + DEFAULT_DATE);

        // Get all the trainingHistoryList where date not equals to UPDATED_DATE
        defaultTrainingHistoryShouldBeFound("date.notEquals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllTrainingHistoriesByDateIsInShouldWork() throws Exception {
        // Initialize the database
        trainingHistoryRepository.saveAndFlush(trainingHistory);

        // Get all the trainingHistoryList where date in DEFAULT_DATE or UPDATED_DATE
        defaultTrainingHistoryShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the trainingHistoryList where date equals to UPDATED_DATE
        defaultTrainingHistoryShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllTrainingHistoriesByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        trainingHistoryRepository.saveAndFlush(trainingHistory);

        // Get all the trainingHistoryList where date is not null
        defaultTrainingHistoryShouldBeFound("date.specified=true");

        // Get all the trainingHistoryList where date is null
        defaultTrainingHistoryShouldNotBeFound("date.specified=false");
    }

    @Test
    @Transactional
    void getAllTrainingHistoriesByDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        trainingHistoryRepository.saveAndFlush(trainingHistory);

        // Get all the trainingHistoryList where date is greater than or equal to DEFAULT_DATE
        defaultTrainingHistoryShouldBeFound("date.greaterThanOrEqual=" + DEFAULT_DATE);

        // Get all the trainingHistoryList where date is greater than or equal to UPDATED_DATE
        defaultTrainingHistoryShouldNotBeFound("date.greaterThanOrEqual=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllTrainingHistoriesByDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        trainingHistoryRepository.saveAndFlush(trainingHistory);

        // Get all the trainingHistoryList where date is less than or equal to DEFAULT_DATE
        defaultTrainingHistoryShouldBeFound("date.lessThanOrEqual=" + DEFAULT_DATE);

        // Get all the trainingHistoryList where date is less than or equal to SMALLER_DATE
        defaultTrainingHistoryShouldNotBeFound("date.lessThanOrEqual=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllTrainingHistoriesByDateIsLessThanSomething() throws Exception {
        // Initialize the database
        trainingHistoryRepository.saveAndFlush(trainingHistory);

        // Get all the trainingHistoryList where date is less than DEFAULT_DATE
        defaultTrainingHistoryShouldNotBeFound("date.lessThan=" + DEFAULT_DATE);

        // Get all the trainingHistoryList where date is less than UPDATED_DATE
        defaultTrainingHistoryShouldBeFound("date.lessThan=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllTrainingHistoriesByDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        trainingHistoryRepository.saveAndFlush(trainingHistory);

        // Get all the trainingHistoryList where date is greater than DEFAULT_DATE
        defaultTrainingHistoryShouldNotBeFound("date.greaterThan=" + DEFAULT_DATE);

        // Get all the trainingHistoryList where date is greater than SMALLER_DATE
        defaultTrainingHistoryShouldBeFound("date.greaterThan=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllTrainingHistoriesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        trainingHistoryRepository.saveAndFlush(trainingHistory);

        // Get all the trainingHistoryList where description equals to DEFAULT_DESCRIPTION
        defaultTrainingHistoryShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the trainingHistoryList where description equals to UPDATED_DESCRIPTION
        defaultTrainingHistoryShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTrainingHistoriesByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        trainingHistoryRepository.saveAndFlush(trainingHistory);

        // Get all the trainingHistoryList where description not equals to DEFAULT_DESCRIPTION
        defaultTrainingHistoryShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the trainingHistoryList where description not equals to UPDATED_DESCRIPTION
        defaultTrainingHistoryShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTrainingHistoriesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        trainingHistoryRepository.saveAndFlush(trainingHistory);

        // Get all the trainingHistoryList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultTrainingHistoryShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the trainingHistoryList where description equals to UPDATED_DESCRIPTION
        defaultTrainingHistoryShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTrainingHistoriesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        trainingHistoryRepository.saveAndFlush(trainingHistory);

        // Get all the trainingHistoryList where description is not null
        defaultTrainingHistoryShouldBeFound("description.specified=true");

        // Get all the trainingHistoryList where description is null
        defaultTrainingHistoryShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllTrainingHistoriesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        trainingHistoryRepository.saveAndFlush(trainingHistory);

        // Get all the trainingHistoryList where description contains DEFAULT_DESCRIPTION
        defaultTrainingHistoryShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the trainingHistoryList where description contains UPDATED_DESCRIPTION
        defaultTrainingHistoryShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTrainingHistoriesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        trainingHistoryRepository.saveAndFlush(trainingHistory);

        // Get all the trainingHistoryList where description does not contain DEFAULT_DESCRIPTION
        defaultTrainingHistoryShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the trainingHistoryList where description does not contain UPDATED_DESCRIPTION
        defaultTrainingHistoryShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTrainingHistoriesByEmployeeIsEqualToSomething() throws Exception {
        // Initialize the database
        trainingHistoryRepository.saveAndFlush(trainingHistory);
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
        trainingHistory.setEmployee(employee);
        trainingHistoryRepository.saveAndFlush(trainingHistory);
        Long employeeId = employee.getId();

        // Get all the trainingHistoryList where employee equals to employeeId
        defaultTrainingHistoryShouldBeFound("employeeId.equals=" + employeeId);

        // Get all the trainingHistoryList where employee equals to (employeeId + 1)
        defaultTrainingHistoryShouldNotBeFound("employeeId.equals=" + (employeeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTrainingHistoryShouldBeFound(String filter) throws Exception {
        restTrainingHistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trainingHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restTrainingHistoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTrainingHistoryShouldNotBeFound(String filter) throws Exception {
        restTrainingHistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTrainingHistoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTrainingHistory() throws Exception {
        // Get the trainingHistory
        restTrainingHistoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTrainingHistory() throws Exception {
        // Initialize the database
        trainingHistoryRepository.saveAndFlush(trainingHistory);

        int databaseSizeBeforeUpdate = trainingHistoryRepository.findAll().size();

        // Update the trainingHistory
        TrainingHistory updatedTrainingHistory = trainingHistoryRepository.findById(trainingHistory.getId()).get();
        // Disconnect from session so that the updates on updatedTrainingHistory are not directly saved in db
        em.detach(updatedTrainingHistory);
        updatedTrainingHistory.name(UPDATED_NAME).date(UPDATED_DATE).description(UPDATED_DESCRIPTION);
        TrainingHistoryDTO trainingHistoryDTO = trainingHistoryMapper.toDto(updatedTrainingHistory);

        restTrainingHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, trainingHistoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(trainingHistoryDTO))
            )
            .andExpect(status().isOk());

        // Validate the TrainingHistory in the database
        List<TrainingHistory> trainingHistoryList = trainingHistoryRepository.findAll();
        assertThat(trainingHistoryList).hasSize(databaseSizeBeforeUpdate);
        TrainingHistory testTrainingHistory = trainingHistoryList.get(trainingHistoryList.size() - 1);
        assertThat(testTrainingHistory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTrainingHistory.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testTrainingHistory.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingTrainingHistory() throws Exception {
        int databaseSizeBeforeUpdate = trainingHistoryRepository.findAll().size();
        trainingHistory.setId(count.incrementAndGet());

        // Create the TrainingHistory
        TrainingHistoryDTO trainingHistoryDTO = trainingHistoryMapper.toDto(trainingHistory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrainingHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, trainingHistoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(trainingHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrainingHistory in the database
        List<TrainingHistory> trainingHistoryList = trainingHistoryRepository.findAll();
        assertThat(trainingHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTrainingHistory() throws Exception {
        int databaseSizeBeforeUpdate = trainingHistoryRepository.findAll().size();
        trainingHistory.setId(count.incrementAndGet());

        // Create the TrainingHistory
        TrainingHistoryDTO trainingHistoryDTO = trainingHistoryMapper.toDto(trainingHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrainingHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(trainingHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrainingHistory in the database
        List<TrainingHistory> trainingHistoryList = trainingHistoryRepository.findAll();
        assertThat(trainingHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTrainingHistory() throws Exception {
        int databaseSizeBeforeUpdate = trainingHistoryRepository.findAll().size();
        trainingHistory.setId(count.incrementAndGet());

        // Create the TrainingHistory
        TrainingHistoryDTO trainingHistoryDTO = trainingHistoryMapper.toDto(trainingHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrainingHistoryMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trainingHistoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TrainingHistory in the database
        List<TrainingHistory> trainingHistoryList = trainingHistoryRepository.findAll();
        assertThat(trainingHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTrainingHistoryWithPatch() throws Exception {
        // Initialize the database
        trainingHistoryRepository.saveAndFlush(trainingHistory);

        int databaseSizeBeforeUpdate = trainingHistoryRepository.findAll().size();

        // Update the trainingHistory using partial update
        TrainingHistory partialUpdatedTrainingHistory = new TrainingHistory();
        partialUpdatedTrainingHistory.setId(trainingHistory.getId());

        partialUpdatedTrainingHistory.description(UPDATED_DESCRIPTION);

        restTrainingHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrainingHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTrainingHistory))
            )
            .andExpect(status().isOk());

        // Validate the TrainingHistory in the database
        List<TrainingHistory> trainingHistoryList = trainingHistoryRepository.findAll();
        assertThat(trainingHistoryList).hasSize(databaseSizeBeforeUpdate);
        TrainingHistory testTrainingHistory = trainingHistoryList.get(trainingHistoryList.size() - 1);
        assertThat(testTrainingHistory.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTrainingHistory.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testTrainingHistory.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateTrainingHistoryWithPatch() throws Exception {
        // Initialize the database
        trainingHistoryRepository.saveAndFlush(trainingHistory);

        int databaseSizeBeforeUpdate = trainingHistoryRepository.findAll().size();

        // Update the trainingHistory using partial update
        TrainingHistory partialUpdatedTrainingHistory = new TrainingHistory();
        partialUpdatedTrainingHistory.setId(trainingHistory.getId());

        partialUpdatedTrainingHistory.name(UPDATED_NAME).date(UPDATED_DATE).description(UPDATED_DESCRIPTION);

        restTrainingHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrainingHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTrainingHistory))
            )
            .andExpect(status().isOk());

        // Validate the TrainingHistory in the database
        List<TrainingHistory> trainingHistoryList = trainingHistoryRepository.findAll();
        assertThat(trainingHistoryList).hasSize(databaseSizeBeforeUpdate);
        TrainingHistory testTrainingHistory = trainingHistoryList.get(trainingHistoryList.size() - 1);
        assertThat(testTrainingHistory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTrainingHistory.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testTrainingHistory.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingTrainingHistory() throws Exception {
        int databaseSizeBeforeUpdate = trainingHistoryRepository.findAll().size();
        trainingHistory.setId(count.incrementAndGet());

        // Create the TrainingHistory
        TrainingHistoryDTO trainingHistoryDTO = trainingHistoryMapper.toDto(trainingHistory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrainingHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, trainingHistoryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(trainingHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrainingHistory in the database
        List<TrainingHistory> trainingHistoryList = trainingHistoryRepository.findAll();
        assertThat(trainingHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTrainingHistory() throws Exception {
        int databaseSizeBeforeUpdate = trainingHistoryRepository.findAll().size();
        trainingHistory.setId(count.incrementAndGet());

        // Create the TrainingHistory
        TrainingHistoryDTO trainingHistoryDTO = trainingHistoryMapper.toDto(trainingHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrainingHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(trainingHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrainingHistory in the database
        List<TrainingHistory> trainingHistoryList = trainingHistoryRepository.findAll();
        assertThat(trainingHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTrainingHistory() throws Exception {
        int databaseSizeBeforeUpdate = trainingHistoryRepository.findAll().size();
        trainingHistory.setId(count.incrementAndGet());

        // Create the TrainingHistory
        TrainingHistoryDTO trainingHistoryDTO = trainingHistoryMapper.toDto(trainingHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrainingHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(trainingHistoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TrainingHistory in the database
        List<TrainingHistory> trainingHistoryList = trainingHistoryRepository.findAll();
        assertThat(trainingHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTrainingHistory() throws Exception {
        // Initialize the database
        trainingHistoryRepository.saveAndFlush(trainingHistory);

        int databaseSizeBeforeDelete = trainingHistoryRepository.findAll().size();

        // Delete the trainingHistory
        restTrainingHistoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, trainingHistory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TrainingHistory> trainingHistoryList = trainingHistoryRepository.findAll();
        assertThat(trainingHistoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
