package com.hrisgsh.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hrisgsh.app.domain.enumeration.EmploymentType;
import com.hrisgsh.app.domain.enumeration.Gender;
import com.hrisgsh.app.domain.enumeration.Status;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Employee.
 */
@Entity
@Table(name = "employee")
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "employee_biometric_id", nullable = false, unique = true)
    private Integer employeeBiometricId;

    @NotNull
    @Size(min = 5, max = 50)
    @Column(name = "username", length = 50, nullable = false, unique = true)
    private String username;

    @Size(min = 5, max = 254)
    @Column(name = "email", length = 254, unique = true)
    private String email;

    @NotNull
    @Size(max = 50)
    @Column(name = "first_name", length = 50, nullable = false)
    private String firstName;

    @Size(max = 50)
    @Column(name = "middle_name", length = 50)
    private String middleName;

    @NotNull
    @Size(max = 50)
    @Column(name = "last_name", length = 50, nullable = false)
    private String lastName;

    @Size(max = 15)
    @Column(name = "name_suffix", length = 15)
    private String nameSuffix;

    @NotNull
    @Column(name = "birthdate", nullable = false)
    private LocalDate birthdate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "employment_type", nullable = false)
    private EmploymentType employmentType;

    @Size(max = 20)
    @Column(name = "mobile_number", length = 20)
    private String mobileNumber;

    @Column(name = "date_hired")
    private LocalDate dateHired;

    @Column(name = "date_deno")
    private LocalDate dateDeno;

    @Column(name = "sick_leave_yearly_credit")
    private Integer sickLeaveYearlyCredit;

    @Column(name = "sick_leave_yearly_credit_used")
    private Integer sickLeaveYearlyCreditUsed;

    @Column(name = "leave_yearly_credit")
    private Integer leaveYearlyCredit;

    @Column(name = "leave_yearly_credit_used")
    private Integer leaveYearlyCreditUsed;

    @Lob
    @Column(name = "profile_image")
    private byte[] profileImage;

    @Column(name = "profile_image_content_type")
    private String profileImageContentType;

    @Column(name = "present_address_street")
    private String presentAddressStreet;

    @Column(name = "present_address_city")
    private String presentAddressCity;

    @Column(name = "present_address_province")
    private String presentAddressProvince;

    @Column(name = "present_address_zipcode")
    private Integer presentAddressZipcode;

    @Column(name = "home_address_street")
    private String homeAddressStreet;

    @Column(name = "home_address_city")
    private String homeAddressCity;

    @Column(name = "home_address_province")
    private String homeAddressProvince;

    @Column(name = "home_address_zipcode")
    private Integer homeAddressZipcode;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(mappedBy = "employee")
    @JsonIgnoreProperties(value = { "employee" }, allowSetters = true)
    private Set<DutySchedule> dutySchedules = new HashSet<>();

    @OneToMany(mappedBy = "employee")
    @JsonIgnoreProperties(value = { "employee" }, allowSetters = true)
    private Set<DailyTimeRecord> dailyTimeRecords = new HashSet<>();

    @OneToMany(mappedBy = "employee")
    @JsonIgnoreProperties(value = { "employee" }, allowSetters = true)
    private Set<Dependents> dependents = new HashSet<>();

    @OneToMany(mappedBy = "employee")
    @JsonIgnoreProperties(value = { "employee" }, allowSetters = true)
    private Set<Education> educations = new HashSet<>();

    @OneToMany(mappedBy = "employee")
    @JsonIgnoreProperties(value = { "employee" }, allowSetters = true)
    private Set<TrainingHistory> trainingHistories = new HashSet<>();

    @OneToMany(mappedBy = "employee")
    @JsonIgnoreProperties(value = { "employee", "leaveType" }, allowSetters = true)
    private Set<Leave> leaves = new HashSet<>();

    @ManyToMany
    @NotNull
    @JoinTable(
        name = "rel_employee__designation",
        joinColumns = @JoinColumn(name = "employee_id"),
        inverseJoinColumns = @JoinColumn(name = "designation_id")
    )
    @JsonIgnoreProperties(value = { "employees" }, allowSetters = true)
    private Set<Designation> designations = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_employee__benefits",
        joinColumns = @JoinColumn(name = "employee_id"),
        inverseJoinColumns = @JoinColumn(name = "benefits_id")
    )
    @JsonIgnoreProperties(value = { "employees" }, allowSetters = true)
    private Set<Benefits> benefits = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "employees", "cluster" }, allowSetters = true)
    private Department department;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Employee id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getEmployeeBiometricId() {
        return this.employeeBiometricId;
    }

    public Employee employeeBiometricId(Integer employeeBiometricId) {
        this.setEmployeeBiometricId(employeeBiometricId);
        return this;
    }

    public void setEmployeeBiometricId(Integer employeeBiometricId) {
        this.employeeBiometricId = employeeBiometricId;
    }

    public String getUsername() {
        return this.username;
    }

    public Employee username(String username) {
        this.setUsername(username);
        return this;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return this.email;
    }

    public Employee email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public Employee firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return this.middleName;
    }

    public Employee middleName(String middleName) {
        this.setMiddleName(middleName);
        return this;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public Employee lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNameSuffix() {
        return this.nameSuffix;
    }

    public Employee nameSuffix(String nameSuffix) {
        this.setNameSuffix(nameSuffix);
        return this;
    }

    public void setNameSuffix(String nameSuffix) {
        this.nameSuffix = nameSuffix;
    }

    public LocalDate getBirthdate() {
        return this.birthdate;
    }

    public Employee birthdate(LocalDate birthdate) {
        this.setBirthdate(birthdate);
        return this;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public Gender getGender() {
        return this.gender;
    }

    public Employee gender(Gender gender) {
        this.setGender(gender);
        return this;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Status getStatus() {
        return this.status;
    }

    public Employee status(Status status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public EmploymentType getEmploymentType() {
        return this.employmentType;
    }

    public Employee employmentType(EmploymentType employmentType) {
        this.setEmploymentType(employmentType);
        return this;
    }

    public void setEmploymentType(EmploymentType employmentType) {
        this.employmentType = employmentType;
    }

    public String getMobileNumber() {
        return this.mobileNumber;
    }

    public Employee mobileNumber(String mobileNumber) {
        this.setMobileNumber(mobileNumber);
        return this;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public LocalDate getDateHired() {
        return this.dateHired;
    }

    public Employee dateHired(LocalDate dateHired) {
        this.setDateHired(dateHired);
        return this;
    }

    public void setDateHired(LocalDate dateHired) {
        this.dateHired = dateHired;
    }

    public LocalDate getDateDeno() {
        return this.dateDeno;
    }

    public Employee dateDeno(LocalDate dateDeno) {
        this.setDateDeno(dateDeno);
        return this;
    }

    public void setDateDeno(LocalDate dateDeno) {
        this.dateDeno = dateDeno;
    }

    public Integer getSickLeaveYearlyCredit() {
        return this.sickLeaveYearlyCredit;
    }

    public Employee sickLeaveYearlyCredit(Integer sickLeaveYearlyCredit) {
        this.setSickLeaveYearlyCredit(sickLeaveYearlyCredit);
        return this;
    }

    public void setSickLeaveYearlyCredit(Integer sickLeaveYearlyCredit) {
        this.sickLeaveYearlyCredit = sickLeaveYearlyCredit;
    }

    public Integer getSickLeaveYearlyCreditUsed() {
        return this.sickLeaveYearlyCreditUsed;
    }

    public Employee sickLeaveYearlyCreditUsed(Integer sickLeaveYearlyCreditUsed) {
        this.setSickLeaveYearlyCreditUsed(sickLeaveYearlyCreditUsed);
        return this;
    }

    public void setSickLeaveYearlyCreditUsed(Integer sickLeaveYearlyCreditUsed) {
        this.sickLeaveYearlyCreditUsed = sickLeaveYearlyCreditUsed;
    }

    public Integer getLeaveYearlyCredit() {
        return this.leaveYearlyCredit;
    }

    public Employee leaveYearlyCredit(Integer leaveYearlyCredit) {
        this.setLeaveYearlyCredit(leaveYearlyCredit);
        return this;
    }

    public void setLeaveYearlyCredit(Integer leaveYearlyCredit) {
        this.leaveYearlyCredit = leaveYearlyCredit;
    }

    public Integer getLeaveYearlyCreditUsed() {
        return this.leaveYearlyCreditUsed;
    }

    public Employee leaveYearlyCreditUsed(Integer leaveYearlyCreditUsed) {
        this.setLeaveYearlyCreditUsed(leaveYearlyCreditUsed);
        return this;
    }

    public void setLeaveYearlyCreditUsed(Integer leaveYearlyCreditUsed) {
        this.leaveYearlyCreditUsed = leaveYearlyCreditUsed;
    }

    public byte[] getProfileImage() {
        return this.profileImage;
    }

    public Employee profileImage(byte[] profileImage) {
        this.setProfileImage(profileImage);
        return this;
    }

    public void setProfileImage(byte[] profileImage) {
        this.profileImage = profileImage;
    }

    public String getProfileImageContentType() {
        return this.profileImageContentType;
    }

    public Employee profileImageContentType(String profileImageContentType) {
        this.profileImageContentType = profileImageContentType;
        return this;
    }

    public void setProfileImageContentType(String profileImageContentType) {
        this.profileImageContentType = profileImageContentType;
    }

    public String getPresentAddressStreet() {
        return this.presentAddressStreet;
    }

    public Employee presentAddressStreet(String presentAddressStreet) {
        this.setPresentAddressStreet(presentAddressStreet);
        return this;
    }

    public void setPresentAddressStreet(String presentAddressStreet) {
        this.presentAddressStreet = presentAddressStreet;
    }

    public String getPresentAddressCity() {
        return this.presentAddressCity;
    }

    public Employee presentAddressCity(String presentAddressCity) {
        this.setPresentAddressCity(presentAddressCity);
        return this;
    }

    public void setPresentAddressCity(String presentAddressCity) {
        this.presentAddressCity = presentAddressCity;
    }

    public String getPresentAddressProvince() {
        return this.presentAddressProvince;
    }

    public Employee presentAddressProvince(String presentAddressProvince) {
        this.setPresentAddressProvince(presentAddressProvince);
        return this;
    }

    public void setPresentAddressProvince(String presentAddressProvince) {
        this.presentAddressProvince = presentAddressProvince;
    }

    public Integer getPresentAddressZipcode() {
        return this.presentAddressZipcode;
    }

    public Employee presentAddressZipcode(Integer presentAddressZipcode) {
        this.setPresentAddressZipcode(presentAddressZipcode);
        return this;
    }

    public void setPresentAddressZipcode(Integer presentAddressZipcode) {
        this.presentAddressZipcode = presentAddressZipcode;
    }

    public String getHomeAddressStreet() {
        return this.homeAddressStreet;
    }

    public Employee homeAddressStreet(String homeAddressStreet) {
        this.setHomeAddressStreet(homeAddressStreet);
        return this;
    }

    public void setHomeAddressStreet(String homeAddressStreet) {
        this.homeAddressStreet = homeAddressStreet;
    }

    public String getHomeAddressCity() {
        return this.homeAddressCity;
    }

    public Employee homeAddressCity(String homeAddressCity) {
        this.setHomeAddressCity(homeAddressCity);
        return this;
    }

    public void setHomeAddressCity(String homeAddressCity) {
        this.homeAddressCity = homeAddressCity;
    }

    public String getHomeAddressProvince() {
        return this.homeAddressProvince;
    }

    public Employee homeAddressProvince(String homeAddressProvince) {
        this.setHomeAddressProvince(homeAddressProvince);
        return this;
    }

    public void setHomeAddressProvince(String homeAddressProvince) {
        this.homeAddressProvince = homeAddressProvince;
    }

    public Integer getHomeAddressZipcode() {
        return this.homeAddressZipcode;
    }

    public Employee homeAddressZipcode(Integer homeAddressZipcode) {
        this.setHomeAddressZipcode(homeAddressZipcode);
        return this;
    }

    public void setHomeAddressZipcode(Integer homeAddressZipcode) {
        this.homeAddressZipcode = homeAddressZipcode;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Employee user(User user) {
        this.setUser(user);
        return this;
    }

    public Set<DutySchedule> getDutySchedules() {
        return this.dutySchedules;
    }

    public void setDutySchedules(Set<DutySchedule> dutySchedules) {
        if (this.dutySchedules != null) {
            this.dutySchedules.forEach(i -> i.setEmployee(null));
        }
        if (dutySchedules != null) {
            dutySchedules.forEach(i -> i.setEmployee(this));
        }
        this.dutySchedules = dutySchedules;
    }

    public Employee dutySchedules(Set<DutySchedule> dutySchedules) {
        this.setDutySchedules(dutySchedules);
        return this;
    }

    public Employee addDutySchedule(DutySchedule dutySchedule) {
        this.dutySchedules.add(dutySchedule);
        dutySchedule.setEmployee(this);
        return this;
    }

    public Employee removeDutySchedule(DutySchedule dutySchedule) {
        this.dutySchedules.remove(dutySchedule);
        dutySchedule.setEmployee(null);
        return this;
    }

    public Set<DailyTimeRecord> getDailyTimeRecords() {
        return this.dailyTimeRecords;
    }

    public void setDailyTimeRecords(Set<DailyTimeRecord> dailyTimeRecords) {
        if (this.dailyTimeRecords != null) {
            this.dailyTimeRecords.forEach(i -> i.setEmployee(null));
        }
        if (dailyTimeRecords != null) {
            dailyTimeRecords.forEach(i -> i.setEmployee(this));
        }
        this.dailyTimeRecords = dailyTimeRecords;
    }

    public Employee dailyTimeRecords(Set<DailyTimeRecord> dailyTimeRecords) {
        this.setDailyTimeRecords(dailyTimeRecords);
        return this;
    }

    public Employee addDailyTimeRecord(DailyTimeRecord dailyTimeRecord) {
        this.dailyTimeRecords.add(dailyTimeRecord);
        dailyTimeRecord.setEmployee(this);
        return this;
    }

    public Employee removeDailyTimeRecord(DailyTimeRecord dailyTimeRecord) {
        this.dailyTimeRecords.remove(dailyTimeRecord);
        dailyTimeRecord.setEmployee(null);
        return this;
    }

    public Set<Dependents> getDependents() {
        return this.dependents;
    }

    public void setDependents(Set<Dependents> dependents) {
        if (this.dependents != null) {
            this.dependents.forEach(i -> i.setEmployee(null));
        }
        if (dependents != null) {
            dependents.forEach(i -> i.setEmployee(this));
        }
        this.dependents = dependents;
    }

    public Employee dependents(Set<Dependents> dependents) {
        this.setDependents(dependents);
        return this;
    }

    public Employee addDependents(Dependents dependents) {
        this.dependents.add(dependents);
        dependents.setEmployee(this);
        return this;
    }

    public Employee removeDependents(Dependents dependents) {
        this.dependents.remove(dependents);
        dependents.setEmployee(null);
        return this;
    }

    public Set<Education> getEducations() {
        return this.educations;
    }

    public void setEducations(Set<Education> educations) {
        if (this.educations != null) {
            this.educations.forEach(i -> i.setEmployee(null));
        }
        if (educations != null) {
            educations.forEach(i -> i.setEmployee(this));
        }
        this.educations = educations;
    }

    public Employee educations(Set<Education> educations) {
        this.setEducations(educations);
        return this;
    }

    public Employee addEducation(Education education) {
        this.educations.add(education);
        education.setEmployee(this);
        return this;
    }

    public Employee removeEducation(Education education) {
        this.educations.remove(education);
        education.setEmployee(null);
        return this;
    }

    public Set<TrainingHistory> getTrainingHistories() {
        return this.trainingHistories;
    }

    public void setTrainingHistories(Set<TrainingHistory> trainingHistories) {
        if (this.trainingHistories != null) {
            this.trainingHistories.forEach(i -> i.setEmployee(null));
        }
        if (trainingHistories != null) {
            trainingHistories.forEach(i -> i.setEmployee(this));
        }
        this.trainingHistories = trainingHistories;
    }

    public Employee trainingHistories(Set<TrainingHistory> trainingHistories) {
        this.setTrainingHistories(trainingHistories);
        return this;
    }

    public Employee addTrainingHistory(TrainingHistory trainingHistory) {
        this.trainingHistories.add(trainingHistory);
        trainingHistory.setEmployee(this);
        return this;
    }

    public Employee removeTrainingHistory(TrainingHistory trainingHistory) {
        this.trainingHistories.remove(trainingHistory);
        trainingHistory.setEmployee(null);
        return this;
    }

    public Set<Leave> getLeaves() {
        return this.leaves;
    }

    public void setLeaves(Set<Leave> leaves) {
        if (this.leaves != null) {
            this.leaves.forEach(i -> i.setEmployee(null));
        }
        if (leaves != null) {
            leaves.forEach(i -> i.setEmployee(this));
        }
        this.leaves = leaves;
    }

    public Employee leaves(Set<Leave> leaves) {
        this.setLeaves(leaves);
        return this;
    }

    public Employee addLeave(Leave leave) {
        this.leaves.add(leave);
        leave.setEmployee(this);
        return this;
    }

    public Employee removeLeave(Leave leave) {
        this.leaves.remove(leave);
        leave.setEmployee(null);
        return this;
    }

    public Set<Designation> getDesignations() {
        return this.designations;
    }

    public void setDesignations(Set<Designation> designations) {
        this.designations = designations;
    }

    public Employee designations(Set<Designation> designations) {
        this.setDesignations(designations);
        return this;
    }

    public Employee addDesignation(Designation designation) {
        this.designations.add(designation);
        designation.getEmployees().add(this);
        return this;
    }

    public Employee removeDesignation(Designation designation) {
        this.designations.remove(designation);
        designation.getEmployees().remove(this);
        return this;
    }

    public Set<Benefits> getBenefits() {
        return this.benefits;
    }

    public void setBenefits(Set<Benefits> benefits) {
        this.benefits = benefits;
    }

    public Employee benefits(Set<Benefits> benefits) {
        this.setBenefits(benefits);
        return this;
    }

    public Employee addBenefits(Benefits benefits) {
        this.benefits.add(benefits);
        benefits.getEmployees().add(this);
        return this;
    }

    public Employee removeBenefits(Benefits benefits) {
        this.benefits.remove(benefits);
        benefits.getEmployees().remove(this);
        return this;
    }

    public Department getDepartment() {
        return this.department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Employee department(Department department) {
        this.setDepartment(department);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Employee)) {
            return false;
        }
        return id != null && id.equals(((Employee) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Employee{" +
            "id=" + getId() +
            ", employeeBiometricId=" + getEmployeeBiometricId() +
            ", username='" + getUsername() + "'" +
            ", email='" + getEmail() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", middleName='" + getMiddleName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", nameSuffix='" + getNameSuffix() + "'" +
            ", birthdate='" + getBirthdate() + "'" +
            ", gender='" + getGender() + "'" +
            ", status='" + getStatus() + "'" +
            ", employmentType='" + getEmploymentType() + "'" +
            ", mobileNumber='" + getMobileNumber() + "'" +
            ", dateHired='" + getDateHired() + "'" +
            ", dateDeno='" + getDateDeno() + "'" +
            ", sickLeaveYearlyCredit=" + getSickLeaveYearlyCredit() +
            ", sickLeaveYearlyCreditUsed=" + getSickLeaveYearlyCreditUsed() +
            ", leaveYearlyCredit=" + getLeaveYearlyCredit() +
            ", leaveYearlyCreditUsed=" + getLeaveYearlyCreditUsed() +
            ", profileImage='" + getProfileImage() + "'" +
            ", profileImageContentType='" + getProfileImageContentType() + "'" +
            ", presentAddressStreet='" + getPresentAddressStreet() + "'" +
            ", presentAddressCity='" + getPresentAddressCity() + "'" +
            ", presentAddressProvince='" + getPresentAddressProvince() + "'" +
            ", presentAddressZipcode=" + getPresentAddressZipcode() +
            ", homeAddressStreet='" + getHomeAddressStreet() + "'" +
            ", homeAddressCity='" + getHomeAddressCity() + "'" +
            ", homeAddressProvince='" + getHomeAddressProvince() + "'" +
            ", homeAddressZipcode=" + getHomeAddressZipcode() +
            "}";
    }
}
