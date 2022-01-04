package com.hrisgsh.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.hrisgsh.app.IntegrationTest;
import com.hrisgsh.app.domain.DutySchedule;
import com.hrisgsh.app.domain.Employee;
import com.hrisgsh.app.repository.DutyScheduleRepository;
import com.hrisgsh.app.service.criteria.DutyScheduleCriteria;
import com.hrisgsh.app.service.dto.DutyScheduleDTO;
import com.hrisgsh.app.service.mapper.DutyScheduleMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link DutyScheduleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DutyScheduleResourceIT {

    private static final Instant DEFAULT_DATE_TIME_IN = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_TIME_IN = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DATE_TIME_OUT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_TIME_OUT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/duty-schedules";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DutyScheduleRepository dutyScheduleRepository;

    @Autowired
    private DutyScheduleMapper dutyScheduleMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDutyScheduleMockMvc;

    private DutySchedule dutySchedule;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DutySchedule createEntity(EntityManager em) {
        DutySchedule dutySchedule = new DutySchedule().dateTimeIn(DEFAULT_DATE_TIME_IN).dateTimeOut(DEFAULT_DATE_TIME_OUT);
        // Add required entity
        Employee employee;
        if (TestUtil.findAll(em, Employee.class).isEmpty()) {
            employee = EmployeeResourceIT.createEntity(em);
            em.persist(employee);
            em.flush();
        } else {
            employee = TestUtil.findAll(em, Employee.class).get(0);
        }
        dutySchedule.setEmployee(employee);
        return dutySchedule;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DutySchedule createUpdatedEntity(EntityManager em) {
        DutySchedule dutySchedule = new DutySchedule().dateTimeIn(UPDATED_DATE_TIME_IN).dateTimeOut(UPDATED_DATE_TIME_OUT);
        // Add required entity
        Employee employee;
        if (TestUtil.findAll(em, Employee.class).isEmpty()) {
            employee = EmployeeResourceIT.createUpdatedEntity(em);
            em.persist(employee);
            em.flush();
        } else {
            employee = TestUtil.findAll(em, Employee.class).get(0);
        }
        dutySchedule.setEmployee(employee);
        return dutySchedule;
    }

    @BeforeEach
    public void initTest() {
        dutySchedule = createEntity(em);
    }

    @Test
    @Transactional
    void createDutySchedule() throws Exception {
        int databaseSizeBeforeCreate = dutyScheduleRepository.findAll().size();
        // Create the DutySchedule
        DutyScheduleDTO dutyScheduleDTO = dutyScheduleMapper.toDto(dutySchedule);
        restDutyScheduleMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dutyScheduleDTO))
            )
            .andExpect(status().isCreated());

        // Validate the DutySchedule in the database
        List<DutySchedule> dutyScheduleList = dutyScheduleRepository.findAll();
        assertThat(dutyScheduleList).hasSize(databaseSizeBeforeCreate + 1);
        DutySchedule testDutySchedule = dutyScheduleList.get(dutyScheduleList.size() - 1);
        assertThat(testDutySchedule.getDateTimeIn()).isEqualTo(DEFAULT_DATE_TIME_IN);
        assertThat(testDutySchedule.getDateTimeOut()).isEqualTo(DEFAULT_DATE_TIME_OUT);
    }

    @Test
    @Transactional
    void createDutyScheduleWithExistingId() throws Exception {
        // Create the DutySchedule with an existing ID
        dutySchedule.setId(1L);
        DutyScheduleDTO dutyScheduleDTO = dutyScheduleMapper.toDto(dutySchedule);

        int databaseSizeBeforeCreate = dutyScheduleRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDutyScheduleMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dutyScheduleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DutySchedule in the database
        List<DutySchedule> dutyScheduleList = dutyScheduleRepository.findAll();
        assertThat(dutyScheduleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDutySchedules() throws Exception {
        // Initialize the database
        dutyScheduleRepository.saveAndFlush(dutySchedule);

        // Get all the dutyScheduleList
        restDutyScheduleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dutySchedule.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateTimeIn").value(hasItem(DEFAULT_DATE_TIME_IN.toString())))
            .andExpect(jsonPath("$.[*].dateTimeOut").value(hasItem(DEFAULT_DATE_TIME_OUT.toString())));
    }

    @Test
    @Transactional
    void getDutySchedule() throws Exception {
        // Initialize the database
        dutyScheduleRepository.saveAndFlush(dutySchedule);

        // Get the dutySchedule
        restDutyScheduleMockMvc
            .perform(get(ENTITY_API_URL_ID, dutySchedule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dutySchedule.getId().intValue()))
            .andExpect(jsonPath("$.dateTimeIn").value(DEFAULT_DATE_TIME_IN.toString()))
            .andExpect(jsonPath("$.dateTimeOut").value(DEFAULT_DATE_TIME_OUT.toString()));
    }

    @Test
    @Transactional
    void getDutySchedulesByIdFiltering() throws Exception {
        // Initialize the database
        dutyScheduleRepository.saveAndFlush(dutySchedule);

        Long id = dutySchedule.getId();

        defaultDutyScheduleShouldBeFound("id.equals=" + id);
        defaultDutyScheduleShouldNotBeFound("id.notEquals=" + id);

        defaultDutyScheduleShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDutyScheduleShouldNotBeFound("id.greaterThan=" + id);

        defaultDutyScheduleShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDutyScheduleShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDutySchedulesByDateTimeInIsEqualToSomething() throws Exception {
        // Initialize the database
        dutyScheduleRepository.saveAndFlush(dutySchedule);

        // Get all the dutyScheduleList where dateTimeIn equals to DEFAULT_DATE_TIME_IN
        defaultDutyScheduleShouldBeFound("dateTimeIn.equals=" + DEFAULT_DATE_TIME_IN);

        // Get all the dutyScheduleList where dateTimeIn equals to UPDATED_DATE_TIME_IN
        defaultDutyScheduleShouldNotBeFound("dateTimeIn.equals=" + UPDATED_DATE_TIME_IN);
    }

    @Test
    @Transactional
    void getAllDutySchedulesByDateTimeInIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dutyScheduleRepository.saveAndFlush(dutySchedule);

        // Get all the dutyScheduleList where dateTimeIn not equals to DEFAULT_DATE_TIME_IN
        defaultDutyScheduleShouldNotBeFound("dateTimeIn.notEquals=" + DEFAULT_DATE_TIME_IN);

        // Get all the dutyScheduleList where dateTimeIn not equals to UPDATED_DATE_TIME_IN
        defaultDutyScheduleShouldBeFound("dateTimeIn.notEquals=" + UPDATED_DATE_TIME_IN);
    }

    @Test
    @Transactional
    void getAllDutySchedulesByDateTimeInIsInShouldWork() throws Exception {
        // Initialize the database
        dutyScheduleRepository.saveAndFlush(dutySchedule);

        // Get all the dutyScheduleList where dateTimeIn in DEFAULT_DATE_TIME_IN or UPDATED_DATE_TIME_IN
        defaultDutyScheduleShouldBeFound("dateTimeIn.in=" + DEFAULT_DATE_TIME_IN + "," + UPDATED_DATE_TIME_IN);

        // Get all the dutyScheduleList where dateTimeIn equals to UPDATED_DATE_TIME_IN
        defaultDutyScheduleShouldNotBeFound("dateTimeIn.in=" + UPDATED_DATE_TIME_IN);
    }

    @Test
    @Transactional
    void getAllDutySchedulesByDateTimeInIsNullOrNotNull() throws Exception {
        // Initialize the database
        dutyScheduleRepository.saveAndFlush(dutySchedule);

        // Get all the dutyScheduleList where dateTimeIn is not null
        defaultDutyScheduleShouldBeFound("dateTimeIn.specified=true");

        // Get all the dutyScheduleList where dateTimeIn is null
        defaultDutyScheduleShouldNotBeFound("dateTimeIn.specified=false");
    }

    @Test
    @Transactional
    void getAllDutySchedulesByDateTimeOutIsEqualToSomething() throws Exception {
        // Initialize the database
        dutyScheduleRepository.saveAndFlush(dutySchedule);

        // Get all the dutyScheduleList where dateTimeOut equals to DEFAULT_DATE_TIME_OUT
        defaultDutyScheduleShouldBeFound("dateTimeOut.equals=" + DEFAULT_DATE_TIME_OUT);

        // Get all the dutyScheduleList where dateTimeOut equals to UPDATED_DATE_TIME_OUT
        defaultDutyScheduleShouldNotBeFound("dateTimeOut.equals=" + UPDATED_DATE_TIME_OUT);
    }

    @Test
    @Transactional
    void getAllDutySchedulesByDateTimeOutIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dutyScheduleRepository.saveAndFlush(dutySchedule);

        // Get all the dutyScheduleList where dateTimeOut not equals to DEFAULT_DATE_TIME_OUT
        defaultDutyScheduleShouldNotBeFound("dateTimeOut.notEquals=" + DEFAULT_DATE_TIME_OUT);

        // Get all the dutyScheduleList where dateTimeOut not equals to UPDATED_DATE_TIME_OUT
        defaultDutyScheduleShouldBeFound("dateTimeOut.notEquals=" + UPDATED_DATE_TIME_OUT);
    }

    @Test
    @Transactional
    void getAllDutySchedulesByDateTimeOutIsInShouldWork() throws Exception {
        // Initialize the database
        dutyScheduleRepository.saveAndFlush(dutySchedule);

        // Get all the dutyScheduleList where dateTimeOut in DEFAULT_DATE_TIME_OUT or UPDATED_DATE_TIME_OUT
        defaultDutyScheduleShouldBeFound("dateTimeOut.in=" + DEFAULT_DATE_TIME_OUT + "," + UPDATED_DATE_TIME_OUT);

        // Get all the dutyScheduleList where dateTimeOut equals to UPDATED_DATE_TIME_OUT
        defaultDutyScheduleShouldNotBeFound("dateTimeOut.in=" + UPDATED_DATE_TIME_OUT);
    }

    @Test
    @Transactional
    void getAllDutySchedulesByDateTimeOutIsNullOrNotNull() throws Exception {
        // Initialize the database
        dutyScheduleRepository.saveAndFlush(dutySchedule);

        // Get all the dutyScheduleList where dateTimeOut is not null
        defaultDutyScheduleShouldBeFound("dateTimeOut.specified=true");

        // Get all the dutyScheduleList where dateTimeOut is null
        defaultDutyScheduleShouldNotBeFound("dateTimeOut.specified=false");
    }

    @Test
    @Transactional
    void getAllDutySchedulesByEmployeeIsEqualToSomething() throws Exception {
        // Initialize the database
        dutyScheduleRepository.saveAndFlush(dutySchedule);
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
        dutySchedule.setEmployee(employee);
        dutyScheduleRepository.saveAndFlush(dutySchedule);
        Long employeeId = employee.getId();

        // Get all the dutyScheduleList where employee equals to employeeId
        defaultDutyScheduleShouldBeFound("employeeId.equals=" + employeeId);

        // Get all the dutyScheduleList where employee equals to (employeeId + 1)
        defaultDutyScheduleShouldNotBeFound("employeeId.equals=" + (employeeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDutyScheduleShouldBeFound(String filter) throws Exception {
        restDutyScheduleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dutySchedule.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateTimeIn").value(hasItem(DEFAULT_DATE_TIME_IN.toString())))
            .andExpect(jsonPath("$.[*].dateTimeOut").value(hasItem(DEFAULT_DATE_TIME_OUT.toString())));

        // Check, that the count call also returns 1
        restDutyScheduleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDutyScheduleShouldNotBeFound(String filter) throws Exception {
        restDutyScheduleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDutyScheduleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDutySchedule() throws Exception {
        // Get the dutySchedule
        restDutyScheduleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDutySchedule() throws Exception {
        // Initialize the database
        dutyScheduleRepository.saveAndFlush(dutySchedule);

        int databaseSizeBeforeUpdate = dutyScheduleRepository.findAll().size();

        // Update the dutySchedule
        DutySchedule updatedDutySchedule = dutyScheduleRepository.findById(dutySchedule.getId()).get();
        // Disconnect from session so that the updates on updatedDutySchedule are not directly saved in db
        em.detach(updatedDutySchedule);
        updatedDutySchedule.dateTimeIn(UPDATED_DATE_TIME_IN).dateTimeOut(UPDATED_DATE_TIME_OUT);
        DutyScheduleDTO dutyScheduleDTO = dutyScheduleMapper.toDto(updatedDutySchedule);

        restDutyScheduleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dutyScheduleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dutyScheduleDTO))
            )
            .andExpect(status().isOk());

        // Validate the DutySchedule in the database
        List<DutySchedule> dutyScheduleList = dutyScheduleRepository.findAll();
        assertThat(dutyScheduleList).hasSize(databaseSizeBeforeUpdate);
        DutySchedule testDutySchedule = dutyScheduleList.get(dutyScheduleList.size() - 1);
        assertThat(testDutySchedule.getDateTimeIn()).isEqualTo(UPDATED_DATE_TIME_IN);
        assertThat(testDutySchedule.getDateTimeOut()).isEqualTo(UPDATED_DATE_TIME_OUT);
    }

    @Test
    @Transactional
    void putNonExistingDutySchedule() throws Exception {
        int databaseSizeBeforeUpdate = dutyScheduleRepository.findAll().size();
        dutySchedule.setId(count.incrementAndGet());

        // Create the DutySchedule
        DutyScheduleDTO dutyScheduleDTO = dutyScheduleMapper.toDto(dutySchedule);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDutyScheduleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dutyScheduleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dutyScheduleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DutySchedule in the database
        List<DutySchedule> dutyScheduleList = dutyScheduleRepository.findAll();
        assertThat(dutyScheduleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDutySchedule() throws Exception {
        int databaseSizeBeforeUpdate = dutyScheduleRepository.findAll().size();
        dutySchedule.setId(count.incrementAndGet());

        // Create the DutySchedule
        DutyScheduleDTO dutyScheduleDTO = dutyScheduleMapper.toDto(dutySchedule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDutyScheduleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dutyScheduleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DutySchedule in the database
        List<DutySchedule> dutyScheduleList = dutyScheduleRepository.findAll();
        assertThat(dutyScheduleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDutySchedule() throws Exception {
        int databaseSizeBeforeUpdate = dutyScheduleRepository.findAll().size();
        dutySchedule.setId(count.incrementAndGet());

        // Create the DutySchedule
        DutyScheduleDTO dutyScheduleDTO = dutyScheduleMapper.toDto(dutySchedule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDutyScheduleMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dutyScheduleDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DutySchedule in the database
        List<DutySchedule> dutyScheduleList = dutyScheduleRepository.findAll();
        assertThat(dutyScheduleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDutyScheduleWithPatch() throws Exception {
        // Initialize the database
        dutyScheduleRepository.saveAndFlush(dutySchedule);

        int databaseSizeBeforeUpdate = dutyScheduleRepository.findAll().size();

        // Update the dutySchedule using partial update
        DutySchedule partialUpdatedDutySchedule = new DutySchedule();
        partialUpdatedDutySchedule.setId(dutySchedule.getId());

        restDutyScheduleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDutySchedule.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDutySchedule))
            )
            .andExpect(status().isOk());

        // Validate the DutySchedule in the database
        List<DutySchedule> dutyScheduleList = dutyScheduleRepository.findAll();
        assertThat(dutyScheduleList).hasSize(databaseSizeBeforeUpdate);
        DutySchedule testDutySchedule = dutyScheduleList.get(dutyScheduleList.size() - 1);
        assertThat(testDutySchedule.getDateTimeIn()).isEqualTo(DEFAULT_DATE_TIME_IN);
        assertThat(testDutySchedule.getDateTimeOut()).isEqualTo(DEFAULT_DATE_TIME_OUT);
    }

    @Test
    @Transactional
    void fullUpdateDutyScheduleWithPatch() throws Exception {
        // Initialize the database
        dutyScheduleRepository.saveAndFlush(dutySchedule);

        int databaseSizeBeforeUpdate = dutyScheduleRepository.findAll().size();

        // Update the dutySchedule using partial update
        DutySchedule partialUpdatedDutySchedule = new DutySchedule();
        partialUpdatedDutySchedule.setId(dutySchedule.getId());

        partialUpdatedDutySchedule.dateTimeIn(UPDATED_DATE_TIME_IN).dateTimeOut(UPDATED_DATE_TIME_OUT);

        restDutyScheduleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDutySchedule.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDutySchedule))
            )
            .andExpect(status().isOk());

        // Validate the DutySchedule in the database
        List<DutySchedule> dutyScheduleList = dutyScheduleRepository.findAll();
        assertThat(dutyScheduleList).hasSize(databaseSizeBeforeUpdate);
        DutySchedule testDutySchedule = dutyScheduleList.get(dutyScheduleList.size() - 1);
        assertThat(testDutySchedule.getDateTimeIn()).isEqualTo(UPDATED_DATE_TIME_IN);
        assertThat(testDutySchedule.getDateTimeOut()).isEqualTo(UPDATED_DATE_TIME_OUT);
    }

    @Test
    @Transactional
    void patchNonExistingDutySchedule() throws Exception {
        int databaseSizeBeforeUpdate = dutyScheduleRepository.findAll().size();
        dutySchedule.setId(count.incrementAndGet());

        // Create the DutySchedule
        DutyScheduleDTO dutyScheduleDTO = dutyScheduleMapper.toDto(dutySchedule);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDutyScheduleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, dutyScheduleDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dutyScheduleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DutySchedule in the database
        List<DutySchedule> dutyScheduleList = dutyScheduleRepository.findAll();
        assertThat(dutyScheduleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDutySchedule() throws Exception {
        int databaseSizeBeforeUpdate = dutyScheduleRepository.findAll().size();
        dutySchedule.setId(count.incrementAndGet());

        // Create the DutySchedule
        DutyScheduleDTO dutyScheduleDTO = dutyScheduleMapper.toDto(dutySchedule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDutyScheduleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dutyScheduleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DutySchedule in the database
        List<DutySchedule> dutyScheduleList = dutyScheduleRepository.findAll();
        assertThat(dutyScheduleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDutySchedule() throws Exception {
        int databaseSizeBeforeUpdate = dutyScheduleRepository.findAll().size();
        dutySchedule.setId(count.incrementAndGet());

        // Create the DutySchedule
        DutyScheduleDTO dutyScheduleDTO = dutyScheduleMapper.toDto(dutySchedule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDutyScheduleMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dutyScheduleDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DutySchedule in the database
        List<DutySchedule> dutyScheduleList = dutyScheduleRepository.findAll();
        assertThat(dutyScheduleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDutySchedule() throws Exception {
        // Initialize the database
        dutyScheduleRepository.saveAndFlush(dutySchedule);

        int databaseSizeBeforeDelete = dutyScheduleRepository.findAll().size();

        // Delete the dutySchedule
        restDutyScheduleMockMvc
            .perform(delete(ENTITY_API_URL_ID, dutySchedule.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DutySchedule> dutyScheduleList = dutyScheduleRepository.findAll();
        assertThat(dutyScheduleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
