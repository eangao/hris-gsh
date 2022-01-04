package com.hrisgsh.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DutyScheduleMapperTest {

    private DutyScheduleMapper dutyScheduleMapper;

    @BeforeEach
    public void setUp() {
        dutyScheduleMapper = new DutyScheduleMapperImpl();
    }
}
