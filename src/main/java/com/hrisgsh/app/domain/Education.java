package com.hrisgsh.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Education.
 */
@Entity
@Table(name = "education")
public class Education implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "bachelor_degree", length = 100, nullable = false)
    private String bachelorDegree;

    @NotNull
    @Column(name = "year_graduated", nullable = false)
    private Integer yearGraduated;

    @NotNull
    @Size(max = 200)
    @Column(name = "school", length = 200, nullable = false)
    private String school;

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

    public Education id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBachelorDegree() {
        return this.bachelorDegree;
    }

    public Education bachelorDegree(String bachelorDegree) {
        this.setBachelorDegree(bachelorDegree);
        return this;
    }

    public void setBachelorDegree(String bachelorDegree) {
        this.bachelorDegree = bachelorDegree;
    }

    public Integer getYearGraduated() {
        return this.yearGraduated;
    }

    public Education yearGraduated(Integer yearGraduated) {
        this.setYearGraduated(yearGraduated);
        return this;
    }

    public void setYearGraduated(Integer yearGraduated) {
        this.yearGraduated = yearGraduated;
    }

    public String getSchool() {
        return this.school;
    }

    public Education school(String school) {
        this.setSchool(school);
        return this;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Education employee(Employee employee) {
        this.setEmployee(employee);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Education)) {
            return false;
        }
        return id != null && id.equals(((Education) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Education{" +
            "id=" + getId() +
            ", bachelorDegree='" + getBachelorDegree() + "'" +
            ", yearGraduated=" + getYearGraduated() +
            ", school='" + getSchool() + "'" +
            "}";
    }
}
