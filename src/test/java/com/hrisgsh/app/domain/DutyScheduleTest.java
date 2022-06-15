package com.hrisgsh.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.hrisgsh.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DutyScheduleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DutySchedule.class);
        DutySchedule dutySchedule1 = new DutySchedule();
        dutySchedule1.setId(1L);
        DutySchedule dutySchedule2 = new DutySchedule();
        dutySchedule2.setId(dutySchedule1.getId());
        assertThat(dutySchedule1).isEqualTo(dutySchedule2);
        dutySchedule2.setId(2L);
        assertThat(dutySchedule1).isNotEqualTo(dutySchedule2);
        dutySchedule1.setId(null);
        assertThat(dutySchedule1).isNotEqualTo(dutySchedule2);
    }
}
