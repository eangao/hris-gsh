package com.hrisgsh.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.hrisgsh.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ClusterDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClusterDTO.class);
        ClusterDTO clusterDTO1 = new ClusterDTO();
        clusterDTO1.setId(1L);
        ClusterDTO clusterDTO2 = new ClusterDTO();
        assertThat(clusterDTO1).isNotEqualTo(clusterDTO2);
        clusterDTO2.setId(clusterDTO1.getId());
        assertThat(clusterDTO1).isEqualTo(clusterDTO2);
        clusterDTO2.setId(2L);
        assertThat(clusterDTO1).isNotEqualTo(clusterDTO2);
        clusterDTO1.setId(null);
        assertThat(clusterDTO1).isNotEqualTo(clusterDTO2);
    }
}
