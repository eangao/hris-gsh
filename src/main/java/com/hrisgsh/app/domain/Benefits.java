package com.hrisgsh.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Benefits.
 */
@Entity
@Table(name = "benefits")
public class Benefits implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @ManyToMany(mappedBy = "benefits")
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
    private Set<Employee> employees = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Benefits id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Benefits name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Employee> getEmployees() {
        return this.employees;
    }

    public void setEmployees(Set<Employee> employees) {
        if (this.employees != null) {
            this.employees.forEach(i -> i.removeBenefits(this));
        }
        if (employees != null) {
            employees.forEach(i -> i.addBenefits(this));
        }
        this.employees = employees;
    }

    public Benefits employees(Set<Employee> employees) {
        this.setEmployees(employees);
        return this;
    }

    public Benefits addEmployee(Employee employee) {
        this.employees.add(employee);
        employee.getBenefits().add(this);
        return this;
    }

    public Benefits removeEmployee(Employee employee) {
        this.employees.remove(employee);
        employee.getBenefits().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Benefits)) {
            return false;
        }
        return id != null && id.equals(((Benefits) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Benefits{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
