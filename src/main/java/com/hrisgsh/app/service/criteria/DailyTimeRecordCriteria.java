package com.hrisgsh.app.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.hrisgsh.app.domain.DailyTimeRecord} entity. This class is used
 * in {@link com.hrisgsh.app.web.rest.DailyTimeRecordResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /daily-time-records?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class DailyTimeRecordCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter employeeBiometricId;

    private StringFilter inputType;

    private StringFilter attendanceType;

    private StringFilter temperature;

    private StringFilter logDate;

    private StringFilter logTime;

    private LongFilter employeeId;

    private Boolean distinct;

    public DailyTimeRecordCriteria() {}

    public DailyTimeRecordCriteria(DailyTimeRecordCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.employeeBiometricId = other.employeeBiometricId == null ? null : other.employeeBiometricId.copy();
        this.inputType = other.inputType == null ? null : other.inputType.copy();
        this.attendanceType = other.attendanceType == null ? null : other.attendanceType.copy();
        this.temperature = other.temperature == null ? null : other.temperature.copy();
        this.logDate = other.logDate == null ? null : other.logDate.copy();
        this.logTime = other.logTime == null ? null : other.logTime.copy();
        this.employeeId = other.employeeId == null ? null : other.employeeId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public DailyTimeRecordCriteria copy() {
        return new DailyTimeRecordCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getEmployeeBiometricId() {
        return employeeBiometricId;
    }

    public LongFilter employeeBiometricId() {
        if (employeeBiometricId == null) {
            employeeBiometricId = new LongFilter();
        }
        return employeeBiometricId;
    }

    public void setEmployeeBiometricId(LongFilter employeeBiometricId) {
        this.employeeBiometricId = employeeBiometricId;
    }

    public StringFilter getInputType() {
        return inputType;
    }

    public StringFilter inputType() {
        if (inputType == null) {
            inputType = new StringFilter();
        }
        return inputType;
    }

    public void setInputType(StringFilter inputType) {
        this.inputType = inputType;
    }

    public StringFilter getAttendanceType() {
        return attendanceType;
    }

    public StringFilter attendanceType() {
        if (attendanceType == null) {
            attendanceType = new StringFilter();
        }
        return attendanceType;
    }

    public void setAttendanceType(StringFilter attendanceType) {
        this.attendanceType = attendanceType;
    }

    public StringFilter getTemperature() {
        return temperature;
    }

    public StringFilter temperature() {
        if (temperature == null) {
            temperature = new StringFilter();
        }
        return temperature;
    }

    public void setTemperature(StringFilter temperature) {
        this.temperature = temperature;
    }

    public StringFilter getLogDate() {
        return logDate;
    }

    public StringFilter logDate() {
        if (logDate == null) {
            logDate = new StringFilter();
        }
        return logDate;
    }

    public void setLogDate(StringFilter logDate) {
        this.logDate = logDate;
    }

    public StringFilter getLogTime() {
        return logTime;
    }

    public StringFilter logTime() {
        if (logTime == null) {
            logTime = new StringFilter();
        }
        return logTime;
    }

    public void setLogTime(StringFilter logTime) {
        this.logTime = logTime;
    }

    public LongFilter getEmployeeId() {
        return employeeId;
    }

    public LongFilter employeeId() {
        if (employeeId == null) {
            employeeId = new LongFilter();
        }
        return employeeId;
    }

    public void setEmployeeId(LongFilter employeeId) {
        this.employeeId = employeeId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DailyTimeRecordCriteria that = (DailyTimeRecordCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(employeeBiometricId, that.employeeBiometricId) &&
            Objects.equals(inputType, that.inputType) &&
            Objects.equals(attendanceType, that.attendanceType) &&
            Objects.equals(temperature, that.temperature) &&
            Objects.equals(logDate, that.logDate) &&
            Objects.equals(logTime, that.logTime) &&
            Objects.equals(employeeId, that.employeeId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, employeeBiometricId, inputType, attendanceType, temperature, logDate, logTime, employeeId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DailyTimeRecordCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (employeeBiometricId != null ? "employeeBiometricId=" + employeeBiometricId + ", " : "") +
            (inputType != null ? "inputType=" + inputType + ", " : "") +
            (attendanceType != null ? "attendanceType=" + attendanceType + ", " : "") +
            (temperature != null ? "temperature=" + temperature + ", " : "") +
            (logDate != null ? "logDate=" + logDate + ", " : "") +
            (logTime != null ? "logTime=" + logTime + ", " : "") +
            (employeeId != null ? "employeeId=" + employeeId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
