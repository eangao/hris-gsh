package com.hrisgsh.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DailyTimeRecordMapperTest {

    private DailyTimeRecordMapper dailyTimeRecordMapper;

    @BeforeEach
    public void setUp() {
        dailyTimeRecordMapper = new DailyTimeRecordMapperImpl();
    }
}
