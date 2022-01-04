package com.hrisgsh.app.service.mapper;

import com.hrisgsh.app.domain.Holiday;
import com.hrisgsh.app.service.dto.HolidayDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Holiday} and its DTO {@link HolidayDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface HolidayMapper extends EntityMapper<HolidayDTO, Holiday> {}
