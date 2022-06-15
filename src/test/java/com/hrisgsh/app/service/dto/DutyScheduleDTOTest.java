package com.hrisgsh.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.hrisgsh.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DutyScheduleDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DutyScheduleDTO.class);
        DutyScheduleDTO dutyScheduleDTO1 = new DutyScheduleDTO();
        dutyScheduleDTO1.setId(1L);
        DutyScheduleDTO dutyScheduleDTO2 = new DutyScheduleDTO();
        assertThat(dutyScheduleDTO1).isNotEqualTo(dutyScheduleDTO2);
        dutyScheduleDTO2.setId(dutyScheduleDTO1.getId());
        assertThat(dutyScheduleDTO1).isEqualTo(dutyScheduleDTO2);
        dutyScheduleDTO2.setId(2L);
        assertThat(dutyScheduleDTO1).isNotEqualTo(dutyScheduleDTO2);
        dutyScheduleDTO1.setId(null);
        assertThat(dutyScheduleDTO1).isNotEqualTo(dutyScheduleDTO2);
    }
}
