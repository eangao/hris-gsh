package com.hrisgsh.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.hrisgsh.app.IntegrationTest;
import com.hrisgsh.app.domain.Cluster;
import com.hrisgsh.app.domain.Department;
import com.hrisgsh.app.repository.ClusterRepository;
import com.hrisgsh.app.service.criteria.ClusterCriteria;
import com.hrisgsh.app.service.dto.ClusterDTO;
import com.hrisgsh.app.service.mapper.ClusterMapper;
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
 * Integration tests for the {@link ClusterResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ClusterResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/clusters";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ClusterRepository clusterRepository;

    @Autowired
    private ClusterMapper clusterMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restClusterMockMvc;

    private Cluster cluster;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cluster createEntity(EntityManager em) {
        Cluster cluster = new Cluster().name(DEFAULT_NAME);
        return cluster;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cluster createUpdatedEntity(EntityManager em) {
        Cluster cluster = new Cluster().name(UPDATED_NAME);
        return cluster;
    }

    @BeforeEach
    public void initTest() {
        cluster = createEntity(em);
    }

    @Test
    @Transactional
    void createCluster() throws Exception {
        int databaseSizeBeforeCreate = clusterRepository.findAll().size();
        // Create the Cluster
        ClusterDTO clusterDTO = clusterMapper.toDto(cluster);
        restClusterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(clusterDTO)))
            .andExpect(status().isCreated());

        // Validate the Cluster in the database
        List<Cluster> clusterList = clusterRepository.findAll();
        assertThat(clusterList).hasSize(databaseSizeBeforeCreate + 1);
        Cluster testCluster = clusterList.get(clusterList.size() - 1);
        assertThat(testCluster.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createClusterWithExistingId() throws Exception {
        // Create the Cluster with an existing ID
        cluster.setId(1L);
        ClusterDTO clusterDTO = clusterMapper.toDto(cluster);

        int databaseSizeBeforeCreate = clusterRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restClusterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(clusterDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Cluster in the database
        List<Cluster> clusterList = clusterRepository.findAll();
        assertThat(clusterList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = clusterRepository.findAll().size();
        // set the field null
        cluster.setName(null);

        // Create the Cluster, which fails.
        ClusterDTO clusterDTO = clusterMapper.toDto(cluster);

        restClusterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(clusterDTO)))
            .andExpect(status().isBadRequest());

        List<Cluster> clusterList = clusterRepository.findAll();
        assertThat(clusterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllClusters() throws Exception {
        // Initialize the database
        clusterRepository.saveAndFlush(cluster);

        // Get all the clusterList
        restClusterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cluster.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getCluster() throws Exception {
        // Initialize the database
        clusterRepository.saveAndFlush(cluster);

        // Get the cluster
        restClusterMockMvc
            .perform(get(ENTITY_API_URL_ID, cluster.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cluster.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getClustersByIdFiltering() throws Exception {
        // Initialize the database
        clusterRepository.saveAndFlush(cluster);

        Long id = cluster.getId();

        defaultClusterShouldBeFound("id.equals=" + id);
        defaultClusterShouldNotBeFound("id.notEquals=" + id);

        defaultClusterShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultClusterShouldNotBeFound("id.greaterThan=" + id);

        defaultClusterShouldBeFound("id.lessThanOrEqual=" + id);
        defaultClusterShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllClustersByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        clusterRepository.saveAndFlush(cluster);

        // Get all the clusterList where name equals to DEFAULT_NAME
        defaultClusterShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the clusterList where name equals to UPDATED_NAME
        defaultClusterShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllClustersByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        clusterRepository.saveAndFlush(cluster);

        // Get all the clusterList where name not equals to DEFAULT_NAME
        defaultClusterShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the clusterList where name not equals to UPDATED_NAME
        defaultClusterShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllClustersByNameIsInShouldWork() throws Exception {
        // Initialize the database
        clusterRepository.saveAndFlush(cluster);

        // Get all the clusterList where name in DEFAULT_NAME or UPDATED_NAME
        defaultClusterShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the clusterList where name equals to UPDATED_NAME
        defaultClusterShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllClustersByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        clusterRepository.saveAndFlush(cluster);

        // Get all the clusterList where name is not null
        defaultClusterShouldBeFound("name.specified=true");

        // Get all the clusterList where name is null
        defaultClusterShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllClustersByNameContainsSomething() throws Exception {
        // Initialize the database
        clusterRepository.saveAndFlush(cluster);

        // Get all the clusterList where name contains DEFAULT_NAME
        defaultClusterShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the clusterList where name contains UPDATED_NAME
        defaultClusterShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllClustersByNameNotContainsSomething() throws Exception {
        // Initialize the database
        clusterRepository.saveAndFlush(cluster);

        // Get all the clusterList where name does not contain DEFAULT_NAME
        defaultClusterShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the clusterList where name does not contain UPDATED_NAME
        defaultClusterShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllClustersByDepartmentIsEqualToSomething() throws Exception {
        // Initialize the database
        clusterRepository.saveAndFlush(cluster);
        Department department;
        if (TestUtil.findAll(em, Department.class).isEmpty()) {
            department = DepartmentResourceIT.createEntity(em);
            em.persist(department);
            em.flush();
        } else {
            department = TestUtil.findAll(em, Department.class).get(0);
        }
        em.persist(department);
        em.flush();
        cluster.addDepartment(department);
        clusterRepository.saveAndFlush(cluster);
        Long departmentId = department.getId();

        // Get all the clusterList where department equals to departmentId
        defaultClusterShouldBeFound("departmentId.equals=" + departmentId);

        // Get all the clusterList where department equals to (departmentId + 1)
        defaultClusterShouldNotBeFound("departmentId.equals=" + (departmentId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultClusterShouldBeFound(String filter) throws Exception {
        restClusterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cluster.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restClusterMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultClusterShouldNotBeFound(String filter) throws Exception {
        restClusterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restClusterMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCluster() throws Exception {
        // Get the cluster
        restClusterMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCluster() throws Exception {
        // Initialize the database
        clusterRepository.saveAndFlush(cluster);

        int databaseSizeBeforeUpdate = clusterRepository.findAll().size();

        // Update the cluster
        Cluster updatedCluster = clusterRepository.findById(cluster.getId()).get();
        // Disconnect from session so that the updates on updatedCluster are not directly saved in db
        em.detach(updatedCluster);
        updatedCluster.name(UPDATED_NAME);
        ClusterDTO clusterDTO = clusterMapper.toDto(updatedCluster);

        restClusterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, clusterDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clusterDTO))
            )
            .andExpect(status().isOk());

        // Validate the Cluster in the database
        List<Cluster> clusterList = clusterRepository.findAll();
        assertThat(clusterList).hasSize(databaseSizeBeforeUpdate);
        Cluster testCluster = clusterList.get(clusterList.size() - 1);
        assertThat(testCluster.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingCluster() throws Exception {
        int databaseSizeBeforeUpdate = clusterRepository.findAll().size();
        cluster.setId(count.incrementAndGet());

        // Create the Cluster
        ClusterDTO clusterDTO = clusterMapper.toDto(cluster);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClusterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, clusterDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clusterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cluster in the database
        List<Cluster> clusterList = clusterRepository.findAll();
        assertThat(clusterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCluster() throws Exception {
        int databaseSizeBeforeUpdate = clusterRepository.findAll().size();
        cluster.setId(count.incrementAndGet());

        // Create the Cluster
        ClusterDTO clusterDTO = clusterMapper.toDto(cluster);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClusterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clusterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cluster in the database
        List<Cluster> clusterList = clusterRepository.findAll();
        assertThat(clusterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCluster() throws Exception {
        int databaseSizeBeforeUpdate = clusterRepository.findAll().size();
        cluster.setId(count.incrementAndGet());

        // Create the Cluster
        ClusterDTO clusterDTO = clusterMapper.toDto(cluster);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClusterMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(clusterDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cluster in the database
        List<Cluster> clusterList = clusterRepository.findAll();
        assertThat(clusterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateClusterWithPatch() throws Exception {
        // Initialize the database
        clusterRepository.saveAndFlush(cluster);

        int databaseSizeBeforeUpdate = clusterRepository.findAll().size();

        // Update the cluster using partial update
        Cluster partialUpdatedCluster = new Cluster();
        partialUpdatedCluster.setId(cluster.getId());

        partialUpdatedCluster.name(UPDATED_NAME);

        restClusterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCluster.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCluster))
            )
            .andExpect(status().isOk());

        // Validate the Cluster in the database
        List<Cluster> clusterList = clusterRepository.findAll();
        assertThat(clusterList).hasSize(databaseSizeBeforeUpdate);
        Cluster testCluster = clusterList.get(clusterList.size() - 1);
        assertThat(testCluster.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateClusterWithPatch() throws Exception {
        // Initialize the database
        clusterRepository.saveAndFlush(cluster);

        int databaseSizeBeforeUpdate = clusterRepository.findAll().size();

        // Update the cluster using partial update
        Cluster partialUpdatedCluster = new Cluster();
        partialUpdatedCluster.setId(cluster.getId());

        partialUpdatedCluster.name(UPDATED_NAME);

        restClusterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCluster.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCluster))
            )
            .andExpect(status().isOk());

        // Validate the Cluster in the database
        List<Cluster> clusterList = clusterRepository.findAll();
        assertThat(clusterList).hasSize(databaseSizeBeforeUpdate);
        Cluster testCluster = clusterList.get(clusterList.size() - 1);
        assertThat(testCluster.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingCluster() throws Exception {
        int databaseSizeBeforeUpdate = clusterRepository.findAll().size();
        cluster.setId(count.incrementAndGet());

        // Create the Cluster
        ClusterDTO clusterDTO = clusterMapper.toDto(cluster);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClusterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, clusterDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(clusterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cluster in the database
        List<Cluster> clusterList = clusterRepository.findAll();
        assertThat(clusterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCluster() throws Exception {
        int databaseSizeBeforeUpdate = clusterRepository.findAll().size();
        cluster.setId(count.incrementAndGet());

        // Create the Cluster
        ClusterDTO clusterDTO = clusterMapper.toDto(cluster);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClusterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(clusterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cluster in the database
        List<Cluster> clusterList = clusterRepository.findAll();
        assertThat(clusterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCluster() throws Exception {
        int databaseSizeBeforeUpdate = clusterRepository.findAll().size();
        cluster.setId(count.incrementAndGet());

        // Create the Cluster
        ClusterDTO clusterDTO = clusterMapper.toDto(cluster);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClusterMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(clusterDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cluster in the database
        List<Cluster> clusterList = clusterRepository.findAll();
        assertThat(clusterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCluster() throws Exception {
        // Initialize the database
        clusterRepository.saveAndFlush(cluster);

        int databaseSizeBeforeDelete = clusterRepository.findAll().size();

        // Delete the cluster
        restClusterMockMvc
            .perform(delete(ENTITY_API_URL_ID, cluster.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Cluster> clusterList = clusterRepository.findAll();
        assertThat(clusterList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
