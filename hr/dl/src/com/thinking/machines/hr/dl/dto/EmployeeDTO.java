package com.thinking.machines.hr.dl.dto;
import com.thinking.machines.enums.*;
import java.math.*;
import java.util.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;

public class EmployeeDTO implements EmployeeDTOInterface {
    String employeeId;
    String name;
    int designationCode;
    Date dateOfBirth;
    char gender;
    boolean isIndian;
    BigDecimal basicSalary;
    String panNumber;
    String aadharCardNumber;

    public EmployeeDTO() {
        this.employeeId = "";
        this.name = "";
        this.designationCode = 0;
        this.dateOfBirth = null;
        this.gender = ' ';
        this.isIndian = false;
        this.basicSalary = null;
        this.panNumber = "";
        this.aadharCardNumber = "";
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public java.lang.String getEmployeeId() {
        return this.employeeId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public java.lang.String getName() {
        return this.name;
    }

    public void setDesignationCode(int designationCode) {
        this.designationCode = designationCode;
    }

    public int getDesignationCode() {
        return this.designationCode;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public java.util.Date getDateOfBirth() {
        return this.dateOfBirth;
    }

    public void setGender(GENDER gender) {
        if (gender ==GENDER.MALE)
            this.gender = 'M';
        else
            this.gender = 'F';
    }

    public char getGender() {
        return this.gender;
    }

    public void setIsIndian(boolean isIndian) {
        this.isIndian = isIndian;
    }

    public boolean getIsIndian() {
        return this.isIndian;
    }

    public void setBasicSalary(BigDecimal basicSalary) {
        this.basicSalary = basicSalary;
    }

    public java.math.BigDecimal getBasicSalary() {
        return this.basicSalary;
    }

    public void setPANNumber(java.lang.String panNumber) {
        this.panNumber = panNumber;
    }

    public java.lang.String getPANNumber() {
        return this.panNumber;
    }

    public void setAadharCardNumber(String aadharCardNumber) {
        this.aadharCardNumber = aadharCardNumber;
    }

    public java.lang.String getAadharCardNumber() {
        return this.aadharCardNumber;
    }

    public boolean equals(Object other) {
        if (!(other instanceof EmployeeDTOInterface))
            return false;
        EmployeeDTOInterface employeeDTO = (EmployeeDTOInterface) other;
        return this.employeeId.equalsIgnoreCase(employeeDTO.getEmployeeId());
    }

    public int compareTo(EmployeeDTOInterface other) {
        return this.employeeId.compareToIgnoreCase(other.getEmployeeId());
    }

    public int hashCode() {
        return this.employeeId.toUpperCase().hashCode();
    }
}
