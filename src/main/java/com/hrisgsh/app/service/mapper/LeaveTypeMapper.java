package com.hrisgsh.app.service.mapper;

import com.hrisgsh.app.domain.LeaveType;
import com.hrisgsh.app.service.dto.LeaveTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link LeaveType} and its DTO {@link LeaveTypeDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface LeaveTypeMapper extends EntityMapper<LeaveTypeDTO, LeaveType> {
    @Named("name")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    LeaveTypeDTO toDtoName(LeaveType leaveType);
}
