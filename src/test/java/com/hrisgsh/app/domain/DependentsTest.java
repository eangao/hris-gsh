package com.hrisgsh.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.hrisgsh.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DependentsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Dependents.class);
        Dependents dependents1 = new Dependents();
        dependents1.setId(1L);
        Dependents dependents2 = new Dependents();
        dependents2.setId(dependents1.getId());
        assertThat(dependents1).isEqualTo(dependents2);
        dependents2.setId(2L);
        assertThat(dependents1).isNotEqualTo(dependents2);
        dependents1.setId(null);
        assertThat(dependents1).isNotEqualTo(dependents2);
    }
}
