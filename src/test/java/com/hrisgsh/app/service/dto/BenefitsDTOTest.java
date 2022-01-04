package com.hrisgsh.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.hrisgsh.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BenefitsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BenefitsDTO.class);
        BenefitsDTO benefitsDTO1 = new BenefitsDTO();
        benefitsDTO1.setId(1L);
        BenefitsDTO benefitsDTO2 = new BenefitsDTO();
        assertThat(benefitsDTO1).isNotEqualTo(benefitsDTO2);
        benefitsDTO2.setId(benefitsDTO1.getId());
        assertThat(benefitsDTO1).isEqualTo(benefitsDTO2);
        benefitsDTO2.setId(2L);
        assertThat(benefitsDTO1).isNotEqualTo(benefitsDTO2);
        benefitsDTO1.setId(null);
        assertThat(benefitsDTO1).isNotEqualTo(benefitsDTO2);
    }
}
