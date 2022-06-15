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
 * Criteria class for the {@link com.hrisgsh.app.domain.Education} entity. This class is used
 * in {@link com.hrisgsh.app.web.rest.EducationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /educations?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class EducationCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter bachelorDegree;

    private IntegerFilter yearGraduated;

    private StringFilter school;

    private LongFilter employeeId;

    private Boolean distinct;

    public EducationCriteria() {}

    public EducationCriteria(EducationCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.bachelorDegree = other.bachelorDegree == null ? null : other.bachelorDegree.copy();
        this.yearGraduated = other.yearGraduated == null ? null : other.yearGraduated.copy();
        this.school = other.school == null ? null : other.school.copy();
        this.employeeId = other.employeeId == null ? null : other.employeeId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public EducationCriteria copy() {
        return new EducationCriteria(this);
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

    public StringFilter getBachelorDegree() {
        return bachelorDegree;
    }

    public StringFilter bachelorDegree() {
        if (bachelorDegree == null) {
            bachelorDegree = new StringFilter();
        }
        return bachelorDegree;
    }

    public void setBachelorDegree(StringFilter bachelorDegree) {
        this.bachelorDegree = bachelorDegree;
    }

    public IntegerFilter getYearGraduated() {
        return yearGraduated;
    }

    public IntegerFilter yearGraduated() {
        if (yearGraduated == null) {
            yearGraduated = new IntegerFilter();
        }
        return yearGraduated;
    }

    public void setYearGraduated(IntegerFilter yearGraduated) {
        this.yearGraduated = yearGraduated;
    }

    public StringFilter getSchool() {
        return school;
    }

    public StringFilter school() {
        if (school == null) {
            school = new StringFilter();
        }
        return school;
    }

    public void setSchool(StringFilter school) {
        this.school = school;
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
        final EducationCriteria that = (EducationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(bachelorDegree, that.bachelorDegree) &&
            Objects.equals(yearGraduated, that.yearGraduated) &&
            Objects.equals(school, that.school) &&
            Objects.equals(employeeId, that.employeeId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, bachelorDegree, yearGraduated, school, employeeId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EducationCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (bachelorDegree != null ? "bachelorDegree=" + bachelorDegree + ", " : "") +
            (yearGraduated != null ? "yearGraduated=" + yearGraduated + ", " : "") +
            (school != null ? "school=" + school + ", " : "") +
            (employeeId != null ? "employeeId=" + employeeId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
