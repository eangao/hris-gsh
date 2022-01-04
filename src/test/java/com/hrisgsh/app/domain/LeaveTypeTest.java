package com.hrisgsh.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.hrisgsh.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LeaveTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LeaveType.class);
        LeaveType leaveType1 = new LeaveType();
        leaveType1.setId(1L);
        LeaveType leaveType2 = new LeaveType();
        leaveType2.setId(leaveType1.getId());
        assertThat(leaveType1).isEqualTo(leaveType2);
        leaveType2.setId(2L);
        assertThat(leaveType1).isNotEqualTo(leaveType2);
        leaveType1.setId(null);
        assertThat(leaveType1).isNotEqualTo(leaveType2);
    }
}
