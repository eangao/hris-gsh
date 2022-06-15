package com.hrisgsh.app.service.mapper;

import com.hrisgsh.app.domain.Benefits;
import com.hrisgsh.app.service.dto.BenefitsDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Benefits} and its DTO {@link BenefitsDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface BenefitsMapper extends EntityMapper<BenefitsDTO, Benefits> {
    @Named("nameSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    Set<BenefitsDTO> toDtoNameSet(Set<Benefits> benefits);
}
