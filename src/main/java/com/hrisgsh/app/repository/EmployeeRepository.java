package com.hrisgsh.app.repository;

import com.hrisgsh.app.domain.Employee;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Employee entity.
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {
    @Query(
        value = "select distinct employee from Employee employee left join fetch employee.designations left join fetch employee.benefits",
        countQuery = "select count(distinct employee) from Employee employee"
    )
    Page<Employee> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct employee from Employee employee left join fetch employee.designations left join fetch employee.benefits")
    List<Employee> findAllWithEagerRelationships();

    @Query(
        "select employee from Employee employee left join fetch employee.designations left join fetch employee.benefits where employee.id =:id"
    )
    Optional<Employee> findOneWithEagerRelationships(@Param("id") Long id);

    Employee findByEmployeeBiometricId(int employeeBiometricId);
}
