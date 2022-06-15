package com.hrisgsh.app.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.hrisgsh.app.domain.Leave} entity. This class is used
 * in {@link com.hrisgsh.app.web.rest.LeaveResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /leaves?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class LeaveCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter dateApply;

    private LocalDateFilter dateStart;

    private LocalDateFilter dateEnd;

    private LocalDateFilter dateReturn;

    private LocalDateFilter checkupDate;

    private IntegerFilter convalescingPeriod;

    private StringFilter diagnosis;

    private LongFilter employeeId;

    private LongFilter leaveTypeId;

    private Boolean distinct;

    public LeaveCriteria() {}

    public LeaveCriteria(LeaveCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.dateApply = other.dateApply == null ? null : other.dateApply.copy();
        this.dateStart = other.dateStart == null ? null : other.dateStart.copy();
        this.dateEnd = other.dateEnd == null ? null : other.dateEnd.copy();
        this.dateReturn = other.dateReturn == null ? null : other.dateReturn.copy();
        this.checkupDate = other.checkupDate == null ? null : other.checkupDate.copy();
        this.convalescingPeriod = other.convalescingPeriod == null ? null : other.convalescingPeriod.copy();
        this.diagnosis = other.diagnosis == null ? null : other.diagnosis.copy();
        this.employeeId = other.employeeId == null ? null : other.employeeId.copy();
        this.leaveTypeId = other.leaveTypeId == null ? null : other.leaveTypeId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public LeaveCriteria copy() {
        return new LeaveCriteria(this);
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

    public LocalDateFilter getDateApply() {
        return dateApply;
    }

    public LocalDateFilter dateApply() {
        if (dateApply == null) {
            dateApply = new LocalDateFilter();
        }
        return dateApply;
    }

    public void setDateApply(LocalDateFilter dateApply) {
        this.dateApply = dateApply;
    }

    public LocalDateFilter getDateStart() {
        return dateStart;
    }

    public LocalDateFilter dateStart() {
        if (dateStart == null) {
            dateStart = new LocalDateFilter();
        }
        return dateStart;
    }

    public void setDateStart(LocalDateFilter dateStart) {
        this.dateStart = dateStart;
    }

    public LocalDateFilter getDateEnd() {
        return dateEnd;
    }

    public LocalDateFilter dateEnd() {
        if (dateEnd == null) {
            dateEnd = new LocalDateFilter();
        }
        return dateEnd;
    }

    public void setDateEnd(LocalDateFilter dateEnd) {
        this.dateEnd = dateEnd;
    }

    public LocalDateFilter getDateReturn() {
        return dateReturn;
    }

    public LocalDateFilter dateReturn() {
        if (dateReturn == null) {
            dateReturn = new LocalDateFilter();
        }
        return dateReturn;
    }

    public void setDateReturn(LocalDateFilter dateReturn) {
        this.dateReturn = dateReturn;
    }

    public LocalDateFilter getCheckupDate() {
        return checkupDate;
    }

    public LocalDateFilter checkupDate() {
        if (checkupDate == null) {
            checkupDate = new LocalDateFilter();
        }
        return checkupDate;
    }

    public void setCheckupDate(LocalDateFilter checkupDate) {
        this.checkupDate = checkupDate;
    }

    public IntegerFilter getConvalescingPeriod() {
        return convalescingPeriod;
    }

    public IntegerFilter convalescingPeriod() {
        if (convalescingPeriod == null) {
            convalescingPeriod = new IntegerFilter();
        }
        return convalescingPeriod;
    }

    public void setConvalescingPeriod(IntegerFilter convalescingPeriod) {
        this.convalescingPeriod = convalescingPeriod;
    }

    public StringFilter getDiagnosis() {
        return diagnosis;
    }

    public StringFilter diagnosis() {
        if (diagnosis == null) {
            diagnosis = new StringFilter();
        }
        return diagnosis;
    }

    public void setDiagnosis(StringFilter diagnosis) {
        this.diagnosis = diagnosis;
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

    public LongFilter getLeaveTypeId() {
        return leaveTypeId;
    }

    public LongFilter leaveTypeId() {
        if (leaveTypeId == null) {
            leaveTypeId = new LongFilter();
        }
        return leaveTypeId;
    }

    public void setLeaveTypeId(LongFilter leaveTypeId) {
        this.leaveTypeId = leaveTypeId;
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
        final LeaveCriteria that = (LeaveCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(dateApply, that.dateApply) &&
            Objects.equals(dateStart, that.dateStart) &&
            Objects.equals(dateEnd, that.dateEnd) &&
            Objects.equals(dateReturn, that.dateReturn) &&
            Objects.equals(checkupDate, that.checkupDate) &&
            Objects.equals(convalescingPeriod, that.convalescingPeriod) &&
            Objects.equals(diagnosis, that.diagnosis) &&
            Objects.equals(employeeId, that.employeeId) &&
            Objects.equals(leaveTypeId, that.leaveTypeId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            dateApply,
            dateStart,
            dateEnd,
            dateReturn,
            checkupDate,
            convalescingPeriod,
            diagnosis,
            employeeId,
            leaveTypeId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LeaveCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (dateApply != null ? "dateApply=" + dateApply + ", " : "") +
            (dateStart != null ? "dateStart=" + dateStart + ", " : "") +
            (dateEnd != null ? "dateEnd=" + dateEnd + ", " : "") +
            (dateReturn != null ? "dateReturn=" + dateReturn + ", " : "") +
            (checkupDate != null ? "checkupDate=" + checkupDate + ", " : "") +
            (convalescingPeriod != null ? "convalescingPeriod=" + convalescingPeriod + ", " : "") +
            (diagnosis != null ? "diagnosis=" + diagnosis + ", " : "") +
            (employeeId != null ? "employeeId=" + employeeId + ", " : "") +
            (leaveTypeId != null ? "leaveTypeId=" + leaveTypeId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
