package com.hrisgsh.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HolidayTypeMapperTest {

    private HolidayTypeMapper holidayTypeMapper;

    @BeforeEach
    public void setUp() {
        holidayTypeMapper = new HolidayTypeMapperImpl();
    }
}
