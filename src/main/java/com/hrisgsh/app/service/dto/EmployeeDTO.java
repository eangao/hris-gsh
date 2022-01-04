package com.hrisgsh.app.service.dto;

import com.hrisgsh.app.domain.enumeration.EmploymentType;
import com.hrisgsh.app.domain.enumeration.Gender;
import com.hrisgsh.app.domain.enumeration.Status;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.hrisgsh.app.domain.Employee} entity.
 */
public class EmployeeDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer employeeBiometricId;

    @NotNull
    @Size(min = 5, max = 50)
    private String username;

    @Size(min = 5, max = 254)
    private String email;

    @NotNull
    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String middleName;

    @NotNull
    @Size(max = 50)
    private String lastName;

    @Size(max = 15)
    private String nameSuffix;

    @NotNull
    private LocalDate birthdate;

    @NotNull
    private Gender gender;

    @NotNull
    private Status status;

    @NotNull
    private EmploymentType employmentType;

    @Size(max = 20)
    private String mobileNumber;

    private LocalDate dateHired;

    private LocalDate dateDeno;

    private Integer sickLeaveYearlyCredit;

    private Integer sickLeaveYearlyCreditUsed;

    private Integer leaveYearlyCredit;

    private Integer leaveYearlyCreditUsed;

    @Lob
    private byte[] profileImage;

    private String profileImageContentType;
    private String presentAddressStreet;

    private String presentAddressCity;

    private String presentAddressProvince;

    private Integer presentAddressZipcode;

    private String homeAddressStreet;

    private String homeAddressCity;

    private String homeAddressProvince;

    private Integer homeAddressZipcode;

    private UserDTO user;

    private Set<DesignationDTO> designations = new HashSet<>();

    private Set<BenefitsDTO> benefits = new HashSet<>();

    private DepartmentDTO department;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getEmployeeBiometricId() {
        return employeeBiometricId;
    }

    public void setEmployeeBiometricId(Integer employeeBiometricId) {
        this.employeeBiometricId = employeeBiometricId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNameSuffix() {
        return nameSuffix;
    }

    public void setNameSuffix(String nameSuffix) {
        this.nameSuffix = nameSuffix;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public EmploymentType getEmploymentType() {
        return employmentType;
    }

    public void setEmploymentType(EmploymentType employmentType) {
        this.employmentType = employmentType;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public LocalDate getDateHired() {
        return dateHired;
    }

    public void setDateHired(LocalDate dateHired) {
        this.dateHired = dateHired;
    }

    public LocalDate getDateDeno() {
        return dateDeno;
    }

    public void setDateDeno(LocalDate dateDeno) {
        this.dateDeno = dateDeno;
    }

    public Integer getSickLeaveYearlyCredit() {
        return sickLeaveYearlyCredit;
    }

    public void setSickLeaveYearlyCredit(Integer sickLeaveYearlyCredit) {
        this.sickLeaveYearlyCredit = sickLeaveYearlyCredit;
    }

    public Integer getSickLeaveYearlyCreditUsed() {
        return sickLeaveYearlyCreditUsed;
    }

    public void setSickLeaveYearlyCreditUsed(Integer sickLeaveYearlyCreditUsed) {
        this.sickLeaveYearlyCreditUsed = sickLeaveYearlyCreditUsed;
    }

    public Integer getLeaveYearlyCredit() {
        return leaveYearlyCredit;
    }

    public void setLeaveYearlyCredit(Integer leaveYearlyCredit) {
        this.leaveYearlyCredit = leaveYearlyCredit;
    }

    public Integer getLeaveYearlyCreditUsed() {
        return leaveYearlyCreditUsed;
    }

    public void setLeaveYearlyCreditUsed(Integer leaveYearlyCreditUsed) {
        this.leaveYearlyCreditUsed = leaveYearlyCreditUsed;
    }

    public byte[] getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(byte[] profileImage) {
        this.profileImage = profileImage;
    }

    public String getProfileImageContentType() {
        return profileImageContentType;
    }

    public void setProfileImageContentType(String profileImageContentType) {
        this.profileImageContentType = profileImageContentType;
    }

    public String getPresentAddressStreet() {
        return presentAddressStreet;
    }

    public void setPresentAddressStreet(String presentAddressStreet) {
        this.presentAddressStreet = presentAddressStreet;
    }

    public String getPresentAddressCity() {
        return presentAddressCity;
    }

    public void setPresentAddressCity(String presentAddressCity) {
        this.presentAddressCity = presentAddressCity;
    }

    public String getPresentAddressProvince() {
        return presentAddressProvince;
    }

    public void setPresentAddressProvince(String presentAddressProvince) {
        this.presentAddressProvince = presentAddressProvince;
    }

    public Integer getPresentAddressZipcode() {
        return presentAddressZipcode;
    }

    public void setPresentAddressZipcode(Integer presentAddressZipcode) {
        this.presentAddressZipcode = presentAddressZipcode;
    }

    public String getHomeAddressStreet() {
        return homeAddressStreet;
    }

    public void setHomeAddressStreet(String homeAddressStreet) {
        this.homeAddressStreet = homeAddressStreet;
    }

    public String getHomeAddressCity() {
        return homeAddressCity;
    }

    public void setHomeAddressCity(String homeAddressCity) {
        this.homeAddressCity = homeAddressCity;
    }

    public String getHomeAddressProvince() {
        return homeAddressProvince;
    }

    public void setHomeAddressProvince(String homeAddressProvince) {
        this.homeAddressProvince = homeAddressProvince;
    }

    public Integer getHomeAddressZipcode() {
        return homeAddressZipcode;
    }

    public void setHomeAddressZipcode(Integer homeAddressZipcode) {
        this.homeAddressZipcode = homeAddressZipcode;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public Set<DesignationDTO> getDesignations() {
        return designations;
    }

    public void setDesignations(Set<DesignationDTO> designations) {
        this.designations = designations;
    }

    public Set<BenefitsDTO> getBenefits() {
        return benefits;
    }

    public void setBenefits(Set<BenefitsDTO> benefits) {
        this.benefits = benefits;
    }

    public DepartmentDTO getDepartment() {
        return department;
    }

    public void setDepartment(DepartmentDTO department) {
        this.department = department;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmployeeDTO)) {
            return false;
        }

        EmployeeDTO employeeDTO = (EmployeeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, employeeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmployeeDTO{" +
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
            ", presentAddressStreet='" + getPresentAddressStreet() + "'" +
            ", presentAddressCity='" + getPresentAddressCity() + "'" +
            ", presentAddressProvince='" + getPresentAddressProvince() + "'" +
            ", presentAddressZipcode=" + getPresentAddressZipcode() +
            ", homeAddressStreet='" + getHomeAddressStreet() + "'" +
            ", homeAddressCity='" + getHomeAddressCity() + "'" +
            ", homeAddressProvince='" + getHomeAddressProvince() + "'" +
            ", homeAddressZipcode=" + getHomeAddressZipcode() +
            ", user=" + getUser() +
            ", designations=" + getDesignations() +
            ", benefits=" + getBenefits() +
            ", department=" + getDepartment() +
            "}";
    }
}
