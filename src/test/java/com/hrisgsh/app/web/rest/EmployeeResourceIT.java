package com.hrisgsh.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.hrisgsh.app.IntegrationTest;
import com.hrisgsh.app.domain.Benefits;
import com.hrisgsh.app.domain.DailyTimeRecord;
import com.hrisgsh.app.domain.Department;
import com.hrisgsh.app.domain.Dependents;
import com.hrisgsh.app.domain.Designation;
import com.hrisgsh.app.domain.DutySchedule;
import com.hrisgsh.app.domain.Education;
import com.hrisgsh.app.domain.Employee;
import com.hrisgsh.app.domain.Leave;
import com.hrisgsh.app.domain.TrainingHistory;
import com.hrisgsh.app.domain.User;
import com.hrisgsh.app.domain.enumeration.EmploymentType;
import com.hrisgsh.app.domain.enumeration.Gender;
import com.hrisgsh.app.domain.enumeration.Status;
import com.hrisgsh.app.repository.EmployeeRepository;
import com.hrisgsh.app.service.EmployeeService;
import com.hrisgsh.app.service.criteria.EmployeeCriteria;
import com.hrisgsh.app.service.dto.EmployeeDTO;
import com.hrisgsh.app.service.mapper.EmployeeMapper;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link EmployeeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class EmployeeResourceIT {

    private static final Integer DEFAULT_EMPLOYEE_BIOMETRIC_ID = 1;
    private static final Integer UPDATED_EMPLOYEE_BIOMETRIC_ID = 2;
    private static final Integer SMALLER_EMPLOYEE_BIOMETRIC_ID = 1 - 1;

    private static final String DEFAULT_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_USERNAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_MIDDLE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_MIDDLE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_NAME_SUFFIX = "AAAAAAAAAA";
    private static final String UPDATED_NAME_SUFFIX = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_BIRTHDATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BIRTHDATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_BIRTHDATE = LocalDate.ofEpochDay(-1L);

    private static final Gender DEFAULT_GENDER = Gender.MALE;
    private static final Gender UPDATED_GENDER = Gender.FEMALE;

    private static final Status DEFAULT_STATUS = Status.SINGLE;
    private static final Status UPDATED_STATUS = Status.MARRIED;

    private static final EmploymentType DEFAULT_EMPLOYMENT_TYPE = EmploymentType.DINOMINATIONAL;
    private static final EmploymentType UPDATED_EMPLOYMENT_TYPE = EmploymentType.INSTITUTIONAL;

    private static final String DEFAULT_MOBILE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_MOBILE_NUMBER = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_HIRED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_HIRED = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_HIRED = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_DATE_DENO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DENO = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_DENO = LocalDate.ofEpochDay(-1L);

    private static final Integer DEFAULT_SICK_LEAVE_YEARLY_CREDIT = 1;
    private static final Integer UPDATED_SICK_LEAVE_YEARLY_CREDIT = 2;
    private static final Integer SMALLER_SICK_LEAVE_YEARLY_CREDIT = 1 - 1;

    private static final Integer DEFAULT_SICK_LEAVE_YEARLY_CREDIT_USED = 1;
    private static final Integer UPDATED_SICK_LEAVE_YEARLY_CREDIT_USED = 2;
    private static final Integer SMALLER_SICK_LEAVE_YEARLY_CREDIT_USED = 1 - 1;

    private static final Integer DEFAULT_LEAVE_YEARLY_CREDIT = 1;
    private static final Integer UPDATED_LEAVE_YEARLY_CREDIT = 2;
    private static final Integer SMALLER_LEAVE_YEARLY_CREDIT = 1 - 1;

    private static final Integer DEFAULT_LEAVE_YEARLY_CREDIT_USED = 1;
    private static final Integer UPDATED_LEAVE_YEARLY_CREDIT_USED = 2;
    private static final Integer SMALLER_LEAVE_YEARLY_CREDIT_USED = 1 - 1;

    private static final byte[] DEFAULT_PROFILE_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PROFILE_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PROFILE_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PROFILE_IMAGE_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_PRESENT_ADDRESS_STREET = "AAAAAAAAAA";
    private static final String UPDATED_PRESENT_ADDRESS_STREET = "BBBBBBBBBB";

    private static final String DEFAULT_PRESENT_ADDRESS_CITY = "AAAAAAAAAA";
    private static final String UPDATED_PRESENT_ADDRESS_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_PRESENT_ADDRESS_PROVINCE = "AAAAAAAAAA";
    private static final String UPDATED_PRESENT_ADDRESS_PROVINCE = "BBBBBBBBBB";

    private static final Integer DEFAULT_PRESENT_ADDRESS_ZIPCODE = 1;
    private static final Integer UPDATED_PRESENT_ADDRESS_ZIPCODE = 2;
    private static final Integer SMALLER_PRESENT_ADDRESS_ZIPCODE = 1 - 1;

    private static final String DEFAULT_HOME_ADDRESS_STREET = "AAAAAAAAAA";
    private static final String UPDATED_HOME_ADDRESS_STREET = "BBBBBBBBBB";

    private static final String DEFAULT_HOME_ADDRESS_CITY = "AAAAAAAAAA";
    private static final String UPDATED_HOME_ADDRESS_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_HOME_ADDRESS_PROVINCE = "AAAAAAAAAA";
    private static final String UPDATED_HOME_ADDRESS_PROVINCE = "BBBBBBBBBB";

    private static final Integer DEFAULT_HOME_ADDRESS_ZIPCODE = 1;
    private static final Integer UPDATED_HOME_ADDRESS_ZIPCODE = 2;
    private static final Integer SMALLER_HOME_ADDRESS_ZIPCODE = 1 - 1;

    private static final String ENTITY_API_URL = "/api/employees";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeRepository employeeRepositoryMock;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Mock
    private EmployeeService employeeServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmployeeMockMvc;

    private Employee employee;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Employee createEntity(EntityManager em) {
        Employee employee = new Employee()
            .employeeBiometricId(DEFAULT_EMPLOYEE_BIOMETRIC_ID)
            .username(DEFAULT_USERNAME)
            .email(DEFAULT_EMAIL)
            .firstName(DEFAULT_FIRST_NAME)
            .middleName(DEFAULT_MIDDLE_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .nameSuffix(DEFAULT_NAME_SUFFIX)
            .birthdate(DEFAULT_BIRTHDATE)
            .gender(DEFAULT_GENDER)
            .status(DEFAULT_STATUS)
            .employmentType(DEFAULT_EMPLOYMENT_TYPE)
            .mobileNumber(DEFAULT_MOBILE_NUMBER)
            .dateHired(DEFAULT_DATE_HIRED)
            .dateDeno(DEFAULT_DATE_DENO)
            .sickLeaveYearlyCredit(DEFAULT_SICK_LEAVE_YEARLY_CREDIT)
            .sickLeaveYearlyCreditUsed(DEFAULT_SICK_LEAVE_YEARLY_CREDIT_USED)
            .leaveYearlyCredit(DEFAULT_LEAVE_YEARLY_CREDIT)
            .leaveYearlyCreditUsed(DEFAULT_LEAVE_YEARLY_CREDIT_USED)
            .profileImage(DEFAULT_PROFILE_IMAGE)
            .profileImageContentType(DEFAULT_PROFILE_IMAGE_CONTENT_TYPE)
            .presentAddressStreet(DEFAULT_PRESENT_ADDRESS_STREET)
            .presentAddressCity(DEFAULT_PRESENT_ADDRESS_CITY)
            .presentAddressProvince(DEFAULT_PRESENT_ADDRESS_PROVINCE)
            .presentAddressZipcode(DEFAULT_PRESENT_ADDRESS_ZIPCODE)
            .homeAddressStreet(DEFAULT_HOME_ADDRESS_STREET)
            .homeAddressCity(DEFAULT_HOME_ADDRESS_CITY)
            .homeAddressProvince(DEFAULT_HOME_ADDRESS_PROVINCE)
            .homeAddressZipcode(DEFAULT_HOME_ADDRESS_ZIPCODE);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        employee.setUser(user);
        // Add required entity
        Designation designation;
        if (TestUtil.findAll(em, Designation.class).isEmpty()) {
            designation = DesignationResourceIT.createEntity(em);
            em.persist(designation);
            em.flush();
        } else {
            designation = TestUtil.findAll(em, Designation.class).get(0);
        }
        employee.getDesignations().add(designation);
        // Add required entity
        Department department;
        if (TestUtil.findAll(em, Department.class).isEmpty()) {
            department = DepartmentResourceIT.createEntity(em);
            em.persist(department);
            em.flush();
        } else {
            department = TestUtil.findAll(em, Department.class).get(0);
        }
        employee.setDepartment(department);
        return employee;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Employee createUpdatedEntity(EntityManager em) {
        Employee employee = new Employee()
            .employeeBiometricId(UPDATED_EMPLOYEE_BIOMETRIC_ID)
            .username(UPDATED_USERNAME)
            .email(UPDATED_EMAIL)
            .firstName(UPDATED_FIRST_NAME)
            .middleName(UPDATED_MIDDLE_NAME)
            .lastName(UPDATED_LAST_NAME)
            .nameSuffix(UPDATED_NAME_SUFFIX)
            .birthdate(UPDATED_BIRTHDATE)
            .gender(UPDATED_GENDER)
            .status(UPDATED_STATUS)
            .employmentType(UPDATED_EMPLOYMENT_TYPE)
            .mobileNumber(UPDATED_MOBILE_NUMBER)
            .dateHired(UPDATED_DATE_HIRED)
            .dateDeno(UPDATED_DATE_DENO)
            .sickLeaveYearlyCredit(UPDATED_SICK_LEAVE_YEARLY_CREDIT)
            .sickLeaveYearlyCreditUsed(UPDATED_SICK_LEAVE_YEARLY_CREDIT_USED)
            .leaveYearlyCredit(UPDATED_LEAVE_YEARLY_CREDIT)
            .leaveYearlyCreditUsed(UPDATED_LEAVE_YEARLY_CREDIT_USED)
            .profileImage(UPDATED_PROFILE_IMAGE)
            .profileImageContentType(UPDATED_PROFILE_IMAGE_CONTENT_TYPE)
            .presentAddressStreet(UPDATED_PRESENT_ADDRESS_STREET)
            .presentAddressCity(UPDATED_PRESENT_ADDRESS_CITY)
            .presentAddressProvince(UPDATED_PRESENT_ADDRESS_PROVINCE)
            .presentAddressZipcode(UPDATED_PRESENT_ADDRESS_ZIPCODE)
            .homeAddressStreet(UPDATED_HOME_ADDRESS_STREET)
            .homeAddressCity(UPDATED_HOME_ADDRESS_CITY)
            .homeAddressProvince(UPDATED_HOME_ADDRESS_PROVINCE)
            .homeAddressZipcode(UPDATED_HOME_ADDRESS_ZIPCODE);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        employee.setUser(user);
        // Add required entity
        Designation designation;
        if (TestUtil.findAll(em, Designation.class).isEmpty()) {
            designation = DesignationResourceIT.createUpdatedEntity(em);
            em.persist(designation);
            em.flush();
        } else {
            designation = TestUtil.findAll(em, Designation.class).get(0);
        }
        employee.getDesignations().add(designation);
        // Add required entity
        Department department;
        if (TestUtil.findAll(em, Department.class).isEmpty()) {
            department = DepartmentResourceIT.createUpdatedEntity(em);
            em.persist(department);
            em.flush();
        } else {
            department = TestUtil.findAll(em, Department.class).get(0);
        }
        employee.setDepartment(department);
        return employee;
    }

    @BeforeEach
    public void initTest() {
        employee = createEntity(em);
    }

    @Test
    @Transactional
    void createEmployee() throws Exception {
        int databaseSizeBeforeCreate = employeeRepository.findAll().size();
        // Create the Employee
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);
        restEmployeeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employeeDTO)))
            .andExpect(status().isCreated());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeCreate + 1);
        Employee testEmployee = employeeList.get(employeeList.size() - 1);
        assertThat(testEmployee.getEmployeeBiometricId()).isEqualTo(DEFAULT_EMPLOYEE_BIOMETRIC_ID);
        assertThat(testEmployee.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(testEmployee.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testEmployee.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testEmployee.getMiddleName()).isEqualTo(DEFAULT_MIDDLE_NAME);
        assertThat(testEmployee.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testEmployee.getNameSuffix()).isEqualTo(DEFAULT_NAME_SUFFIX);
        assertThat(testEmployee.getBirthdate()).isEqualTo(DEFAULT_BIRTHDATE);
        assertThat(testEmployee.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testEmployee.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testEmployee.getEmploymentType()).isEqualTo(DEFAULT_EMPLOYMENT_TYPE);
        assertThat(testEmployee.getMobileNumber()).isEqualTo(DEFAULT_MOBILE_NUMBER);
        assertThat(testEmployee.getDateHired()).isEqualTo(DEFAULT_DATE_HIRED);
        assertThat(testEmployee.getDateDeno()).isEqualTo(DEFAULT_DATE_DENO);
        assertThat(testEmployee.getSickLeaveYearlyCredit()).isEqualTo(DEFAULT_SICK_LEAVE_YEARLY_CREDIT);
        assertThat(testEmployee.getSickLeaveYearlyCreditUsed()).isEqualTo(DEFAULT_SICK_LEAVE_YEARLY_CREDIT_USED);
        assertThat(testEmployee.getLeaveYearlyCredit()).isEqualTo(DEFAULT_LEAVE_YEARLY_CREDIT);
        assertThat(testEmployee.getLeaveYearlyCreditUsed()).isEqualTo(DEFAULT_LEAVE_YEARLY_CREDIT_USED);
        assertThat(testEmployee.getProfileImage()).isEqualTo(DEFAULT_PROFILE_IMAGE);
        assertThat(testEmployee.getProfileImageContentType()).isEqualTo(DEFAULT_PROFILE_IMAGE_CONTENT_TYPE);
        assertThat(testEmployee.getPresentAddressStreet()).isEqualTo(DEFAULT_PRESENT_ADDRESS_STREET);
        assertThat(testEmployee.getPresentAddressCity()).isEqualTo(DEFAULT_PRESENT_ADDRESS_CITY);
        assertThat(testEmployee.getPresentAddressProvince()).isEqualTo(DEFAULT_PRESENT_ADDRESS_PROVINCE);
        assertThat(testEmployee.getPresentAddressZipcode()).isEqualTo(DEFAULT_PRESENT_ADDRESS_ZIPCODE);
        assertThat(testEmployee.getHomeAddressStreet()).isEqualTo(DEFAULT_HOME_ADDRESS_STREET);
        assertThat(testEmployee.getHomeAddressCity()).isEqualTo(DEFAULT_HOME_ADDRESS_CITY);
        assertThat(testEmployee.getHomeAddressProvince()).isEqualTo(DEFAULT_HOME_ADDRESS_PROVINCE);
        assertThat(testEmployee.getHomeAddressZipcode()).isEqualTo(DEFAULT_HOME_ADDRESS_ZIPCODE);
    }

    @Test
    @Transactional
    void createEmployeeWithExistingId() throws Exception {
        // Create the Employee with an existing ID
        employee.setId(1L);
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        int databaseSizeBeforeCreate = employeeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmployeeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employeeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEmployeeBiometricIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeRepository.findAll().size();
        // set the field null
        employee.setEmployeeBiometricId(null);

        // Create the Employee, which fails.
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        restEmployeeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employeeDTO)))
            .andExpect(status().isBadRequest());

        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUsernameIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeRepository.findAll().size();
        // set the field null
        employee.setUsername(null);

        // Create the Employee, which fails.
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        restEmployeeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employeeDTO)))
            .andExpect(status().isBadRequest());

        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeRepository.findAll().size();
        // set the field null
        employee.setFirstName(null);

        // Create the Employee, which fails.
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        restEmployeeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employeeDTO)))
            .andExpect(status().isBadRequest());

        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeRepository.findAll().size();
        // set the field null
        employee.setLastName(null);

        // Create the Employee, which fails.
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        restEmployeeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employeeDTO)))
            .andExpect(status().isBadRequest());

        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBirthdateIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeRepository.findAll().size();
        // set the field null
        employee.setBirthdate(null);

        // Create the Employee, which fails.
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        restEmployeeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employeeDTO)))
            .andExpect(status().isBadRequest());

        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkGenderIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeRepository.findAll().size();
        // set the field null
        employee.setGender(null);

        // Create the Employee, which fails.
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        restEmployeeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employeeDTO)))
            .andExpect(status().isBadRequest());

        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeRepository.findAll().size();
        // set the field null
        employee.setStatus(null);

        // Create the Employee, which fails.
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        restEmployeeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employeeDTO)))
            .andExpect(status().isBadRequest());

        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmploymentTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeRepository.findAll().size();
        // set the field null
        employee.setEmploymentType(null);

        // Create the Employee, which fails.
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        restEmployeeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employeeDTO)))
            .andExpect(status().isBadRequest());

        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEmployees() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList
        restEmployeeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employee.getId().intValue())))
            .andExpect(jsonPath("$.[*].employeeBiometricId").value(hasItem(DEFAULT_EMPLOYEE_BIOMETRIC_ID)))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].middleName").value(hasItem(DEFAULT_MIDDLE_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].nameSuffix").value(hasItem(DEFAULT_NAME_SUFFIX)))
            .andExpect(jsonPath("$.[*].birthdate").value(hasItem(DEFAULT_BIRTHDATE.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].employmentType").value(hasItem(DEFAULT_EMPLOYMENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].mobileNumber").value(hasItem(DEFAULT_MOBILE_NUMBER)))
            .andExpect(jsonPath("$.[*].dateHired").value(hasItem(DEFAULT_DATE_HIRED.toString())))
            .andExpect(jsonPath("$.[*].dateDeno").value(hasItem(DEFAULT_DATE_DENO.toString())))
            .andExpect(jsonPath("$.[*].sickLeaveYearlyCredit").value(hasItem(DEFAULT_SICK_LEAVE_YEARLY_CREDIT)))
            .andExpect(jsonPath("$.[*].sickLeaveYearlyCreditUsed").value(hasItem(DEFAULT_SICK_LEAVE_YEARLY_CREDIT_USED)))
            .andExpect(jsonPath("$.[*].leaveYearlyCredit").value(hasItem(DEFAULT_LEAVE_YEARLY_CREDIT)))
            .andExpect(jsonPath("$.[*].leaveYearlyCreditUsed").value(hasItem(DEFAULT_LEAVE_YEARLY_CREDIT_USED)))
            .andExpect(jsonPath("$.[*].profileImageContentType").value(hasItem(DEFAULT_PROFILE_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].profileImage").value(hasItem(Base64Utils.encodeToString(DEFAULT_PROFILE_IMAGE))))
            .andExpect(jsonPath("$.[*].presentAddressStreet").value(hasItem(DEFAULT_PRESENT_ADDRESS_STREET)))
            .andExpect(jsonPath("$.[*].presentAddressCity").value(hasItem(DEFAULT_PRESENT_ADDRESS_CITY)))
            .andExpect(jsonPath("$.[*].presentAddressProvince").value(hasItem(DEFAULT_PRESENT_ADDRESS_PROVINCE)))
            .andExpect(jsonPath("$.[*].presentAddressZipcode").value(hasItem(DEFAULT_PRESENT_ADDRESS_ZIPCODE)))
            .andExpect(jsonPath("$.[*].homeAddressStreet").value(hasItem(DEFAULT_HOME_ADDRESS_STREET)))
            .andExpect(jsonPath("$.[*].homeAddressCity").value(hasItem(DEFAULT_HOME_ADDRESS_CITY)))
            .andExpect(jsonPath("$.[*].homeAddressProvince").value(hasItem(DEFAULT_HOME_ADDRESS_PROVINCE)))
            .andExpect(jsonPath("$.[*].homeAddressZipcode").value(hasItem(DEFAULT_HOME_ADDRESS_ZIPCODE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEmployeesWithEagerRelationshipsIsEnabled() throws Exception {
        when(employeeServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEmployeeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(employeeServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEmployeesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(employeeServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEmployeeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(employeeServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getEmployee() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get the employee
        restEmployeeMockMvc
            .perform(get(ENTITY_API_URL_ID, employee.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(employee.getId().intValue()))
            .andExpect(jsonPath("$.employeeBiometricId").value(DEFAULT_EMPLOYEE_BIOMETRIC_ID))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.middleName").value(DEFAULT_MIDDLE_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.nameSuffix").value(DEFAULT_NAME_SUFFIX))
            .andExpect(jsonPath("$.birthdate").value(DEFAULT_BIRTHDATE.toString()))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.employmentType").value(DEFAULT_EMPLOYMENT_TYPE.toString()))
            .andExpect(jsonPath("$.mobileNumber").value(DEFAULT_MOBILE_NUMBER))
            .andExpect(jsonPath("$.dateHired").value(DEFAULT_DATE_HIRED.toString()))
            .andExpect(jsonPath("$.dateDeno").value(DEFAULT_DATE_DENO.toString()))
            .andExpect(jsonPath("$.sickLeaveYearlyCredit").value(DEFAULT_SICK_LEAVE_YEARLY_CREDIT))
            .andExpect(jsonPath("$.sickLeaveYearlyCreditUsed").value(DEFAULT_SICK_LEAVE_YEARLY_CREDIT_USED))
            .andExpect(jsonPath("$.leaveYearlyCredit").value(DEFAULT_LEAVE_YEARLY_CREDIT))
            .andExpect(jsonPath("$.leaveYearlyCreditUsed").value(DEFAULT_LEAVE_YEARLY_CREDIT_USED))
            .andExpect(jsonPath("$.profileImageContentType").value(DEFAULT_PROFILE_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.profileImage").value(Base64Utils.encodeToString(DEFAULT_PROFILE_IMAGE)))
            .andExpect(jsonPath("$.presentAddressStreet").value(DEFAULT_PRESENT_ADDRESS_STREET))
            .andExpect(jsonPath("$.presentAddressCity").value(DEFAULT_PRESENT_ADDRESS_CITY))
            .andExpect(jsonPath("$.presentAddressProvince").value(DEFAULT_PRESENT_ADDRESS_PROVINCE))
            .andExpect(jsonPath("$.presentAddressZipcode").value(DEFAULT_PRESENT_ADDRESS_ZIPCODE))
            .andExpect(jsonPath("$.homeAddressStreet").value(DEFAULT_HOME_ADDRESS_STREET))
            .andExpect(jsonPath("$.homeAddressCity").value(DEFAULT_HOME_ADDRESS_CITY))
            .andExpect(jsonPath("$.homeAddressProvince").value(DEFAULT_HOME_ADDRESS_PROVINCE))
            .andExpect(jsonPath("$.homeAddressZipcode").value(DEFAULT_HOME_ADDRESS_ZIPCODE));
    }

    @Test
    @Transactional
    void getEmployeesByIdFiltering() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        Long id = employee.getId();

        defaultEmployeeShouldBeFound("id.equals=" + id);
        defaultEmployeeShouldNotBeFound("id.notEquals=" + id);

        defaultEmployeeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEmployeeShouldNotBeFound("id.greaterThan=" + id);

        defaultEmployeeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEmployeeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEmployeesByEmployeeBiometricIdIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where employeeBiometricId equals to DEFAULT_EMPLOYEE_BIOMETRIC_ID
        defaultEmployeeShouldBeFound("employeeBiometricId.equals=" + DEFAULT_EMPLOYEE_BIOMETRIC_ID);

        // Get all the employeeList where employeeBiometricId equals to UPDATED_EMPLOYEE_BIOMETRIC_ID
        defaultEmployeeShouldNotBeFound("employeeBiometricId.equals=" + UPDATED_EMPLOYEE_BIOMETRIC_ID);
    }

    @Test
    @Transactional
    void getAllEmployeesByEmployeeBiometricIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where employeeBiometricId not equals to DEFAULT_EMPLOYEE_BIOMETRIC_ID
        defaultEmployeeShouldNotBeFound("employeeBiometricId.notEquals=" + DEFAULT_EMPLOYEE_BIOMETRIC_ID);

        // Get all the employeeList where employeeBiometricId not equals to UPDATED_EMPLOYEE_BIOMETRIC_ID
        defaultEmployeeShouldBeFound("employeeBiometricId.notEquals=" + UPDATED_EMPLOYEE_BIOMETRIC_ID);
    }

    @Test
    @Transactional
    void getAllEmployeesByEmployeeBiometricIdIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where employeeBiometricId in DEFAULT_EMPLOYEE_BIOMETRIC_ID or UPDATED_EMPLOYEE_BIOMETRIC_ID
        defaultEmployeeShouldBeFound("employeeBiometricId.in=" + DEFAULT_EMPLOYEE_BIOMETRIC_ID + "," + UPDATED_EMPLOYEE_BIOMETRIC_ID);

        // Get all the employeeList where employeeBiometricId equals to UPDATED_EMPLOYEE_BIOMETRIC_ID
        defaultEmployeeShouldNotBeFound("employeeBiometricId.in=" + UPDATED_EMPLOYEE_BIOMETRIC_ID);
    }

    @Test
    @Transactional
    void getAllEmployeesByEmployeeBiometricIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where employeeBiometricId is not null
        defaultEmployeeShouldBeFound("employeeBiometricId.specified=true");

        // Get all the employeeList where employeeBiometricId is null
        defaultEmployeeShouldNotBeFound("employeeBiometricId.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByEmployeeBiometricIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where employeeBiometricId is greater than or equal to DEFAULT_EMPLOYEE_BIOMETRIC_ID
        defaultEmployeeShouldBeFound("employeeBiometricId.greaterThanOrEqual=" + DEFAULT_EMPLOYEE_BIOMETRIC_ID);

        // Get all the employeeList where employeeBiometricId is greater than or equal to UPDATED_EMPLOYEE_BIOMETRIC_ID
        defaultEmployeeShouldNotBeFound("employeeBiometricId.greaterThanOrEqual=" + UPDATED_EMPLOYEE_BIOMETRIC_ID);
    }

    @Test
    @Transactional
    void getAllEmployeesByEmployeeBiometricIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where employeeBiometricId is less than or equal to DEFAULT_EMPLOYEE_BIOMETRIC_ID
        defaultEmployeeShouldBeFound("employeeBiometricId.lessThanOrEqual=" + DEFAULT_EMPLOYEE_BIOMETRIC_ID);

        // Get all the employeeList where employeeBiometricId is less than or equal to SMALLER_EMPLOYEE_BIOMETRIC_ID
        defaultEmployeeShouldNotBeFound("employeeBiometricId.lessThanOrEqual=" + SMALLER_EMPLOYEE_BIOMETRIC_ID);
    }

    @Test
    @Transactional
    void getAllEmployeesByEmployeeBiometricIdIsLessThanSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where employeeBiometricId is less than DEFAULT_EMPLOYEE_BIOMETRIC_ID
        defaultEmployeeShouldNotBeFound("employeeBiometricId.lessThan=" + DEFAULT_EMPLOYEE_BIOMETRIC_ID);

        // Get all the employeeList where employeeBiometricId is less than UPDATED_EMPLOYEE_BIOMETRIC_ID
        defaultEmployeeShouldBeFound("employeeBiometricId.lessThan=" + UPDATED_EMPLOYEE_BIOMETRIC_ID);
    }

    @Test
    @Transactional
    void getAllEmployeesByEmployeeBiometricIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where employeeBiometricId is greater than DEFAULT_EMPLOYEE_BIOMETRIC_ID
        defaultEmployeeShouldNotBeFound("employeeBiometricId.greaterThan=" + DEFAULT_EMPLOYEE_BIOMETRIC_ID);

        // Get all the employeeList where employeeBiometricId is greater than SMALLER_EMPLOYEE_BIOMETRIC_ID
        defaultEmployeeShouldBeFound("employeeBiometricId.greaterThan=" + SMALLER_EMPLOYEE_BIOMETRIC_ID);
    }

    @Test
    @Transactional
    void getAllEmployeesByUsernameIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where username equals to DEFAULT_USERNAME
        defaultEmployeeShouldBeFound("username.equals=" + DEFAULT_USERNAME);

        // Get all the employeeList where username equals to UPDATED_USERNAME
        defaultEmployeeShouldNotBeFound("username.equals=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    void getAllEmployeesByUsernameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where username not equals to DEFAULT_USERNAME
        defaultEmployeeShouldNotBeFound("username.notEquals=" + DEFAULT_USERNAME);

        // Get all the employeeList where username not equals to UPDATED_USERNAME
        defaultEmployeeShouldBeFound("username.notEquals=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    void getAllEmployeesByUsernameIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where username in DEFAULT_USERNAME or UPDATED_USERNAME
        defaultEmployeeShouldBeFound("username.in=" + DEFAULT_USERNAME + "," + UPDATED_USERNAME);

        // Get all the employeeList where username equals to UPDATED_USERNAME
        defaultEmployeeShouldNotBeFound("username.in=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    void getAllEmployeesByUsernameIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where username is not null
        defaultEmployeeShouldBeFound("username.specified=true");

        // Get all the employeeList where username is null
        defaultEmployeeShouldNotBeFound("username.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByUsernameContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where username contains DEFAULT_USERNAME
        defaultEmployeeShouldBeFound("username.contains=" + DEFAULT_USERNAME);

        // Get all the employeeList where username contains UPDATED_USERNAME
        defaultEmployeeShouldNotBeFound("username.contains=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    void getAllEmployeesByUsernameNotContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where username does not contain DEFAULT_USERNAME
        defaultEmployeeShouldNotBeFound("username.doesNotContain=" + DEFAULT_USERNAME);

        // Get all the employeeList where username does not contain UPDATED_USERNAME
        defaultEmployeeShouldBeFound("username.doesNotContain=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    void getAllEmployeesByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where email equals to DEFAULT_EMAIL
        defaultEmployeeShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the employeeList where email equals to UPDATED_EMAIL
        defaultEmployeeShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllEmployeesByEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where email not equals to DEFAULT_EMAIL
        defaultEmployeeShouldNotBeFound("email.notEquals=" + DEFAULT_EMAIL);

        // Get all the employeeList where email not equals to UPDATED_EMAIL
        defaultEmployeeShouldBeFound("email.notEquals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllEmployeesByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultEmployeeShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the employeeList where email equals to UPDATED_EMAIL
        defaultEmployeeShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllEmployeesByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where email is not null
        defaultEmployeeShouldBeFound("email.specified=true");

        // Get all the employeeList where email is null
        defaultEmployeeShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByEmailContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where email contains DEFAULT_EMAIL
        defaultEmployeeShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the employeeList where email contains UPDATED_EMAIL
        defaultEmployeeShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllEmployeesByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where email does not contain DEFAULT_EMAIL
        defaultEmployeeShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the employeeList where email does not contain UPDATED_EMAIL
        defaultEmployeeShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllEmployeesByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where firstName equals to DEFAULT_FIRST_NAME
        defaultEmployeeShouldBeFound("firstName.equals=" + DEFAULT_FIRST_NAME);

        // Get all the employeeList where firstName equals to UPDATED_FIRST_NAME
        defaultEmployeeShouldNotBeFound("firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeesByFirstNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where firstName not equals to DEFAULT_FIRST_NAME
        defaultEmployeeShouldNotBeFound("firstName.notEquals=" + DEFAULT_FIRST_NAME);

        // Get all the employeeList where firstName not equals to UPDATED_FIRST_NAME
        defaultEmployeeShouldBeFound("firstName.notEquals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeesByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where firstName in DEFAULT_FIRST_NAME or UPDATED_FIRST_NAME
        defaultEmployeeShouldBeFound("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME);

        // Get all the employeeList where firstName equals to UPDATED_FIRST_NAME
        defaultEmployeeShouldNotBeFound("firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeesByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where firstName is not null
        defaultEmployeeShouldBeFound("firstName.specified=true");

        // Get all the employeeList where firstName is null
        defaultEmployeeShouldNotBeFound("firstName.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where firstName contains DEFAULT_FIRST_NAME
        defaultEmployeeShouldBeFound("firstName.contains=" + DEFAULT_FIRST_NAME);

        // Get all the employeeList where firstName contains UPDATED_FIRST_NAME
        defaultEmployeeShouldNotBeFound("firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeesByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where firstName does not contain DEFAULT_FIRST_NAME
        defaultEmployeeShouldNotBeFound("firstName.doesNotContain=" + DEFAULT_FIRST_NAME);

        // Get all the employeeList where firstName does not contain UPDATED_FIRST_NAME
        defaultEmployeeShouldBeFound("firstName.doesNotContain=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeesByMiddleNameIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where middleName equals to DEFAULT_MIDDLE_NAME
        defaultEmployeeShouldBeFound("middleName.equals=" + DEFAULT_MIDDLE_NAME);

        // Get all the employeeList where middleName equals to UPDATED_MIDDLE_NAME
        defaultEmployeeShouldNotBeFound("middleName.equals=" + UPDATED_MIDDLE_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeesByMiddleNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where middleName not equals to DEFAULT_MIDDLE_NAME
        defaultEmployeeShouldNotBeFound("middleName.notEquals=" + DEFAULT_MIDDLE_NAME);

        // Get all the employeeList where middleName not equals to UPDATED_MIDDLE_NAME
        defaultEmployeeShouldBeFound("middleName.notEquals=" + UPDATED_MIDDLE_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeesByMiddleNameIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where middleName in DEFAULT_MIDDLE_NAME or UPDATED_MIDDLE_NAME
        defaultEmployeeShouldBeFound("middleName.in=" + DEFAULT_MIDDLE_NAME + "," + UPDATED_MIDDLE_NAME);

        // Get all the employeeList where middleName equals to UPDATED_MIDDLE_NAME
        defaultEmployeeShouldNotBeFound("middleName.in=" + UPDATED_MIDDLE_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeesByMiddleNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where middleName is not null
        defaultEmployeeShouldBeFound("middleName.specified=true");

        // Get all the employeeList where middleName is null
        defaultEmployeeShouldNotBeFound("middleName.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByMiddleNameContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where middleName contains DEFAULT_MIDDLE_NAME
        defaultEmployeeShouldBeFound("middleName.contains=" + DEFAULT_MIDDLE_NAME);

        // Get all the employeeList where middleName contains UPDATED_MIDDLE_NAME
        defaultEmployeeShouldNotBeFound("middleName.contains=" + UPDATED_MIDDLE_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeesByMiddleNameNotContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where middleName does not contain DEFAULT_MIDDLE_NAME
        defaultEmployeeShouldNotBeFound("middleName.doesNotContain=" + DEFAULT_MIDDLE_NAME);

        // Get all the employeeList where middleName does not contain UPDATED_MIDDLE_NAME
        defaultEmployeeShouldBeFound("middleName.doesNotContain=" + UPDATED_MIDDLE_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeesByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where lastName equals to DEFAULT_LAST_NAME
        defaultEmployeeShouldBeFound("lastName.equals=" + DEFAULT_LAST_NAME);

        // Get all the employeeList where lastName equals to UPDATED_LAST_NAME
        defaultEmployeeShouldNotBeFound("lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeesByLastNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where lastName not equals to DEFAULT_LAST_NAME
        defaultEmployeeShouldNotBeFound("lastName.notEquals=" + DEFAULT_LAST_NAME);

        // Get all the employeeList where lastName not equals to UPDATED_LAST_NAME
        defaultEmployeeShouldBeFound("lastName.notEquals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeesByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where lastName in DEFAULT_LAST_NAME or UPDATED_LAST_NAME
        defaultEmployeeShouldBeFound("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME);

        // Get all the employeeList where lastName equals to UPDATED_LAST_NAME
        defaultEmployeeShouldNotBeFound("lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeesByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where lastName is not null
        defaultEmployeeShouldBeFound("lastName.specified=true");

        // Get all the employeeList where lastName is null
        defaultEmployeeShouldNotBeFound("lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByLastNameContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where lastName contains DEFAULT_LAST_NAME
        defaultEmployeeShouldBeFound("lastName.contains=" + DEFAULT_LAST_NAME);

        // Get all the employeeList where lastName contains UPDATED_LAST_NAME
        defaultEmployeeShouldNotBeFound("lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeesByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where lastName does not contain DEFAULT_LAST_NAME
        defaultEmployeeShouldNotBeFound("lastName.doesNotContain=" + DEFAULT_LAST_NAME);

        // Get all the employeeList where lastName does not contain UPDATED_LAST_NAME
        defaultEmployeeShouldBeFound("lastName.doesNotContain=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeesByNameSuffixIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where nameSuffix equals to DEFAULT_NAME_SUFFIX
        defaultEmployeeShouldBeFound("nameSuffix.equals=" + DEFAULT_NAME_SUFFIX);

        // Get all the employeeList where nameSuffix equals to UPDATED_NAME_SUFFIX
        defaultEmployeeShouldNotBeFound("nameSuffix.equals=" + UPDATED_NAME_SUFFIX);
    }

    @Test
    @Transactional
    void getAllEmployeesByNameSuffixIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where nameSuffix not equals to DEFAULT_NAME_SUFFIX
        defaultEmployeeShouldNotBeFound("nameSuffix.notEquals=" + DEFAULT_NAME_SUFFIX);

        // Get all the employeeList where nameSuffix not equals to UPDATED_NAME_SUFFIX
        defaultEmployeeShouldBeFound("nameSuffix.notEquals=" + UPDATED_NAME_SUFFIX);
    }

    @Test
    @Transactional
    void getAllEmployeesByNameSuffixIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where nameSuffix in DEFAULT_NAME_SUFFIX or UPDATED_NAME_SUFFIX
        defaultEmployeeShouldBeFound("nameSuffix.in=" + DEFAULT_NAME_SUFFIX + "," + UPDATED_NAME_SUFFIX);

        // Get all the employeeList where nameSuffix equals to UPDATED_NAME_SUFFIX
        defaultEmployeeShouldNotBeFound("nameSuffix.in=" + UPDATED_NAME_SUFFIX);
    }

    @Test
    @Transactional
    void getAllEmployeesByNameSuffixIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where nameSuffix is not null
        defaultEmployeeShouldBeFound("nameSuffix.specified=true");

        // Get all the employeeList where nameSuffix is null
        defaultEmployeeShouldNotBeFound("nameSuffix.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByNameSuffixContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where nameSuffix contains DEFAULT_NAME_SUFFIX
        defaultEmployeeShouldBeFound("nameSuffix.contains=" + DEFAULT_NAME_SUFFIX);

        // Get all the employeeList where nameSuffix contains UPDATED_NAME_SUFFIX
        defaultEmployeeShouldNotBeFound("nameSuffix.contains=" + UPDATED_NAME_SUFFIX);
    }

    @Test
    @Transactional
    void getAllEmployeesByNameSuffixNotContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where nameSuffix does not contain DEFAULT_NAME_SUFFIX
        defaultEmployeeShouldNotBeFound("nameSuffix.doesNotContain=" + DEFAULT_NAME_SUFFIX);

        // Get all the employeeList where nameSuffix does not contain UPDATED_NAME_SUFFIX
        defaultEmployeeShouldBeFound("nameSuffix.doesNotContain=" + UPDATED_NAME_SUFFIX);
    }

    @Test
    @Transactional
    void getAllEmployeesByBirthdateIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where birthdate equals to DEFAULT_BIRTHDATE
        defaultEmployeeShouldBeFound("birthdate.equals=" + DEFAULT_BIRTHDATE);

        // Get all the employeeList where birthdate equals to UPDATED_BIRTHDATE
        defaultEmployeeShouldNotBeFound("birthdate.equals=" + UPDATED_BIRTHDATE);
    }

    @Test
    @Transactional
    void getAllEmployeesByBirthdateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where birthdate not equals to DEFAULT_BIRTHDATE
        defaultEmployeeShouldNotBeFound("birthdate.notEquals=" + DEFAULT_BIRTHDATE);

        // Get all the employeeList where birthdate not equals to UPDATED_BIRTHDATE
        defaultEmployeeShouldBeFound("birthdate.notEquals=" + UPDATED_BIRTHDATE);
    }

    @Test
    @Transactional
    void getAllEmployeesByBirthdateIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where birthdate in DEFAULT_BIRTHDATE or UPDATED_BIRTHDATE
        defaultEmployeeShouldBeFound("birthdate.in=" + DEFAULT_BIRTHDATE + "," + UPDATED_BIRTHDATE);

        // Get all the employeeList where birthdate equals to UPDATED_BIRTHDATE
        defaultEmployeeShouldNotBeFound("birthdate.in=" + UPDATED_BIRTHDATE);
    }

    @Test
    @Transactional
    void getAllEmployeesByBirthdateIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where birthdate is not null
        defaultEmployeeShouldBeFound("birthdate.specified=true");

        // Get all the employeeList where birthdate is null
        defaultEmployeeShouldNotBeFound("birthdate.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByBirthdateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where birthdate is greater than or equal to DEFAULT_BIRTHDATE
        defaultEmployeeShouldBeFound("birthdate.greaterThanOrEqual=" + DEFAULT_BIRTHDATE);

        // Get all the employeeList where birthdate is greater than or equal to UPDATED_BIRTHDATE
        defaultEmployeeShouldNotBeFound("birthdate.greaterThanOrEqual=" + UPDATED_BIRTHDATE);
    }

    @Test
    @Transactional
    void getAllEmployeesByBirthdateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where birthdate is less than or equal to DEFAULT_BIRTHDATE
        defaultEmployeeShouldBeFound("birthdate.lessThanOrEqual=" + DEFAULT_BIRTHDATE);

        // Get all the employeeList where birthdate is less than or equal to SMALLER_BIRTHDATE
        defaultEmployeeShouldNotBeFound("birthdate.lessThanOrEqual=" + SMALLER_BIRTHDATE);
    }

    @Test
    @Transactional
    void getAllEmployeesByBirthdateIsLessThanSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where birthdate is less than DEFAULT_BIRTHDATE
        defaultEmployeeShouldNotBeFound("birthdate.lessThan=" + DEFAULT_BIRTHDATE);

        // Get all the employeeList where birthdate is less than UPDATED_BIRTHDATE
        defaultEmployeeShouldBeFound("birthdate.lessThan=" + UPDATED_BIRTHDATE);
    }

    @Test
    @Transactional
    void getAllEmployeesByBirthdateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where birthdate is greater than DEFAULT_BIRTHDATE
        defaultEmployeeShouldNotBeFound("birthdate.greaterThan=" + DEFAULT_BIRTHDATE);

        // Get all the employeeList where birthdate is greater than SMALLER_BIRTHDATE
        defaultEmployeeShouldBeFound("birthdate.greaterThan=" + SMALLER_BIRTHDATE);
    }

    @Test
    @Transactional
    void getAllEmployeesByGenderIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where gender equals to DEFAULT_GENDER
        defaultEmployeeShouldBeFound("gender.equals=" + DEFAULT_GENDER);

        // Get all the employeeList where gender equals to UPDATED_GENDER
        defaultEmployeeShouldNotBeFound("gender.equals=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    void getAllEmployeesByGenderIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where gender not equals to DEFAULT_GENDER
        defaultEmployeeShouldNotBeFound("gender.notEquals=" + DEFAULT_GENDER);

        // Get all the employeeList where gender not equals to UPDATED_GENDER
        defaultEmployeeShouldBeFound("gender.notEquals=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    void getAllEmployeesByGenderIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where gender in DEFAULT_GENDER or UPDATED_GENDER
        defaultEmployeeShouldBeFound("gender.in=" + DEFAULT_GENDER + "," + UPDATED_GENDER);

        // Get all the employeeList where gender equals to UPDATED_GENDER
        defaultEmployeeShouldNotBeFound("gender.in=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    void getAllEmployeesByGenderIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where gender is not null
        defaultEmployeeShouldBeFound("gender.specified=true");

        // Get all the employeeList where gender is null
        defaultEmployeeShouldNotBeFound("gender.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where status equals to DEFAULT_STATUS
        defaultEmployeeShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the employeeList where status equals to UPDATED_STATUS
        defaultEmployeeShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllEmployeesByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where status not equals to DEFAULT_STATUS
        defaultEmployeeShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the employeeList where status not equals to UPDATED_STATUS
        defaultEmployeeShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllEmployeesByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultEmployeeShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the employeeList where status equals to UPDATED_STATUS
        defaultEmployeeShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllEmployeesByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where status is not null
        defaultEmployeeShouldBeFound("status.specified=true");

        // Get all the employeeList where status is null
        defaultEmployeeShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByEmploymentTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where employmentType equals to DEFAULT_EMPLOYMENT_TYPE
        defaultEmployeeShouldBeFound("employmentType.equals=" + DEFAULT_EMPLOYMENT_TYPE);

        // Get all the employeeList where employmentType equals to UPDATED_EMPLOYMENT_TYPE
        defaultEmployeeShouldNotBeFound("employmentType.equals=" + UPDATED_EMPLOYMENT_TYPE);
    }

    @Test
    @Transactional
    void getAllEmployeesByEmploymentTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where employmentType not equals to DEFAULT_EMPLOYMENT_TYPE
        defaultEmployeeShouldNotBeFound("employmentType.notEquals=" + DEFAULT_EMPLOYMENT_TYPE);

        // Get all the employeeList where employmentType not equals to UPDATED_EMPLOYMENT_TYPE
        defaultEmployeeShouldBeFound("employmentType.notEquals=" + UPDATED_EMPLOYMENT_TYPE);
    }

    @Test
    @Transactional
    void getAllEmployeesByEmploymentTypeIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where employmentType in DEFAULT_EMPLOYMENT_TYPE or UPDATED_EMPLOYMENT_TYPE
        defaultEmployeeShouldBeFound("employmentType.in=" + DEFAULT_EMPLOYMENT_TYPE + "," + UPDATED_EMPLOYMENT_TYPE);

        // Get all the employeeList where employmentType equals to UPDATED_EMPLOYMENT_TYPE
        defaultEmployeeShouldNotBeFound("employmentType.in=" + UPDATED_EMPLOYMENT_TYPE);
    }

    @Test
    @Transactional
    void getAllEmployeesByEmploymentTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where employmentType is not null
        defaultEmployeeShouldBeFound("employmentType.specified=true");

        // Get all the employeeList where employmentType is null
        defaultEmployeeShouldNotBeFound("employmentType.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByMobileNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where mobileNumber equals to DEFAULT_MOBILE_NUMBER
        defaultEmployeeShouldBeFound("mobileNumber.equals=" + DEFAULT_MOBILE_NUMBER);

        // Get all the employeeList where mobileNumber equals to UPDATED_MOBILE_NUMBER
        defaultEmployeeShouldNotBeFound("mobileNumber.equals=" + UPDATED_MOBILE_NUMBER);
    }

    @Test
    @Transactional
    void getAllEmployeesByMobileNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where mobileNumber not equals to DEFAULT_MOBILE_NUMBER
        defaultEmployeeShouldNotBeFound("mobileNumber.notEquals=" + DEFAULT_MOBILE_NUMBER);

        // Get all the employeeList where mobileNumber not equals to UPDATED_MOBILE_NUMBER
        defaultEmployeeShouldBeFound("mobileNumber.notEquals=" + UPDATED_MOBILE_NUMBER);
    }

    @Test
    @Transactional
    void getAllEmployeesByMobileNumberIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where mobileNumber in DEFAULT_MOBILE_NUMBER or UPDATED_MOBILE_NUMBER
        defaultEmployeeShouldBeFound("mobileNumber.in=" + DEFAULT_MOBILE_NUMBER + "," + UPDATED_MOBILE_NUMBER);

        // Get all the employeeList where mobileNumber equals to UPDATED_MOBILE_NUMBER
        defaultEmployeeShouldNotBeFound("mobileNumber.in=" + UPDATED_MOBILE_NUMBER);
    }

    @Test
    @Transactional
    void getAllEmployeesByMobileNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where mobileNumber is not null
        defaultEmployeeShouldBeFound("mobileNumber.specified=true");

        // Get all the employeeList where mobileNumber is null
        defaultEmployeeShouldNotBeFound("mobileNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByMobileNumberContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where mobileNumber contains DEFAULT_MOBILE_NUMBER
        defaultEmployeeShouldBeFound("mobileNumber.contains=" + DEFAULT_MOBILE_NUMBER);

        // Get all the employeeList where mobileNumber contains UPDATED_MOBILE_NUMBER
        defaultEmployeeShouldNotBeFound("mobileNumber.contains=" + UPDATED_MOBILE_NUMBER);
    }

    @Test
    @Transactional
    void getAllEmployeesByMobileNumberNotContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where mobileNumber does not contain DEFAULT_MOBILE_NUMBER
        defaultEmployeeShouldNotBeFound("mobileNumber.doesNotContain=" + DEFAULT_MOBILE_NUMBER);

        // Get all the employeeList where mobileNumber does not contain UPDATED_MOBILE_NUMBER
        defaultEmployeeShouldBeFound("mobileNumber.doesNotContain=" + UPDATED_MOBILE_NUMBER);
    }

    @Test
    @Transactional
    void getAllEmployeesByDateHiredIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where dateHired equals to DEFAULT_DATE_HIRED
        defaultEmployeeShouldBeFound("dateHired.equals=" + DEFAULT_DATE_HIRED);

        // Get all the employeeList where dateHired equals to UPDATED_DATE_HIRED
        defaultEmployeeShouldNotBeFound("dateHired.equals=" + UPDATED_DATE_HIRED);
    }

    @Test
    @Transactional
    void getAllEmployeesByDateHiredIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where dateHired not equals to DEFAULT_DATE_HIRED
        defaultEmployeeShouldNotBeFound("dateHired.notEquals=" + DEFAULT_DATE_HIRED);

        // Get all the employeeList where dateHired not equals to UPDATED_DATE_HIRED
        defaultEmployeeShouldBeFound("dateHired.notEquals=" + UPDATED_DATE_HIRED);
    }

    @Test
    @Transactional
    void getAllEmployeesByDateHiredIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where dateHired in DEFAULT_DATE_HIRED or UPDATED_DATE_HIRED
        defaultEmployeeShouldBeFound("dateHired.in=" + DEFAULT_DATE_HIRED + "," + UPDATED_DATE_HIRED);

        // Get all the employeeList where dateHired equals to UPDATED_DATE_HIRED
        defaultEmployeeShouldNotBeFound("dateHired.in=" + UPDATED_DATE_HIRED);
    }

    @Test
    @Transactional
    void getAllEmployeesByDateHiredIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where dateHired is not null
        defaultEmployeeShouldBeFound("dateHired.specified=true");

        // Get all the employeeList where dateHired is null
        defaultEmployeeShouldNotBeFound("dateHired.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByDateHiredIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where dateHired is greater than or equal to DEFAULT_DATE_HIRED
        defaultEmployeeShouldBeFound("dateHired.greaterThanOrEqual=" + DEFAULT_DATE_HIRED);

        // Get all the employeeList where dateHired is greater than or equal to UPDATED_DATE_HIRED
        defaultEmployeeShouldNotBeFound("dateHired.greaterThanOrEqual=" + UPDATED_DATE_HIRED);
    }

    @Test
    @Transactional
    void getAllEmployeesByDateHiredIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where dateHired is less than or equal to DEFAULT_DATE_HIRED
        defaultEmployeeShouldBeFound("dateHired.lessThanOrEqual=" + DEFAULT_DATE_HIRED);

        // Get all the employeeList where dateHired is less than or equal to SMALLER_DATE_HIRED
        defaultEmployeeShouldNotBeFound("dateHired.lessThanOrEqual=" + SMALLER_DATE_HIRED);
    }

    @Test
    @Transactional
    void getAllEmployeesByDateHiredIsLessThanSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where dateHired is less than DEFAULT_DATE_HIRED
        defaultEmployeeShouldNotBeFound("dateHired.lessThan=" + DEFAULT_DATE_HIRED);

        // Get all the employeeList where dateHired is less than UPDATED_DATE_HIRED
        defaultEmployeeShouldBeFound("dateHired.lessThan=" + UPDATED_DATE_HIRED);
    }

    @Test
    @Transactional
    void getAllEmployeesByDateHiredIsGreaterThanSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where dateHired is greater than DEFAULT_DATE_HIRED
        defaultEmployeeShouldNotBeFound("dateHired.greaterThan=" + DEFAULT_DATE_HIRED);

        // Get all the employeeList where dateHired is greater than SMALLER_DATE_HIRED
        defaultEmployeeShouldBeFound("dateHired.greaterThan=" + SMALLER_DATE_HIRED);
    }

    @Test
    @Transactional
    void getAllEmployeesByDateDenoIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where dateDeno equals to DEFAULT_DATE_DENO
        defaultEmployeeShouldBeFound("dateDeno.equals=" + DEFAULT_DATE_DENO);

        // Get all the employeeList where dateDeno equals to UPDATED_DATE_DENO
        defaultEmployeeShouldNotBeFound("dateDeno.equals=" + UPDATED_DATE_DENO);
    }

    @Test
    @Transactional
    void getAllEmployeesByDateDenoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where dateDeno not equals to DEFAULT_DATE_DENO
        defaultEmployeeShouldNotBeFound("dateDeno.notEquals=" + DEFAULT_DATE_DENO);

        // Get all the employeeList where dateDeno not equals to UPDATED_DATE_DENO
        defaultEmployeeShouldBeFound("dateDeno.notEquals=" + UPDATED_DATE_DENO);
    }

    @Test
    @Transactional
    void getAllEmployeesByDateDenoIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where dateDeno in DEFAULT_DATE_DENO or UPDATED_DATE_DENO
        defaultEmployeeShouldBeFound("dateDeno.in=" + DEFAULT_DATE_DENO + "," + UPDATED_DATE_DENO);

        // Get all the employeeList where dateDeno equals to UPDATED_DATE_DENO
        defaultEmployeeShouldNotBeFound("dateDeno.in=" + UPDATED_DATE_DENO);
    }

    @Test
    @Transactional
    void getAllEmployeesByDateDenoIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where dateDeno is not null
        defaultEmployeeShouldBeFound("dateDeno.specified=true");

        // Get all the employeeList where dateDeno is null
        defaultEmployeeShouldNotBeFound("dateDeno.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByDateDenoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where dateDeno is greater than or equal to DEFAULT_DATE_DENO
        defaultEmployeeShouldBeFound("dateDeno.greaterThanOrEqual=" + DEFAULT_DATE_DENO);

        // Get all the employeeList where dateDeno is greater than or equal to UPDATED_DATE_DENO
        defaultEmployeeShouldNotBeFound("dateDeno.greaterThanOrEqual=" + UPDATED_DATE_DENO);
    }

    @Test
    @Transactional
    void getAllEmployeesByDateDenoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where dateDeno is less than or equal to DEFAULT_DATE_DENO
        defaultEmployeeShouldBeFound("dateDeno.lessThanOrEqual=" + DEFAULT_DATE_DENO);

        // Get all the employeeList where dateDeno is less than or equal to SMALLER_DATE_DENO
        defaultEmployeeShouldNotBeFound("dateDeno.lessThanOrEqual=" + SMALLER_DATE_DENO);
    }

    @Test
    @Transactional
    void getAllEmployeesByDateDenoIsLessThanSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where dateDeno is less than DEFAULT_DATE_DENO
        defaultEmployeeShouldNotBeFound("dateDeno.lessThan=" + DEFAULT_DATE_DENO);

        // Get all the employeeList where dateDeno is less than UPDATED_DATE_DENO
        defaultEmployeeShouldBeFound("dateDeno.lessThan=" + UPDATED_DATE_DENO);
    }

    @Test
    @Transactional
    void getAllEmployeesByDateDenoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where dateDeno is greater than DEFAULT_DATE_DENO
        defaultEmployeeShouldNotBeFound("dateDeno.greaterThan=" + DEFAULT_DATE_DENO);

        // Get all the employeeList where dateDeno is greater than SMALLER_DATE_DENO
        defaultEmployeeShouldBeFound("dateDeno.greaterThan=" + SMALLER_DATE_DENO);
    }

    @Test
    @Transactional
    void getAllEmployeesBySickLeaveYearlyCreditIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where sickLeaveYearlyCredit equals to DEFAULT_SICK_LEAVE_YEARLY_CREDIT
        defaultEmployeeShouldBeFound("sickLeaveYearlyCredit.equals=" + DEFAULT_SICK_LEAVE_YEARLY_CREDIT);

        // Get all the employeeList where sickLeaveYearlyCredit equals to UPDATED_SICK_LEAVE_YEARLY_CREDIT
        defaultEmployeeShouldNotBeFound("sickLeaveYearlyCredit.equals=" + UPDATED_SICK_LEAVE_YEARLY_CREDIT);
    }

    @Test
    @Transactional
    void getAllEmployeesBySickLeaveYearlyCreditIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where sickLeaveYearlyCredit not equals to DEFAULT_SICK_LEAVE_YEARLY_CREDIT
        defaultEmployeeShouldNotBeFound("sickLeaveYearlyCredit.notEquals=" + DEFAULT_SICK_LEAVE_YEARLY_CREDIT);

        // Get all the employeeList where sickLeaveYearlyCredit not equals to UPDATED_SICK_LEAVE_YEARLY_CREDIT
        defaultEmployeeShouldBeFound("sickLeaveYearlyCredit.notEquals=" + UPDATED_SICK_LEAVE_YEARLY_CREDIT);
    }

    @Test
    @Transactional
    void getAllEmployeesBySickLeaveYearlyCreditIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where sickLeaveYearlyCredit in DEFAULT_SICK_LEAVE_YEARLY_CREDIT or UPDATED_SICK_LEAVE_YEARLY_CREDIT
        defaultEmployeeShouldBeFound(
            "sickLeaveYearlyCredit.in=" + DEFAULT_SICK_LEAVE_YEARLY_CREDIT + "," + UPDATED_SICK_LEAVE_YEARLY_CREDIT
        );

        // Get all the employeeList where sickLeaveYearlyCredit equals to UPDATED_SICK_LEAVE_YEARLY_CREDIT
        defaultEmployeeShouldNotBeFound("sickLeaveYearlyCredit.in=" + UPDATED_SICK_LEAVE_YEARLY_CREDIT);
    }

    @Test
    @Transactional
    void getAllEmployeesBySickLeaveYearlyCreditIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where sickLeaveYearlyCredit is not null
        defaultEmployeeShouldBeFound("sickLeaveYearlyCredit.specified=true");

        // Get all the employeeList where sickLeaveYearlyCredit is null
        defaultEmployeeShouldNotBeFound("sickLeaveYearlyCredit.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesBySickLeaveYearlyCreditIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where sickLeaveYearlyCredit is greater than or equal to DEFAULT_SICK_LEAVE_YEARLY_CREDIT
        defaultEmployeeShouldBeFound("sickLeaveYearlyCredit.greaterThanOrEqual=" + DEFAULT_SICK_LEAVE_YEARLY_CREDIT);

        // Get all the employeeList where sickLeaveYearlyCredit is greater than or equal to UPDATED_SICK_LEAVE_YEARLY_CREDIT
        defaultEmployeeShouldNotBeFound("sickLeaveYearlyCredit.greaterThanOrEqual=" + UPDATED_SICK_LEAVE_YEARLY_CREDIT);
    }

    @Test
    @Transactional
    void getAllEmployeesBySickLeaveYearlyCreditIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where sickLeaveYearlyCredit is less than or equal to DEFAULT_SICK_LEAVE_YEARLY_CREDIT
        defaultEmployeeShouldBeFound("sickLeaveYearlyCredit.lessThanOrEqual=" + DEFAULT_SICK_LEAVE_YEARLY_CREDIT);

        // Get all the employeeList where sickLeaveYearlyCredit is less than or equal to SMALLER_SICK_LEAVE_YEARLY_CREDIT
        defaultEmployeeShouldNotBeFound("sickLeaveYearlyCredit.lessThanOrEqual=" + SMALLER_SICK_LEAVE_YEARLY_CREDIT);
    }

    @Test
    @Transactional
    void getAllEmployeesBySickLeaveYearlyCreditIsLessThanSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where sickLeaveYearlyCredit is less than DEFAULT_SICK_LEAVE_YEARLY_CREDIT
        defaultEmployeeShouldNotBeFound("sickLeaveYearlyCredit.lessThan=" + DEFAULT_SICK_LEAVE_YEARLY_CREDIT);

        // Get all the employeeList where sickLeaveYearlyCredit is less than UPDATED_SICK_LEAVE_YEARLY_CREDIT
        defaultEmployeeShouldBeFound("sickLeaveYearlyCredit.lessThan=" + UPDATED_SICK_LEAVE_YEARLY_CREDIT);
    }

    @Test
    @Transactional
    void getAllEmployeesBySickLeaveYearlyCreditIsGreaterThanSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where sickLeaveYearlyCredit is greater than DEFAULT_SICK_LEAVE_YEARLY_CREDIT
        defaultEmployeeShouldNotBeFound("sickLeaveYearlyCredit.greaterThan=" + DEFAULT_SICK_LEAVE_YEARLY_CREDIT);

        // Get all the employeeList where sickLeaveYearlyCredit is greater than SMALLER_SICK_LEAVE_YEARLY_CREDIT
        defaultEmployeeShouldBeFound("sickLeaveYearlyCredit.greaterThan=" + SMALLER_SICK_LEAVE_YEARLY_CREDIT);
    }

    @Test
    @Transactional
    void getAllEmployeesBySickLeaveYearlyCreditUsedIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where sickLeaveYearlyCreditUsed equals to DEFAULT_SICK_LEAVE_YEARLY_CREDIT_USED
        defaultEmployeeShouldBeFound("sickLeaveYearlyCreditUsed.equals=" + DEFAULT_SICK_LEAVE_YEARLY_CREDIT_USED);

        // Get all the employeeList where sickLeaveYearlyCreditUsed equals to UPDATED_SICK_LEAVE_YEARLY_CREDIT_USED
        defaultEmployeeShouldNotBeFound("sickLeaveYearlyCreditUsed.equals=" + UPDATED_SICK_LEAVE_YEARLY_CREDIT_USED);
    }

    @Test
    @Transactional
    void getAllEmployeesBySickLeaveYearlyCreditUsedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where sickLeaveYearlyCreditUsed not equals to DEFAULT_SICK_LEAVE_YEARLY_CREDIT_USED
        defaultEmployeeShouldNotBeFound("sickLeaveYearlyCreditUsed.notEquals=" + DEFAULT_SICK_LEAVE_YEARLY_CREDIT_USED);

        // Get all the employeeList where sickLeaveYearlyCreditUsed not equals to UPDATED_SICK_LEAVE_YEARLY_CREDIT_USED
        defaultEmployeeShouldBeFound("sickLeaveYearlyCreditUsed.notEquals=" + UPDATED_SICK_LEAVE_YEARLY_CREDIT_USED);
    }

    @Test
    @Transactional
    void getAllEmployeesBySickLeaveYearlyCreditUsedIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where sickLeaveYearlyCreditUsed in DEFAULT_SICK_LEAVE_YEARLY_CREDIT_USED or UPDATED_SICK_LEAVE_YEARLY_CREDIT_USED
        defaultEmployeeShouldBeFound(
            "sickLeaveYearlyCreditUsed.in=" + DEFAULT_SICK_LEAVE_YEARLY_CREDIT_USED + "," + UPDATED_SICK_LEAVE_YEARLY_CREDIT_USED
        );

        // Get all the employeeList where sickLeaveYearlyCreditUsed equals to UPDATED_SICK_LEAVE_YEARLY_CREDIT_USED
        defaultEmployeeShouldNotBeFound("sickLeaveYearlyCreditUsed.in=" + UPDATED_SICK_LEAVE_YEARLY_CREDIT_USED);
    }

    @Test
    @Transactional
    void getAllEmployeesBySickLeaveYearlyCreditUsedIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where sickLeaveYearlyCreditUsed is not null
        defaultEmployeeShouldBeFound("sickLeaveYearlyCreditUsed.specified=true");

        // Get all the employeeList where sickLeaveYearlyCreditUsed is null
        defaultEmployeeShouldNotBeFound("sickLeaveYearlyCreditUsed.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesBySickLeaveYearlyCreditUsedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where sickLeaveYearlyCreditUsed is greater than or equal to DEFAULT_SICK_LEAVE_YEARLY_CREDIT_USED
        defaultEmployeeShouldBeFound("sickLeaveYearlyCreditUsed.greaterThanOrEqual=" + DEFAULT_SICK_LEAVE_YEARLY_CREDIT_USED);

        // Get all the employeeList where sickLeaveYearlyCreditUsed is greater than or equal to UPDATED_SICK_LEAVE_YEARLY_CREDIT_USED
        defaultEmployeeShouldNotBeFound("sickLeaveYearlyCreditUsed.greaterThanOrEqual=" + UPDATED_SICK_LEAVE_YEARLY_CREDIT_USED);
    }

    @Test
    @Transactional
    void getAllEmployeesBySickLeaveYearlyCreditUsedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where sickLeaveYearlyCreditUsed is less than or equal to DEFAULT_SICK_LEAVE_YEARLY_CREDIT_USED
        defaultEmployeeShouldBeFound("sickLeaveYearlyCreditUsed.lessThanOrEqual=" + DEFAULT_SICK_LEAVE_YEARLY_CREDIT_USED);

        // Get all the employeeList where sickLeaveYearlyCreditUsed is less than or equal to SMALLER_SICK_LEAVE_YEARLY_CREDIT_USED
        defaultEmployeeShouldNotBeFound("sickLeaveYearlyCreditUsed.lessThanOrEqual=" + SMALLER_SICK_LEAVE_YEARLY_CREDIT_USED);
    }

    @Test
    @Transactional
    void getAllEmployeesBySickLeaveYearlyCreditUsedIsLessThanSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where sickLeaveYearlyCreditUsed is less than DEFAULT_SICK_LEAVE_YEARLY_CREDIT_USED
        defaultEmployeeShouldNotBeFound("sickLeaveYearlyCreditUsed.lessThan=" + DEFAULT_SICK_LEAVE_YEARLY_CREDIT_USED);

        // Get all the employeeList where sickLeaveYearlyCreditUsed is less than UPDATED_SICK_LEAVE_YEARLY_CREDIT_USED
        defaultEmployeeShouldBeFound("sickLeaveYearlyCreditUsed.lessThan=" + UPDATED_SICK_LEAVE_YEARLY_CREDIT_USED);
    }

    @Test
    @Transactional
    void getAllEmployeesBySickLeaveYearlyCreditUsedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where sickLeaveYearlyCreditUsed is greater than DEFAULT_SICK_LEAVE_YEARLY_CREDIT_USED
        defaultEmployeeShouldNotBeFound("sickLeaveYearlyCreditUsed.greaterThan=" + DEFAULT_SICK_LEAVE_YEARLY_CREDIT_USED);

        // Get all the employeeList where sickLeaveYearlyCreditUsed is greater than SMALLER_SICK_LEAVE_YEARLY_CREDIT_USED
        defaultEmployeeShouldBeFound("sickLeaveYearlyCreditUsed.greaterThan=" + SMALLER_SICK_LEAVE_YEARLY_CREDIT_USED);
    }

    @Test
    @Transactional
    void getAllEmployeesByLeaveYearlyCreditIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where leaveYearlyCredit equals to DEFAULT_LEAVE_YEARLY_CREDIT
        defaultEmployeeShouldBeFound("leaveYearlyCredit.equals=" + DEFAULT_LEAVE_YEARLY_CREDIT);

        // Get all the employeeList where leaveYearlyCredit equals to UPDATED_LEAVE_YEARLY_CREDIT
        defaultEmployeeShouldNotBeFound("leaveYearlyCredit.equals=" + UPDATED_LEAVE_YEARLY_CREDIT);
    }

    @Test
    @Transactional
    void getAllEmployeesByLeaveYearlyCreditIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where leaveYearlyCredit not equals to DEFAULT_LEAVE_YEARLY_CREDIT
        defaultEmployeeShouldNotBeFound("leaveYearlyCredit.notEquals=" + DEFAULT_LEAVE_YEARLY_CREDIT);

        // Get all the employeeList where leaveYearlyCredit not equals to UPDATED_LEAVE_YEARLY_CREDIT
        defaultEmployeeShouldBeFound("leaveYearlyCredit.notEquals=" + UPDATED_LEAVE_YEARLY_CREDIT);
    }

    @Test
    @Transactional
    void getAllEmployeesByLeaveYearlyCreditIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where leaveYearlyCredit in DEFAULT_LEAVE_YEARLY_CREDIT or UPDATED_LEAVE_YEARLY_CREDIT
        defaultEmployeeShouldBeFound("leaveYearlyCredit.in=" + DEFAULT_LEAVE_YEARLY_CREDIT + "," + UPDATED_LEAVE_YEARLY_CREDIT);

        // Get all the employeeList where leaveYearlyCredit equals to UPDATED_LEAVE_YEARLY_CREDIT
        defaultEmployeeShouldNotBeFound("leaveYearlyCredit.in=" + UPDATED_LEAVE_YEARLY_CREDIT);
    }

    @Test
    @Transactional
    void getAllEmployeesByLeaveYearlyCreditIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where leaveYearlyCredit is not null
        defaultEmployeeShouldBeFound("leaveYearlyCredit.specified=true");

        // Get all the employeeList where leaveYearlyCredit is null
        defaultEmployeeShouldNotBeFound("leaveYearlyCredit.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByLeaveYearlyCreditIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where leaveYearlyCredit is greater than or equal to DEFAULT_LEAVE_YEARLY_CREDIT
        defaultEmployeeShouldBeFound("leaveYearlyCredit.greaterThanOrEqual=" + DEFAULT_LEAVE_YEARLY_CREDIT);

        // Get all the employeeList where leaveYearlyCredit is greater than or equal to UPDATED_LEAVE_YEARLY_CREDIT
        defaultEmployeeShouldNotBeFound("leaveYearlyCredit.greaterThanOrEqual=" + UPDATED_LEAVE_YEARLY_CREDIT);
    }

    @Test
    @Transactional
    void getAllEmployeesByLeaveYearlyCreditIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where leaveYearlyCredit is less than or equal to DEFAULT_LEAVE_YEARLY_CREDIT
        defaultEmployeeShouldBeFound("leaveYearlyCredit.lessThanOrEqual=" + DEFAULT_LEAVE_YEARLY_CREDIT);

        // Get all the employeeList where leaveYearlyCredit is less than or equal to SMALLER_LEAVE_YEARLY_CREDIT
        defaultEmployeeShouldNotBeFound("leaveYearlyCredit.lessThanOrEqual=" + SMALLER_LEAVE_YEARLY_CREDIT);
    }

    @Test
    @Transactional
    void getAllEmployeesByLeaveYearlyCreditIsLessThanSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where leaveYearlyCredit is less than DEFAULT_LEAVE_YEARLY_CREDIT
        defaultEmployeeShouldNotBeFound("leaveYearlyCredit.lessThan=" + DEFAULT_LEAVE_YEARLY_CREDIT);

        // Get all the employeeList where leaveYearlyCredit is less than UPDATED_LEAVE_YEARLY_CREDIT
        defaultEmployeeShouldBeFound("leaveYearlyCredit.lessThan=" + UPDATED_LEAVE_YEARLY_CREDIT);
    }

    @Test
    @Transactional
    void getAllEmployeesByLeaveYearlyCreditIsGreaterThanSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where leaveYearlyCredit is greater than DEFAULT_LEAVE_YEARLY_CREDIT
        defaultEmployeeShouldNotBeFound("leaveYearlyCredit.greaterThan=" + DEFAULT_LEAVE_YEARLY_CREDIT);

        // Get all the employeeList where leaveYearlyCredit is greater than SMALLER_LEAVE_YEARLY_CREDIT
        defaultEmployeeShouldBeFound("leaveYearlyCredit.greaterThan=" + SMALLER_LEAVE_YEARLY_CREDIT);
    }

    @Test
    @Transactional
    void getAllEmployeesByLeaveYearlyCreditUsedIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where leaveYearlyCreditUsed equals to DEFAULT_LEAVE_YEARLY_CREDIT_USED
        defaultEmployeeShouldBeFound("leaveYearlyCreditUsed.equals=" + DEFAULT_LEAVE_YEARLY_CREDIT_USED);

        // Get all the employeeList where leaveYearlyCreditUsed equals to UPDATED_LEAVE_YEARLY_CREDIT_USED
        defaultEmployeeShouldNotBeFound("leaveYearlyCreditUsed.equals=" + UPDATED_LEAVE_YEARLY_CREDIT_USED);
    }

    @Test
    @Transactional
    void getAllEmployeesByLeaveYearlyCreditUsedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where leaveYearlyCreditUsed not equals to DEFAULT_LEAVE_YEARLY_CREDIT_USED
        defaultEmployeeShouldNotBeFound("leaveYearlyCreditUsed.notEquals=" + DEFAULT_LEAVE_YEARLY_CREDIT_USED);

        // Get all the employeeList where leaveYearlyCreditUsed not equals to UPDATED_LEAVE_YEARLY_CREDIT_USED
        defaultEmployeeShouldBeFound("leaveYearlyCreditUsed.notEquals=" + UPDATED_LEAVE_YEARLY_CREDIT_USED);
    }

    @Test
    @Transactional
    void getAllEmployeesByLeaveYearlyCreditUsedIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where leaveYearlyCreditUsed in DEFAULT_LEAVE_YEARLY_CREDIT_USED or UPDATED_LEAVE_YEARLY_CREDIT_USED
        defaultEmployeeShouldBeFound(
            "leaveYearlyCreditUsed.in=" + DEFAULT_LEAVE_YEARLY_CREDIT_USED + "," + UPDATED_LEAVE_YEARLY_CREDIT_USED
        );

        // Get all the employeeList where leaveYearlyCreditUsed equals to UPDATED_LEAVE_YEARLY_CREDIT_USED
        defaultEmployeeShouldNotBeFound("leaveYearlyCreditUsed.in=" + UPDATED_LEAVE_YEARLY_CREDIT_USED);
    }

    @Test
    @Transactional
    void getAllEmployeesByLeaveYearlyCreditUsedIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where leaveYearlyCreditUsed is not null
        defaultEmployeeShouldBeFound("leaveYearlyCreditUsed.specified=true");

        // Get all the employeeList where leaveYearlyCreditUsed is null
        defaultEmployeeShouldNotBeFound("leaveYearlyCreditUsed.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByLeaveYearlyCreditUsedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where leaveYearlyCreditUsed is greater than or equal to DEFAULT_LEAVE_YEARLY_CREDIT_USED
        defaultEmployeeShouldBeFound("leaveYearlyCreditUsed.greaterThanOrEqual=" + DEFAULT_LEAVE_YEARLY_CREDIT_USED);

        // Get all the employeeList where leaveYearlyCreditUsed is greater than or equal to UPDATED_LEAVE_YEARLY_CREDIT_USED
        defaultEmployeeShouldNotBeFound("leaveYearlyCreditUsed.greaterThanOrEqual=" + UPDATED_LEAVE_YEARLY_CREDIT_USED);
    }

    @Test
    @Transactional
    void getAllEmployeesByLeaveYearlyCreditUsedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where leaveYearlyCreditUsed is less than or equal to DEFAULT_LEAVE_YEARLY_CREDIT_USED
        defaultEmployeeShouldBeFound("leaveYearlyCreditUsed.lessThanOrEqual=" + DEFAULT_LEAVE_YEARLY_CREDIT_USED);

        // Get all the employeeList where leaveYearlyCreditUsed is less than or equal to SMALLER_LEAVE_YEARLY_CREDIT_USED
        defaultEmployeeShouldNotBeFound("leaveYearlyCreditUsed.lessThanOrEqual=" + SMALLER_LEAVE_YEARLY_CREDIT_USED);
    }

    @Test
    @Transactional
    void getAllEmployeesByLeaveYearlyCreditUsedIsLessThanSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where leaveYearlyCreditUsed is less than DEFAULT_LEAVE_YEARLY_CREDIT_USED
        defaultEmployeeShouldNotBeFound("leaveYearlyCreditUsed.lessThan=" + DEFAULT_LEAVE_YEARLY_CREDIT_USED);

        // Get all the employeeList where leaveYearlyCreditUsed is less than UPDATED_LEAVE_YEARLY_CREDIT_USED
        defaultEmployeeShouldBeFound("leaveYearlyCreditUsed.lessThan=" + UPDATED_LEAVE_YEARLY_CREDIT_USED);
    }

    @Test
    @Transactional
    void getAllEmployeesByLeaveYearlyCreditUsedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where leaveYearlyCreditUsed is greater than DEFAULT_LEAVE_YEARLY_CREDIT_USED
        defaultEmployeeShouldNotBeFound("leaveYearlyCreditUsed.greaterThan=" + DEFAULT_LEAVE_YEARLY_CREDIT_USED);

        // Get all the employeeList where leaveYearlyCreditUsed is greater than SMALLER_LEAVE_YEARLY_CREDIT_USED
        defaultEmployeeShouldBeFound("leaveYearlyCreditUsed.greaterThan=" + SMALLER_LEAVE_YEARLY_CREDIT_USED);
    }

    @Test
    @Transactional
    void getAllEmployeesByPresentAddressStreetIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where presentAddressStreet equals to DEFAULT_PRESENT_ADDRESS_STREET
        defaultEmployeeShouldBeFound("presentAddressStreet.equals=" + DEFAULT_PRESENT_ADDRESS_STREET);

        // Get all the employeeList where presentAddressStreet equals to UPDATED_PRESENT_ADDRESS_STREET
        defaultEmployeeShouldNotBeFound("presentAddressStreet.equals=" + UPDATED_PRESENT_ADDRESS_STREET);
    }

    @Test
    @Transactional
    void getAllEmployeesByPresentAddressStreetIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where presentAddressStreet not equals to DEFAULT_PRESENT_ADDRESS_STREET
        defaultEmployeeShouldNotBeFound("presentAddressStreet.notEquals=" + DEFAULT_PRESENT_ADDRESS_STREET);

        // Get all the employeeList where presentAddressStreet not equals to UPDATED_PRESENT_ADDRESS_STREET
        defaultEmployeeShouldBeFound("presentAddressStreet.notEquals=" + UPDATED_PRESENT_ADDRESS_STREET);
    }

    @Test
    @Transactional
    void getAllEmployeesByPresentAddressStreetIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where presentAddressStreet in DEFAULT_PRESENT_ADDRESS_STREET or UPDATED_PRESENT_ADDRESS_STREET
        defaultEmployeeShouldBeFound("presentAddressStreet.in=" + DEFAULT_PRESENT_ADDRESS_STREET + "," + UPDATED_PRESENT_ADDRESS_STREET);

        // Get all the employeeList where presentAddressStreet equals to UPDATED_PRESENT_ADDRESS_STREET
        defaultEmployeeShouldNotBeFound("presentAddressStreet.in=" + UPDATED_PRESENT_ADDRESS_STREET);
    }

    @Test
    @Transactional
    void getAllEmployeesByPresentAddressStreetIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where presentAddressStreet is not null
        defaultEmployeeShouldBeFound("presentAddressStreet.specified=true");

        // Get all the employeeList where presentAddressStreet is null
        defaultEmployeeShouldNotBeFound("presentAddressStreet.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByPresentAddressStreetContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where presentAddressStreet contains DEFAULT_PRESENT_ADDRESS_STREET
        defaultEmployeeShouldBeFound("presentAddressStreet.contains=" + DEFAULT_PRESENT_ADDRESS_STREET);

        // Get all the employeeList where presentAddressStreet contains UPDATED_PRESENT_ADDRESS_STREET
        defaultEmployeeShouldNotBeFound("presentAddressStreet.contains=" + UPDATED_PRESENT_ADDRESS_STREET);
    }

    @Test
    @Transactional
    void getAllEmployeesByPresentAddressStreetNotContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where presentAddressStreet does not contain DEFAULT_PRESENT_ADDRESS_STREET
        defaultEmployeeShouldNotBeFound("presentAddressStreet.doesNotContain=" + DEFAULT_PRESENT_ADDRESS_STREET);

        // Get all the employeeList where presentAddressStreet does not contain UPDATED_PRESENT_ADDRESS_STREET
        defaultEmployeeShouldBeFound("presentAddressStreet.doesNotContain=" + UPDATED_PRESENT_ADDRESS_STREET);
    }

    @Test
    @Transactional
    void getAllEmployeesByPresentAddressCityIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where presentAddressCity equals to DEFAULT_PRESENT_ADDRESS_CITY
        defaultEmployeeShouldBeFound("presentAddressCity.equals=" + DEFAULT_PRESENT_ADDRESS_CITY);

        // Get all the employeeList where presentAddressCity equals to UPDATED_PRESENT_ADDRESS_CITY
        defaultEmployeeShouldNotBeFound("presentAddressCity.equals=" + UPDATED_PRESENT_ADDRESS_CITY);
    }

    @Test
    @Transactional
    void getAllEmployeesByPresentAddressCityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where presentAddressCity not equals to DEFAULT_PRESENT_ADDRESS_CITY
        defaultEmployeeShouldNotBeFound("presentAddressCity.notEquals=" + DEFAULT_PRESENT_ADDRESS_CITY);

        // Get all the employeeList where presentAddressCity not equals to UPDATED_PRESENT_ADDRESS_CITY
        defaultEmployeeShouldBeFound("presentAddressCity.notEquals=" + UPDATED_PRESENT_ADDRESS_CITY);
    }

    @Test
    @Transactional
    void getAllEmployeesByPresentAddressCityIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where presentAddressCity in DEFAULT_PRESENT_ADDRESS_CITY or UPDATED_PRESENT_ADDRESS_CITY
        defaultEmployeeShouldBeFound("presentAddressCity.in=" + DEFAULT_PRESENT_ADDRESS_CITY + "," + UPDATED_PRESENT_ADDRESS_CITY);

        // Get all the employeeList where presentAddressCity equals to UPDATED_PRESENT_ADDRESS_CITY
        defaultEmployeeShouldNotBeFound("presentAddressCity.in=" + UPDATED_PRESENT_ADDRESS_CITY);
    }

    @Test
    @Transactional
    void getAllEmployeesByPresentAddressCityIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where presentAddressCity is not null
        defaultEmployeeShouldBeFound("presentAddressCity.specified=true");

        // Get all the employeeList where presentAddressCity is null
        defaultEmployeeShouldNotBeFound("presentAddressCity.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByPresentAddressCityContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where presentAddressCity contains DEFAULT_PRESENT_ADDRESS_CITY
        defaultEmployeeShouldBeFound("presentAddressCity.contains=" + DEFAULT_PRESENT_ADDRESS_CITY);

        // Get all the employeeList where presentAddressCity contains UPDATED_PRESENT_ADDRESS_CITY
        defaultEmployeeShouldNotBeFound("presentAddressCity.contains=" + UPDATED_PRESENT_ADDRESS_CITY);
    }

    @Test
    @Transactional
    void getAllEmployeesByPresentAddressCityNotContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where presentAddressCity does not contain DEFAULT_PRESENT_ADDRESS_CITY
        defaultEmployeeShouldNotBeFound("presentAddressCity.doesNotContain=" + DEFAULT_PRESENT_ADDRESS_CITY);

        // Get all the employeeList where presentAddressCity does not contain UPDATED_PRESENT_ADDRESS_CITY
        defaultEmployeeShouldBeFound("presentAddressCity.doesNotContain=" + UPDATED_PRESENT_ADDRESS_CITY);
    }

    @Test
    @Transactional
    void getAllEmployeesByPresentAddressProvinceIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where presentAddressProvince equals to DEFAULT_PRESENT_ADDRESS_PROVINCE
        defaultEmployeeShouldBeFound("presentAddressProvince.equals=" + DEFAULT_PRESENT_ADDRESS_PROVINCE);

        // Get all the employeeList where presentAddressProvince equals to UPDATED_PRESENT_ADDRESS_PROVINCE
        defaultEmployeeShouldNotBeFound("presentAddressProvince.equals=" + UPDATED_PRESENT_ADDRESS_PROVINCE);
    }

    @Test
    @Transactional
    void getAllEmployeesByPresentAddressProvinceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where presentAddressProvince not equals to DEFAULT_PRESENT_ADDRESS_PROVINCE
        defaultEmployeeShouldNotBeFound("presentAddressProvince.notEquals=" + DEFAULT_PRESENT_ADDRESS_PROVINCE);

        // Get all the employeeList where presentAddressProvince not equals to UPDATED_PRESENT_ADDRESS_PROVINCE
        defaultEmployeeShouldBeFound("presentAddressProvince.notEquals=" + UPDATED_PRESENT_ADDRESS_PROVINCE);
    }

    @Test
    @Transactional
    void getAllEmployeesByPresentAddressProvinceIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where presentAddressProvince in DEFAULT_PRESENT_ADDRESS_PROVINCE or UPDATED_PRESENT_ADDRESS_PROVINCE
        defaultEmployeeShouldBeFound(
            "presentAddressProvince.in=" + DEFAULT_PRESENT_ADDRESS_PROVINCE + "," + UPDATED_PRESENT_ADDRESS_PROVINCE
        );

        // Get all the employeeList where presentAddressProvince equals to UPDATED_PRESENT_ADDRESS_PROVINCE
        defaultEmployeeShouldNotBeFound("presentAddressProvince.in=" + UPDATED_PRESENT_ADDRESS_PROVINCE);
    }

    @Test
    @Transactional
    void getAllEmployeesByPresentAddressProvinceIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where presentAddressProvince is not null
        defaultEmployeeShouldBeFound("presentAddressProvince.specified=true");

        // Get all the employeeList where presentAddressProvince is null
        defaultEmployeeShouldNotBeFound("presentAddressProvince.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByPresentAddressProvinceContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where presentAddressProvince contains DEFAULT_PRESENT_ADDRESS_PROVINCE
        defaultEmployeeShouldBeFound("presentAddressProvince.contains=" + DEFAULT_PRESENT_ADDRESS_PROVINCE);

        // Get all the employeeList where presentAddressProvince contains UPDATED_PRESENT_ADDRESS_PROVINCE
        defaultEmployeeShouldNotBeFound("presentAddressProvince.contains=" + UPDATED_PRESENT_ADDRESS_PROVINCE);
    }

    @Test
    @Transactional
    void getAllEmployeesByPresentAddressProvinceNotContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where presentAddressProvince does not contain DEFAULT_PRESENT_ADDRESS_PROVINCE
        defaultEmployeeShouldNotBeFound("presentAddressProvince.doesNotContain=" + DEFAULT_PRESENT_ADDRESS_PROVINCE);

        // Get all the employeeList where presentAddressProvince does not contain UPDATED_PRESENT_ADDRESS_PROVINCE
        defaultEmployeeShouldBeFound("presentAddressProvince.doesNotContain=" + UPDATED_PRESENT_ADDRESS_PROVINCE);
    }

    @Test
    @Transactional
    void getAllEmployeesByPresentAddressZipcodeIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where presentAddressZipcode equals to DEFAULT_PRESENT_ADDRESS_ZIPCODE
        defaultEmployeeShouldBeFound("presentAddressZipcode.equals=" + DEFAULT_PRESENT_ADDRESS_ZIPCODE);

        // Get all the employeeList where presentAddressZipcode equals to UPDATED_PRESENT_ADDRESS_ZIPCODE
        defaultEmployeeShouldNotBeFound("presentAddressZipcode.equals=" + UPDATED_PRESENT_ADDRESS_ZIPCODE);
    }

    @Test
    @Transactional
    void getAllEmployeesByPresentAddressZipcodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where presentAddressZipcode not equals to DEFAULT_PRESENT_ADDRESS_ZIPCODE
        defaultEmployeeShouldNotBeFound("presentAddressZipcode.notEquals=" + DEFAULT_PRESENT_ADDRESS_ZIPCODE);

        // Get all the employeeList where presentAddressZipcode not equals to UPDATED_PRESENT_ADDRESS_ZIPCODE
        defaultEmployeeShouldBeFound("presentAddressZipcode.notEquals=" + UPDATED_PRESENT_ADDRESS_ZIPCODE);
    }

    @Test
    @Transactional
    void getAllEmployeesByPresentAddressZipcodeIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where presentAddressZipcode in DEFAULT_PRESENT_ADDRESS_ZIPCODE or UPDATED_PRESENT_ADDRESS_ZIPCODE
        defaultEmployeeShouldBeFound("presentAddressZipcode.in=" + DEFAULT_PRESENT_ADDRESS_ZIPCODE + "," + UPDATED_PRESENT_ADDRESS_ZIPCODE);

        // Get all the employeeList where presentAddressZipcode equals to UPDATED_PRESENT_ADDRESS_ZIPCODE
        defaultEmployeeShouldNotBeFound("presentAddressZipcode.in=" + UPDATED_PRESENT_ADDRESS_ZIPCODE);
    }

    @Test
    @Transactional
    void getAllEmployeesByPresentAddressZipcodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where presentAddressZipcode is not null
        defaultEmployeeShouldBeFound("presentAddressZipcode.specified=true");

        // Get all the employeeList where presentAddressZipcode is null
        defaultEmployeeShouldNotBeFound("presentAddressZipcode.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByPresentAddressZipcodeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where presentAddressZipcode is greater than or equal to DEFAULT_PRESENT_ADDRESS_ZIPCODE
        defaultEmployeeShouldBeFound("presentAddressZipcode.greaterThanOrEqual=" + DEFAULT_PRESENT_ADDRESS_ZIPCODE);

        // Get all the employeeList where presentAddressZipcode is greater than or equal to UPDATED_PRESENT_ADDRESS_ZIPCODE
        defaultEmployeeShouldNotBeFound("presentAddressZipcode.greaterThanOrEqual=" + UPDATED_PRESENT_ADDRESS_ZIPCODE);
    }

    @Test
    @Transactional
    void getAllEmployeesByPresentAddressZipcodeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where presentAddressZipcode is less than or equal to DEFAULT_PRESENT_ADDRESS_ZIPCODE
        defaultEmployeeShouldBeFound("presentAddressZipcode.lessThanOrEqual=" + DEFAULT_PRESENT_ADDRESS_ZIPCODE);

        // Get all the employeeList where presentAddressZipcode is less than or equal to SMALLER_PRESENT_ADDRESS_ZIPCODE
        defaultEmployeeShouldNotBeFound("presentAddressZipcode.lessThanOrEqual=" + SMALLER_PRESENT_ADDRESS_ZIPCODE);
    }

    @Test
    @Transactional
    void getAllEmployeesByPresentAddressZipcodeIsLessThanSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where presentAddressZipcode is less than DEFAULT_PRESENT_ADDRESS_ZIPCODE
        defaultEmployeeShouldNotBeFound("presentAddressZipcode.lessThan=" + DEFAULT_PRESENT_ADDRESS_ZIPCODE);

        // Get all the employeeList where presentAddressZipcode is less than UPDATED_PRESENT_ADDRESS_ZIPCODE
        defaultEmployeeShouldBeFound("presentAddressZipcode.lessThan=" + UPDATED_PRESENT_ADDRESS_ZIPCODE);
    }

    @Test
    @Transactional
    void getAllEmployeesByPresentAddressZipcodeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where presentAddressZipcode is greater than DEFAULT_PRESENT_ADDRESS_ZIPCODE
        defaultEmployeeShouldNotBeFound("presentAddressZipcode.greaterThan=" + DEFAULT_PRESENT_ADDRESS_ZIPCODE);

        // Get all the employeeList where presentAddressZipcode is greater than SMALLER_PRESENT_ADDRESS_ZIPCODE
        defaultEmployeeShouldBeFound("presentAddressZipcode.greaterThan=" + SMALLER_PRESENT_ADDRESS_ZIPCODE);
    }

    @Test
    @Transactional
    void getAllEmployeesByHomeAddressStreetIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where homeAddressStreet equals to DEFAULT_HOME_ADDRESS_STREET
        defaultEmployeeShouldBeFound("homeAddressStreet.equals=" + DEFAULT_HOME_ADDRESS_STREET);

        // Get all the employeeList where homeAddressStreet equals to UPDATED_HOME_ADDRESS_STREET
        defaultEmployeeShouldNotBeFound("homeAddressStreet.equals=" + UPDATED_HOME_ADDRESS_STREET);
    }

    @Test
    @Transactional
    void getAllEmployeesByHomeAddressStreetIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where homeAddressStreet not equals to DEFAULT_HOME_ADDRESS_STREET
        defaultEmployeeShouldNotBeFound("homeAddressStreet.notEquals=" + DEFAULT_HOME_ADDRESS_STREET);

        // Get all the employeeList where homeAddressStreet not equals to UPDATED_HOME_ADDRESS_STREET
        defaultEmployeeShouldBeFound("homeAddressStreet.notEquals=" + UPDATED_HOME_ADDRESS_STREET);
    }

    @Test
    @Transactional
    void getAllEmployeesByHomeAddressStreetIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where homeAddressStreet in DEFAULT_HOME_ADDRESS_STREET or UPDATED_HOME_ADDRESS_STREET
        defaultEmployeeShouldBeFound("homeAddressStreet.in=" + DEFAULT_HOME_ADDRESS_STREET + "," + UPDATED_HOME_ADDRESS_STREET);

        // Get all the employeeList where homeAddressStreet equals to UPDATED_HOME_ADDRESS_STREET
        defaultEmployeeShouldNotBeFound("homeAddressStreet.in=" + UPDATED_HOME_ADDRESS_STREET);
    }

    @Test
    @Transactional
    void getAllEmployeesByHomeAddressStreetIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where homeAddressStreet is not null
        defaultEmployeeShouldBeFound("homeAddressStreet.specified=true");

        // Get all the employeeList where homeAddressStreet is null
        defaultEmployeeShouldNotBeFound("homeAddressStreet.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByHomeAddressStreetContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where homeAddressStreet contains DEFAULT_HOME_ADDRESS_STREET
        defaultEmployeeShouldBeFound("homeAddressStreet.contains=" + DEFAULT_HOME_ADDRESS_STREET);

        // Get all the employeeList where homeAddressStreet contains UPDATED_HOME_ADDRESS_STREET
        defaultEmployeeShouldNotBeFound("homeAddressStreet.contains=" + UPDATED_HOME_ADDRESS_STREET);
    }

    @Test
    @Transactional
    void getAllEmployeesByHomeAddressStreetNotContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where homeAddressStreet does not contain DEFAULT_HOME_ADDRESS_STREET
        defaultEmployeeShouldNotBeFound("homeAddressStreet.doesNotContain=" + DEFAULT_HOME_ADDRESS_STREET);

        // Get all the employeeList where homeAddressStreet does not contain UPDATED_HOME_ADDRESS_STREET
        defaultEmployeeShouldBeFound("homeAddressStreet.doesNotContain=" + UPDATED_HOME_ADDRESS_STREET);
    }

    @Test
    @Transactional
    void getAllEmployeesByHomeAddressCityIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where homeAddressCity equals to DEFAULT_HOME_ADDRESS_CITY
        defaultEmployeeShouldBeFound("homeAddressCity.equals=" + DEFAULT_HOME_ADDRESS_CITY);

        // Get all the employeeList where homeAddressCity equals to UPDATED_HOME_ADDRESS_CITY
        defaultEmployeeShouldNotBeFound("homeAddressCity.equals=" + UPDATED_HOME_ADDRESS_CITY);
    }

    @Test
    @Transactional
    void getAllEmployeesByHomeAddressCityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where homeAddressCity not equals to DEFAULT_HOME_ADDRESS_CITY
        defaultEmployeeShouldNotBeFound("homeAddressCity.notEquals=" + DEFAULT_HOME_ADDRESS_CITY);

        // Get all the employeeList where homeAddressCity not equals to UPDATED_HOME_ADDRESS_CITY
        defaultEmployeeShouldBeFound("homeAddressCity.notEquals=" + UPDATED_HOME_ADDRESS_CITY);
    }

    @Test
    @Transactional
    void getAllEmployeesByHomeAddressCityIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where homeAddressCity in DEFAULT_HOME_ADDRESS_CITY or UPDATED_HOME_ADDRESS_CITY
        defaultEmployeeShouldBeFound("homeAddressCity.in=" + DEFAULT_HOME_ADDRESS_CITY + "," + UPDATED_HOME_ADDRESS_CITY);

        // Get all the employeeList where homeAddressCity equals to UPDATED_HOME_ADDRESS_CITY
        defaultEmployeeShouldNotBeFound("homeAddressCity.in=" + UPDATED_HOME_ADDRESS_CITY);
    }

    @Test
    @Transactional
    void getAllEmployeesByHomeAddressCityIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where homeAddressCity is not null
        defaultEmployeeShouldBeFound("homeAddressCity.specified=true");

        // Get all the employeeList where homeAddressCity is null
        defaultEmployeeShouldNotBeFound("homeAddressCity.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByHomeAddressCityContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where homeAddressCity contains DEFAULT_HOME_ADDRESS_CITY
        defaultEmployeeShouldBeFound("homeAddressCity.contains=" + DEFAULT_HOME_ADDRESS_CITY);

        // Get all the employeeList where homeAddressCity contains UPDATED_HOME_ADDRESS_CITY
        defaultEmployeeShouldNotBeFound("homeAddressCity.contains=" + UPDATED_HOME_ADDRESS_CITY);
    }

    @Test
    @Transactional
    void getAllEmployeesByHomeAddressCityNotContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where homeAddressCity does not contain DEFAULT_HOME_ADDRESS_CITY
        defaultEmployeeShouldNotBeFound("homeAddressCity.doesNotContain=" + DEFAULT_HOME_ADDRESS_CITY);

        // Get all the employeeList where homeAddressCity does not contain UPDATED_HOME_ADDRESS_CITY
        defaultEmployeeShouldBeFound("homeAddressCity.doesNotContain=" + UPDATED_HOME_ADDRESS_CITY);
    }

    @Test
    @Transactional
    void getAllEmployeesByHomeAddressProvinceIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where homeAddressProvince equals to DEFAULT_HOME_ADDRESS_PROVINCE
        defaultEmployeeShouldBeFound("homeAddressProvince.equals=" + DEFAULT_HOME_ADDRESS_PROVINCE);

        // Get all the employeeList where homeAddressProvince equals to UPDATED_HOME_ADDRESS_PROVINCE
        defaultEmployeeShouldNotBeFound("homeAddressProvince.equals=" + UPDATED_HOME_ADDRESS_PROVINCE);
    }

    @Test
    @Transactional
    void getAllEmployeesByHomeAddressProvinceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where homeAddressProvince not equals to DEFAULT_HOME_ADDRESS_PROVINCE
        defaultEmployeeShouldNotBeFound("homeAddressProvince.notEquals=" + DEFAULT_HOME_ADDRESS_PROVINCE);

        // Get all the employeeList where homeAddressProvince not equals to UPDATED_HOME_ADDRESS_PROVINCE
        defaultEmployeeShouldBeFound("homeAddressProvince.notEquals=" + UPDATED_HOME_ADDRESS_PROVINCE);
    }

    @Test
    @Transactional
    void getAllEmployeesByHomeAddressProvinceIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where homeAddressProvince in DEFAULT_HOME_ADDRESS_PROVINCE or UPDATED_HOME_ADDRESS_PROVINCE
        defaultEmployeeShouldBeFound("homeAddressProvince.in=" + DEFAULT_HOME_ADDRESS_PROVINCE + "," + UPDATED_HOME_ADDRESS_PROVINCE);

        // Get all the employeeList where homeAddressProvince equals to UPDATED_HOME_ADDRESS_PROVINCE
        defaultEmployeeShouldNotBeFound("homeAddressProvince.in=" + UPDATED_HOME_ADDRESS_PROVINCE);
    }

    @Test
    @Transactional
    void getAllEmployeesByHomeAddressProvinceIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where homeAddressProvince is not null
        defaultEmployeeShouldBeFound("homeAddressProvince.specified=true");

        // Get all the employeeList where homeAddressProvince is null
        defaultEmployeeShouldNotBeFound("homeAddressProvince.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByHomeAddressProvinceContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where homeAddressProvince contains DEFAULT_HOME_ADDRESS_PROVINCE
        defaultEmployeeShouldBeFound("homeAddressProvince.contains=" + DEFAULT_HOME_ADDRESS_PROVINCE);

        // Get all the employeeList where homeAddressProvince contains UPDATED_HOME_ADDRESS_PROVINCE
        defaultEmployeeShouldNotBeFound("homeAddressProvince.contains=" + UPDATED_HOME_ADDRESS_PROVINCE);
    }

    @Test
    @Transactional
    void getAllEmployeesByHomeAddressProvinceNotContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where homeAddressProvince does not contain DEFAULT_HOME_ADDRESS_PROVINCE
        defaultEmployeeShouldNotBeFound("homeAddressProvince.doesNotContain=" + DEFAULT_HOME_ADDRESS_PROVINCE);

        // Get all the employeeList where homeAddressProvince does not contain UPDATED_HOME_ADDRESS_PROVINCE
        defaultEmployeeShouldBeFound("homeAddressProvince.doesNotContain=" + UPDATED_HOME_ADDRESS_PROVINCE);
    }

    @Test
    @Transactional
    void getAllEmployeesByHomeAddressZipcodeIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where homeAddressZipcode equals to DEFAULT_HOME_ADDRESS_ZIPCODE
        defaultEmployeeShouldBeFound("homeAddressZipcode.equals=" + DEFAULT_HOME_ADDRESS_ZIPCODE);

        // Get all the employeeList where homeAddressZipcode equals to UPDATED_HOME_ADDRESS_ZIPCODE
        defaultEmployeeShouldNotBeFound("homeAddressZipcode.equals=" + UPDATED_HOME_ADDRESS_ZIPCODE);
    }

    @Test
    @Transactional
    void getAllEmployeesByHomeAddressZipcodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where homeAddressZipcode not equals to DEFAULT_HOME_ADDRESS_ZIPCODE
        defaultEmployeeShouldNotBeFound("homeAddressZipcode.notEquals=" + DEFAULT_HOME_ADDRESS_ZIPCODE);

        // Get all the employeeList where homeAddressZipcode not equals to UPDATED_HOME_ADDRESS_ZIPCODE
        defaultEmployeeShouldBeFound("homeAddressZipcode.notEquals=" + UPDATED_HOME_ADDRESS_ZIPCODE);
    }

    @Test
    @Transactional
    void getAllEmployeesByHomeAddressZipcodeIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where homeAddressZipcode in DEFAULT_HOME_ADDRESS_ZIPCODE or UPDATED_HOME_ADDRESS_ZIPCODE
        defaultEmployeeShouldBeFound("homeAddressZipcode.in=" + DEFAULT_HOME_ADDRESS_ZIPCODE + "," + UPDATED_HOME_ADDRESS_ZIPCODE);

        // Get all the employeeList where homeAddressZipcode equals to UPDATED_HOME_ADDRESS_ZIPCODE
        defaultEmployeeShouldNotBeFound("homeAddressZipcode.in=" + UPDATED_HOME_ADDRESS_ZIPCODE);
    }

    @Test
    @Transactional
    void getAllEmployeesByHomeAddressZipcodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where homeAddressZipcode is not null
        defaultEmployeeShouldBeFound("homeAddressZipcode.specified=true");

        // Get all the employeeList where homeAddressZipcode is null
        defaultEmployeeShouldNotBeFound("homeAddressZipcode.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByHomeAddressZipcodeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where homeAddressZipcode is greater than or equal to DEFAULT_HOME_ADDRESS_ZIPCODE
        defaultEmployeeShouldBeFound("homeAddressZipcode.greaterThanOrEqual=" + DEFAULT_HOME_ADDRESS_ZIPCODE);

        // Get all the employeeList where homeAddressZipcode is greater than or equal to UPDATED_HOME_ADDRESS_ZIPCODE
        defaultEmployeeShouldNotBeFound("homeAddressZipcode.greaterThanOrEqual=" + UPDATED_HOME_ADDRESS_ZIPCODE);
    }

    @Test
    @Transactional
    void getAllEmployeesByHomeAddressZipcodeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where homeAddressZipcode is less than or equal to DEFAULT_HOME_ADDRESS_ZIPCODE
        defaultEmployeeShouldBeFound("homeAddressZipcode.lessThanOrEqual=" + DEFAULT_HOME_ADDRESS_ZIPCODE);

        // Get all the employeeList where homeAddressZipcode is less than or equal to SMALLER_HOME_ADDRESS_ZIPCODE
        defaultEmployeeShouldNotBeFound("homeAddressZipcode.lessThanOrEqual=" + SMALLER_HOME_ADDRESS_ZIPCODE);
    }

    @Test
    @Transactional
    void getAllEmployeesByHomeAddressZipcodeIsLessThanSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where homeAddressZipcode is less than DEFAULT_HOME_ADDRESS_ZIPCODE
        defaultEmployeeShouldNotBeFound("homeAddressZipcode.lessThan=" + DEFAULT_HOME_ADDRESS_ZIPCODE);

        // Get all the employeeList where homeAddressZipcode is less than UPDATED_HOME_ADDRESS_ZIPCODE
        defaultEmployeeShouldBeFound("homeAddressZipcode.lessThan=" + UPDATED_HOME_ADDRESS_ZIPCODE);
    }

    @Test
    @Transactional
    void getAllEmployeesByHomeAddressZipcodeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where homeAddressZipcode is greater than DEFAULT_HOME_ADDRESS_ZIPCODE
        defaultEmployeeShouldNotBeFound("homeAddressZipcode.greaterThan=" + DEFAULT_HOME_ADDRESS_ZIPCODE);

        // Get all the employeeList where homeAddressZipcode is greater than SMALLER_HOME_ADDRESS_ZIPCODE
        defaultEmployeeShouldBeFound("homeAddressZipcode.greaterThan=" + SMALLER_HOME_ADDRESS_ZIPCODE);
    }

    @Test
    @Transactional
    void getAllEmployeesByUserIsEqualToSomething() throws Exception {
        // Get already existing entity
        User user = employee.getUser();
        employeeRepository.saveAndFlush(employee);
        Long userId = user.getId();

        // Get all the employeeList where user equals to userId
        defaultEmployeeShouldBeFound("userId.equals=" + userId);

        // Get all the employeeList where user equals to (userId + 1)
        defaultEmployeeShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    @Test
    @Transactional
    void getAllEmployeesByDutyScheduleIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);
        DutySchedule dutySchedule;
        if (TestUtil.findAll(em, DutySchedule.class).isEmpty()) {
            dutySchedule = DutyScheduleResourceIT.createEntity(em);
            em.persist(dutySchedule);
            em.flush();
        } else {
            dutySchedule = TestUtil.findAll(em, DutySchedule.class).get(0);
        }
        em.persist(dutySchedule);
        em.flush();
        employee.addDutySchedule(dutySchedule);
        employeeRepository.saveAndFlush(employee);
        Long dutyScheduleId = dutySchedule.getId();

        // Get all the employeeList where dutySchedule equals to dutyScheduleId
        defaultEmployeeShouldBeFound("dutyScheduleId.equals=" + dutyScheduleId);

        // Get all the employeeList where dutySchedule equals to (dutyScheduleId + 1)
        defaultEmployeeShouldNotBeFound("dutyScheduleId.equals=" + (dutyScheduleId + 1));
    }

    @Test
    @Transactional
    void getAllEmployeesByDailyTimeRecordIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);
        DailyTimeRecord dailyTimeRecord;
        if (TestUtil.findAll(em, DailyTimeRecord.class).isEmpty()) {
            dailyTimeRecord = DailyTimeRecordResourceIT.createEntity(em);
            em.persist(dailyTimeRecord);
            em.flush();
        } else {
            dailyTimeRecord = TestUtil.findAll(em, DailyTimeRecord.class).get(0);
        }
        em.persist(dailyTimeRecord);
        em.flush();
        employee.addDailyTimeRecord(dailyTimeRecord);
        employeeRepository.saveAndFlush(employee);
        Long dailyTimeRecordId = dailyTimeRecord.getId();

        // Get all the employeeList where dailyTimeRecord equals to dailyTimeRecordId
        defaultEmployeeShouldBeFound("dailyTimeRecordId.equals=" + dailyTimeRecordId);

        // Get all the employeeList where dailyTimeRecord equals to (dailyTimeRecordId + 1)
        defaultEmployeeShouldNotBeFound("dailyTimeRecordId.equals=" + (dailyTimeRecordId + 1));
    }

    @Test
    @Transactional
    void getAllEmployeesByDependentsIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);
        Dependents dependents;
        if (TestUtil.findAll(em, Dependents.class).isEmpty()) {
            dependents = DependentsResourceIT.createEntity(em);
            em.persist(dependents);
            em.flush();
        } else {
            dependents = TestUtil.findAll(em, Dependents.class).get(0);
        }
        em.persist(dependents);
        em.flush();
        employee.addDependents(dependents);
        employeeRepository.saveAndFlush(employee);
        Long dependentsId = dependents.getId();

        // Get all the employeeList where dependents equals to dependentsId
        defaultEmployeeShouldBeFound("dependentsId.equals=" + dependentsId);

        // Get all the employeeList where dependents equals to (dependentsId + 1)
        defaultEmployeeShouldNotBeFound("dependentsId.equals=" + (dependentsId + 1));
    }

    @Test
    @Transactional
    void getAllEmployeesByEducationIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);
        Education education;
        if (TestUtil.findAll(em, Education.class).isEmpty()) {
            education = EducationResourceIT.createEntity(em);
            em.persist(education);
            em.flush();
        } else {
            education = TestUtil.findAll(em, Education.class).get(0);
        }
        em.persist(education);
        em.flush();
        employee.addEducation(education);
        employeeRepository.saveAndFlush(employee);
        Long educationId = education.getId();

        // Get all the employeeList where education equals to educationId
        defaultEmployeeShouldBeFound("educationId.equals=" + educationId);

        // Get all the employeeList where education equals to (educationId + 1)
        defaultEmployeeShouldNotBeFound("educationId.equals=" + (educationId + 1));
    }

    @Test
    @Transactional
    void getAllEmployeesByTrainingHistoryIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);
        TrainingHistory trainingHistory;
        if (TestUtil.findAll(em, TrainingHistory.class).isEmpty()) {
            trainingHistory = TrainingHistoryResourceIT.createEntity(em);
            em.persist(trainingHistory);
            em.flush();
        } else {
            trainingHistory = TestUtil.findAll(em, TrainingHistory.class).get(0);
        }
        em.persist(trainingHistory);
        em.flush();
        employee.addTrainingHistory(trainingHistory);
        employeeRepository.saveAndFlush(employee);
        Long trainingHistoryId = trainingHistory.getId();

        // Get all the employeeList where trainingHistory equals to trainingHistoryId
        defaultEmployeeShouldBeFound("trainingHistoryId.equals=" + trainingHistoryId);

        // Get all the employeeList where trainingHistory equals to (trainingHistoryId + 1)
        defaultEmployeeShouldNotBeFound("trainingHistoryId.equals=" + (trainingHistoryId + 1));
    }

    @Test
    @Transactional
    void getAllEmployeesByLeaveIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);
        Leave leave;
        if (TestUtil.findAll(em, Leave.class).isEmpty()) {
            leave = LeaveResourceIT.createEntity(em);
            em.persist(leave);
            em.flush();
        } else {
            leave = TestUtil.findAll(em, Leave.class).get(0);
        }
        em.persist(leave);
        em.flush();
        employee.addLeave(leave);
        employeeRepository.saveAndFlush(employee);
        Long leaveId = leave.getId();

        // Get all the employeeList where leave equals to leaveId
        defaultEmployeeShouldBeFound("leaveId.equals=" + leaveId);

        // Get all the employeeList where leave equals to (leaveId + 1)
        defaultEmployeeShouldNotBeFound("leaveId.equals=" + (leaveId + 1));
    }

    @Test
    @Transactional
    void getAllEmployeesByDesignationIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);
        Designation designation;
        if (TestUtil.findAll(em, Designation.class).isEmpty()) {
            designation = DesignationResourceIT.createEntity(em);
            em.persist(designation);
            em.flush();
        } else {
            designation = TestUtil.findAll(em, Designation.class).get(0);
        }
        em.persist(designation);
        em.flush();
        employee.addDesignation(designation);
        employeeRepository.saveAndFlush(employee);
        Long designationId = designation.getId();

        // Get all the employeeList where designation equals to designationId
        defaultEmployeeShouldBeFound("designationId.equals=" + designationId);

        // Get all the employeeList where designation equals to (designationId + 1)
        defaultEmployeeShouldNotBeFound("designationId.equals=" + (designationId + 1));
    }

    @Test
    @Transactional
    void getAllEmployeesByBenefitsIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);
        Benefits benefits;
        if (TestUtil.findAll(em, Benefits.class).isEmpty()) {
            benefits = BenefitsResourceIT.createEntity(em);
            em.persist(benefits);
            em.flush();
        } else {
            benefits = TestUtil.findAll(em, Benefits.class).get(0);
        }
        em.persist(benefits);
        em.flush();
        employee.addBenefits(benefits);
        employeeRepository.saveAndFlush(employee);
        Long benefitsId = benefits.getId();

        // Get all the employeeList where benefits equals to benefitsId
        defaultEmployeeShouldBeFound("benefitsId.equals=" + benefitsId);

        // Get all the employeeList where benefits equals to (benefitsId + 1)
        defaultEmployeeShouldNotBeFound("benefitsId.equals=" + (benefitsId + 1));
    }

    @Test
    @Transactional
    void getAllEmployeesByDepartmentIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);
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
        employee.setDepartment(department);
        employeeRepository.saveAndFlush(employee);
        Long departmentId = department.getId();

        // Get all the employeeList where department equals to departmentId
        defaultEmployeeShouldBeFound("departmentId.equals=" + departmentId);

        // Get all the employeeList where department equals to (departmentId + 1)
        defaultEmployeeShouldNotBeFound("departmentId.equals=" + (departmentId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEmployeeShouldBeFound(String filter) throws Exception {
        restEmployeeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employee.getId().intValue())))
            .andExpect(jsonPath("$.[*].employeeBiometricId").value(hasItem(DEFAULT_EMPLOYEE_BIOMETRIC_ID)))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].middleName").value(hasItem(DEFAULT_MIDDLE_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].nameSuffix").value(hasItem(DEFAULT_NAME_SUFFIX)))
            .andExpect(jsonPath("$.[*].birthdate").value(hasItem(DEFAULT_BIRTHDATE.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].employmentType").value(hasItem(DEFAULT_EMPLOYMENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].mobileNumber").value(hasItem(DEFAULT_MOBILE_NUMBER)))
            .andExpect(jsonPath("$.[*].dateHired").value(hasItem(DEFAULT_DATE_HIRED.toString())))
            .andExpect(jsonPath("$.[*].dateDeno").value(hasItem(DEFAULT_DATE_DENO.toString())))
            .andExpect(jsonPath("$.[*].sickLeaveYearlyCredit").value(hasItem(DEFAULT_SICK_LEAVE_YEARLY_CREDIT)))
            .andExpect(jsonPath("$.[*].sickLeaveYearlyCreditUsed").value(hasItem(DEFAULT_SICK_LEAVE_YEARLY_CREDIT_USED)))
            .andExpect(jsonPath("$.[*].leaveYearlyCredit").value(hasItem(DEFAULT_LEAVE_YEARLY_CREDIT)))
            .andExpect(jsonPath("$.[*].leaveYearlyCreditUsed").value(hasItem(DEFAULT_LEAVE_YEARLY_CREDIT_USED)))
            .andExpect(jsonPath("$.[*].profileImageContentType").value(hasItem(DEFAULT_PROFILE_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].profileImage").value(hasItem(Base64Utils.encodeToString(DEFAULT_PROFILE_IMAGE))))
            .andExpect(jsonPath("$.[*].presentAddressStreet").value(hasItem(DEFAULT_PRESENT_ADDRESS_STREET)))
            .andExpect(jsonPath("$.[*].presentAddressCity").value(hasItem(DEFAULT_PRESENT_ADDRESS_CITY)))
            .andExpect(jsonPath("$.[*].presentAddressProvince").value(hasItem(DEFAULT_PRESENT_ADDRESS_PROVINCE)))
            .andExpect(jsonPath("$.[*].presentAddressZipcode").value(hasItem(DEFAULT_PRESENT_ADDRESS_ZIPCODE)))
            .andExpect(jsonPath("$.[*].homeAddressStreet").value(hasItem(DEFAULT_HOME_ADDRESS_STREET)))
            .andExpect(jsonPath("$.[*].homeAddressCity").value(hasItem(DEFAULT_HOME_ADDRESS_CITY)))
            .andExpect(jsonPath("$.[*].homeAddressProvince").value(hasItem(DEFAULT_HOME_ADDRESS_PROVINCE)))
            .andExpect(jsonPath("$.[*].homeAddressZipcode").value(hasItem(DEFAULT_HOME_ADDRESS_ZIPCODE)));

        // Check, that the count call also returns 1
        restEmployeeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEmployeeShouldNotBeFound(String filter) throws Exception {
        restEmployeeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEmployeeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEmployee() throws Exception {
        // Get the employee
        restEmployeeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEmployee() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();

        // Update the employee
        Employee updatedEmployee = employeeRepository.findById(employee.getId()).get();
        // Disconnect from session so that the updates on updatedEmployee are not directly saved in db
        em.detach(updatedEmployee);
        updatedEmployee
            .employeeBiometricId(UPDATED_EMPLOYEE_BIOMETRIC_ID)
            .username(UPDATED_USERNAME)
            .email(UPDATED_EMAIL)
            .firstName(UPDATED_FIRST_NAME)
            .middleName(UPDATED_MIDDLE_NAME)
            .lastName(UPDATED_LAST_NAME)
            .nameSuffix(UPDATED_NAME_SUFFIX)
            .birthdate(UPDATED_BIRTHDATE)
            .gender(UPDATED_GENDER)
            .status(UPDATED_STATUS)
            .employmentType(UPDATED_EMPLOYMENT_TYPE)
            .mobileNumber(UPDATED_MOBILE_NUMBER)
            .dateHired(UPDATED_DATE_HIRED)
            .dateDeno(UPDATED_DATE_DENO)
            .sickLeaveYearlyCredit(UPDATED_SICK_LEAVE_YEARLY_CREDIT)
            .sickLeaveYearlyCreditUsed(UPDATED_SICK_LEAVE_YEARLY_CREDIT_USED)
            .leaveYearlyCredit(UPDATED_LEAVE_YEARLY_CREDIT)
            .leaveYearlyCreditUsed(UPDATED_LEAVE_YEARLY_CREDIT_USED)
            .profileImage(UPDATED_PROFILE_IMAGE)
            .profileImageContentType(UPDATED_PROFILE_IMAGE_CONTENT_TYPE)
            .presentAddressStreet(UPDATED_PRESENT_ADDRESS_STREET)
            .presentAddressCity(UPDATED_PRESENT_ADDRESS_CITY)
            .presentAddressProvince(UPDATED_PRESENT_ADDRESS_PROVINCE)
            .presentAddressZipcode(UPDATED_PRESENT_ADDRESS_ZIPCODE)
            .homeAddressStreet(UPDATED_HOME_ADDRESS_STREET)
            .homeAddressCity(UPDATED_HOME_ADDRESS_CITY)
            .homeAddressProvince(UPDATED_HOME_ADDRESS_PROVINCE)
            .homeAddressZipcode(UPDATED_HOME_ADDRESS_ZIPCODE);
        EmployeeDTO employeeDTO = employeeMapper.toDto(updatedEmployee);

        restEmployeeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, employeeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employeeDTO))
            )
            .andExpect(status().isOk());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
        Employee testEmployee = employeeList.get(employeeList.size() - 1);
        assertThat(testEmployee.getEmployeeBiometricId()).isEqualTo(UPDATED_EMPLOYEE_BIOMETRIC_ID);
        assertThat(testEmployee.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testEmployee.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testEmployee.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testEmployee.getMiddleName()).isEqualTo(UPDATED_MIDDLE_NAME);
        assertThat(testEmployee.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testEmployee.getNameSuffix()).isEqualTo(UPDATED_NAME_SUFFIX);
        assertThat(testEmployee.getBirthdate()).isEqualTo(UPDATED_BIRTHDATE);
        assertThat(testEmployee.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testEmployee.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testEmployee.getEmploymentType()).isEqualTo(UPDATED_EMPLOYMENT_TYPE);
        assertThat(testEmployee.getMobileNumber()).isEqualTo(UPDATED_MOBILE_NUMBER);
        assertThat(testEmployee.getDateHired()).isEqualTo(UPDATED_DATE_HIRED);
        assertThat(testEmployee.getDateDeno()).isEqualTo(UPDATED_DATE_DENO);
        assertThat(testEmployee.getSickLeaveYearlyCredit()).isEqualTo(UPDATED_SICK_LEAVE_YEARLY_CREDIT);
        assertThat(testEmployee.getSickLeaveYearlyCreditUsed()).isEqualTo(UPDATED_SICK_LEAVE_YEARLY_CREDIT_USED);
        assertThat(testEmployee.getLeaveYearlyCredit()).isEqualTo(UPDATED_LEAVE_YEARLY_CREDIT);
        assertThat(testEmployee.getLeaveYearlyCreditUsed()).isEqualTo(UPDATED_LEAVE_YEARLY_CREDIT_USED);
        assertThat(testEmployee.getProfileImage()).isEqualTo(UPDATED_PROFILE_IMAGE);
        assertThat(testEmployee.getProfileImageContentType()).isEqualTo(UPDATED_PROFILE_IMAGE_CONTENT_TYPE);
        assertThat(testEmployee.getPresentAddressStreet()).isEqualTo(UPDATED_PRESENT_ADDRESS_STREET);
        assertThat(testEmployee.getPresentAddressCity()).isEqualTo(UPDATED_PRESENT_ADDRESS_CITY);
        assertThat(testEmployee.getPresentAddressProvince()).isEqualTo(UPDATED_PRESENT_ADDRESS_PROVINCE);
        assertThat(testEmployee.getPresentAddressZipcode()).isEqualTo(UPDATED_PRESENT_ADDRESS_ZIPCODE);
        assertThat(testEmployee.getHomeAddressStreet()).isEqualTo(UPDATED_HOME_ADDRESS_STREET);
        assertThat(testEmployee.getHomeAddressCity()).isEqualTo(UPDATED_HOME_ADDRESS_CITY);
        assertThat(testEmployee.getHomeAddressProvince()).isEqualTo(UPDATED_HOME_ADDRESS_PROVINCE);
        assertThat(testEmployee.getHomeAddressZipcode()).isEqualTo(UPDATED_HOME_ADDRESS_ZIPCODE);
    }

    @Test
    @Transactional
    void putNonExistingEmployee() throws Exception {
        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();
        employee.setId(count.incrementAndGet());

        // Create the Employee
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, employeeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employeeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEmployee() throws Exception {
        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();
        employee.setId(count.incrementAndGet());

        // Create the Employee
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employeeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEmployee() throws Exception {
        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();
        employee.setId(count.incrementAndGet());

        // Create the Employee
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employeeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEmployeeWithPatch() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();

        // Update the employee using partial update
        Employee partialUpdatedEmployee = new Employee();
        partialUpdatedEmployee.setId(employee.getId());

        partialUpdatedEmployee
            .username(UPDATED_USERNAME)
            .email(UPDATED_EMAIL)
            .nameSuffix(UPDATED_NAME_SUFFIX)
            .birthdate(UPDATED_BIRTHDATE)
            .gender(UPDATED_GENDER)
            .status(UPDATED_STATUS)
            .dateDeno(UPDATED_DATE_DENO)
            .homeAddressProvince(UPDATED_HOME_ADDRESS_PROVINCE);

        restEmployeeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployee.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmployee))
            )
            .andExpect(status().isOk());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
        Employee testEmployee = employeeList.get(employeeList.size() - 1);
        assertThat(testEmployee.getEmployeeBiometricId()).isEqualTo(DEFAULT_EMPLOYEE_BIOMETRIC_ID);
        assertThat(testEmployee.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testEmployee.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testEmployee.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testEmployee.getMiddleName()).isEqualTo(DEFAULT_MIDDLE_NAME);
        assertThat(testEmployee.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testEmployee.getNameSuffix()).isEqualTo(UPDATED_NAME_SUFFIX);
        assertThat(testEmployee.getBirthdate()).isEqualTo(UPDATED_BIRTHDATE);
        assertThat(testEmployee.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testEmployee.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testEmployee.getEmploymentType()).isEqualTo(DEFAULT_EMPLOYMENT_TYPE);
        assertThat(testEmployee.getMobileNumber()).isEqualTo(DEFAULT_MOBILE_NUMBER);
        assertThat(testEmployee.getDateHired()).isEqualTo(DEFAULT_DATE_HIRED);
        assertThat(testEmployee.getDateDeno()).isEqualTo(UPDATED_DATE_DENO);
        assertThat(testEmployee.getSickLeaveYearlyCredit()).isEqualTo(DEFAULT_SICK_LEAVE_YEARLY_CREDIT);
        assertThat(testEmployee.getSickLeaveYearlyCreditUsed()).isEqualTo(DEFAULT_SICK_LEAVE_YEARLY_CREDIT_USED);
        assertThat(testEmployee.getLeaveYearlyCredit()).isEqualTo(DEFAULT_LEAVE_YEARLY_CREDIT);
        assertThat(testEmployee.getLeaveYearlyCreditUsed()).isEqualTo(DEFAULT_LEAVE_YEARLY_CREDIT_USED);
        assertThat(testEmployee.getProfileImage()).isEqualTo(DEFAULT_PROFILE_IMAGE);
        assertThat(testEmployee.getProfileImageContentType()).isEqualTo(DEFAULT_PROFILE_IMAGE_CONTENT_TYPE);
        assertThat(testEmployee.getPresentAddressStreet()).isEqualTo(DEFAULT_PRESENT_ADDRESS_STREET);
        assertThat(testEmployee.getPresentAddressCity()).isEqualTo(DEFAULT_PRESENT_ADDRESS_CITY);
        assertThat(testEmployee.getPresentAddressProvince()).isEqualTo(DEFAULT_PRESENT_ADDRESS_PROVINCE);
        assertThat(testEmployee.getPresentAddressZipcode()).isEqualTo(DEFAULT_PRESENT_ADDRESS_ZIPCODE);
        assertThat(testEmployee.getHomeAddressStreet()).isEqualTo(DEFAULT_HOME_ADDRESS_STREET);
        assertThat(testEmployee.getHomeAddressCity()).isEqualTo(DEFAULT_HOME_ADDRESS_CITY);
        assertThat(testEmployee.getHomeAddressProvince()).isEqualTo(UPDATED_HOME_ADDRESS_PROVINCE);
        assertThat(testEmployee.getHomeAddressZipcode()).isEqualTo(DEFAULT_HOME_ADDRESS_ZIPCODE);
    }

    @Test
    @Transactional
    void fullUpdateEmployeeWithPatch() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();

        // Update the employee using partial update
        Employee partialUpdatedEmployee = new Employee();
        partialUpdatedEmployee.setId(employee.getId());

        partialUpdatedEmployee
            .employeeBiometricId(UPDATED_EMPLOYEE_BIOMETRIC_ID)
            .username(UPDATED_USERNAME)
            .email(UPDATED_EMAIL)
            .firstName(UPDATED_FIRST_NAME)
            .middleName(UPDATED_MIDDLE_NAME)
            .lastName(UPDATED_LAST_NAME)
            .nameSuffix(UPDATED_NAME_SUFFIX)
            .birthdate(UPDATED_BIRTHDATE)
            .gender(UPDATED_GENDER)
            .status(UPDATED_STATUS)
            .employmentType(UPDATED_EMPLOYMENT_TYPE)
            .mobileNumber(UPDATED_MOBILE_NUMBER)
            .dateHired(UPDATED_DATE_HIRED)
            .dateDeno(UPDATED_DATE_DENO)
            .sickLeaveYearlyCredit(UPDATED_SICK_LEAVE_YEARLY_CREDIT)
            .sickLeaveYearlyCreditUsed(UPDATED_SICK_LEAVE_YEARLY_CREDIT_USED)
            .leaveYearlyCredit(UPDATED_LEAVE_YEARLY_CREDIT)
            .leaveYearlyCreditUsed(UPDATED_LEAVE_YEARLY_CREDIT_USED)
            .profileImage(UPDATED_PROFILE_IMAGE)
            .profileImageContentType(UPDATED_PROFILE_IMAGE_CONTENT_TYPE)
            .presentAddressStreet(UPDATED_PRESENT_ADDRESS_STREET)
            .presentAddressCity(UPDATED_PRESENT_ADDRESS_CITY)
            .presentAddressProvince(UPDATED_PRESENT_ADDRESS_PROVINCE)
            .presentAddressZipcode(UPDATED_PRESENT_ADDRESS_ZIPCODE)
            .homeAddressStreet(UPDATED_HOME_ADDRESS_STREET)
            .homeAddressCity(UPDATED_HOME_ADDRESS_CITY)
            .homeAddressProvince(UPDATED_HOME_ADDRESS_PROVINCE)
            .homeAddressZipcode(UPDATED_HOME_ADDRESS_ZIPCODE);

        restEmployeeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployee.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmployee))
            )
            .andExpect(status().isOk());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
        Employee testEmployee = employeeList.get(employeeList.size() - 1);
        assertThat(testEmployee.getEmployeeBiometricId()).isEqualTo(UPDATED_EMPLOYEE_BIOMETRIC_ID);
        assertThat(testEmployee.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testEmployee.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testEmployee.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testEmployee.getMiddleName()).isEqualTo(UPDATED_MIDDLE_NAME);
        assertThat(testEmployee.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testEmployee.getNameSuffix()).isEqualTo(UPDATED_NAME_SUFFIX);
        assertThat(testEmployee.getBirthdate()).isEqualTo(UPDATED_BIRTHDATE);
        assertThat(testEmployee.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testEmployee.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testEmployee.getEmploymentType()).isEqualTo(UPDATED_EMPLOYMENT_TYPE);
        assertThat(testEmployee.getMobileNumber()).isEqualTo(UPDATED_MOBILE_NUMBER);
        assertThat(testEmployee.getDateHired()).isEqualTo(UPDATED_DATE_HIRED);
        assertThat(testEmployee.getDateDeno()).isEqualTo(UPDATED_DATE_DENO);
        assertThat(testEmployee.getSickLeaveYearlyCredit()).isEqualTo(UPDATED_SICK_LEAVE_YEARLY_CREDIT);
        assertThat(testEmployee.getSickLeaveYearlyCreditUsed()).isEqualTo(UPDATED_SICK_LEAVE_YEARLY_CREDIT_USED);
        assertThat(testEmployee.getLeaveYearlyCredit()).isEqualTo(UPDATED_LEAVE_YEARLY_CREDIT);
        assertThat(testEmployee.getLeaveYearlyCreditUsed()).isEqualTo(UPDATED_LEAVE_YEARLY_CREDIT_USED);
        assertThat(testEmployee.getProfileImage()).isEqualTo(UPDATED_PROFILE_IMAGE);
        assertThat(testEmployee.getProfileImageContentType()).isEqualTo(UPDATED_PROFILE_IMAGE_CONTENT_TYPE);
        assertThat(testEmployee.getPresentAddressStreet()).isEqualTo(UPDATED_PRESENT_ADDRESS_STREET);
        assertThat(testEmployee.getPresentAddressCity()).isEqualTo(UPDATED_PRESENT_ADDRESS_CITY);
        assertThat(testEmployee.getPresentAddressProvince()).isEqualTo(UPDATED_PRESENT_ADDRESS_PROVINCE);
        assertThat(testEmployee.getPresentAddressZipcode()).isEqualTo(UPDATED_PRESENT_ADDRESS_ZIPCODE);
        assertThat(testEmployee.getHomeAddressStreet()).isEqualTo(UPDATED_HOME_ADDRESS_STREET);
        assertThat(testEmployee.getHomeAddressCity()).isEqualTo(UPDATED_HOME_ADDRESS_CITY);
        assertThat(testEmployee.getHomeAddressProvince()).isEqualTo(UPDATED_HOME_ADDRESS_PROVINCE);
        assertThat(testEmployee.getHomeAddressZipcode()).isEqualTo(UPDATED_HOME_ADDRESS_ZIPCODE);
    }

    @Test
    @Transactional
    void patchNonExistingEmployee() throws Exception {
        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();
        employee.setId(count.incrementAndGet());

        // Create the Employee
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, employeeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(employeeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEmployee() throws Exception {
        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();
        employee.setId(count.incrementAndGet());

        // Create the Employee
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(employeeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEmployee() throws Exception {
        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();
        employee.setId(count.incrementAndGet());

        // Create the Employee
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(employeeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEmployee() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        int databaseSizeBeforeDelete = employeeRepository.findAll().size();

        // Delete the employee
        restEmployeeMockMvc
            .perform(delete(ENTITY_API_URL_ID, employee.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
