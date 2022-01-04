package com.hrisgsh.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.hrisgsh.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DependentsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DependentsDTO.class);
        DependentsDTO dependentsDTO1 = new DependentsDTO();
        dependentsDTO1.setId(1L);
        DependentsDTO dependentsDTO2 = new DependentsDTO();
        assertThat(dependentsDTO1).isNotEqualTo(dependentsDTO2);
        dependentsDTO2.setId(dependentsDTO1.getId());
        assertThat(dependentsDTO1).isEqualTo(dependentsDTO2);
        dependentsDTO2.setId(2L);
        assertThat(dependentsDTO1).isNotEqualTo(dependentsDTO2);
        dependentsDTO1.setId(null);
        assertThat(dependentsDTO1).isNotEqualTo(dependentsDTO2);
    }
}
