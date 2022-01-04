package com.hrisgsh.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DesignationMapperTest {

    private DesignationMapper designationMapper;

    @BeforeEach
    public void setUp() {
        designationMapper = new DesignationMapperImpl();
    }
}
