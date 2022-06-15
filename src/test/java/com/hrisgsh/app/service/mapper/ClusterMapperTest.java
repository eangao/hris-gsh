package com.hrisgsh.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ClusterMapperTest {

    private ClusterMapper clusterMapper;

    @BeforeEach
    public void setUp() {
        clusterMapper = new ClusterMapperImpl();
    }
}
