package com.hrisgsh.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.hrisgsh.app.IntegrationTest;
import com.hrisgsh.app.domain.Education;
import com.hrisgsh.app.domain.Employee;
import com.hrisgsh.app.repository.EducationRepository;
import com.hrisgsh.app.service.criteria.EducationCriteria;
import com.hrisgsh.app.service.dto.EducationDTO;
import com.hrisgsh.app.service.mapper.EducationMapper;
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
 * Integration tests for the {@link EducationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EducationResourceIT {

    private static final String DEFAULT_BACHELOR_DEGREE = "AAAAAAAAAA";
    private static final String UPDATED_BACHELOR_DEGREE = "BBBBBBBBBB";

    private static final Integer DEFAULT_YEAR_GRADUATED = 1;
    private static final Integer UPDATED_YEAR_GRADUATED = 2;
    private static final Integer SMALLER_YEAR_GRADUATED = 1 - 1;

    private static final String DEFAULT_SCHOOL = "AAAAAAAAAA";
    private static final String UPDATED_SCHOOL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/educations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EducationRepository educationRepository;

    @Autowired
    private EducationMapper educationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEducationMockMvc;

    private Education education;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Education createEntity(EntityManager em) {
        Education education = new Education()
            .bachelorDegree(DEFAULT_BACHELOR_DEGREE)
            .yearGraduated(DEFAULT_YEAR_GRADUATED)
            .school(DEFAULT_SCHOOL);
        // Add required entity
        Employee employee;
        if (TestUtil.findAll(em, Employee.class).isEmpty()) {
            employee = EmployeeResourceIT.createEntity(em);
            em.persist(employee);
            em.flush();
        } else {
            employee = TestUtil.findAll(em, Employee.class).get(0);
        }
        education.setEmployee(employee);
        return education;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Education createUpdatedEntity(EntityManager em) {
        Education education = new Education()
            .bachelorDegree(UPDATED_BACHELOR_DEGREE)
            .yearGraduated(UPDATED_YEAR_GRADUATED)
            .school(UPDATED_SCHOOL);
        // Add required entity
        Employee employee;
        if (TestUtil.findAll(em, Employee.class).isEmpty()) {
            employee = EmployeeResourceIT.createUpdatedEntity(em);
            em.persist(employee);
            em.flush();
        } else {
            employee = TestUtil.findAll(em, Employee.class).get(0);
        }
        education.setEmployee(employee);
        return education;
    }

    @BeforeEach
    public void initTest() {
        education = createEntity(em);
    }

    @Test
    @Transactional
    void createEducation() throws Exception {
        int databaseSizeBeforeCreate = educationRepository.findAll().size();
        // Create the Education
        EducationDTO educationDTO = educationMapper.toDto(education);
        restEducationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(educationDTO)))
            .andExpect(status().isCreated());

        // Validate the Education in the database
        List<Education> educationList = educationRepository.findAll();
        assertThat(educationList).hasSize(databaseSizeBeforeCreate + 1);
        Education testEducation = educationList.get(educationList.size() - 1);
        assertThat(testEducation.getBachelorDegree()).isEqualTo(DEFAULT_BACHELOR_DEGREE);
        assertThat(testEducation.getYearGraduated()).isEqualTo(DEFAULT_YEAR_GRADUATED);
        assertThat(testEducation.getSchool()).isEqualTo(DEFAULT_SCHOOL);
    }

    @Test
    @Transactional
    void createEducationWithExistingId() throws Exception {
        // Create the Education with an existing ID
        education.setId(1L);
        EducationDTO educationDTO = educationMapper.toDto(education);

        int databaseSizeBeforeCreate = educationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEducationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(educationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Education in the database
        List<Education> educationList = educationRepository.findAll();
        assertThat(educationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkBachelorDegreeIsRequired() throws Exception {
        int databaseSizeBeforeTest = educationRepository.findAll().size();
        // set the field null
        education.setBachelorDegree(null);

        // Create the Education, which fails.
        EducationDTO educationDTO = educationMapper.toDto(education);

        restEducationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(educationDTO)))
            .andExpect(status().isBadRequest());

        List<Education> educationList = educationRepository.findAll();
        assertThat(educationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkYearGraduatedIsRequired() throws Exception {
        int databaseSizeBeforeTest = educationRepository.findAll().size();
        // set the field null
        education.setYearGraduated(null);

        // Create the Education, which fails.
        EducationDTO educationDTO = educationMapper.toDto(education);

        restEducationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(educationDTO)))
            .andExpect(status().isBadRequest());

        List<Education> educationList = educationRepository.findAll();
        assertThat(educationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSchoolIsRequired() throws Exception {
        int databaseSizeBeforeTest = educationRepository.findAll().size();
        // set the field null
        education.setSchool(null);

        // Create the Education, which fails.
        EducationDTO educationDTO = educationMapper.toDto(education);

        restEducationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(educationDTO)))
            .andExpect(status().isBadRequest());

        List<Education> educationList = educationRepository.findAll();
        assertThat(educationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEducations() throws Exception {
        // Initialize the database
        educationRepository.saveAndFlush(education);

        // Get all the educationList
        restEducationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(education.getId().intValue())))
            .andExpect(jsonPath("$.[*].bachelorDegree").value(hasItem(DEFAULT_BACHELOR_DEGREE)))
            .andExpect(jsonPath("$.[*].yearGraduated").value(hasItem(DEFAULT_YEAR_GRADUATED)))
            .andExpect(jsonPath("$.[*].school").value(hasItem(DEFAULT_SCHOOL)));
    }

    @Test
    @Transactional
    void getEducation() throws Exception {
        // Initialize the database
        educationRepository.saveAndFlush(education);

        // Get the education
        restEducationMockMvc
            .perform(get(ENTITY_API_URL_ID, education.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(education.getId().intValue()))
            .andExpect(jsonPath("$.bachelorDegree").value(DEFAULT_BACHELOR_DEGREE))
            .andExpect(jsonPath("$.yearGraduated").value(DEFAULT_YEAR_GRADUATED))
            .andExpect(jsonPath("$.school").value(DEFAULT_SCHOOL));
    }

    @Test
    @Transactional
    void getEducationsByIdFiltering() throws Exception {
        // Initialize the database
        educationRepository.saveAndFlush(education);

        Long id = education.getId();

        defaultEducationShouldBeFound("id.equals=" + id);
        defaultEducationShouldNotBeFound("id.notEquals=" + id);

        defaultEducationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEducationShouldNotBeFound("id.greaterThan=" + id);

        defaultEducationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEducationShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEducationsByBachelorDegreeIsEqualToSomething() throws Exception {
        // Initialize the database
        educationRepository.saveAndFlush(education);

        // Get all the educationList where bachelorDegree equals to DEFAULT_BACHELOR_DEGREE
        defaultEducationShouldBeFound("bachelorDegree.equals=" + DEFAULT_BACHELOR_DEGREE);

        // Get all the educationList where bachelorDegree equals to UPDATED_BACHELOR_DEGREE
        defaultEducationShouldNotBeFound("bachelorDegree.equals=" + UPDATED_BACHELOR_DEGREE);
    }

    @Test
    @Transactional
    void getAllEducationsByBachelorDegreeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        educationRepository.saveAndFlush(education);

        // Get all the educationList where bachelorDegree not equals to DEFAULT_BACHELOR_DEGREE
        defaultEducationShouldNotBeFound("bachelorDegree.notEquals=" + DEFAULT_BACHELOR_DEGREE);

        // Get all the educationList where bachelorDegree not equals to UPDATED_BACHELOR_DEGREE
        defaultEducationShouldBeFound("bachelorDegree.notEquals=" + UPDATED_BACHELOR_DEGREE);
    }

    @Test
    @Transactional
    void getAllEducationsByBachelorDegreeIsInShouldWork() throws Exception {
        // Initialize the database
        educationRepository.saveAndFlush(education);

        // Get all the educationList where bachelorDegree in DEFAULT_BACHELOR_DEGREE or UPDATED_BACHELOR_DEGREE
        defaultEducationShouldBeFound("bachelorDegree.in=" + DEFAULT_BACHELOR_DEGREE + "," + UPDATED_BACHELOR_DEGREE);

        // Get all the educationList where bachelorDegree equals to UPDATED_BACHELOR_DEGREE
        defaultEducationShouldNotBeFound("bachelorDegree.in=" + UPDATED_BACHELOR_DEGREE);
    }

    @Test
    @Transactional
    void getAllEducationsByBachelorDegreeIsNullOrNotNull() throws Exception {
        // Initialize the database
        educationRepository.saveAndFlush(education);

        // Get all the educationList where bachelorDegree is not null
        defaultEducationShouldBeFound("bachelorDegree.specified=true");

        // Get all the educationList where bachelorDegree is null
        defaultEducationShouldNotBeFound("bachelorDegree.specified=false");
    }

    @Test
    @Transactional
    void getAllEducationsByBachelorDegreeContainsSomething() throws Exception {
        // Initialize the database
        educationRepository.saveAndFlush(education);

        // Get all the educationList where bachelorDegree contains DEFAULT_BACHELOR_DEGREE
        defaultEducationShouldBeFound("bachelorDegree.contains=" + DEFAULT_BACHELOR_DEGREE);

        // Get all the educationList where bachelorDegree contains UPDATED_BACHELOR_DEGREE
        defaultEducationShouldNotBeFound("bachelorDegree.contains=" + UPDATED_BACHELOR_DEGREE);
    }

    @Test
    @Transactional
    void getAllEducationsByBachelorDegreeNotContainsSomething() throws Exception {
        // Initialize the database
        educationRepository.saveAndFlush(education);

        // Get all the educationList where bachelorDegree does not contain DEFAULT_BACHELOR_DEGREE
        defaultEducationShouldNotBeFound("bachelorDegree.doesNotContain=" + DEFAULT_BACHELOR_DEGREE);

        // Get all the educationList where bachelorDegree does not contain UPDATED_BACHELOR_DEGREE
        defaultEducationShouldBeFound("bachelorDegree.doesNotContain=" + UPDATED_BACHELOR_DEGREE);
    }

    @Test
    @Transactional
    void getAllEducationsByYearGraduatedIsEqualToSomething() throws Exception {
        // Initialize the database
        educationRepository.saveAndFlush(education);

        // Get all the educationList where yearGraduated equals to DEFAULT_YEAR_GRADUATED
        defaultEducationShouldBeFound("yearGraduated.equals=" + DEFAULT_YEAR_GRADUATED);

        // Get all the educationList where yearGraduated equals to UPDATED_YEAR_GRADUATED
        defaultEducationShouldNotBeFound("yearGraduated.equals=" + UPDATED_YEAR_GRADUATED);
    }

    @Test
    @Transactional
    void getAllEducationsByYearGraduatedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        educationRepository.saveAndFlush(education);

        // Get all the educationList where yearGraduated not equals to DEFAULT_YEAR_GRADUATED
        defaultEducationShouldNotBeFound("yearGraduated.notEquals=" + DEFAULT_YEAR_GRADUATED);

        // Get all the educationList where yearGraduated not equals to UPDATED_YEAR_GRADUATED
        defaultEducationShouldBeFound("yearGraduated.notEquals=" + UPDATED_YEAR_GRADUATED);
    }

    @Test
    @Transactional
    void getAllEducationsByYearGraduatedIsInShouldWork() throws Exception {
        // Initialize the database
        educationRepository.saveAndFlush(education);

        // Get all the educationList where yearGraduated in DEFAULT_YEAR_GRADUATED or UPDATED_YEAR_GRADUATED
        defaultEducationShouldBeFound("yearGraduated.in=" + DEFAULT_YEAR_GRADUATED + "," + UPDATED_YEAR_GRADUATED);

        // Get all the educationList where yearGraduated equals to UPDATED_YEAR_GRADUATED
        defaultEducationShouldNotBeFound("yearGraduated.in=" + UPDATED_YEAR_GRADUATED);
    }

    @Test
    @Transactional
    void getAllEducationsByYearGraduatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        educationRepository.saveAndFlush(education);

        // Get all the educationList where yearGraduated is not null
        defaultEducationShouldBeFound("yearGraduated.specified=true");

        // Get all the educationList where yearGraduated is null
        defaultEducationShouldNotBeFound("yearGraduated.specified=false");
    }

    @Test
    @Transactional
    void getAllEducationsByYearGraduatedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        educationRepository.saveAndFlush(education);

        // Get all the educationList where yearGraduated is greater than or equal to DEFAULT_YEAR_GRADUATED
        defaultEducationShouldBeFound("yearGraduated.greaterThanOrEqual=" + DEFAULT_YEAR_GRADUATED);

        // Get all the educationList where yearGraduated is greater than or equal to UPDATED_YEAR_GRADUATED
        defaultEducationShouldNotBeFound("yearGraduated.greaterThanOrEqual=" + UPDATED_YEAR_GRADUATED);
    }

    @Test
    @Transactional
    void getAllEducationsByYearGraduatedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        educationRepository.saveAndFlush(education);

        // Get all the educationList where yearGraduated is less than or equal to DEFAULT_YEAR_GRADUATED
        defaultEducationShouldBeFound("yearGraduated.lessThanOrEqual=" + DEFAULT_YEAR_GRADUATED);

        // Get all the educationList where yearGraduated is less than or equal to SMALLER_YEAR_GRADUATED
        defaultEducationShouldNotBeFound("yearGraduated.lessThanOrEqual=" + SMALLER_YEAR_GRADUATED);
    }

    @Test
    @Transactional
    void getAllEducationsByYearGraduatedIsLessThanSomething() throws Exception {
        // Initialize the database
        educationRepository.saveAndFlush(education);

        // Get all the educationList where yearGraduated is less than DEFAULT_YEAR_GRADUATED
        defaultEducationShouldNotBeFound("yearGraduated.lessThan=" + DEFAULT_YEAR_GRADUATED);

        // Get all the educationList where yearGraduated is less than UPDATED_YEAR_GRADUATED
        defaultEducationShouldBeFound("yearGraduated.lessThan=" + UPDATED_YEAR_GRADUATED);
    }

    @Test
    @Transactional
    void getAllEducationsByYearGraduatedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        educationRepository.saveAndFlush(education);

        // Get all the educationList where yearGraduated is greater than DEFAULT_YEAR_GRADUATED
        defaultEducationShouldNotBeFound("yearGraduated.greaterThan=" + DEFAULT_YEAR_GRADUATED);

        // Get all the educationList where yearGraduated is greater than SMALLER_YEAR_GRADUATED
        defaultEducationShouldBeFound("yearGraduated.greaterThan=" + SMALLER_YEAR_GRADUATED);
    }

    @Test
    @Transactional
    void getAllEducationsBySchoolIsEqualToSomething() throws Exception {
        // Initialize the database
        educationRepository.saveAndFlush(education);

        // Get all the educationList where school equals to DEFAULT_SCHOOL
        defaultEducationShouldBeFound("school.equals=" + DEFAULT_SCHOOL);

        // Get all the educationList where school equals to UPDATED_SCHOOL
        defaultEducationShouldNotBeFound("school.equals=" + UPDATED_SCHOOL);
    }

    @Test
    @Transactional
    void getAllEducationsBySchoolIsNotEqualToSomething() throws Exception {
        // Initialize the database
        educationRepository.saveAndFlush(education);

        // Get all the educationList where school not equals to DEFAULT_SCHOOL
        defaultEducationShouldNotBeFound("school.notEquals=" + DEFAULT_SCHOOL);

        // Get all the educationList where school not equals to UPDATED_SCHOOL
        defaultEducationShouldBeFound("school.notEquals=" + UPDATED_SCHOOL);
    }

    @Test
    @Transactional
    void getAllEducationsBySchoolIsInShouldWork() throws Exception {
        // Initialize the database
        educationRepository.saveAndFlush(education);

        // Get all the educationList where school in DEFAULT_SCHOOL or UPDATED_SCHOOL
        defaultEducationShouldBeFound("school.in=" + DEFAULT_SCHOOL + "," + UPDATED_SCHOOL);

        // Get all the educationList where school equals to UPDATED_SCHOOL
        defaultEducationShouldNotBeFound("school.in=" + UPDATED_SCHOOL);
    }

    @Test
    @Transactional
    void getAllEducationsBySchoolIsNullOrNotNull() throws Exception {
        // Initialize the database
        educationRepository.saveAndFlush(education);

        // Get all the educationList where school is not null
        defaultEducationShouldBeFound("school.specified=true");

        // Get all the educationList where school is null
        defaultEducationShouldNotBeFound("school.specified=false");
    }

    @Test
    @Transactional
    void getAllEducationsBySchoolContainsSomething() throws Exception {
        // Initialize the database
        educationRepository.saveAndFlush(education);

        // Get all the educationList where school contains DEFAULT_SCHOOL
        defaultEducationShouldBeFound("school.contains=" + DEFAULT_SCHOOL);

        // Get all the educationList where school contains UPDATED_SCHOOL
        defaultEducationShouldNotBeFound("school.contains=" + UPDATED_SCHOOL);
    }

    @Test
    @Transactional
    void getAllEducationsBySchoolNotContainsSomething() throws Exception {
        // Initialize the database
        educationRepository.saveAndFlush(education);

        // Get all the educationList where school does not contain DEFAULT_SCHOOL
        defaultEducationShouldNotBeFound("school.doesNotContain=" + DEFAULT_SCHOOL);

        // Get all the educationList where school does not contain UPDATED_SCHOOL
        defaultEducationShouldBeFound("school.doesNotContain=" + UPDATED_SCHOOL);
    }

    @Test
    @Transactional
    void getAllEducationsByEmployeeIsEqualToSomething() throws Exception {
        // Initialize the database
        educationRepository.saveAndFlush(education);
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
        education.setEmployee(employee);
        educationRepository.saveAndFlush(education);
        Long employeeId = employee.getId();

        // Get all the educationList where employee equals to employeeId
        defaultEducationShouldBeFound("employeeId.equals=" + employeeId);

        // Get all the educationList where employee equals to (employeeId + 1)
        defaultEducationShouldNotBeFound("employeeId.equals=" + (employeeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEducationShouldBeFound(String filter) throws Exception {
        restEducationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(education.getId().intValue())))
            .andExpect(jsonPath("$.[*].bachelorDegree").value(hasItem(DEFAULT_BACHELOR_DEGREE)))
            .andExpect(jsonPath("$.[*].yearGraduated").value(hasItem(DEFAULT_YEAR_GRADUATED)))
            .andExpect(jsonPath("$.[*].school").value(hasItem(DEFAULT_SCHOOL)));

        // Check, that the count call also returns 1
        restEducationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEducationShouldNotBeFound(String filter) throws Exception {
        restEducationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEducationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEducation() throws Exception {
        // Get the education
        restEducationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEducation() throws Exception {
        // Initialize the database
        educationRepository.saveAndFlush(education);

        int databaseSizeBeforeUpdate = educationRepository.findAll().size();

        // Update the education
        Education updatedEducation = educationRepository.findById(education.getId()).get();
        // Disconnect from session so that the updates on updatedEducation are not directly saved in db
        em.detach(updatedEducation);
        updatedEducation.bachelorDegree(UPDATED_BACHELOR_DEGREE).yearGraduated(UPDATED_YEAR_GRADUATED).school(UPDATED_SCHOOL);
        EducationDTO educationDTO = educationMapper.toDto(updatedEducation);

        restEducationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, educationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(educationDTO))
            )
            .andExpect(status().isOk());

        // Validate the Education in the database
        List<Education> educationList = educationRepository.findAll();
        assertThat(educationList).hasSize(databaseSizeBeforeUpdate);
        Education testEducation = educationList.get(educationList.size() - 1);
        assertThat(testEducation.getBachelorDegree()).isEqualTo(UPDATED_BACHELOR_DEGREE);
        assertThat(testEducation.getYearGraduated()).isEqualTo(UPDATED_YEAR_GRADUATED);
        assertThat(testEducation.getSchool()).isEqualTo(UPDATED_SCHOOL);
    }

    @Test
    @Transactional
    void putNonExistingEducation() throws Exception {
        int databaseSizeBeforeUpdate = educationRepository.findAll().size();
        education.setId(count.incrementAndGet());

        // Create the Education
        EducationDTO educationDTO = educationMapper.toDto(education);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEducationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, educationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(educationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Education in the database
        List<Education> educationList = educationRepository.findAll();
        assertThat(educationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEducation() throws Exception {
        int databaseSizeBeforeUpdate = educationRepository.findAll().size();
        education.setId(count.incrementAndGet());

        // Create the Education
        EducationDTO educationDTO = educationMapper.toDto(education);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEducationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(educationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Education in the database
        List<Education> educationList = educationRepository.findAll();
        assertThat(educationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEducation() throws Exception {
        int databaseSizeBeforeUpdate = educationRepository.findAll().size();
        education.setId(count.incrementAndGet());

        // Create the Education
        EducationDTO educationDTO = educationMapper.toDto(education);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEducationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(educationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Education in the database
        List<Education> educationList = educationRepository.findAll();
        assertThat(educationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEducationWithPatch() throws Exception {
        // Initialize the database
        educationRepository.saveAndFlush(education);

        int databaseSizeBeforeUpdate = educationRepository.findAll().size();

        // Update the education using partial update
        Education partialUpdatedEducation = new Education();
        partialUpdatedEducation.setId(education.getId());

        restEducationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEducation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEducation))
            )
            .andExpect(status().isOk());

        // Validate the Education in the database
        List<Education> educationList = educationRepository.findAll();
        assertThat(educationList).hasSize(databaseSizeBeforeUpdate);
        Education testEducation = educationList.get(educationList.size() - 1);
        assertThat(testEducation.getBachelorDegree()).isEqualTo(DEFAULT_BACHELOR_DEGREE);
        assertThat(testEducation.getYearGraduated()).isEqualTo(DEFAULT_YEAR_GRADUATED);
        assertThat(testEducation.getSchool()).isEqualTo(DEFAULT_SCHOOL);
    }

    @Test
    @Transactional
    void fullUpdateEducationWithPatch() throws Exception {
        // Initialize the database
        educationRepository.saveAndFlush(education);

        int databaseSizeBeforeUpdate = educationRepository.findAll().size();

        // Update the education using partial update
        Education partialUpdatedEducation = new Education();
        partialUpdatedEducation.setId(education.getId());

        partialUpdatedEducation.bachelorDegree(UPDATED_BACHELOR_DEGREE).yearGraduated(UPDATED_YEAR_GRADUATED).school(UPDATED_SCHOOL);

        restEducationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEducation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEducation))
            )
            .andExpect(status().isOk());

        // Validate the Education in the database
        List<Education> educationList = educationRepository.findAll();
        assertThat(educationList).hasSize(databaseSizeBeforeUpdate);
        Education testEducation = educationList.get(educationList.size() - 1);
        assertThat(testEducation.getBachelorDegree()).isEqualTo(UPDATED_BACHELOR_DEGREE);
        assertThat(testEducation.getYearGraduated()).isEqualTo(UPDATED_YEAR_GRADUATED);
        assertThat(testEducation.getSchool()).isEqualTo(UPDATED_SCHOOL);
    }

    @Test
    @Transactional
    void patchNonExistingEducation() throws Exception {
        int databaseSizeBeforeUpdate = educationRepository.findAll().size();
        education.setId(count.incrementAndGet());

        // Create the Education
        EducationDTO educationDTO = educationMapper.toDto(education);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEducationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, educationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(educationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Education in the database
        List<Education> educationList = educationRepository.findAll();
        assertThat(educationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEducation() throws Exception {
        int databaseSizeBeforeUpdate = educationRepository.findAll().size();
        education.setId(count.incrementAndGet());

        // Create the Education
        EducationDTO educationDTO = educationMapper.toDto(education);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEducationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(educationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Education in the database
        List<Education> educationList = educationRepository.findAll();
        assertThat(educationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEducation() throws Exception {
        int databaseSizeBeforeUpdate = educationRepository.findAll().size();
        education.setId(count.incrementAndGet());

        // Create the Education
        EducationDTO educationDTO = educationMapper.toDto(education);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEducationMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(educationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Education in the database
        List<Education> educationList = educationRepository.findAll();
        assertThat(educationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEducation() throws Exception {
        // Initialize the database
        educationRepository.saveAndFlush(education);

        int databaseSizeBeforeDelete = educationRepository.findAll().size();

        // Delete the education
        restEducationMockMvc
            .perform(delete(ENTITY_API_URL_ID, education.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Education> educationList = educationRepository.findAll();
        assertThat(educationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
