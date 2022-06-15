package com.hrisgsh.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.hrisgsh.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DailyTimeRecordDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DailyTimeRecordDTO.class);
        DailyTimeRecordDTO dailyTimeRecordDTO1 = new DailyTimeRecordDTO();
        dailyTimeRecordDTO1.setId(1L);
        DailyTimeRecordDTO dailyTimeRecordDTO2 = new DailyTimeRecordDTO();
        assertThat(dailyTimeRecordDTO1).isNotEqualTo(dailyTimeRecordDTO2);
        dailyTimeRecordDTO2.setId(dailyTimeRecordDTO1.getId());
        assertThat(dailyTimeRecordDTO1).isEqualTo(dailyTimeRecordDTO2);
        dailyTimeRecordDTO2.setId(2L);
        assertThat(dailyTimeRecordDTO1).isNotEqualTo(dailyTimeRecordDTO2);
        dailyTimeRecordDTO1.setId(null);
        assertThat(dailyTimeRecordDTO1).isNotEqualTo(dailyTimeRecordDTO2);
    }
}
