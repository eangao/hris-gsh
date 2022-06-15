package com.hrisgsh.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.hrisgsh.app.IntegrationTest;
import com.hrisgsh.app.domain.Designation;
import com.hrisgsh.app.domain.Employee;
import com.hrisgsh.app.repository.DesignationRepository;
import com.hrisgsh.app.service.criteria.DesignationCriteria;
import com.hrisgsh.app.service.dto.DesignationDTO;
import com.hrisgsh.app.service.mapper.DesignationMapper;
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
 * Integration tests for the {@link DesignationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DesignationResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/designations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DesignationRepository designationRepository;

    @Autowired
    private DesignationMapper designationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDesignationMockMvc;

    private Designation designation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Designation createEntity(EntityManager em) {
        Designation designation = new Designation().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
        return designation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Designation createUpdatedEntity(EntityManager em) {
        Designation designation = new Designation().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        return designation;
    }

    @BeforeEach
    public void initTest() {
        designation = createEntity(em);
    }

    @Test
    @Transactional
    void createDesignation() throws Exception {
        int databaseSizeBeforeCreate = designationRepository.findAll().size();
        // Create the Designation
        DesignationDTO designationDTO = designationMapper.toDto(designation);
        restDesignationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(designationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Designation in the database
        List<Designation> designationList = designationRepository.findAll();
        assertThat(designationList).hasSize(databaseSizeBeforeCreate + 1);
        Designation testDesignation = designationList.get(designationList.size() - 1);
        assertThat(testDesignation.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDesignation.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createDesignationWithExistingId() throws Exception {
        // Create the Designation with an existing ID
        designation.setId(1L);
        DesignationDTO designationDTO = designationMapper.toDto(designation);

        int databaseSizeBeforeCreate = designationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDesignationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(designationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Designation in the database
        List<Designation> designationList = designationRepository.findAll();
        assertThat(designationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = designationRepository.findAll().size();
        // set the field null
        designation.setName(null);

        // Create the Designation, which fails.
        DesignationDTO designationDTO = designationMapper.toDto(designation);

        restDesignationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(designationDTO))
            )
            .andExpect(status().isBadRequest());

        List<Designation> designationList = designationRepository.findAll();
        assertThat(designationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDesignations() throws Exception {
        // Initialize the database
        designationRepository.saveAndFlush(designation);

        // Get all the designationList
        restDesignationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(designation.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getDesignation() throws Exception {
        // Initialize the database
        designationRepository.saveAndFlush(designation);

        // Get the designation
        restDesignationMockMvc
            .perform(get(ENTITY_API_URL_ID, designation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(designation.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getDesignationsByIdFiltering() throws Exception {
        // Initialize the database
        designationRepository.saveAndFlush(designation);

        Long id = designation.getId();

        defaultDesignationShouldBeFound("id.equals=" + id);
        defaultDesignationShouldNotBeFound("id.notEquals=" + id);

        defaultDesignationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDesignationShouldNotBeFound("id.greaterThan=" + id);

        defaultDesignationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDesignationShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDesignationsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        designationRepository.saveAndFlush(designation);

        // Get all the designationList where name equals to DEFAULT_NAME
        defaultDesignationShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the designationList where name equals to UPDATED_NAME
        defaultDesignationShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDesignationsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        designationRepository.saveAndFlush(designation);

        // Get all the designationList where name not equals to DEFAULT_NAME
        defaultDesignationShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the designationList where name not equals to UPDATED_NAME
        defaultDesignationShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDesignationsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        designationRepository.saveAndFlush(designation);

        // Get all the designationList where name in DEFAULT_NAME or UPDATED_NAME
        defaultDesignationShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the designationList where name equals to UPDATED_NAME
        defaultDesignationShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDesignationsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        designationRepository.saveAndFlush(designation);

        // Get all the designationList where name is not null
        defaultDesignationShouldBeFound("name.specified=true");

        // Get all the designationList where name is null
        defaultDesignationShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllDesignationsByNameContainsSomething() throws Exception {
        // Initialize the database
        designationRepository.saveAndFlush(designation);

        // Get all the designationList where name contains DEFAULT_NAME
        defaultDesignationShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the designationList where name contains UPDATED_NAME
        defaultDesignationShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDesignationsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        designationRepository.saveAndFlush(designation);

        // Get all the designationList where name does not contain DEFAULT_NAME
        defaultDesignationShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the designationList where name does not contain UPDATED_NAME
        defaultDesignationShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDesignationsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        designationRepository.saveAndFlush(designation);

        // Get all the designationList where description equals to DEFAULT_DESCRIPTION
        defaultDesignationShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the designationList where description equals to UPDATED_DESCRIPTION
        defaultDesignationShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllDesignationsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        designationRepository.saveAndFlush(designation);

        // Get all the designationList where description not equals to DEFAULT_DESCRIPTION
        defaultDesignationShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the designationList where description not equals to UPDATED_DESCRIPTION
        defaultDesignationShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllDesignationsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        designationRepository.saveAndFlush(designation);

        // Get all the designationList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultDesignationShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the designationList where description equals to UPDATED_DESCRIPTION
        defaultDesignationShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllDesignationsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        designationRepository.saveAndFlush(designation);

        // Get all the designationList where description is not null
        defaultDesignationShouldBeFound("description.specified=true");

        // Get all the designationList where description is null
        defaultDesignationShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllDesignationsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        designationRepository.saveAndFlush(designation);

        // Get all the designationList where description contains DEFAULT_DESCRIPTION
        defaultDesignationShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the designationList where description contains UPDATED_DESCRIPTION
        defaultDesignationShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllDesignationsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        designationRepository.saveAndFlush(designation);

        // Get all the designationList where description does not contain DEFAULT_DESCRIPTION
        defaultDesignationShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the designationList where description does not contain UPDATED_DESCRIPTION
        defaultDesignationShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllDesignationsByEmployeeIsEqualToSomething() throws Exception {
        // Initialize the database
        designationRepository.saveAndFlush(designation);
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
        designation.addEmployee(employee);
        designationRepository.saveAndFlush(designation);
        Long employeeId = employee.getId();

        // Get all the designationList where employee equals to employeeId
        defaultDesignationShouldBeFound("employeeId.equals=" + employeeId);

        // Get all the designationList where employee equals to (employeeId + 1)
        defaultDesignationShouldNotBeFound("employeeId.equals=" + (employeeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDesignationShouldBeFound(String filter) throws Exception {
        restDesignationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(designation.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restDesignationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDesignationShouldNotBeFound(String filter) throws Exception {
        restDesignationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDesignationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDesignation() throws Exception {
        // Get the designation
        restDesignationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDesignation() throws Exception {
        // Initialize the database
        designationRepository.saveAndFlush(designation);

        int databaseSizeBeforeUpdate = designationRepository.findAll().size();

        // Update the designation
        Designation updatedDesignation = designationRepository.findById(designation.getId()).get();
        // Disconnect from session so that the updates on updatedDesignation are not directly saved in db
        em.detach(updatedDesignation);
        updatedDesignation.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        DesignationDTO designationDTO = designationMapper.toDto(updatedDesignation);

        restDesignationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, designationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(designationDTO))
            )
            .andExpect(status().isOk());

        // Validate the Designation in the database
        List<Designation> designationList = designationRepository.findAll();
        assertThat(designationList).hasSize(databaseSizeBeforeUpdate);
        Designation testDesignation = designationList.get(designationList.size() - 1);
        assertThat(testDesignation.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDesignation.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingDesignation() throws Exception {
        int databaseSizeBeforeUpdate = designationRepository.findAll().size();
        designation.setId(count.incrementAndGet());

        // Create the Designation
        DesignationDTO designationDTO = designationMapper.toDto(designation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDesignationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, designationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(designationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Designation in the database
        List<Designation> designationList = designationRepository.findAll();
        assertThat(designationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDesignation() throws Exception {
        int databaseSizeBeforeUpdate = designationRepository.findAll().size();
        designation.setId(count.incrementAndGet());

        // Create the Designation
        DesignationDTO designationDTO = designationMapper.toDto(designation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDesignationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(designationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Designation in the database
        List<Designation> designationList = designationRepository.findAll();
        assertThat(designationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDesignation() throws Exception {
        int databaseSizeBeforeUpdate = designationRepository.findAll().size();
        designation.setId(count.incrementAndGet());

        // Create the Designation
        DesignationDTO designationDTO = designationMapper.toDto(designation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDesignationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(designationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Designation in the database
        List<Designation> designationList = designationRepository.findAll();
        assertThat(designationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDesignationWithPatch() throws Exception {
        // Initialize the database
        designationRepository.saveAndFlush(designation);

        int databaseSizeBeforeUpdate = designationRepository.findAll().size();

        // Update the designation using partial update
        Designation partialUpdatedDesignation = new Designation();
        partialUpdatedDesignation.setId(designation.getId());

        partialUpdatedDesignation.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restDesignationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDesignation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDesignation))
            )
            .andExpect(status().isOk());

        // Validate the Designation in the database
        List<Designation> designationList = designationRepository.findAll();
        assertThat(designationList).hasSize(databaseSizeBeforeUpdate);
        Designation testDesignation = designationList.get(designationList.size() - 1);
        assertThat(testDesignation.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDesignation.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateDesignationWithPatch() throws Exception {
        // Initialize the database
        designationRepository.saveAndFlush(designation);

        int databaseSizeBeforeUpdate = designationRepository.findAll().size();

        // Update the designation using partial update
        Designation partialUpdatedDesignation = new Designation();
        partialUpdatedDesignation.setId(designation.getId());

        partialUpdatedDesignation.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restDesignationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDesignation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDesignation))
            )
            .andExpect(status().isOk());

        // Validate the Designation in the database
        List<Designation> designationList = designationRepository.findAll();
        assertThat(designationList).hasSize(databaseSizeBeforeUpdate);
        Designation testDesignation = designationList.get(designationList.size() - 1);
        assertThat(testDesignation.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDesignation.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingDesignation() throws Exception {
        int databaseSizeBeforeUpdate = designationRepository.findAll().size();
        designation.setId(count.incrementAndGet());

        // Create the Designation
        DesignationDTO designationDTO = designationMapper.toDto(designation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDesignationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, designationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(designationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Designation in the database
        List<Designation> designationList = designationRepository.findAll();
        assertThat(designationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDesignation() throws Exception {
        int databaseSizeBeforeUpdate = designationRepository.findAll().size();
        designation.setId(count.incrementAndGet());

        // Create the Designation
        DesignationDTO designationDTO = designationMapper.toDto(designation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDesignationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(designationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Designation in the database
        List<Designation> designationList = designationRepository.findAll();
        assertThat(designationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDesignation() throws Exception {
        int databaseSizeBeforeUpdate = designationRepository.findAll().size();
        designation.setId(count.incrementAndGet());

        // Create the Designation
        DesignationDTO designationDTO = designationMapper.toDto(designation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDesignationMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(designationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Designation in the database
        List<Designation> designationList = designationRepository.findAll();
        assertThat(designationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDesignation() throws Exception {
        // Initialize the database
        designationRepository.saveAndFlush(designation);

        int databaseSizeBeforeDelete = designationRepository.findAll().size();

        // Delete the designation
        restDesignationMockMvc
            .perform(delete(ENTITY_API_URL_ID, designation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Designation> designationList = designationRepository.findAll();
        assertThat(designationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
