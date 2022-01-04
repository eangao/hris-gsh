package com.hrisgsh.app.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.hrisgsh.app.domain.DailyTimeRecord} entity.
 */
public class DailyTimeRecordDTO implements Serializable {

    private Long id;

    private Long employeeBiometricId;

    private String inputType;

    private String attendanceType;

    private String temperature;

    private String logDate;

    private String logTime;

    private EmployeeDTO employee;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEmployeeBiometricId() {
        return employeeBiometricId;
    }

    public void setEmployeeBiometricId(Long employeeBiometricId) {
        this.employeeBiometricId = employeeBiometricId;
    }

    public String getInputType() {
        return inputType;
    }

    public void setInputType(String inputType) {
        this.inputType = inputType;
    }

    public String getAttendanceType() {
        return attendanceType;
    }

    public void setAttendanceType(String attendanceType) {
        this.attendanceType = attendanceType;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getLogDate() {
        return logDate;
    }

    public void setLogDate(String logDate) {
        this.logDate = logDate;
    }

    public String getLogTime() {
        return logTime;
    }

    public void setLogTime(String logTime) {
        this.logTime = logTime;
    }

    public EmployeeDTO getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeDTO employee) {
        this.employee = employee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DailyTimeRecordDTO)) {
            return false;
        }

        DailyTimeRecordDTO dailyTimeRecordDTO = (DailyTimeRecordDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, dailyTimeRecordDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DailyTimeRecordDTO{" +
            "id=" + getId() +
            ", employeeBiometricId=" + getEmployeeBiometricId() +
            ", inputType='" + getInputType() + "'" +
            ", attendanceType='" + getAttendanceType() + "'" +
            ", temperature='" + getTemperature() + "'" +
            ", logDate='" + getLogDate() + "'" +
            ", logTime='" + getLogTime() + "'" +
            ", employee=" + getEmployee() +
            "}";
    }
}
