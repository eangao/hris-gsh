package com.hrisgsh.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.hrisgsh.app.IntegrationTest;
import com.hrisgsh.app.domain.Benefits;
import com.hrisgsh.app.domain.Employee;
import com.hrisgsh.app.repository.BenefitsRepository;
import com.hrisgsh.app.service.criteria.BenefitsCriteria;
import com.hrisgsh.app.service.dto.BenefitsDTO;
import com.hrisgsh.app.service.mapper.BenefitsMapper;
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
 * Integration tests for the {@link BenefitsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BenefitsResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/benefits";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BenefitsRepository benefitsRepository;

    @Autowired
    private BenefitsMapper benefitsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBenefitsMockMvc;

    private Benefits benefits;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Benefits createEntity(EntityManager em) {
        Benefits benefits = new Benefits().name(DEFAULT_NAME);
        return benefits;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Benefits createUpdatedEntity(EntityManager em) {
        Benefits benefits = new Benefits().name(UPDATED_NAME);
        return benefits;
    }

    @BeforeEach
    public void initTest() {
        benefits = createEntity(em);
    }

    @Test
    @Transactional
    void createBenefits() throws Exception {
        int databaseSizeBeforeCreate = benefitsRepository.findAll().size();
        // Create the Benefits
        BenefitsDTO benefitsDTO = benefitsMapper.toDto(benefits);
        restBenefitsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(benefitsDTO)))
            .andExpect(status().isCreated());

        // Validate the Benefits in the database
        List<Benefits> benefitsList = benefitsRepository.findAll();
        assertThat(benefitsList).hasSize(databaseSizeBeforeCreate + 1);
        Benefits testBenefits = benefitsList.get(benefitsList.size() - 1);
        assertThat(testBenefits.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createBenefitsWithExistingId() throws Exception {
        // Create the Benefits with an existing ID
        benefits.setId(1L);
        BenefitsDTO benefitsDTO = benefitsMapper.toDto(benefits);

        int databaseSizeBeforeCreate = benefitsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBenefitsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(benefitsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Benefits in the database
        List<Benefits> benefitsList = benefitsRepository.findAll();
        assertThat(benefitsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = benefitsRepository.findAll().size();
        // set the field null
        benefits.setName(null);

        // Create the Benefits, which fails.
        BenefitsDTO benefitsDTO = benefitsMapper.toDto(benefits);

        restBenefitsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(benefitsDTO)))
            .andExpect(status().isBadRequest());

        List<Benefits> benefitsList = benefitsRepository.findAll();
        assertThat(benefitsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBenefits() throws Exception {
        // Initialize the database
        benefitsRepository.saveAndFlush(benefits);

        // Get all the benefitsList
        restBenefitsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(benefits.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getBenefits() throws Exception {
        // Initialize the database
        benefitsRepository.saveAndFlush(benefits);

        // Get the benefits
        restBenefitsMockMvc
            .perform(get(ENTITY_API_URL_ID, benefits.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(benefits.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getBenefitsByIdFiltering() throws Exception {
        // Initialize the database
        benefitsRepository.saveAndFlush(benefits);

        Long id = benefits.getId();

        defaultBenefitsShouldBeFound("id.equals=" + id);
        defaultBenefitsShouldNotBeFound("id.notEquals=" + id);

        defaultBenefitsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultBenefitsShouldNotBeFound("id.greaterThan=" + id);

        defaultBenefitsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultBenefitsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllBenefitsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        benefitsRepository.saveAndFlush(benefits);

        // Get all the benefitsList where name equals to DEFAULT_NAME
        defaultBenefitsShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the benefitsList where name equals to UPDATED_NAME
        defaultBenefitsShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllBenefitsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        benefitsRepository.saveAndFlush(benefits);

        // Get all the benefitsList where name not equals to DEFAULT_NAME
        defaultBenefitsShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the benefitsList where name not equals to UPDATED_NAME
        defaultBenefitsShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllBenefitsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        benefitsRepository.saveAndFlush(benefits);

        // Get all the benefitsList where name in DEFAULT_NAME or UPDATED_NAME
        defaultBenefitsShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the benefitsList where name equals to UPDATED_NAME
        defaultBenefitsShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllBenefitsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        benefitsRepository.saveAndFlush(benefits);

        // Get all the benefitsList where name is not null
        defaultBenefitsShouldBeFound("name.specified=true");

        // Get all the benefitsList where name is null
        defaultBenefitsShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllBenefitsByNameContainsSomething() throws Exception {
        // Initialize the database
        benefitsRepository.saveAndFlush(benefits);

        // Get all the benefitsList where name contains DEFAULT_NAME
        defaultBenefitsShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the benefitsList where name contains UPDATED_NAME
        defaultBenefitsShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllBenefitsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        benefitsRepository.saveAndFlush(benefits);

        // Get all the benefitsList where name does not contain DEFAULT_NAME
        defaultBenefitsShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the benefitsList where name does not contain UPDATED_NAME
        defaultBenefitsShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllBenefitsByEmployeeIsEqualToSomething() throws Exception {
        // Initialize the database
        benefitsRepository.saveAndFlush(benefits);
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
        benefits.addEmployee(employee);
        benefitsRepository.saveAndFlush(benefits);
        Long employeeId = employee.getId();

        // Get all the benefitsList where employee equals to employeeId
        defaultBenefitsShouldBeFound("employeeId.equals=" + employeeId);

        // Get all the benefitsList where employee equals to (employeeId + 1)
        defaultBenefitsShouldNotBeFound("employeeId.equals=" + (employeeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBenefitsShouldBeFound(String filter) throws Exception {
        restBenefitsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(benefits.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restBenefitsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBenefitsShouldNotBeFound(String filter) throws Exception {
        restBenefitsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBenefitsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingBenefits() throws Exception {
        // Get the benefits
        restBenefitsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBenefits() throws Exception {
        // Initialize the database
        benefitsRepository.saveAndFlush(benefits);

        int databaseSizeBeforeUpdate = benefitsRepository.findAll().size();

        // Update the benefits
        Benefits updatedBenefits = benefitsRepository.findById(benefits.getId()).get();
        // Disconnect from session so that the updates on updatedBenefits are not directly saved in db
        em.detach(updatedBenefits);
        updatedBenefits.name(UPDATED_NAME);
        BenefitsDTO benefitsDTO = benefitsMapper.toDto(updatedBenefits);

        restBenefitsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, benefitsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(benefitsDTO))
            )
            .andExpect(status().isOk());

        // Validate the Benefits in the database
        List<Benefits> benefitsList = benefitsRepository.findAll();
        assertThat(benefitsList).hasSize(databaseSizeBeforeUpdate);
        Benefits testBenefits = benefitsList.get(benefitsList.size() - 1);
        assertThat(testBenefits.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingBenefits() throws Exception {
        int databaseSizeBeforeUpdate = benefitsRepository.findAll().size();
        benefits.setId(count.incrementAndGet());

        // Create the Benefits
        BenefitsDTO benefitsDTO = benefitsMapper.toDto(benefits);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBenefitsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, benefitsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(benefitsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Benefits in the database
        List<Benefits> benefitsList = benefitsRepository.findAll();
        assertThat(benefitsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBenefits() throws Exception {
        int databaseSizeBeforeUpdate = benefitsRepository.findAll().size();
        benefits.setId(count.incrementAndGet());

        // Create the Benefits
        BenefitsDTO benefitsDTO = benefitsMapper.toDto(benefits);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBenefitsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(benefitsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Benefits in the database
        List<Benefits> benefitsList = benefitsRepository.findAll();
        assertThat(benefitsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBenefits() throws Exception {
        int databaseSizeBeforeUpdate = benefitsRepository.findAll().size();
        benefits.setId(count.incrementAndGet());

        // Create the Benefits
        BenefitsDTO benefitsDTO = benefitsMapper.toDto(benefits);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBenefitsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(benefitsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Benefits in the database
        List<Benefits> benefitsList = benefitsRepository.findAll();
        assertThat(benefitsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBenefitsWithPatch() throws Exception {
        // Initialize the database
        benefitsRepository.saveAndFlush(benefits);

        int databaseSizeBeforeUpdate = benefitsRepository.findAll().size();

        // Update the benefits using partial update
        Benefits partialUpdatedBenefits = new Benefits();
        partialUpdatedBenefits.setId(benefits.getId());

        partialUpdatedBenefits.name(UPDATED_NAME);

        restBenefitsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBenefits.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBenefits))
            )
            .andExpect(status().isOk());

        // Validate the Benefits in the database
        List<Benefits> benefitsList = benefitsRepository.findAll();
        assertThat(benefitsList).hasSize(databaseSizeBeforeUpdate);
        Benefits testBenefits = benefitsList.get(benefitsList.size() - 1);
        assertThat(testBenefits.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateBenefitsWithPatch() throws Exception {
        // Initialize the database
        benefitsRepository.saveAndFlush(benefits);

        int databaseSizeBeforeUpdate = benefitsRepository.findAll().size();

        // Update the benefits using partial update
        Benefits partialUpdatedBenefits = new Benefits();
        partialUpdatedBenefits.setId(benefits.getId());

        partialUpdatedBenefits.name(UPDATED_NAME);

        restBenefitsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBenefits.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBenefits))
            )
            .andExpect(status().isOk());

        // Validate the Benefits in the database
        List<Benefits> benefitsList = benefitsRepository.findAll();
        assertThat(benefitsList).hasSize(databaseSizeBeforeUpdate);
        Benefits testBenefits = benefitsList.get(benefitsList.size() - 1);
        assertThat(testBenefits.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingBenefits() throws Exception {
        int databaseSizeBeforeUpdate = benefitsRepository.findAll().size();
        benefits.setId(count.incrementAndGet());

        // Create the Benefits
        BenefitsDTO benefitsDTO = benefitsMapper.toDto(benefits);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBenefitsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, benefitsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(benefitsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Benefits in the database
        List<Benefits> benefitsList = benefitsRepository.findAll();
        assertThat(benefitsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBenefits() throws Exception {
        int databaseSizeBeforeUpdate = benefitsRepository.findAll().size();
        benefits.setId(count.incrementAndGet());

        // Create the Benefits
        BenefitsDTO benefitsDTO = benefitsMapper.toDto(benefits);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBenefitsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(benefitsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Benefits in the database
        List<Benefits> benefitsList = benefitsRepository.findAll();
        assertThat(benefitsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBenefits() throws Exception {
        int databaseSizeBeforeUpdate = benefitsRepository.findAll().size();
        benefits.setId(count.incrementAndGet());

        // Create the Benefits
        BenefitsDTO benefitsDTO = benefitsMapper.toDto(benefits);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBenefitsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(benefitsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Benefits in the database
        List<Benefits> benefitsList = benefitsRepository.findAll();
        assertThat(benefitsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBenefits() throws Exception {
        // Initialize the database
        benefitsRepository.saveAndFlush(benefits);

        int databaseSizeBeforeDelete = benefitsRepository.findAll().size();

        // Delete the benefits
        restBenefitsMockMvc
            .perform(delete(ENTITY_API_URL_ID, benefits.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Benefits> benefitsList = benefitsRepository.findAll();
        assertThat(benefitsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
