package com.hrisgsh.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A DutySchedule.
 */
@Entity
@Table(name = "duty_schedule")
public class DutySchedule implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "date_time_in")
    private Instant dateTimeIn;

    @Column(name = "date_time_out")
    private Instant dateTimeOut;

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

    public DutySchedule id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDateTimeIn() {
        return this.dateTimeIn;
    }

    public DutySchedule dateTimeIn(Instant dateTimeIn) {
        this.setDateTimeIn(dateTimeIn);
        return this;
    }

    public void setDateTimeIn(Instant dateTimeIn) {
        this.dateTimeIn = dateTimeIn;
    }

    public Instant getDateTimeOut() {
        return this.dateTimeOut;
    }

    public DutySchedule dateTimeOut(Instant dateTimeOut) {
        this.setDateTimeOut(dateTimeOut);
        return this;
    }

    public void setDateTimeOut(Instant dateTimeOut) {
        this.dateTimeOut = dateTimeOut;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public DutySchedule employee(Employee employee) {
        this.setEmployee(employee);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DutySchedule)) {
            return false;
        }
        return id != null && id.equals(((DutySchedule) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DutySchedule{" +
            "id=" + getId() +
            ", dateTimeIn='" + getDateTimeIn() + "'" +
            ", dateTimeOut='" + getDateTimeOut() + "'" +
            "}";
    }
}
