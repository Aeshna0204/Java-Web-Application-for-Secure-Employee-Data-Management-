 package hr.dl.testcases;
import com.thinking.machines.hr.dl.exceptions.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.hr.dl.dao.*;

public class DesignationGetByCodeTestCase {
    public static void main(String gg[]){
        int code=Integer.parseInt(gg[0]);
        try{
            DesignationDTOInterface designationDTO;
            designationDTO=new DesignationDTO();
            DesignationDAOInterface designationDAO;
            designationDAO=new DesignationDAO();
            designationDTO=designationDAO.getByCode(code);
            System.out.println("Designation at code :"+ code + designationDTO.getTitle());

        }catch(DAOException daoException){
            System.out.println(daoException.getMessage());
        }
    }
}

