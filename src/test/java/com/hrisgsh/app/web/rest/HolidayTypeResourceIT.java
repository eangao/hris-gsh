package com.hrisgsh.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.hrisgsh.app.IntegrationTest;
import com.hrisgsh.app.domain.HolidayType;
import com.hrisgsh.app.repository.HolidayTypeRepository;
import com.hrisgsh.app.service.criteria.HolidayTypeCriteria;
import com.hrisgsh.app.service.dto.HolidayTypeDTO;
import com.hrisgsh.app.service.mapper.HolidayTypeMapper;
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
 * Integration tests for the {@link HolidayTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HolidayTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/holiday-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private HolidayTypeRepository holidayTypeRepository;

    @Autowired
    private HolidayTypeMapper holidayTypeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHolidayTypeMockMvc;

    private HolidayType holidayType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HolidayType createEntity(EntityManager em) {
        HolidayType holidayType = new HolidayType().name(DEFAULT_NAME);
        return holidayType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HolidayType createUpdatedEntity(EntityManager em) {
        HolidayType holidayType = new HolidayType().name(UPDATED_NAME);
        return holidayType;
    }

    @BeforeEach
    public void initTest() {
        holidayType = createEntity(em);
    }

    @Test
    @Transactional
    void createHolidayType() throws Exception {
        int databaseSizeBeforeCreate = holidayTypeRepository.findAll().size();
        // Create the HolidayType
        HolidayTypeDTO holidayTypeDTO = holidayTypeMapper.toDto(holidayType);
        restHolidayTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(holidayTypeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the HolidayType in the database
        List<HolidayType> holidayTypeList = holidayTypeRepository.findAll();
        assertThat(holidayTypeList).hasSize(databaseSizeBeforeCreate + 1);
        HolidayType testHolidayType = holidayTypeList.get(holidayTypeList.size() - 1);
        assertThat(testHolidayType.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createHolidayTypeWithExistingId() throws Exception {
        // Create the HolidayType with an existing ID
        holidayType.setId(1L);
        HolidayTypeDTO holidayTypeDTO = holidayTypeMapper.toDto(holidayType);

        int databaseSizeBeforeCreate = holidayTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHolidayTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(holidayTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HolidayType in the database
        List<HolidayType> holidayTypeList = holidayTypeRepository.findAll();
        assertThat(holidayTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = holidayTypeRepository.findAll().size();
        // set the field null
        holidayType.setName(null);

        // Create the HolidayType, which fails.
        HolidayTypeDTO holidayTypeDTO = holidayTypeMapper.toDto(holidayType);

        restHolidayTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(holidayTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<HolidayType> holidayTypeList = holidayTypeRepository.findAll();
        assertThat(holidayTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllHolidayTypes() throws Exception {
        // Initialize the database
        holidayTypeRepository.saveAndFlush(holidayType);

        // Get all the holidayTypeList
        restHolidayTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(holidayType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getHolidayType() throws Exception {
        // Initialize the database
        holidayTypeRepository.saveAndFlush(holidayType);

        // Get the holidayType
        restHolidayTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, holidayType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(holidayType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getHolidayTypesByIdFiltering() throws Exception {
        // Initialize the database
        holidayTypeRepository.saveAndFlush(holidayType);

        Long id = holidayType.getId();

        defaultHolidayTypeShouldBeFound("id.equals=" + id);
        defaultHolidayTypeShouldNotBeFound("id.notEquals=" + id);

        defaultHolidayTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultHolidayTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultHolidayTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultHolidayTypeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllHolidayTypesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        holidayTypeRepository.saveAndFlush(holidayType);

        // Get all the holidayTypeList where name equals to DEFAULT_NAME
        defaultHolidayTypeShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the holidayTypeList where name equals to UPDATED_NAME
        defaultHolidayTypeShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllHolidayTypesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        holidayTypeRepository.saveAndFlush(holidayType);

        // Get all the holidayTypeList where name not equals to DEFAULT_NAME
        defaultHolidayTypeShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the holidayTypeList where name not equals to UPDATED_NAME
        defaultHolidayTypeShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllHolidayTypesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        holidayTypeRepository.saveAndFlush(holidayType);

        // Get all the holidayTypeList where name in DEFAULT_NAME or UPDATED_NAME
        defaultHolidayTypeShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the holidayTypeList where name equals to UPDATED_NAME
        defaultHolidayTypeShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllHolidayTypesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        holidayTypeRepository.saveAndFlush(holidayType);

        // Get all the holidayTypeList where name is not null
        defaultHolidayTypeShouldBeFound("name.specified=true");

        // Get all the holidayTypeList where name is null
        defaultHolidayTypeShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllHolidayTypesByNameContainsSomething() throws Exception {
        // Initialize the database
        holidayTypeRepository.saveAndFlush(holidayType);

        // Get all the holidayTypeList where name contains DEFAULT_NAME
        defaultHolidayTypeShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the holidayTypeList where name contains UPDATED_NAME
        defaultHolidayTypeShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllHolidayTypesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        holidayTypeRepository.saveAndFlush(holidayType);

        // Get all the holidayTypeList where name does not contain DEFAULT_NAME
        defaultHolidayTypeShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the holidayTypeList where name does not contain UPDATED_NAME
        defaultHolidayTypeShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultHolidayTypeShouldBeFound(String filter) throws Exception {
        restHolidayTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(holidayType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restHolidayTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultHolidayTypeShouldNotBeFound(String filter) throws Exception {
        restHolidayTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restHolidayTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingHolidayType() throws Exception {
        // Get the holidayType
        restHolidayTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewHolidayType() throws Exception {
        // Initialize the database
        holidayTypeRepository.saveAndFlush(holidayType);

        int databaseSizeBeforeUpdate = holidayTypeRepository.findAll().size();

        // Update the holidayType
        HolidayType updatedHolidayType = holidayTypeRepository.findById(holidayType.getId()).get();
        // Disconnect from session so that the updates on updatedHolidayType are not directly saved in db
        em.detach(updatedHolidayType);
        updatedHolidayType.name(UPDATED_NAME);
        HolidayTypeDTO holidayTypeDTO = holidayTypeMapper.toDto(updatedHolidayType);

        restHolidayTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, holidayTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(holidayTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the HolidayType in the database
        List<HolidayType> holidayTypeList = holidayTypeRepository.findAll();
        assertThat(holidayTypeList).hasSize(databaseSizeBeforeUpdate);
        HolidayType testHolidayType = holidayTypeList.get(holidayTypeList.size() - 1);
        assertThat(testHolidayType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingHolidayType() throws Exception {
        int databaseSizeBeforeUpdate = holidayTypeRepository.findAll().size();
        holidayType.setId(count.incrementAndGet());

        // Create the HolidayType
        HolidayTypeDTO holidayTypeDTO = holidayTypeMapper.toDto(holidayType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHolidayTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, holidayTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(holidayTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HolidayType in the database
        List<HolidayType> holidayTypeList = holidayTypeRepository.findAll();
        assertThat(holidayTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHolidayType() throws Exception {
        int databaseSizeBeforeUpdate = holidayTypeRepository.findAll().size();
        holidayType.setId(count.incrementAndGet());

        // Create the HolidayType
        HolidayTypeDTO holidayTypeDTO = holidayTypeMapper.toDto(holidayType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHolidayTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(holidayTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HolidayType in the database
        List<HolidayType> holidayTypeList = holidayTypeRepository.findAll();
        assertThat(holidayTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHolidayType() throws Exception {
        int databaseSizeBeforeUpdate = holidayTypeRepository.findAll().size();
        holidayType.setId(count.incrementAndGet());

        // Create the HolidayType
        HolidayTypeDTO holidayTypeDTO = holidayTypeMapper.toDto(holidayType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHolidayTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(holidayTypeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the HolidayType in the database
        List<HolidayType> holidayTypeList = holidayTypeRepository.findAll();
        assertThat(holidayTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHolidayTypeWithPatch() throws Exception {
        // Initialize the database
        holidayTypeRepository.saveAndFlush(holidayType);

        int databaseSizeBeforeUpdate = holidayTypeRepository.findAll().size();

        // Update the holidayType using partial update
        HolidayType partialUpdatedHolidayType = new HolidayType();
        partialUpdatedHolidayType.setId(holidayType.getId());

        partialUpdatedHolidayType.name(UPDATED_NAME);

        restHolidayTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHolidayType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHolidayType))
            )
            .andExpect(status().isOk());

        // Validate the HolidayType in the database
        List<HolidayType> holidayTypeList = holidayTypeRepository.findAll();
        assertThat(holidayTypeList).hasSize(databaseSizeBeforeUpdate);
        HolidayType testHolidayType = holidayTypeList.get(holidayTypeList.size() - 1);
        assertThat(testHolidayType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateHolidayTypeWithPatch() throws Exception {
        // Initialize the database
        holidayTypeRepository.saveAndFlush(holidayType);

        int databaseSizeBeforeUpdate = holidayTypeRepository.findAll().size();

        // Update the holidayType using partial update
        HolidayType partialUpdatedHolidayType = new HolidayType();
        partialUpdatedHolidayType.setId(holidayType.getId());

        partialUpdatedHolidayType.name(UPDATED_NAME);

        restHolidayTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHolidayType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHolidayType))
            )
            .andExpect(status().isOk());

        // Validate the HolidayType in the database
        List<HolidayType> holidayTypeList = holidayTypeRepository.findAll();
        assertThat(holidayTypeList).hasSize(databaseSizeBeforeUpdate);
        HolidayType testHolidayType = holidayTypeList.get(holidayTypeList.size() - 1);
        assertThat(testHolidayType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingHolidayType() throws Exception {
        int databaseSizeBeforeUpdate = holidayTypeRepository.findAll().size();
        holidayType.setId(count.incrementAndGet());

        // Create the HolidayType
        HolidayTypeDTO holidayTypeDTO = holidayTypeMapper.toDto(holidayType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHolidayTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, holidayTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(holidayTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HolidayType in the database
        List<HolidayType> holidayTypeList = holidayTypeRepository.findAll();
        assertThat(holidayTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHolidayType() throws Exception {
        int databaseSizeBeforeUpdate = holidayTypeRepository.findAll().size();
        holidayType.setId(count.incrementAndGet());

        // Create the HolidayType
        HolidayTypeDTO holidayTypeDTO = holidayTypeMapper.toDto(holidayType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHolidayTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(holidayTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HolidayType in the database
        List<HolidayType> holidayTypeList = holidayTypeRepository.findAll();
        assertThat(holidayTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHolidayType() throws Exception {
        int databaseSizeBeforeUpdate = holidayTypeRepository.findAll().size();
        holidayType.setId(count.incrementAndGet());

        // Create the HolidayType
        HolidayTypeDTO holidayTypeDTO = holidayTypeMapper.toDto(holidayType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHolidayTypeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(holidayTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HolidayType in the database
        List<HolidayType> holidayTypeList = holidayTypeRepository.findAll();
        assertThat(holidayTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHolidayType() throws Exception {
        // Initialize the database
        holidayTypeRepository.saveAndFlush(holidayType);

        int databaseSizeBeforeDelete = holidayTypeRepository.findAll().size();

        // Delete the holidayType
        restHolidayTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, holidayType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<HolidayType> holidayTypeList = holidayTypeRepository.findAll();
        assertThat(holidayTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
