package com.hrisgsh.app.service;

import com.hrisgsh.app.domain.*; // for static metamodels
import com.hrisgsh.app.domain.Employee;
import com.hrisgsh.app.repository.EmployeeRepository;
import com.hrisgsh.app.service.criteria.EmployeeCriteria;
import com.hrisgsh.app.service.dto.EmployeeDTO;
import com.hrisgsh.app.service.mapper.EmployeeMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Employee} entities in the database.
 * The main input is a {@link EmployeeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EmployeeDTO} or a {@link Page} of {@link EmployeeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EmployeeQueryService extends QueryService<Employee> {

    private final Logger log = LoggerFactory.getLogger(EmployeeQueryService.class);

    private final EmployeeRepository employeeRepository;

    private final EmployeeMapper employeeMapper;

    public EmployeeQueryService(EmployeeRepository employeeRepository, EmployeeMapper employeeMapper) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
    }

    /**
     * Return a {@link List} of {@link EmployeeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EmployeeDTO> findByCriteria(EmployeeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Employee> specification = createSpecification(criteria);
        return employeeMapper.toDto(employeeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link EmployeeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EmployeeDTO> findByCriteria(EmployeeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Employee> specification = createSpecification(criteria);
        return employeeRepository.findAll(specification, page).map(employeeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EmployeeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Employee> specification = createSpecification(criteria);
        return employeeRepository.count(specification);
    }

    /**
     * Function to convert {@link EmployeeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Employee> createSpecification(EmployeeCriteria criteria) {
        Specification<Employee> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Employee_.id));
            }
            if (criteria.getEmployeeBiometricId() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getEmployeeBiometricId(), Employee_.employeeBiometricId));
            }
            if (criteria.getUsername() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUsername(), Employee_.username));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Employee_.email));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), Employee_.firstName));
            }
            if (criteria.getMiddleName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMiddleName(), Employee_.middleName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), Employee_.lastName));
            }
            if (criteria.getNameSuffix() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNameSuffix(), Employee_.nameSuffix));
            }
            if (criteria.getBirthdate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBirthdate(), Employee_.birthdate));
            }
            if (criteria.getGender() != null) {
                specification = specification.and(buildSpecification(criteria.getGender(), Employee_.gender));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), Employee_.status));
            }
            if (criteria.getEmploymentType() != null) {
                specification = specification.and(buildSpecification(criteria.getEmploymentType(), Employee_.employmentType));
            }
            if (criteria.getMobileNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMobileNumber(), Employee_.mobileNumber));
            }
            if (criteria.getDateHired() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateHired(), Employee_.dateHired));
            }
            if (criteria.getDateDeno() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateDeno(), Employee_.dateDeno));
            }
            if (criteria.getSickLeaveYearlyCredit() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getSickLeaveYearlyCredit(), Employee_.sickLeaveYearlyCredit));
            }
            if (criteria.getSickLeaveYearlyCreditUsed() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getSickLeaveYearlyCreditUsed(), Employee_.sickLeaveYearlyCreditUsed)
                    );
            }
            if (criteria.getLeaveYearlyCredit() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLeaveYearlyCredit(), Employee_.leaveYearlyCredit));
            }
            if (criteria.getLeaveYearlyCreditUsed() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getLeaveYearlyCreditUsed(), Employee_.leaveYearlyCreditUsed));
            }
            if (criteria.getPresentAddressStreet() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getPresentAddressStreet(), Employee_.presentAddressStreet));
            }
            if (criteria.getPresentAddressCity() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPresentAddressCity(), Employee_.presentAddressCity));
            }
            if (criteria.getPresentAddressProvince() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getPresentAddressProvince(), Employee_.presentAddressProvince));
            }
            if (criteria.getPresentAddressZipcode() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getPresentAddressZipcode(), Employee_.presentAddressZipcode));
            }
            if (criteria.getHomeAddressStreet() != null) {
                specification = specification.and(buildStringSpecification(criteria.getHomeAddressStreet(), Employee_.homeAddressStreet));
            }
            if (criteria.getHomeAddressCity() != null) {
                specification = specification.and(buildStringSpecification(criteria.getHomeAddressCity(), Employee_.homeAddressCity));
            }
            if (criteria.getHomeAddressProvince() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getHomeAddressProvince(), Employee_.homeAddressProvince));
            }
            if (criteria.getHomeAddressZipcode() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getHomeAddressZipcode(), Employee_.homeAddressZipcode));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(Employee_.user, JoinType.LEFT).get(User_.id))
                    );
            }
            if (criteria.getDutyScheduleId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDutyScheduleId(),
                            root -> root.join(Employee_.dutySchedules, JoinType.LEFT).get(DutySchedule_.id)
                        )
                    );
            }
            if (criteria.getDailyTimeRecordId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDailyTimeRecordId(),
                            root -> root.join(Employee_.dailyTimeRecords, JoinType.LEFT).get(DailyTimeRecord_.id)
                        )
                    );
            }
            if (criteria.getDependentsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDependentsId(),
                            root -> root.join(Employee_.dependents, JoinType.LEFT).get(Dependents_.id)
                        )
                    );
            }
            if (criteria.getEducationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEducationId(),
                            root -> root.join(Employee_.educations, JoinType.LEFT).get(Education_.id)
                        )
                    );
            }
            if (criteria.getTrainingHistoryId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTrainingHistoryId(),
                            root -> root.join(Employee_.trainingHistories, JoinType.LEFT).get(TrainingHistory_.id)
                        )
                    );
            }
            if (criteria.getLeaveId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getLeaveId(), root -> root.join(Employee_.leaves, JoinType.LEFT).get(Leave_.id))
                    );
            }
            if (criteria.getDesignationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDesignationId(),
                            root -> root.join(Employee_.designations, JoinType.LEFT).get(Designation_.id)
                        )
                    );
            }
            if (criteria.getBenefitsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getBenefitsId(), root -> root.join(Employee_.benefits, JoinType.LEFT).get(Benefits_.id))
                    );
            }
            if (criteria.getDepartmentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDepartmentId(),
                            root -> root.join(Employee_.department, JoinType.LEFT).get(Department_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
