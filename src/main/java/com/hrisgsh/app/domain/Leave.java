package com.hrisgsh.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Leave.
 */
@Entity
@Table(name = "jhi_leave")
public class Leave implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "date_apply")
    private LocalDate dateApply;

    @Column(name = "date_start")
    private LocalDate dateStart;

    @Column(name = "date_end")
    private LocalDate dateEnd;

    @Column(name = "date_return")
    private LocalDate dateReturn;

    @Column(name = "checkup_date")
    private LocalDate checkupDate;

    @Column(name = "convalescing_period")
    private Integer convalescingPeriod;

    @Column(name = "diagnosis")
    private String diagnosis;

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

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "leaves" }, allowSetters = true)
    private LeaveType leaveType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Leave id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateApply() {
        return this.dateApply;
    }

    public Leave dateApply(LocalDate dateApply) {
        this.setDateApply(dateApply);
        return this;
    }

    public void setDateApply(LocalDate dateApply) {
        this.dateApply = dateApply;
    }

    public LocalDate getDateStart() {
        return this.dateStart;
    }

    public Leave dateStart(LocalDate dateStart) {
        this.setDateStart(dateStart);
        return this;
    }

    public void setDateStart(LocalDate dateStart) {
        this.dateStart = dateStart;
    }

    public LocalDate getDateEnd() {
        return this.dateEnd;
    }

    public Leave dateEnd(LocalDate dateEnd) {
        this.setDateEnd(dateEnd);
        return this;
    }

    public void setDateEnd(LocalDate dateEnd) {
        this.dateEnd = dateEnd;
    }

    public LocalDate getDateReturn() {
        return this.dateReturn;
    }

    public Leave dateReturn(LocalDate dateReturn) {
        this.setDateReturn(dateReturn);
        return this;
    }

    public void setDateReturn(LocalDate dateReturn) {
        this.dateReturn = dateReturn;
    }

    public LocalDate getCheckupDate() {
        return this.checkupDate;
    }

    public Leave checkupDate(LocalDate checkupDate) {
        this.setCheckupDate(checkupDate);
        return this;
    }

    public void setCheckupDate(LocalDate checkupDate) {
        this.checkupDate = checkupDate;
    }

    public Integer getConvalescingPeriod() {
        return this.convalescingPeriod;
    }

    public Leave convalescingPeriod(Integer convalescingPeriod) {
        this.setConvalescingPeriod(convalescingPeriod);
        return this;
    }

    public void setConvalescingPeriod(Integer convalescingPeriod) {
        this.convalescingPeriod = convalescingPeriod;
    }

    public String getDiagnosis() {
        return this.diagnosis;
    }

    public Leave diagnosis(String diagnosis) {
        this.setDiagnosis(diagnosis);
        return this;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Leave employee(Employee employee) {
        this.setEmployee(employee);
        return this;
    }

    public LeaveType getLeaveType() {
        return this.leaveType;
    }

    public void setLeaveType(LeaveType leaveType) {
        this.leaveType = leaveType;
    }

    public Leave leaveType(LeaveType leaveType) {
        this.setLeaveType(leaveType);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Leave)) {
            return false;
        }
        return id != null && id.equals(((Leave) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Leave{" +
            "id=" + getId() +
            ", dateApply='" + getDateApply() + "'" +
            ", dateStart='" + getDateStart() + "'" +
            ", dateEnd='" + getDateEnd() + "'" +
            ", dateReturn='" + getDateReturn() + "'" +
            ", checkupDate='" + getCheckupDate() + "'" +
            ", convalescingPeriod=" + getConvalescingPeriod() +
            ", diagnosis='" + getDiagnosis() + "'" +
            "}";
    }
}
