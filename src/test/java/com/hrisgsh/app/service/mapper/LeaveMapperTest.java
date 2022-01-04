package com.hrisgsh.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LeaveMapperTest {

    private LeaveMapper leaveMapper;

    @BeforeEach
    public void setUp() {
        leaveMapper = new LeaveMapperImpl();
    }
}
