package com.hrisgsh.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BenefitsMapperTest {

    private BenefitsMapper benefitsMapper;

    @BeforeEach
    public void setUp() {
        benefitsMapper = new BenefitsMapperImpl();
    }
}
