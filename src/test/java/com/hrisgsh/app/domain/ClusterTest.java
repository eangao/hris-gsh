package com.hrisgsh.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.hrisgsh.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ClusterTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cluster.class);
        Cluster cluster1 = new Cluster();
        cluster1.setId(1L);
        Cluster cluster2 = new Cluster();
        cluster2.setId(cluster1.getId());
        assertThat(cluster1).isEqualTo(cluster2);
        cluster2.setId(2L);
        assertThat(cluster1).isNotEqualTo(cluster2);
        cluster1.setId(null);
        assertThat(cluster1).isNotEqualTo(cluster2);
    }
}
