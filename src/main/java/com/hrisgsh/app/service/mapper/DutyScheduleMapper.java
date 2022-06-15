package com.hrisgsh.app.service.mapper;

import com.hrisgsh.app.domain.DutySchedule;
import com.hrisgsh.app.service.dto.DutyScheduleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DutySchedule} and its DTO {@link DutyScheduleDTO}.
 */
@Mapper(componentModel = "spring", uses = { EmployeeMapper.class })
public interface DutyScheduleMapper extends EntityMapper<DutyScheduleDTO, DutySchedule> {
    @Mapping(target = "employee", source = "employee", qualifiedByName = "employeeBiometricId")
    DutyScheduleDTO toDto(DutySchedule s);
}
