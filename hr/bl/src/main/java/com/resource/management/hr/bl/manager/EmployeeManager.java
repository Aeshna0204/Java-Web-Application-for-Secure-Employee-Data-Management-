package com.resource.management.hr.bl.manager;

import com.resource.management.hr.bl.interfaces.pojo.*;
import com.resource.management.hr.bl.interfaces.manager.*;
import com.resource.management.hr.bl.pojo.*;
import com.resource.management.hr.bl.exceptions.*;
import com.thinking.machines.hr.dl.exceptions.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.hr.dl.dao.*;
import java.util.*;
import java.math.*;
import com.thinking.machines.enums.*;
import java.text.*;

public class EmployeeManager implements EmployeeManagerInterface {

    private Map<String, EmployeeInterface> employeeIdWiseEmployeesMap;
    private Map<String, EmployeeInterface> panNumberWiseEmployeesMap;
    private Map<String, EmployeeInterface> aadharCardNumberWiseEmployeesMap;
    private Set<EmployeeInterface> employeesSet;
    private Map<Integer, Set<EmployeeInterface>> designationCodeWiseEmployeesMap;
    private static EmployeeManagerInterface employeeManager = null;

    private EmployeeManager() throws BLException {
        populateDataStructure();
    }

    private void populateDataStructure() throws BLException {
        this.employeeIdWiseEmployeesMap = new HashMap<>();
        this.panNumberWiseEmployeesMap = new HashMap<>();
        this.aadharCardNumberWiseEmployeesMap = new HashMap<>();
        this.employeesSet = new TreeSet<>();
        this.designationCodeWiseEmployeesMap = new HashMap<>();

        try {

            Set<EmployeeDTOInterface> dlEmployees = new EmployeeDAO().getAll();
            EmployeeInterface employee;
            DesignationManagerInterface designationManager = DesignationManager.getDesignationManager();
            DesignationInterface designation;
            for (EmployeeDTOInterface dlEmployee : dlEmployees) {
                employee = new Employee();
                employee.setEmployeeId(dlEmployee.getEmployeeId());
                employee.setName(dlEmployee.getName());
                designation = designationManager.getDesignationByCode(dlEmployee.getDesignationCode());
                employee.setDesignation(designation);
                employee.setDateOfBirth(dlEmployee.getDateOfBirth());
                if (dlEmployee.getGender() == 'M')
                    employee.setGender(GENDER.MALE);
                else
                    employee.setGender(GENDER.FEMALE);
                employee.setBasicSalary(dlEmployee.getBasicSalary());
                employee.setIsIndian(dlEmployee.getIsIndian());
                employee.setPANNumber(dlEmployee.getPANNumber());
                employee.setAadharCardNumber(dlEmployee.getAadharCardNumber());
                this.employeeIdWiseEmployeesMap.put(employee.getEmployeeId().toUpperCase(), employee);
                this.panNumberWiseEmployeesMap.put(employee.getPANNumber().toUpperCase(), employee);
                this.aadharCardNumberWiseEmployeesMap.put(employee.getAadharCardNumber().toUpperCase(), employee);
                this.employeesSet.add(employee);

            }

        } catch (DAOException dao) {
            BLException ble = new BLException();
            ble.setGenericException(dao.getMessage());
            throw ble;
        }

    }

    public static EmployeeManagerInterface getEmployeeManager() throws BLException {
        if (employeeManager == null)
            employeeManager = new EmployeeManager();
        return employeeManager;
    }
    // -------------------------------------------------------------------------------------------------------------------------------------------------------

    public void addEmployee(EmployeeInterface employee) throws BLException {

        BLException blException = new BLException();
        if (employee == null) {
            blException.setGenericException("employee is null");
            throw blException;
        }
        String employeeId = employee.getEmployeeId();
        String name = employee.getName();
        int designationCode = 0;
        DesignationInterface designation = employee.getDesignation();
        Date dateOfBirth = employee.getDateOfBirth();
        char gender = employee.getGender();
        boolean isIndian = employee.getIsIndian();
        BigDecimal basicSalary = employee.getBasicSalary();
        String panNumber = employee.getPANNumber();
        String aadharCardNumber = employee.getAadharCardNumber();

        if (employeeId != null) {
            employeeId = employeeId.trim();
            if (employeeId.length() > 0) {
                blException.addException("employeeId", "Employee Id should be nil/empty");
            }
        }
        if (name == null) {
            blException.addException("name", "Name Required");
        } else {
            name = name.trim();
            if (name.length() == 0)
                blException.addException("name", "Name Required");
        }
        DesignationManagerInterface designationManager = DesignationManager.getDesignationManager();
        if (designation == null)
            blException.addException("designation", "Designation Required");
        else {
            designationCode = designation.getCode();
            if (designationManager.designationCodeExists(designation.getCode()) == false) {
                blException.addException("designation", "Invalid Designation");
            }
        }
        if (dateOfBirth == null) {
            blException.addException("dateOfBirth", "Date of Birth Required");
        }
        if (gender == ' ') {
            blException.addException("gender", "Gender Required");
        }
        if (basicSalary == null) {
            blException.addException("basicSalary", "Basic Salary Required");
        } else {
            if (basicSalary.signum() == -1) {
                blException.addException("basicSalary", "Basic Salary cannot be Negative");
            }
        }
        if (panNumber == null) {
            blException.addException("panNumber", "PAN Number Required");
        } else {
            panNumber = panNumber.trim();
            if (panNumber.length() == 0) {
                blException.addException("panNumber", "PAN Number Required");
            }
        }
        if (aadharCardNumber == null) {
            blException.addException("aadharCardNumber", "Aadhar Card Number Required");
        } else {
            aadharCardNumber = aadharCardNumber.trim();
            if (aadharCardNumber.length() == 0) {
                blException.addException("aadharCardNumber", "Aadhar Card Number Required");
            }
        }
        if (panNumber != null && panNumber.length() > 0) {
            if (panNumberWiseEmployeesMap.containsKey(panNumber.toUpperCase())) {
                blException.addException("panNumber", "PAN Number: " + panNumber + " exists");
            }
        }
        if (aadharCardNumber != null && aadharCardNumber.length() > 0) {
            if (aadharCardNumberWiseEmployeesMap.containsKey(aadharCardNumber.toUpperCase())) {
                blException.addException("aadharCardNumber", "Aadhar Card Number: " + aadharCardNumber + " exists");
            }
        }
        if (blException.hasExceptions())
            throw blException;

        try {
            EmployeeDAOInterface employeeDAO = new EmployeeDAO();
            EmployeeDTOInterface dlEmployee = new EmployeeDTO();
            dlEmployee.setName(name);
            dlEmployee.setDesignationCode(designation.getCode());
            dlEmployee.setDateOfBirth(dateOfBirth);
            dlEmployee.setGender((gender == 'M') ? GENDER.MALE : GENDER.FEMALE);
            dlEmployee.setIsIndian(isIndian);
            dlEmployee.setBasicSalary(basicSalary);
            dlEmployee.setPANNumber(panNumber);
            dlEmployee.setAadharCardNumber(aadharCardNumber);
            employeeDAO.add(dlEmployee);
            employee.setEmployeeId(dlEmployee.getEmployeeId());
            EmployeeInterface dsEmployee = new Employee();
            dsEmployee.setEmployeeId(employee.getEmployeeId());
            dsEmployee.setName(employee.getName());
            dsEmployee.setDesignation(
                    ((DesignationManager) designationManager).getDSDesignationByCode(designation.getCode()));
            dsEmployee.setDateOfBirth((Date) dateOfBirth.clone());
            dsEmployee.setGender((gender == 'M') ? GENDER.MALE : GENDER.FEMALE);
            dsEmployee.setIsIndian(isIndian);
            dsEmployee.setBasicSalary(basicSalary);
            dsEmployee.setPANNumber(panNumber);
            dsEmployee.setAadharCardNumber(aadharCardNumber);
            employeesSet.add(dsEmployee);
            employeeIdWiseEmployeesMap.put(dsEmployee.getEmployeeId().toUpperCase(), dsEmployee);
            panNumberWiseEmployeesMap.put(panNumber.toUpperCase(), dsEmployee);
            aadharCardNumberWiseEmployeesMap.put(aadharCardNumber.toUpperCase(), dsEmployee);
            Set<EmployeeInterface> ets;
            ets = this.designationCodeWiseEmployeesMap.get(dsEmployee.getDesignation().getCode());
            if (ets == null) {
                ets = new TreeSet<>();
                ets.add(dsEmployee);
                designationCodeWiseEmployeesMap.put(new Integer(dsEmployee.getDesignation().getCode()), ets);
            } else {
                ets.add(dsEmployee);
            }
        } catch (DAOException daoException) {
            blException.setGenericException(daoException.getMessage());
            throw blException;
        }
    }
    // ----------------------------------------------------------------------------------------------------------------------------------------------------------------------

    public void updateEmployee(EmployeeInterface employee) throws BLException {

        BLException blException = new BLException();
        String employeeId = employee.getEmployeeId();
        int oldDesignationCode = employee.getDesignation().getCode();
        String name = employee.getName();
        DesignationInterface designation = employee.getDesignation();
        int designationCode = 0;
        Date dateOfBirth = employee.getDateOfBirth();
        char gender = employee.getGender();
        boolean isIndian = employee.getIsIndian();
        BigDecimal basicSalary = employee.getBasicSalary();
        String panNumber = employee.getPANNumber();
        String aadharCardNumber = employee.getAadharCardNumber();

        if (employeeId == null) {
            blException.addException("employeeId", "Employee Required");
        } else {
            employeeId = employeeId.trim();
            if (employeeId.length() == 0)
                blException.addException("employeeId", "employeeId required");
            else {
                if (employeeIdWiseEmployeesMap.containsKey(employeeId.toUpperCase().trim()) == false) {
                    blException.addException("employeeId", "Invalid EmployeeID" + employeeId);
                    throw blException;
                }
            }
        }
        if (name == null) {
            blException.addException("name", "Name Required");
        } else {
            name = name.trim();
            if (name.length() == 0)
                blException.addException("name", "Name Required");
        }

        DesignationManagerInterface designationManager = DesignationManager.getDesignationManager();
        if (designation == null)
            blException.addException("designation", "Designation Required");
        else {
            designationCode = designation.getCode();
            if (designationManager.designationCodeExists(designation.getCode()) == false) {
                blException.addException("designation", "Invalid Designation");
            }
        }

        if (dateOfBirth == null) {
            blException.addException("dateOfBirth", "Date of Birth Required");
        }
        if (gender == ' ') {
            blException.addException("gender", "Gender Required");
        }
        if (basicSalary == null) {
            blException.addException("basicSalary", "Basic Salary Required");
        } else {
            if (basicSalary.signum() == -1) {
                blException.addException("basicSalary", "Basic Salary cannot be Negative");
            }
        }
        if (panNumber == null) {
            blException.addException("panNumber", "PAN Number Required");
        } else {
            panNumber = panNumber.trim();
            if (panNumber.length() == 0) {
                blException.addException("panNumber", "PAN Number Required");
            }
        }
        if (aadharCardNumber == null) {
            blException.addException("aadharCardNumber", "Aadhar Card Number Required");
        } else {
            aadharCardNumber = aadharCardNumber.trim();
            if (aadharCardNumber.length() == 0) {
                blException.addException("aadharCardNumber", "Aadhar Card Number Required");
            }
        }

        if (panNumber != null && panNumber.length() > 0) {
            EmployeeInterface ee = panNumberWiseEmployeesMap.get(panNumber.toUpperCase());
            if (ee != null && ee.getEmployeeId().equalsIgnoreCase(employeeId) == false) {
                blException.addException("panNumber", "PAN Number " + panNumber + " exists");
            }
        }
        if (aadharCardNumber != null && aadharCardNumber.length() > 0) {

            EmployeeInterface ee = aadharCardNumberWiseEmployeesMap.get(aadharCardNumber.toUpperCase());
            if (ee != null && ee.getEmployeeId().equalsIgnoreCase(employeeId) == false) {
                blException.addException("aadharCardNumber", "aadharCardNUmber" + aadharCardNumber + "exists");

            }

        }
        if (blException.hasExceptions())
            throw blException;

        try {

            EmployeeInterface dsEmployee = employeeIdWiseEmployeesMap.get(employeeId.toUpperCase());
            String oldPANNumber = dsEmployee.getPANNumber();
            String oldAadharCardNumber = dsEmployee.getAadharCardNumber();
            EmployeeDAOInterface employeeDAO = new EmployeeDAO();
            EmployeeDTOInterface dlEmployee = new EmployeeDTO();
            dlEmployee.setEmployeeId(dsEmployee.getEmployeeId());
            dlEmployee.setName(name);
            dlEmployee.setDesignationCode(designation.getCode());
            dlEmployee.setDateOfBirth(dateOfBirth);
            dlEmployee.setGender((gender == 'M') ? GENDER.MALE : GENDER.FEMALE);
            dlEmployee.setIsIndian(isIndian);
            dlEmployee.setBasicSalary(basicSalary);
            dlEmployee.setPANNumber(panNumber);
            dlEmployee.setAadharCardNumber(aadharCardNumber);
            // passing DTO in DAO to call Update method
            employeeDAO.update(dlEmployee);
            // making clone to datastructure by setting values to a object and then store it
            // in ds
            dsEmployee.setName(employee.getName());
            dsEmployee.setDesignation(
                    ((DesignationManager) designationManager).getDSDesignationByCode(designation.getCode()));
            dsEmployee.setDateOfBirth((Date) dateOfBirth.clone());
            dsEmployee.setGender((gender == 'M') ? GENDER.MALE : GENDER.FEMALE);
            dsEmployee.setIsIndian(isIndian);
            dsEmployee.setBasicSalary(basicSalary);
            dsEmployee.setPANNumber(panNumber);
            dsEmployee.setAadharCardNumber(aadharCardNumber);

            // clear old values from ds
            employeesSet.remove(dsEmployee);
            employeeIdWiseEmployeesMap.remove(employeeId.toUpperCase());
            panNumberWiseEmployeesMap.remove(oldPANNumber.toUpperCase());
            aadharCardNumberWiseEmployeesMap.remove(oldAadharCardNumber.toUpperCase());

            // Add new values to the ds
            employeesSet.add(dsEmployee);
            employeeIdWiseEmployeesMap.put(employeeId, dsEmployee);
            panNumberWiseEmployeesMap.put(panNumber.toUpperCase(), dsEmployee);
            aadharCardNumberWiseEmployeesMap.put(aadharCardNumber.toUpperCase(), dsEmployee);
            if (oldDesignationCode != dsEmployee.getDesignation().getCode()) {
                Set<EmployeeInterface> ets = this.designationCodeWiseEmployeesMap.get(oldDesignationCode);
                ets.remove(dsEmployee);
                ets = this.designationCodeWiseEmployeesMap.get(dsEmployee.getDesignation().getCode());
                if (ets == null) {
                    ets = new TreeSet<>();
                    ets.add(dsEmployee);
                    designationCodeWiseEmployeesMap.put(Integer.valueOf(dsEmployee.getDesignation().getCode()), ets);
                } else {
                    ets.add(dsEmployee);
                }
            }

        } catch (DAOException daoException) {
            blException.setGenericException(daoException.getMessage());
            throw blException;
        }

    }

    public void removeEmployee(String employeeId) throws BLException {

        if (employeeId == null) {
            BLException blException = new BLException();
            blException.addException("employeeId", "Employee ID required");
            throw blException;
        } else {
            employeeId = employeeId.trim();
            if (employeeId.length() == 0) {
                BLException blException = new BLException();
                blException.addException("employeeId", "Employee ID required");
                throw blException;
            } else {
                if (employeeIdWiseEmployeesMap.containsKey(employeeId.trim()) == false) {
                    BLException blException = new BLException();
                    blException.addException("employeeId", "Invalid Employee ID");
                    throw blException;
                }
            }
        }
        try {
            EmployeeInterface dsEmployee = employeeIdWiseEmployeesMap.get(employeeId.toUpperCase());
            EmployeeDAOInterface employeeDAO = new EmployeeDAO();
            employeeDAO.delete(dsEmployee.getEmployeeId());
            employeesSet.remove(dsEmployee);
            employeeIdWiseEmployeesMap.remove(employeeId.toUpperCase());
            panNumberWiseEmployeesMap.remove(dsEmployee.getPANNumber().toUpperCase());
            aadharCardNumberWiseEmployeesMap.remove(dsEmployee.getAadharCardNumber().toUpperCase());
            Set<EmployeeInterface> ets = this.designationCodeWiseEmployeesMap
                    .get(dsEmployee.getDesignation().getCode());
            ets.remove(dsEmployee);
        } catch (DAOException daoException) {
            BLException blException = new BLException();
            blException.setGenericException(daoException.getMessage());
            throw blException;
        }
    }

    // -----------------------------------------------------------------------------------------------------------------------------------------------------------------------

    public EmployeeInterface getEmployeeByEmployeeId(String employeeId) throws BLException {
        BLException blException = new BLException();
        blException.setGenericException("not yet implemented");
        throw blException;
    }

    public EmployeeInterface getEmployeeByPANNumber(String panNumber) throws BLException {

        return panNumberWiseEmployeesMap.get(panNumber);
    }

    public EmployeeInterface getEmployeeByAadharCardNumber(String aadharCardNumber) throws BLException {
        return aadharCardNumberWiseEmployeesMap.get(aadharCardNumber);
    }

    public int getEmployeeCount() {
        return employeesSet.size();
    }

    public boolean employeeIdExists(String employeeId) {
        return employeeIdWiseEmployeesMap.containsKey(employeeId);
    }

    public boolean employeePANNumberExists(String panNumber) {
        return panNumberWiseEmployeesMap.containsKey(panNumber);
    }

    public boolean employeeAadharCardNumberExists(String aadharCardNumber) {
        return aadharCardNumberWiseEmployeesMap.containsKey(aadharCardNumber);
    }

    public Set<EmployeeInterface> getEmployees() {
        Set<EmployeeInterface> employees = new TreeSet<>();
        EmployeeInterface employee;
        DesignationInterface dsDesignation;
        DesignationInterface designation;
        for (EmployeeInterface dsEmployee : employeesSet) {
            employee = new Employee();
            employee.setName(dsEmployee.getName());
            dsDesignation = dsEmployee.getDesignation();
            designation = new Designation();
            designation.setCode(dsDesignation.getCode());
            designation.setTitle(dsDesignation.getTitle());
            employee.setDesignation(designation);
            employee.setDateOfBirth((Date) dsEmployee.getDateOfBirth().clone());
            employee.setGender((dsEmployee.getGender() == 'M') ? GENDER.MALE : GENDER.FEMALE);
            employee.setIsIndian(dsEmployee.getIsIndian());
            employee.setBasicSalary(dsEmployee.getBasicSalary());
            employee.setPANNumber(dsEmployee.getPANNumber());
            employee.setAadharCardNumber(dsEmployee.getAadharCardNumber());
            employees.add(employee);

        }
        return employees;
    }

    public Set<EmployeeInterface> getEmployeesByDesignationCode(int designationCode) throws BLException {
        DesignationManagerInterface designationManager = DesignationManager.getDesignationManager();
        if (designationManager.designationCodeExists(designationCode) == false) {
            BLException blException = new BLException();
            blException.setGenericException("Invalid Designation Code: " + designationCode);
        }
        Set<EmployeeInterface> employees = new TreeSet<>();
        Set<EmployeeInterface> ets;
        ets = designationCodeWiseEmployeesMap.get(designationCode);
        if (ets == null) {
            return employees;
        }
        EmployeeInterface employee;
        DesignationInterface dsDesignation;
        DesignationInterface designation;
        for (EmployeeInterface dsEmployee : employeesSet) {
            employee = new Employee();
            employee.setEmployeeId(dsEmployee.getEmployeeId());
            employee.setName(dsEmployee.getName());
            dsDesignation = dsEmployee.getDesignation();
            designation = new Designation();
            designation.setCode(dsDesignation.getCode());
            designation.setTitle(dsDesignation.getTitle());
            employee.setDesignation(designation);
            employee.setDateOfBirth((Date) dsEmployee.getDateOfBirth().clone());
            employee.setGender((dsEmployee.getGender() == 'M') ? GENDER.MALE : GENDER.FEMALE);
            employee.setIsIndian(dsEmployee.getIsIndian());
            employee.setBasicSalary(dsEmployee.getBasicSalary());
            employee.setPANNumber(dsEmployee.getPANNumber());
            employee.setAadharCardNumber(dsEmployee.getAadharCardNumber());
            employees.add(employee);
        }
        return employees;

    }

    public int getEmployeeCountByDesignationCode(int designationCode) throws BLException {
        Set<EmployeeInterface> ets = this.designationCodeWiseEmployeesMap.get(designationCode);
        if (ets == null)
            return 0;
        return ets.size();
    }

    public boolean designationAlloted(int designationCode) throws BLException {
        return this.designationCodeWiseEmployeesMap.containsKey(designationCode);
    }

}