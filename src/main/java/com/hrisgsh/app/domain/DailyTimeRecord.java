package com.hrisgsh.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A DailyTimeRecord.
 */
@Entity
@Table(name = "daily_time_record")
public class DailyTimeRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "employee_biometric_id")
    private Long employeeBiometricId;

    @Column(name = "input_type")
    private String inputType;

    @Column(name = "attendance_type")
    private String attendanceType;

    @Column(name = "temperature")
    private String temperature;

    @Column(name = "log_date")
    private String logDate;

    @Column(name = "log_time")
    private String logTime;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = {
            "user",
            "dutySchedules",
            "dailyTimeRecords",
            "dependents",
            "educations",
            "trainingHistories",
            "leaves",
            "designations",
            "benefits",
            "department",
        },
        allowSetters = true
    )
    private Employee employee;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DailyTimeRecord id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEmployeeBiometricId() {
        return this.employeeBiometricId;
    }

    public DailyTimeRecord employeeBiometricId(Long employeeBiometricId) {
        this.setEmployeeBiometricId(employeeBiometricId);
        return this;
    }

    public void setEmployeeBiometricId(Long employeeBiometricId) {
        this.employeeBiometricId = employeeBiometricId;
    }

    public String getInputType() {
        return this.inputType;
    }

    public DailyTimeRecord inputType(String inputType) {
        this.setInputType(inputType);
        return this;
    }

    public void setInputType(String inputType) {
        this.inputType = inputType;
    }

    public String getAttendanceType() {
        return this.attendanceType;
    }

    public DailyTimeRecord attendanceType(String attendanceType) {
        this.setAttendanceType(attendanceType);
        return this;
    }

    public void setAttendanceType(String attendanceType) {
        this.attendanceType = attendanceType;
    }

    public String getTemperature() {
        return this.temperature;
    }

    public DailyTimeRecord temperature(String temperature) {
        this.setTemperature(temperature);
        return this;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getLogDate() {
        return this.logDate;
    }

    public DailyTimeRecord logDate(String logDate) {
        this.setLogDate(logDate);
        return this;
    }

    public void setLogDate(String logDate) {
        this.logDate = logDate;
    }

    public String getLogTime() {
        return this.logTime;
    }

    public DailyTimeRecord logTime(String logTime) {
        this.setLogTime(logTime);
        return this;
    }

    public void setLogTime(String logTime) {
        this.logTime = logTime;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public DailyTimeRecord employee(Employee employee) {
        this.setEmployee(employee);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DailyTimeRecord)) {
            return false;
        }
        return id != null && id.equals(((DailyTimeRecord) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DailyTimeRecord{" +
            "id=" + getId() +
            ", employeeBiometricId=" + getEmployeeBiometricId() +
            ", inputType='" + getInputType() + "'" +
            ", attendanceType='" + getAttendanceType() + "'" +
            ", temperature='" + getTemperature() + "'" +
            ", logDate='" + getLogDate() + "'" +
            ", logTime='" + getLogTime() + "'" +
            "}";
    }
}
