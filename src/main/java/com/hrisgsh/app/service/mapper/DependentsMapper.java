package com.hrisgsh.app.service.mapper;

import com.hrisgsh.app.domain.Dependents;
import com.hrisgsh.app.service.dto.DependentsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Dependents} and its DTO {@link DependentsDTO}.
 */
@Mapper(componentModel = "spring", uses = { EmployeeMapper.class })
public interface DependentsMapper extends EntityMapper<DependentsDTO, Dependents> {
    @Mapping(target = "employee", source = "employee", qualifiedByName = "employeeBiometricId")
    DependentsDTO toDto(Dependents s);
}
