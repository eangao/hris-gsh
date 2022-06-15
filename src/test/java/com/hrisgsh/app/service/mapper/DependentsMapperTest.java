package com.hrisgsh.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DependentsMapperTest {

    private DependentsMapper dependentsMapper;

    @BeforeEach
    public void setUp() {
        dependentsMapper = new DependentsMapperImpl();
    }
}
