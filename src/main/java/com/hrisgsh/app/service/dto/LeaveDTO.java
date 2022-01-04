package com.hrisgsh.app.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.hrisgsh.app.domain.Leave} entity.
 */
public class LeaveDTO implements Serializable {

    private Long id;

    private LocalDate dateApply;

    private LocalDate dateStart;

    private LocalDate dateEnd;

    private LocalDate dateReturn;

    private LocalDate checkupDate;

    private Integer convalescingPeriod;

    private String diagnosis;

    private EmployeeDTO employee;

    private LeaveTypeDTO leaveType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateApply() {
        return dateApply;
    }

    public void setDateApply(LocalDate dateApply) {
        this.dateApply = dateApply;
    }

    public LocalDate getDateStart() {
        return dateStart;
    }

    public void setDateStart(LocalDate dateStart) {
        this.dateStart = dateStart;
    }

    public LocalDate getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(LocalDate dateEnd) {
        this.dateEnd = dateEnd;
    }

    public LocalDate getDateReturn() {
        return dateReturn;
    }

    public void setDateReturn(LocalDate dateReturn) {
        this.dateReturn = dateReturn;
    }

    public LocalDate getCheckupDate() {
        return checkupDate;
    }

    public void setCheckupDate(LocalDate checkupDate) {
        this.checkupDate = checkupDate;
    }

    public Integer getConvalescingPeriod() {
        return convalescingPeriod;
    }

    public void setConvalescingPeriod(Integer convalescingPeriod) {
        this.convalescingPeriod = convalescingPeriod;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public EmployeeDTO getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeDTO employee) {
        this.employee = employee;
    }

    public LeaveTypeDTO getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(LeaveTypeDTO leaveType) {
        this.leaveType = leaveType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LeaveDTO)) {
            return false;
        }

        LeaveDTO leaveDTO = (LeaveDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, leaveDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LeaveDTO{" +
            "id=" + getId() +
            ", dateApply='" + getDateApply() + "'" +
            ", dateStart='" + getDateStart() + "'" +
            ", dateEnd='" + getDateEnd() + "'" +
            ", dateReturn='" + getDateReturn() + "'" +
            ", checkupDate='" + getCheckupDate() + "'" +
            ", convalescingPeriod=" + getConvalescingPeriod() +
            ", diagnosis='" + getDiagnosis() + "'" +
            ", employee=" + getEmployee() +
            ", leaveType=" + getLeaveType() +
            "}";
    }
}
