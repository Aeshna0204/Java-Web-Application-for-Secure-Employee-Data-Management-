package hr.dl.testcases;
import com.thinking.machines.hr.dl.exceptions.*;
// import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
// import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.hr.dl.dao.*;
// import java.util.*;
// import java.text.*;

public class EmployeeDeleteTestCase {
    public static void main(String gg[]) {
        String employeeId = gg[0];
        try {
            EmployeeDAOInterface employeeDAO = new EmployeeDAO();
            employeeDAO.delete(employeeId);
            System.out.println("Employee Deleted......");
        } catch (DAOException daoException) {
            System.out.println(daoException.getMessage());
        }
    }
}