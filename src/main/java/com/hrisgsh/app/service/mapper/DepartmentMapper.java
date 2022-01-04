package com.hrisgsh.app.service.mapper;

import com.hrisgsh.app.domain.Department;
import com.hrisgsh.app.service.dto.DepartmentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Department} and its DTO {@link DepartmentDTO}.
 */
@Mapper(componentModel = "spring", uses = { ClusterMapper.class })
public interface DepartmentMapper extends EntityMapper<DepartmentDTO, Department> {
    @Mapping(target = "cluster", source = "cluster", qualifiedByName = "name")
    DepartmentDTO toDto(Department s);

    @Named("name")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    DepartmentDTO toDtoName(Department department);
}
