package com.hrisgsh.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.hrisgsh.app.IntegrationTest;
import com.hrisgsh.app.domain.Dependents;
import com.hrisgsh.app.domain.Employee;
import com.hrisgsh.app.repository.DependentsRepository;
import com.hrisgsh.app.service.criteria.DependentsCriteria;
import com.hrisgsh.app.service.dto.DependentsDTO;
import com.hrisgsh.app.service.mapper.DependentsMapper;
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
 * Integration tests for the {@link DependentsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DependentsResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_MIDDLE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_MIDDLE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_RELATION = "AAAAAAAAAA";
    private static final String UPDATED_RELATION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/dependents";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DependentsRepository dependentsRepository;

    @Autowired
    private DependentsMapper dependentsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDependentsMockMvc;

    private Dependents dependents;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dependents createEntity(EntityManager em) {
        Dependents dependents = new Dependents()
            .firstName(DEFAULT_FIRST_NAME)
            .middleName(DEFAULT_MIDDLE_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .relation(DEFAULT_RELATION);
        // Add required entity
        Employee employee;
        if (TestUtil.findAll(em, Employee.class).isEmpty()) {
            employee = EmployeeResourceIT.createEntity(em);
            em.persist(employee);
            em.flush();
        } else {
            employee = TestUtil.findAll(em, Employee.class).get(0);
        }
        dependents.setEmployee(employee);
        return dependents;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dependents createUpdatedEntity(EntityManager em) {
        Dependents dependents = new Dependents()
            .firstName(UPDATED_FIRST_NAME)
            .middleName(UPDATED_MIDDLE_NAME)
            .lastName(UPDATED_LAST_NAME)
            .relation(UPDATED_RELATION);
        // Add required entity
        Employee employee;
        if (TestUtil.findAll(em, Employee.class).isEmpty()) {
            employee = EmployeeResourceIT.createUpdatedEntity(em);
            em.persist(employee);
            em.flush();
        } else {
            employee = TestUtil.findAll(em, Employee.class).get(0);
        }
        dependents.setEmployee(employee);
        return dependents;
    }

    @BeforeEach
    public void initTest() {
        dependents = createEntity(em);
    }

    @Test
    @Transactional
    void createDependents() throws Exception {
        int databaseSizeBeforeCreate = dependentsRepository.findAll().size();
        // Create the Dependents
        DependentsDTO dependentsDTO = dependentsMapper.toDto(dependents);
        restDependentsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dependentsDTO)))
            .andExpect(status().isCreated());

        // Validate the Dependents in the database
        List<Dependents> dependentsList = dependentsRepository.findAll();
        assertThat(dependentsList).hasSize(databaseSizeBeforeCreate + 1);
        Dependents testDependents = dependentsList.get(dependentsList.size() - 1);
        assertThat(testDependents.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testDependents.getMiddleName()).isEqualTo(DEFAULT_MIDDLE_NAME);
        assertThat(testDependents.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testDependents.getRelation()).isEqualTo(DEFAULT_RELATION);
    }

    @Test
    @Transactional
    void createDependentsWithExistingId() throws Exception {
        // Create the Dependents with an existing ID
        dependents.setId(1L);
        DependentsDTO dependentsDTO = dependentsMapper.toDto(dependents);

        int databaseSizeBeforeCreate = dependentsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDependentsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dependentsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Dependents in the database
        List<Dependents> dependentsList = dependentsRepository.findAll();
        assertThat(dependentsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = dependentsRepository.findAll().size();
        // set the field null
        dependents.setFirstName(null);

        // Create the Dependents, which fails.
        DependentsDTO dependentsDTO = dependentsMapper.toDto(dependents);

        restDependentsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dependentsDTO)))
            .andExpect(status().isBadRequest());

        List<Dependents> dependentsList = dependentsRepository.findAll();
        assertThat(dependentsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = dependentsRepository.findAll().size();
        // set the field null
        dependents.setLastName(null);

        // Create the Dependents, which fails.
        DependentsDTO dependentsDTO = dependentsMapper.toDto(dependents);

        restDependentsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dependentsDTO)))
            .andExpect(status().isBadRequest());

        List<Dependents> dependentsList = dependentsRepository.findAll();
        assertThat(dependentsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRelationIsRequired() throws Exception {
        int databaseSizeBeforeTest = dependentsRepository.findAll().size();
        // set the field null
        dependents.setRelation(null);

        // Create the Dependents, which fails.
        DependentsDTO dependentsDTO = dependentsMapper.toDto(dependents);

        restDependentsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dependentsDTO)))
            .andExpect(status().isBadRequest());

        List<Dependents> dependentsList = dependentsRepository.findAll();
        assertThat(dependentsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDependents() throws Exception {
        // Initialize the database
        dependentsRepository.saveAndFlush(dependents);

        // Get all the dependentsList
        restDependentsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dependents.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].middleName").value(hasItem(DEFAULT_MIDDLE_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].relation").value(hasItem(DEFAULT_RELATION)));
    }

    @Test
    @Transactional
    void getDependents() throws Exception {
        // Initialize the database
        dependentsRepository.saveAndFlush(dependents);

        // Get the dependents
        restDependentsMockMvc
            .perform(get(ENTITY_API_URL_ID, dependents.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dependents.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.middleName").value(DEFAULT_MIDDLE_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.relation").value(DEFAULT_RELATION));
    }

    @Test
    @Transactional
    void getDependentsByIdFiltering() throws Exception {
        // Initialize the database
        dependentsRepository.saveAndFlush(dependents);

        Long id = dependents.getId();

        defaultDependentsShouldBeFound("id.equals=" + id);
        defaultDependentsShouldNotBeFound("id.notEquals=" + id);

        defaultDependentsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDependentsShouldNotBeFound("id.greaterThan=" + id);

        defaultDependentsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDependentsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDependentsByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        dependentsRepository.saveAndFlush(dependents);

        // Get all the dependentsList where firstName equals to DEFAULT_FIRST_NAME
        defaultDependentsShouldBeFound("firstName.equals=" + DEFAULT_FIRST_NAME);

        // Get all the dependentsList where firstName equals to UPDATED_FIRST_NAME
        defaultDependentsShouldNotBeFound("firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllDependentsByFirstNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dependentsRepository.saveAndFlush(dependents);

        // Get all the dependentsList where firstName not equals to DEFAULT_FIRST_NAME
        defaultDependentsShouldNotBeFound("firstName.notEquals=" + DEFAULT_FIRST_NAME);

        // Get all the dependentsList where firstName not equals to UPDATED_FIRST_NAME
        defaultDependentsShouldBeFound("firstName.notEquals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllDependentsByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        dependentsRepository.saveAndFlush(dependents);

        // Get all the dependentsList where firstName in DEFAULT_FIRST_NAME or UPDATED_FIRST_NAME
        defaultDependentsShouldBeFound("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME);

        // Get all the dependentsList where firstName equals to UPDATED_FIRST_NAME
        defaultDependentsShouldNotBeFound("firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllDependentsByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        dependentsRepository.saveAndFlush(dependents);

        // Get all the dependentsList where firstName is not null
        defaultDependentsShouldBeFound("firstName.specified=true");

        // Get all the dependentsList where firstName is null
        defaultDependentsShouldNotBeFound("firstName.specified=false");
    }

    @Test
    @Transactional
    void getAllDependentsByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        dependentsRepository.saveAndFlush(dependents);

        // Get all the dependentsList where firstName contains DEFAULT_FIRST_NAME
        defaultDependentsShouldBeFound("firstName.contains=" + DEFAULT_FIRST_NAME);

        // Get all the dependentsList where firstName contains UPDATED_FIRST_NAME
        defaultDependentsShouldNotBeFound("firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllDependentsByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        dependentsRepository.saveAndFlush(dependents);

        // Get all the dependentsList where firstName does not contain DEFAULT_FIRST_NAME
        defaultDependentsShouldNotBeFound("firstName.doesNotContain=" + DEFAULT_FIRST_NAME);

        // Get all the dependentsList where firstName does not contain UPDATED_FIRST_NAME
        defaultDependentsShouldBeFound("firstName.doesNotContain=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllDependentsByMiddleNameIsEqualToSomething() throws Exception {
        // Initialize the database
        dependentsRepository.saveAndFlush(dependents);

        // Get all the dependentsList where middleName equals to DEFAULT_MIDDLE_NAME
        defaultDependentsShouldBeFound("middleName.equals=" + DEFAULT_MIDDLE_NAME);

        // Get all the dependentsList where middleName equals to UPDATED_MIDDLE_NAME
        defaultDependentsShouldNotBeFound("middleName.equals=" + UPDATED_MIDDLE_NAME);
    }

    @Test
    @Transactional
    void getAllDependentsByMiddleNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dependentsRepository.saveAndFlush(dependents);

        // Get all the dependentsList where middleName not equals to DEFAULT_MIDDLE_NAME
        defaultDependentsShouldNotBeFound("middleName.notEquals=" + DEFAULT_MIDDLE_NAME);

        // Get all the dependentsList where middleName not equals to UPDATED_MIDDLE_NAME
        defaultDependentsShouldBeFound("middleName.notEquals=" + UPDATED_MIDDLE_NAME);
    }

    @Test
    @Transactional
    void getAllDependentsByMiddleNameIsInShouldWork() throws Exception {
        // Initialize the database
        dependentsRepository.saveAndFlush(dependents);

        // Get all the dependentsList where middleName in DEFAULT_MIDDLE_NAME or UPDATED_MIDDLE_NAME
        defaultDependentsShouldBeFound("middleName.in=" + DEFAULT_MIDDLE_NAME + "," + UPDATED_MIDDLE_NAME);

        // Get all the dependentsList where middleName equals to UPDATED_MIDDLE_NAME
        defaultDependentsShouldNotBeFound("middleName.in=" + UPDATED_MIDDLE_NAME);
    }

    @Test
    @Transactional
    void getAllDependentsByMiddleNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        dependentsRepository.saveAndFlush(dependents);

        // Get all the dependentsList where middleName is not null
        defaultDependentsShouldBeFound("middleName.specified=true");

        // Get all the dependentsList where middleName is null
        defaultDependentsShouldNotBeFound("middleName.specified=false");
    }

    @Test
    @Transactional
    void getAllDependentsByMiddleNameContainsSomething() throws Exception {
        // Initialize the database
        dependentsRepository.saveAndFlush(dependents);

        // Get all the dependentsList where middleName contains DEFAULT_MIDDLE_NAME
        defaultDependentsShouldBeFound("middleName.contains=" + DEFAULT_MIDDLE_NAME);

        // Get all the dependentsList where middleName contains UPDATED_MIDDLE_NAME
        defaultDependentsShouldNotBeFound("middleName.contains=" + UPDATED_MIDDLE_NAME);
    }

    @Test
    @Transactional
    void getAllDependentsByMiddleNameNotContainsSomething() throws Exception {
        // Initialize the database
        dependentsRepository.saveAndFlush(dependents);

        // Get all the dependentsList where middleName does not contain DEFAULT_MIDDLE_NAME
        defaultDependentsShouldNotBeFound("middleName.doesNotContain=" + DEFAULT_MIDDLE_NAME);

        // Get all the dependentsList where middleName does not contain UPDATED_MIDDLE_NAME
        defaultDependentsShouldBeFound("middleName.doesNotContain=" + UPDATED_MIDDLE_NAME);
    }

    @Test
    @Transactional
    void getAllDependentsByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        dependentsRepository.saveAndFlush(dependents);

        // Get all the dependentsList where lastName equals to DEFAULT_LAST_NAME
        defaultDependentsShouldBeFound("lastName.equals=" + DEFAULT_LAST_NAME);

        // Get all the dependentsList where lastName equals to UPDATED_LAST_NAME
        defaultDependentsShouldNotBeFound("lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllDependentsByLastNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dependentsRepository.saveAndFlush(dependents);

        // Get all the dependentsList where lastName not equals to DEFAULT_LAST_NAME
        defaultDependentsShouldNotBeFound("lastName.notEquals=" + DEFAULT_LAST_NAME);

        // Get all the dependentsList where lastName not equals to UPDATED_LAST_NAME
        defaultDependentsShouldBeFound("lastName.notEquals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllDependentsByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        dependentsRepository.saveAndFlush(dependents);

        // Get all the dependentsList where lastName in DEFAULT_LAST_NAME or UPDATED_LAST_NAME
        defaultDependentsShouldBeFound("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME);

        // Get all the dependentsList where lastName equals to UPDATED_LAST_NAME
        defaultDependentsShouldNotBeFound("lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllDependentsByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        dependentsRepository.saveAndFlush(dependents);

        // Get all the dependentsList where lastName is not null
        defaultDependentsShouldBeFound("lastName.specified=true");

        // Get all the dependentsList where lastName is null
        defaultDependentsShouldNotBeFound("lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllDependentsByLastNameContainsSomething() throws Exception {
        // Initialize the database
        dependentsRepository.saveAndFlush(dependents);

        // Get all the dependentsList where lastName contains DEFAULT_LAST_NAME
        defaultDependentsShouldBeFound("lastName.contains=" + DEFAULT_LAST_NAME);

        // Get all the dependentsList where lastName contains UPDATED_LAST_NAME
        defaultDependentsShouldNotBeFound("lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllDependentsByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        dependentsRepository.saveAndFlush(dependents);

        // Get all the dependentsList where lastName does not contain DEFAULT_LAST_NAME
        defaultDependentsShouldNotBeFound("lastName.doesNotContain=" + DEFAULT_LAST_NAME);

        // Get all the dependentsList where lastName does not contain UPDATED_LAST_NAME
        defaultDependentsShouldBeFound("lastName.doesNotContain=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllDependentsByRelationIsEqualToSomething() throws Exception {
        // Initialize the database
        dependentsRepository.saveAndFlush(dependents);

        // Get all the dependentsList where relation equals to DEFAULT_RELATION
        defaultDependentsShouldBeFound("relation.equals=" + DEFAULT_RELATION);

        // Get all the dependentsList where relation equals to UPDATED_RELATION
        defaultDependentsShouldNotBeFound("relation.equals=" + UPDATED_RELATION);
    }

    @Test
    @Transactional
    void getAllDependentsByRelationIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dependentsRepository.saveAndFlush(dependents);

        // Get all the dependentsList where relation not equals to DEFAULT_RELATION
        defaultDependentsShouldNotBeFound("relation.notEquals=" + DEFAULT_RELATION);

        // Get all the dependentsList where relation not equals to UPDATED_RELATION
        defaultDependentsShouldBeFound("relation.notEquals=" + UPDATED_RELATION);
    }

    @Test
    @Transactional
    void getAllDependentsByRelationIsInShouldWork() throws Exception {
        // Initialize the database
        dependentsRepository.saveAndFlush(dependents);

        // Get all the dependentsList where relation in DEFAULT_RELATION or UPDATED_RELATION
        defaultDependentsShouldBeFound("relation.in=" + DEFAULT_RELATION + "," + UPDATED_RELATION);

        // Get all the dependentsList where relation equals to UPDATED_RELATION
        defaultDependentsShouldNotBeFound("relation.in=" + UPDATED_RELATION);
    }

    @Test
    @Transactional
    void getAllDependentsByRelationIsNullOrNotNull() throws Exception {
        // Initialize the database
        dependentsRepository.saveAndFlush(dependents);

        // Get all the dependentsList where relation is not null
        defaultDependentsShouldBeFound("relation.specified=true");

        // Get all the dependentsList where relation is null
        defaultDependentsShouldNotBeFound("relation.specified=false");
    }

    @Test
    @Transactional
    void getAllDependentsByRelationContainsSomething() throws Exception {
        // Initialize the database
        dependentsRepository.saveAndFlush(dependents);

        // Get all the dependentsList where relation contains DEFAULT_RELATION
        defaultDependentsShouldBeFound("relation.contains=" + DEFAULT_RELATION);

        // Get all the dependentsList where relation contains UPDATED_RELATION
        defaultDependentsShouldNotBeFound("relation.contains=" + UPDATED_RELATION);
    }

    @Test
    @Transactional
    void getAllDependentsByRelationNotContainsSomething() throws Exception {
        // Initialize the database
        dependentsRepository.saveAndFlush(dependents);

        // Get all the dependentsList where relation does not contain DEFAULT_RELATION
        defaultDependentsShouldNotBeFound("relation.doesNotContain=" + DEFAULT_RELATION);

        // Get all the dependentsList where relation does not contain UPDATED_RELATION
        defaultDependentsShouldBeFound("relation.doesNotContain=" + UPDATED_RELATION);
    }

    @Test
    @Transactional
    void getAllDependentsByEmployeeIsEqualToSomething() throws Exception {
        // Initialize the database
        dependentsRepository.saveAndFlush(dependents);
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
        dependents.setEmployee(employee);
        dependentsRepository.saveAndFlush(dependents);
        Long employeeId = employee.getId();

        // Get all the dependentsList where employee equals to employeeId
        defaultDependentsShouldBeFound("employeeId.equals=" + employeeId);

        // Get all the dependentsList where employee equals to (employeeId + 1)
        defaultDependentsShouldNotBeFound("employeeId.equals=" + (employeeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDependentsShouldBeFound(String filter) throws Exception {
        restDependentsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dependents.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].middleName").value(hasItem(DEFAULT_MIDDLE_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].relation").value(hasItem(DEFAULT_RELATION)));

        // Check, that the count call also returns 1
        restDependentsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDependentsShouldNotBeFound(String filter) throws Exception {
        restDependentsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDependentsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDependents() throws Exception {
        // Get the dependents
        restDependentsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDependents() throws Exception {
        // Initialize the database
        dependentsRepository.saveAndFlush(dependents);

        int databaseSizeBeforeUpdate = dependentsRepository.findAll().size();

        // Update the dependents
        Dependents updatedDependents = dependentsRepository.findById(dependents.getId()).get();
        // Disconnect from session so that the updates on updatedDependents are not directly saved in db
        em.detach(updatedDependents);
        updatedDependents
            .firstName(UPDATED_FIRST_NAME)
            .middleName(UPDATED_MIDDLE_NAME)
            .lastName(UPDATED_LAST_NAME)
            .relation(UPDATED_RELATION);
        DependentsDTO dependentsDTO = dependentsMapper.toDto(updatedDependents);

        restDependentsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dependentsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dependentsDTO))
            )
            .andExpect(status().isOk());

        // Validate the Dependents in the database
        List<Dependents> dependentsList = dependentsRepository.findAll();
        assertThat(dependentsList).hasSize(databaseSizeBeforeUpdate);
        Dependents testDependents = dependentsList.get(dependentsList.size() - 1);
        assertThat(testDependents.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testDependents.getMiddleName()).isEqualTo(UPDATED_MIDDLE_NAME);
        assertThat(testDependents.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testDependents.getRelation()).isEqualTo(UPDATED_RELATION);
    }

    @Test
    @Transactional
    void putNonExistingDependents() throws Exception {
        int databaseSizeBeforeUpdate = dependentsRepository.findAll().size();
        dependents.setId(count.incrementAndGet());

        // Create the Dependents
        DependentsDTO dependentsDTO = dependentsMapper.toDto(dependents);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDependentsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dependentsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dependentsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dependents in the database
        List<Dependents> dependentsList = dependentsRepository.findAll();
        assertThat(dependentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDependents() throws Exception {
        int databaseSizeBeforeUpdate = dependentsRepository.findAll().size();
        dependents.setId(count.incrementAndGet());

        // Create the Dependents
        DependentsDTO dependentsDTO = dependentsMapper.toDto(dependents);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDependentsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dependentsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dependents in the database
        List<Dependents> dependentsList = dependentsRepository.findAll();
        assertThat(dependentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDependents() throws Exception {
        int databaseSizeBeforeUpdate = dependentsRepository.findAll().size();
        dependents.setId(count.incrementAndGet());

        // Create the Dependents
        DependentsDTO dependentsDTO = dependentsMapper.toDto(dependents);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDependentsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dependentsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Dependents in the database
        List<Dependents> dependentsList = dependentsRepository.findAll();
        assertThat(dependentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDependentsWithPatch() throws Exception {
        // Initialize the database
        dependentsRepository.saveAndFlush(dependents);

        int databaseSizeBeforeUpdate = dependentsRepository.findAll().size();

        // Update the dependents using partial update
        Dependents partialUpdatedDependents = new Dependents();
        partialUpdatedDependents.setId(dependents.getId());

        partialUpdatedDependents.firstName(UPDATED_FIRST_NAME);

        restDependentsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDependents.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDependents))
            )
            .andExpect(status().isOk());

        // Validate the Dependents in the database
        List<Dependents> dependentsList = dependentsRepository.findAll();
        assertThat(dependentsList).hasSize(databaseSizeBeforeUpdate);
        Dependents testDependents = dependentsList.get(dependentsList.size() - 1);
        assertThat(testDependents.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testDependents.getMiddleName()).isEqualTo(DEFAULT_MIDDLE_NAME);
        assertThat(testDependents.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testDependents.getRelation()).isEqualTo(DEFAULT_RELATION);
    }

    @Test
    @Transactional
    void fullUpdateDependentsWithPatch() throws Exception {
        // Initialize the database
        dependentsRepository.saveAndFlush(dependents);

        int databaseSizeBeforeUpdate = dependentsRepository.findAll().size();

        // Update the dependents using partial update
        Dependents partialUpdatedDependents = new Dependents();
        partialUpdatedDependents.setId(dependents.getId());

        partialUpdatedDependents
            .firstName(UPDATED_FIRST_NAME)
            .middleName(UPDATED_MIDDLE_NAME)
            .lastName(UPDATED_LAST_NAME)
            .relation(UPDATED_RELATION);

        restDependentsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDependents.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDependents))
            )
            .andExpect(status().isOk());

        // Validate the Dependents in the database
        List<Dependents> dependentsList = dependentsRepository.findAll();
        assertThat(dependentsList).hasSize(databaseSizeBeforeUpdate);
        Dependents testDependents = dependentsList.get(dependentsList.size() - 1);
        assertThat(testDependents.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testDependents.getMiddleName()).isEqualTo(UPDATED_MIDDLE_NAME);
        assertThat(testDependents.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testDependents.getRelation()).isEqualTo(UPDATED_RELATION);
    }

    @Test
    @Transactional
    void patchNonExistingDependents() throws Exception {
        int databaseSizeBeforeUpdate = dependentsRepository.findAll().size();
        dependents.setId(count.incrementAndGet());

        // Create the Dependents
        DependentsDTO dependentsDTO = dependentsMapper.toDto(dependents);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDependentsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, dependentsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dependentsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dependents in the database
        List<Dependents> dependentsList = dependentsRepository.findAll();
        assertThat(dependentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDependents() throws Exception {
        int databaseSizeBeforeUpdate = dependentsRepository.findAll().size();
        dependents.setId(count.incrementAndGet());

        // Create the Dependents
        DependentsDTO dependentsDTO = dependentsMapper.toDto(dependents);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDependentsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dependentsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dependents in the database
        List<Dependents> dependentsList = dependentsRepository.findAll();
        assertThat(dependentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDependents() throws Exception {
        int databaseSizeBeforeUpdate = dependentsRepository.findAll().size();
        dependents.setId(count.incrementAndGet());

        // Create the Dependents
        DependentsDTO dependentsDTO = dependentsMapper.toDto(dependents);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDependentsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(dependentsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Dependents in the database
        List<Dependents> dependentsList = dependentsRepository.findAll();
        assertThat(dependentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDependents() throws Exception {
        // Initialize the database
        dependentsRepository.saveAndFlush(dependents);

        int databaseSizeBeforeDelete = dependentsRepository.findAll().size();

        // Delete the dependents
        restDependentsMockMvc
            .perform(delete(ENTITY_API_URL_ID, dependents.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Dependents> dependentsList = dependentsRepository.findAll();
        assertThat(dependentsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
