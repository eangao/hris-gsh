package com.hrisgsh.app.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.hrisgsh.app.domain.DutySchedule} entity.
 */
public class DutyScheduleDTO implements Serializable {

    private Long id;

    private Instant dateTimeIn;

    private Instant dateTimeOut;

    private EmployeeDTO employee;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDateTimeIn() {
        return dateTimeIn;
    }

    public void setDateTimeIn(Instant dateTimeIn) {
        this.dateTimeIn = dateTimeIn;
    }

    public Instant getDateTimeOut() {
        return dateTimeOut;
    }

    public void setDateTimeOut(Instant dateTimeOut) {
        this.dateTimeOut = dateTimeOut;
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
        if (!(o instanceof DutyScheduleDTO)) {
            return false;
        }

        DutyScheduleDTO dutyScheduleDTO = (DutyScheduleDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, dutyScheduleDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DutyScheduleDTO{" +
            "id=" + getId() +
            ", dateTimeIn='" + getDateTimeIn() + "'" +
            ", dateTimeOut='" + getDateTimeOut() + "'" +
            ", employee=" + getEmployee() +
            "}";
    }
}
