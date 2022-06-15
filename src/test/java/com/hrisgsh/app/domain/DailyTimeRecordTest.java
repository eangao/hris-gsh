package com.hrisgsh.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.hrisgsh.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DailyTimeRecordTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DailyTimeRecord.class);
        DailyTimeRecord dailyTimeRecord1 = new DailyTimeRecord();
        dailyTimeRecord1.setId(1L);
        DailyTimeRecord dailyTimeRecord2 = new DailyTimeRecord();
        dailyTimeRecord2.setId(dailyTimeRecord1.getId());
        assertThat(dailyTimeRecord1).isEqualTo(dailyTimeRecord2);
        dailyTimeRecord2.setId(2L);
        assertThat(dailyTimeRecord1).isNotEqualTo(dailyTimeRecord2);
        dailyTimeRecord1.setId(null);
        assertThat(dailyTimeRecord1).isNotEqualTo(dailyTimeRecord2);
    }
}
