package hr.dl.testcases;
import com.thinking.machines.hr.dl.exceptions.*;
// import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
// import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.hr.dl.dao.*;
// import java.util.*;
// import java.text.*;

public class EmployeeGetCountByDesignationTestCase {
    public static void main(String gg[]) {
        int designationCode = Integer.parseInt(gg[0]);
        try {
            EmployeeDAOInterface employeeDAO = new EmployeeDAO();
            System.out.println("Number of records for Designation Code " + designationCode + " is/are: "
                    + employeeDAO.getCountByDesignation(designationCode));
        } catch (DAOException daoException) {
            System.out.println(daoException.getMessage());
        }
    }
}