package com.hrisgsh.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EducationMapperTest {

    private EducationMapper educationMapper;

    @BeforeEach
    public void setUp() {
        educationMapper = new EducationMapperImpl();
    }
}
