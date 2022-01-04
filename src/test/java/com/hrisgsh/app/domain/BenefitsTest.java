package com.hrisgsh.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.hrisgsh.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BenefitsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Benefits.class);
        Benefits benefits1 = new Benefits();
        benefits1.setId(1L);
        Benefits benefits2 = new Benefits();
        benefits2.setId(benefits1.getId());
        assertThat(benefits1).isEqualTo(benefits2);
        benefits2.setId(2L);
        assertThat(benefits1).isNotEqualTo(benefits2);
        benefits1.setId(null);
        assertThat(benefits1).isNotEqualTo(benefits2);
    }
}
