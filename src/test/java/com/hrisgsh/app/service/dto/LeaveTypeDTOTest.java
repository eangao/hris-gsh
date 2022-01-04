package com.hrisgsh.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.hrisgsh.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LeaveTypeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LeaveTypeDTO.class);
        LeaveTypeDTO leaveTypeDTO1 = new LeaveTypeDTO();
        leaveTypeDTO1.setId(1L);
        LeaveTypeDTO leaveTypeDTO2 = new LeaveTypeDTO();
        assertThat(leaveTypeDTO1).isNotEqualTo(leaveTypeDTO2);
        leaveTypeDTO2.setId(leaveTypeDTO1.getId());
        assertThat(leaveTypeDTO1).isEqualTo(leaveTypeDTO2);
        leaveTypeDTO2.setId(2L);
        assertThat(leaveTypeDTO1).isNotEqualTo(leaveTypeDTO2);
        leaveTypeDTO1.setId(null);
        assertThat(leaveTypeDTO1).isNotEqualTo(leaveTypeDTO2);
    }
}
