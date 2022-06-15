package com.hrisgsh.app.service.mapper;

import com.hrisgsh.app.domain.Education;
import com.hrisgsh.app.service.dto.EducationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Education} and its DTO {@link EducationDTO}.
 */
@Mapper(componentModel = "spring", uses = { EmployeeMapper.class })
public interface EducationMapper extends EntityMapper<EducationDTO, Education> {
    @Mapping(target = "employee", source = "employee", qualifiedByName = "employeeBiometricId")
    EducationDTO toDto(Education s);
}
