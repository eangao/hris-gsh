package com.hrisgsh.app.service.mapper;

import com.hrisgsh.app.domain.Leave;
import com.hrisgsh.app.service.dto.LeaveDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Leave} and its DTO {@link LeaveDTO}.
 */
@Mapper(componentModel = "spring", uses = { EmployeeMapper.class, LeaveTypeMapper.class })
public interface LeaveMapper extends EntityMapper<LeaveDTO, Leave> {
    @Mapping(target = "employee", source = "employee", qualifiedByName = "employeeBiometricId")
    @Mapping(target = "leaveType", source = "leaveType", qualifiedByName = "name")
    LeaveDTO toDto(Leave s);
}
