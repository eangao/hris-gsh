package com.hrisgsh.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.hrisgsh.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HolidayTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HolidayType.class);
        HolidayType holidayType1 = new HolidayType();
        holidayType1.setId(1L);
        HolidayType holidayType2 = new HolidayType();
        holidayType2.setId(holidayType1.getId());
        assertThat(holidayType1).isEqualTo(holidayType2);
        holidayType2.setId(2L);
        assertThat(holidayType1).isNotEqualTo(holidayType2);
        holidayType1.setId(null);
        assertThat(holidayType1).isNotEqualTo(holidayType2);
    }
}
