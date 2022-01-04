package com.hrisgsh.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.hrisgsh.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HolidayTypeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HolidayTypeDTO.class);
        HolidayTypeDTO holidayTypeDTO1 = new HolidayTypeDTO();
        holidayTypeDTO1.setId(1L);
        HolidayTypeDTO holidayTypeDTO2 = new HolidayTypeDTO();
        assertThat(holidayTypeDTO1).isNotEqualTo(holidayTypeDTO2);
        holidayTypeDTO2.setId(holidayTypeDTO1.getId());
        assertThat(holidayTypeDTO1).isEqualTo(holidayTypeDTO2);
        holidayTypeDTO2.setId(2L);
        assertThat(holidayTypeDTO1).isNotEqualTo(holidayTypeDTO2);
        holidayTypeDTO1.setId(null);
        assertThat(holidayTypeDTO1).isNotEqualTo(holidayTypeDTO2);
    }
}
