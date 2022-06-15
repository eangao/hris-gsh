package com.hrisgsh.app.service.criteria;

import com.hrisgsh.app.domain.enumeration.EmploymentType;
import com.hrisgsh.app.domain.enumeration.Gender;
import com.hrisgsh.app.domain.enumeration.Status;
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
 * Criteria class for the {@link com.hrisgsh.app.domain.Employee} entity. This class is used
 * in {@link com.hrisgsh.app.web.rest.EmployeeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /employees?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class EmployeeCriteria implements Serializable, Criteria {

    /**
     * Class for filtering Gender
     */
    public static class GenderFilter extends Filter<Gender> {

        public GenderFilter() {}

        public GenderFilter(GenderFilter filter) {
            super(filter);
        }

        @Override
        public GenderFilter copy() {
            return new GenderFilter(this);
        }
    }

    /**
     * Class for filtering Status
     */
    public static class StatusFilter extends Filter<Status> {

        public StatusFilter() {}

        public StatusFilter(StatusFilter filter) {
            super(filter);
        }

        @Override
        public StatusFilter copy() {
            return new StatusFilter(this);
        }
    }

    /**
     * Class for filtering EmploymentType
     */
    public static class EmploymentTypeFilter extends Filter<EmploymentType> {

        public EmploymentTypeFilter() {}

        public EmploymentTypeFilter(EmploymentTypeFilter filter) {
            super(filter);
        }

        @Override
        public EmploymentTypeFilter copy() {
            return new EmploymentTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter employeeBiometricId;

    private StringFilter username;

    private StringFilter email;

    private StringFilter firstName;

    private StringFilter middleName;

    private StringFilter lastName;

    private StringFilter nameSuffix;

    private LocalDateFilter birthdate;

    private GenderFilter gender;

    private StatusFilter status;

    private EmploymentTypeFilter employmentType;

    private StringFilter mobileNumber;

    private LocalDateFilter dateHired;

    private LocalDateFilter dateDeno;

    private IntegerFilter sickLeaveYearlyCredit;

    private IntegerFilter sickLeaveYearlyCreditUsed;

    private IntegerFilter leaveYearlyCredit;

    private IntegerFilter leaveYearlyCreditUsed;

    private StringFilter presentAddressStreet;

    private StringFilter presentAddressCity;

    private StringFilter presentAddressProvince;

    private IntegerFilter presentAddressZipcode;

    private StringFilter homeAddressStreet;

    private StringFilter homeAddressCity;

    private StringFilter homeAddressProvince;

    private IntegerFilter homeAddressZipcode;

    private LongFilter userId;

    private LongFilter dutyScheduleId;

    private LongFilter dailyTimeRecordId;

    private LongFilter dependentsId;

    private LongFilter educationId;

    private LongFilter trainingHistoryId;

    private LongFilter leaveId;

    private LongFilter designationId;

    private LongFilter benefitsId;

    private LongFilter departmentId;

    private Boolean distinct;

    public EmployeeCriteria() {}

    public EmployeeCriteria(EmployeeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.employeeBiometricId = other.employeeBiometricId == null ? null : other.employeeBiometricId.copy();
        this.username = other.username == null ? null : other.username.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.firstName = other.firstName == null ? null : other.firstName.copy();
        this.middleName = other.middleName == null ? null : other.middleName.copy();
        this.lastName = other.lastName == null ? null : other.lastName.copy();
        this.nameSuffix = other.nameSuffix == null ? null : other.nameSuffix.copy();
        this.birthdate = other.birthdate == null ? null : other.birthdate.copy();
        this.gender = other.gender == null ? null : other.gender.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.employmentType = other.employmentType == null ? null : other.employmentType.copy();
        this.mobileNumber = other.mobileNumber == null ? null : other.mobileNumber.copy();
        this.dateHired = other.dateHired == null ? null : other.dateHired.copy();
        this.dateDeno = other.dateDeno == null ? null : other.dateDeno.copy();
        this.sickLeaveYearlyCredit = other.sickLeaveYearlyCredit == null ? null : other.sickLeaveYearlyCredit.copy();
        this.sickLeaveYearlyCreditUsed = other.sickLeaveYearlyCreditUsed == null ? null : other.sickLeaveYearlyCreditUsed.copy();
        this.leaveYearlyCredit = other.leaveYearlyCredit == null ? null : other.leaveYearlyCredit.copy();
        this.leaveYearlyCreditUsed = other.leaveYearlyCreditUsed == null ? null : other.leaveYearlyCreditUsed.copy();
        this.presentAddressStreet = other.presentAddressStreet == null ? null : other.presentAddressStreet.copy();
        this.presentAddressCity = other.presentAddressCity == null ? null : other.presentAddressCity.copy();
        this.presentAddressProvince = other.presentAddressProvince == null ? null : other.presentAddressProvince.copy();
        this.presentAddressZipcode = other.presentAddressZipcode == null ? null : other.presentAddressZipcode.copy();
        this.homeAddressStreet = other.homeAddressStreet == null ? null : other.homeAddressStreet.copy();
        this.homeAddressCity = other.homeAddressCity == null ? null : other.homeAddressCity.copy();
        this.homeAddressProvince = other.homeAddressProvince == null ? null : other.homeAddressProvince.copy();
        this.homeAddressZipcode = other.homeAddressZipcode == null ? null : other.homeAddressZipcode.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.dutyScheduleId = other.dutyScheduleId == null ? null : other.dutyScheduleId.copy();
        this.dailyTimeRecordId = other.dailyTimeRecordId == null ? null : other.dailyTimeRecordId.copy();
        this.dependentsId = other.dependentsId == null ? null : other.dependentsId.copy();
        this.educationId = other.educationId == null ? null : other.educationId.copy();
        this.trainingHistoryId = other.trainingHistoryId == null ? null : other.trainingHistoryId.copy();
        this.leaveId = other.leaveId == null ? null : other.leaveId.copy();
        this.designationId = other.designationId == null ? null : other.designationId.copy();
        this.benefitsId = other.benefitsId == null ? null : other.benefitsId.copy();
        this.departmentId = other.departmentId == null ? null : other.departmentId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public EmployeeCriteria copy() {
        return new EmployeeCriteria(this);
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

    public IntegerFilter getEmployeeBiometricId() {
        return employeeBiometricId;
    }

    public IntegerFilter employeeBiometricId() {
        if (employeeBiometricId == null) {
            employeeBiometricId = new IntegerFilter();
        }
        return employeeBiometricId;
    }

    public void setEmployeeBiometricId(IntegerFilter employeeBiometricId) {
        this.employeeBiometricId = employeeBiometricId;
    }

    public StringFilter getUsername() {
        return username;
    }

    public StringFilter username() {
        if (username == null) {
            username = new StringFilter();
        }
        return username;
    }

    public void setUsername(StringFilter username) {
        this.username = username;
    }

    public StringFilter getEmail() {
        return email;
    }

    public StringFilter email() {
        if (email == null) {
            email = new StringFilter();
        }
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public StringFilter getFirstName() {
        return firstName;
    }

    public StringFilter firstName() {
        if (firstName == null) {
            firstName = new StringFilter();
        }
        return firstName;
    }

    public void setFirstName(StringFilter firstName) {
        this.firstName = firstName;
    }

    public StringFilter getMiddleName() {
        return middleName;
    }

    public StringFilter middleName() {
        if (middleName == null) {
            middleName = new StringFilter();
        }
        return middleName;
    }

    public void setMiddleName(StringFilter middleName) {
        this.middleName = middleName;
    }

    public StringFilter getLastName() {
        return lastName;
    }

    public StringFilter lastName() {
        if (lastName == null) {
            lastName = new StringFilter();
        }
        return lastName;
    }

    public void setLastName(StringFilter lastName) {
        this.lastName = lastName;
    }

    public StringFilter getNameSuffix() {
        return nameSuffix;
    }

    public StringFilter nameSuffix() {
        if (nameSuffix == null) {
            nameSuffix = new StringFilter();
        }
        return nameSuffix;
    }

    public void setNameSuffix(StringFilter nameSuffix) {
        this.nameSuffix = nameSuffix;
    }

    public LocalDateFilter getBirthdate() {
        return birthdate;
    }

    public LocalDateFilter birthdate() {
        if (birthdate == null) {
            birthdate = new LocalDateFilter();
        }
        return birthdate;
    }

    public void setBirthdate(LocalDateFilter birthdate) {
        this.birthdate = birthdate;
    }

    public GenderFilter getGender() {
        return gender;
    }

    public GenderFilter gender() {
        if (gender == null) {
            gender = new GenderFilter();
        }
        return gender;
    }

    public void setGender(GenderFilter gender) {
        this.gender = gender;
    }

    public StatusFilter getStatus() {
        return status;
    }

    public StatusFilter status() {
        if (status == null) {
            status = new StatusFilter();
        }
        return status;
    }

    public void setStatus(StatusFilter status) {
        this.status = status;
    }

    public EmploymentTypeFilter getEmploymentType() {
        return employmentType;
    }

    public EmploymentTypeFilter employmentType() {
        if (employmentType == null) {
            employmentType = new EmploymentTypeFilter();
        }
        return employmentType;
    }

    public void setEmploymentType(EmploymentTypeFilter employmentType) {
        this.employmentType = employmentType;
    }

    public StringFilter getMobileNumber() {
        return mobileNumber;
    }

    public StringFilter mobileNumber() {
        if (mobileNumber == null) {
            mobileNumber = new StringFilter();
        }
        return mobileNumber;
    }

    public void setMobileNumber(StringFilter mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public LocalDateFilter getDateHired() {
        return dateHired;
    }

    public LocalDateFilter dateHired() {
        if (dateHired == null) {
            dateHired = new LocalDateFilter();
        }
        return dateHired;
    }

    public void setDateHired(LocalDateFilter dateHired) {
        this.dateHired = dateHired;
    }

    public LocalDateFilter getDateDeno() {
        return dateDeno;
    }

    public LocalDateFilter dateDeno() {
        if (dateDeno == null) {
            dateDeno = new LocalDateFilter();
        }
        return dateDeno;
    }

    public void setDateDeno(LocalDateFilter dateDeno) {
        this.dateDeno = dateDeno;
    }

    public IntegerFilter getSickLeaveYearlyCredit() {
        return sickLeaveYearlyCredit;
    }

    public IntegerFilter sickLeaveYearlyCredit() {
        if (sickLeaveYearlyCredit == null) {
            sickLeaveYearlyCredit = new IntegerFilter();
        }
        return sickLeaveYearlyCredit;
    }

    public void setSickLeaveYearlyCredit(IntegerFilter sickLeaveYearlyCredit) {
        this.sickLeaveYearlyCredit = sickLeaveYearlyCredit;
    }

    public IntegerFilter getSickLeaveYearlyCreditUsed() {
        return sickLeaveYearlyCreditUsed;
    }

    public IntegerFilter sickLeaveYearlyCreditUsed() {
        if (sickLeaveYearlyCreditUsed == null) {
            sickLeaveYearlyCreditUsed = new IntegerFilter();
        }
        return sickLeaveYearlyCreditUsed;
    }

    public void setSickLeaveYearlyCreditUsed(IntegerFilter sickLeaveYearlyCreditUsed) {
        this.sickLeaveYearlyCreditUsed = sickLeaveYearlyCreditUsed;
    }

    public IntegerFilter getLeaveYearlyCredit() {
        return leaveYearlyCredit;
    }

    public IntegerFilter leaveYearlyCredit() {
        if (leaveYearlyCredit == null) {
            leaveYearlyCredit = new IntegerFilter();
        }
        return leaveYearlyCredit;
    }

    public void setLeaveYearlyCredit(IntegerFilter leaveYearlyCredit) {
        this.leaveYearlyCredit = leaveYearlyCredit;
    }

    public IntegerFilter getLeaveYearlyCreditUsed() {
        return leaveYearlyCreditUsed;
    }

    public IntegerFilter leaveYearlyCreditUsed() {
        if (leaveYearlyCreditUsed == null) {
            leaveYearlyCreditUsed = new IntegerFilter();
        }
        return leaveYearlyCreditUsed;
    }

    public void setLeaveYearlyCreditUsed(IntegerFilter leaveYearlyCreditUsed) {
        this.leaveYearlyCreditUsed = leaveYearlyCreditUsed;
    }

    public StringFilter getPresentAddressStreet() {
        return presentAddressStreet;
    }

    public StringFilter presentAddressStreet() {
        if (presentAddressStreet == null) {
            presentAddressStreet = new StringFilter();
        }
        return presentAddressStreet;
    }

    public void setPresentAddressStreet(StringFilter presentAddressStreet) {
        this.presentAddressStreet = presentAddressStreet;
    }

    public StringFilter getPresentAddressCity() {
        return presentAddressCity;
    }

    public StringFilter presentAddressCity() {
        if (presentAddressCity == null) {
            presentAddressCity = new StringFilter();
        }
        return presentAddressCity;
    }

    public void setPresentAddressCity(StringFilter presentAddressCity) {
        this.presentAddressCity = presentAddressCity;
    }

    public StringFilter getPresentAddressProvince() {
        return presentAddressProvince;
    }

    public StringFilter presentAddressProvince() {
        if (presentAddressProvince == null) {
            presentAddressProvince = new StringFilter();
        }
        return presentAddressProvince;
    }

    public void setPresentAddressProvince(StringFilter presentAddressProvince) {
        this.presentAddressProvince = presentAddressProvince;
    }

    public IntegerFilter getPresentAddressZipcode() {
        return presentAddressZipcode;
    }

    public IntegerFilter presentAddressZipcode() {
        if (presentAddressZipcode == null) {
            presentAddressZipcode = new IntegerFilter();
        }
        return presentAddressZipcode;
    }

    public void setPresentAddressZipcode(IntegerFilter presentAddressZipcode) {
        this.presentAddressZipcode = presentAddressZipcode;
    }

    public StringFilter getHomeAddressStreet() {
        return homeAddressStreet;
    }

    public StringFilter homeAddressStreet() {
        if (homeAddressStreet == null) {
            homeAddressStreet = new StringFilter();
        }
        return homeAddressStreet;
    }

    public void setHomeAddressStreet(StringFilter homeAddressStreet) {
        this.homeAddressStreet = homeAddressStreet;
    }

    public StringFilter getHomeAddressCity() {
        return homeAddressCity;
    }

    public StringFilter homeAddressCity() {
        if (homeAddressCity == null) {
            homeAddressCity = new StringFilter();
        }
        return homeAddressCity;
    }

    public void setHomeAddressCity(StringFilter homeAddressCity) {
        this.homeAddressCity = homeAddressCity;
    }

    public StringFilter getHomeAddressProvince() {
        return homeAddressProvince;
    }

    public StringFilter homeAddressProvince() {
        if (homeAddressProvince == null) {
            homeAddressProvince = new StringFilter();
        }
        return homeAddressProvince;
    }

    public void setHomeAddressProvince(StringFilter homeAddressProvince) {
        this.homeAddressProvince = homeAddressProvince;
    }

    public IntegerFilter getHomeAddressZipcode() {
        return homeAddressZipcode;
    }

    public IntegerFilter homeAddressZipcode() {
        if (homeAddressZipcode == null) {
            homeAddressZipcode = new IntegerFilter();
        }
        return homeAddressZipcode;
    }

    public void setHomeAddressZipcode(IntegerFilter homeAddressZipcode) {
        this.homeAddressZipcode = homeAddressZipcode;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public LongFilter userId() {
        if (userId == null) {
            userId = new LongFilter();
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getDutyScheduleId() {
        return dutyScheduleId;
    }

    public LongFilter dutyScheduleId() {
        if (dutyScheduleId == null) {
            dutyScheduleId = new LongFilter();
        }
        return dutyScheduleId;
    }

    public void setDutyScheduleId(LongFilter dutyScheduleId) {
        this.dutyScheduleId = dutyScheduleId;
    }

    public LongFilter getDailyTimeRecordId() {
        return dailyTimeRecordId;
    }

    public LongFilter dailyTimeRecordId() {
        if (dailyTimeRecordId == null) {
            dailyTimeRecordId = new LongFilter();
        }
        return dailyTimeRecordId;
    }

    public void setDailyTimeRecordId(LongFilter dailyTimeRecordId) {
        this.dailyTimeRecordId = dailyTimeRecordId;
    }

    public LongFilter getDependentsId() {
        return dependentsId;
    }

    public LongFilter dependentsId() {
        if (dependentsId == null) {
            dependentsId = new LongFilter();
        }
        return dependentsId;
    }

    public void setDependentsId(LongFilter dependentsId) {
        this.dependentsId = dependentsId;
    }

    public LongFilter getEducationId() {
        return educationId;
    }

    public LongFilter educationId() {
        if (educationId == null) {
            educationId = new LongFilter();
        }
        return educationId;
    }

    public void setEducationId(LongFilter educationId) {
        this.educationId = educationId;
    }

    public LongFilter getTrainingHistoryId() {
        return trainingHistoryId;
    }

    public LongFilter trainingHistoryId() {
        if (trainingHistoryId == null) {
            trainingHistoryId = new LongFilter();
        }
        return trainingHistoryId;
    }

    public void setTrainingHistoryId(LongFilter trainingHistoryId) {
        this.trainingHistoryId = trainingHistoryId;
    }

    public LongFilter getLeaveId() {
        return leaveId;
    }

    public LongFilter leaveId() {
        if (leaveId == null) {
            leaveId = new LongFilter();
        }
        return leaveId;
    }

    public void setLeaveId(LongFilter leaveId) {
        this.leaveId = leaveId;
    }

    public LongFilter getDesignationId() {
        return designationId;
    }

    public LongFilter designationId() {
        if (designationId == null) {
            designationId = new LongFilter();
        }
        return designationId;
    }

    public void setDesignationId(LongFilter designationId) {
        this.designationId = designationId;
    }

    public LongFilter getBenefitsId() {
        return benefitsId;
    }

    public LongFilter benefitsId() {
        if (benefitsId == null) {
            benefitsId = new LongFilter();
        }
        return benefitsId;
    }

    public void setBenefitsId(LongFilter benefitsId) {
        this.benefitsId = benefitsId;
    }

    public LongFilter getDepartmentId() {
        return departmentId;
    }

    public LongFilter departmentId() {
        if (departmentId == null) {
            departmentId = new LongFilter();
        }
        return departmentId;
    }

    public void setDepartmentId(LongFilter departmentId) {
        this.departmentId = departmentId;
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
        final EmployeeCriteria that = (EmployeeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(employeeBiometricId, that.employeeBiometricId) &&
            Objects.equals(username, that.username) &&
            Objects.equals(email, that.email) &&
            Objects.equals(firstName, that.firstName) &&
            Objects.equals(middleName, that.middleName) &&
            Objects.equals(lastName, that.lastName) &&
            Objects.equals(nameSuffix, that.nameSuffix) &&
            Objects.equals(birthdate, that.birthdate) &&
            Objects.equals(gender, that.gender) &&
            Objects.equals(status, that.status) &&
            Objects.equals(employmentType, that.employmentType) &&
            Objects.equals(mobileNumber, that.mobileNumber) &&
            Objects.equals(dateHired, that.dateHired) &&
            Objects.equals(dateDeno, that.dateDeno) &&
            Objects.equals(sickLeaveYearlyCredit, that.sickLeaveYearlyCredit) &&
            Objects.equals(sickLeaveYearlyCreditUsed, that.sickLeaveYearlyCreditUsed) &&
            Objects.equals(leaveYearlyCredit, that.leaveYearlyCredit) &&
            Objects.equals(leaveYearlyCreditUsed, that.leaveYearlyCreditUsed) &&
            Objects.equals(presentAddressStreet, that.presentAddressStreet) &&
            Objects.equals(presentAddressCity, that.presentAddressCity) &&
            Objects.equals(presentAddressProvince, that.presentAddressProvince) &&
            Objects.equals(presentAddressZipcode, that.presentAddressZipcode) &&
            Objects.equals(homeAddressStreet, that.homeAddressStreet) &&
            Objects.equals(homeAddressCity, that.homeAddressCity) &&
            Objects.equals(homeAddressProvince, that.homeAddressProvince) &&
            Objects.equals(homeAddressZipcode, that.homeAddressZipcode) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(dutyScheduleId, that.dutyScheduleId) &&
            Objects.equals(dailyTimeRecordId, that.dailyTimeRecordId) &&
            Objects.equals(dependentsId, that.dependentsId) &&
            Objects.equals(educationId, that.educationId) &&
            Objects.equals(trainingHistoryId, that.trainingHistoryId) &&
            Objects.equals(leaveId, that.leaveId) &&
            Objects.equals(designationId, that.designationId) &&
            Objects.equals(benefitsId, that.benefitsId) &&
            Objects.equals(departmentId, that.departmentId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            employeeBiometricId,
            username,
            email,
            firstName,
            middleName,
            lastName,
            nameSuffix,
            birthdate,
            gender,
            status,
            employmentType,
            mobileNumber,
            dateHired,
            dateDeno,
            sickLeaveYearlyCredit,
            sickLeaveYearlyCreditUsed,
            leaveYearlyCredit,
            leaveYearlyCreditUsed,
            presentAddressStreet,
            presentAddressCity,
            presentAddressProvince,
            presentAddressZipcode,
            homeAddressStreet,
            homeAddressCity,
            homeAddressProvince,
            homeAddressZipcode,
            userId,
            dutyScheduleId,
            dailyTimeRecordId,
            dependentsId,
            educationId,
            trainingHistoryId,
            leaveId,
            designationId,
            benefitsId,
            departmentId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmployeeCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (employeeBiometricId != null ? "employeeBiometricId=" + employeeBiometricId + ", " : "") +
            (username != null ? "username=" + username + ", " : "") +
            (email != null ? "email=" + email + ", " : "") +
            (firstName != null ? "firstName=" + firstName + ", " : "") +
            (middleName != null ? "middleName=" + middleName + ", " : "") +
            (lastName != null ? "lastName=" + lastName + ", " : "") +
            (nameSuffix != null ? "nameSuffix=" + nameSuffix + ", " : "") +
            (birthdate != null ? "birthdate=" + birthdate + ", " : "") +
            (gender != null ? "gender=" + gender + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (employmentType != null ? "employmentType=" + employmentType + ", " : "") +
            (mobileNumber != null ? "mobileNumber=" + mobileNumber + ", " : "") +
            (dateHired != null ? "dateHired=" + dateHired + ", " : "") +
            (dateDeno != null ? "dateDeno=" + dateDeno + ", " : "") +
            (sickLeaveYearlyCredit != null ? "sickLeaveYearlyCredit=" + sickLeaveYearlyCredit + ", " : "") +
            (sickLeaveYearlyCreditUsed != null ? "sickLeaveYearlyCreditUsed=" + sickLeaveYearlyCreditUsed + ", " : "") +
            (leaveYearlyCredit != null ? "leaveYearlyCredit=" + leaveYearlyCredit + ", " : "") +
            (leaveYearlyCreditUsed != null ? "leaveYearlyCreditUsed=" + leaveYearlyCreditUsed + ", " : "") +
            (presentAddressStreet != null ? "presentAddressStreet=" + presentAddressStreet + ", " : "") +
            (presentAddressCity != null ? "presentAddressCity=" + presentAddressCity + ", " : "") +
            (presentAddressProvince != null ? "presentAddressProvince=" + presentAddressProvince + ", " : "") +
            (presentAddressZipcode != null ? "presentAddressZipcode=" + presentAddressZipcode + ", " : "") +
            (homeAddressStreet != null ? "homeAddressStreet=" + homeAddressStreet + ", " : "") +
            (homeAddressCity != null ? "homeAddressCity=" + homeAddressCity + ", " : "") +
            (homeAddressProvince != null ? "homeAddressProvince=" + homeAddressProvince + ", " : "") +
            (homeAddressZipcode != null ? "homeAddressZipcode=" + homeAddressZipcode + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (dutyScheduleId != null ? "dutyScheduleId=" + dutyScheduleId + ", " : "") +
            (dailyTimeRecordId != null ? "dailyTimeRecordId=" + dailyTimeRecordId + ", " : "") +
            (dependentsId != null ? "dependentsId=" + dependentsId + ", " : "") +
            (educationId != null ? "educationId=" + educationId + ", " : "") +
            (trainingHistoryId != null ? "trainingHistoryId=" + trainingHistoryId + ", " : "") +
            (leaveId != null ? "leaveId=" + leaveId + ", " : "") +
            (designationId != null ? "designationId=" + designationId + ", " : "") +
            (benefitsId != null ? "benefitsId=" + benefitsId + ", " : "") +
            (departmentId != null ? "departmentId=" + departmentId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
