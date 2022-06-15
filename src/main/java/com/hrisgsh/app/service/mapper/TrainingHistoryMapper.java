package com.hrisgsh.app.service.mapper;

import com.hrisgsh.app.domain.TrainingHistory;
import com.hrisgsh.app.service.dto.TrainingHistoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TrainingHistory} and its DTO {@link TrainingHistoryDTO}.
 */
@Mapper(componentModel = "spring", uses = { EmployeeMapper.class })
public interface TrainingHistoryMapper extends EntityMapper<TrainingHistoryDTO, TrainingHistory> {
    @Mapping(target = "employee", source = "employee", qualifiedByName = "employeeBiometricId")
    TrainingHistoryDTO toDto(TrainingHistory s);
}
