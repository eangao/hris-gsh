package com.hrisgsh.app.service.mapper;

import com.hrisgsh.app.domain.DailyTimeRecord;
import com.hrisgsh.app.service.dto.DailyTimeRecordDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DailyTimeRecord} and its DTO {@link DailyTimeRecordDTO}.
 */
@Mapper(componentModel = "spring", uses = { EmployeeMapper.class })
public interface DailyTimeRecordMapper extends EntityMapper<DailyTimeRecordDTO, DailyTimeRecord> {
    @Mapping(target = "employee", source = "employee", qualifiedByName = "employeeBiometricId")
    DailyTimeRecordDTO toDto(DailyTimeRecord s);
}
