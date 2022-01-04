package com.hrisgsh.app.service.mapper;

import com.hrisgsh.app.domain.Employee;
import com.hrisgsh.app.service.dto.EmployeeDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Employee} and its DTO {@link EmployeeDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class, DesignationMapper.class, BenefitsMapper.class, DepartmentMapper.class })
public interface EmployeeMapper extends EntityMapper<EmployeeDTO, Employee> {
    @Mapping(target = "user", source = "user", qualifiedByName = "login")
    @Mapping(target = "designations", source = "designations", qualifiedByName = "nameSet")
    @Mapping(target = "benefits", source = "benefits", qualifiedByName = "nameSet")
    @Mapping(target = "department", source = "department", qualifiedByName = "name")
    EmployeeDTO toDto(Employee s);

    @Mapping(target = "removeDesignation", ignore = true)
    @Mapping(target = "removeBenefits", ignore = true)
    Employee toEntity(EmployeeDTO employeeDTO);

    @Named("employeeBiometricId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "employeeBiometricId", source = "employeeBiometricId")
    EmployeeDTO toDtoEmployeeBiometricId(Employee employee);
}
