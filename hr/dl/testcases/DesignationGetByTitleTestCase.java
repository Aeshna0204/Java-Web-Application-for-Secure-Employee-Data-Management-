 package hr.dl.testcases;
import com.thinking.machines.hr.dl.exceptions.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.hr.dl.dao.*;

public class DesignationGetByTitleTestCase {
    public static void main(String gg[]){
        String title=gg[0];
        try{
            DesignationDTOInterface designationDTO;
            designationDTO=new DesignationDTO();
            DesignationDAOInterface designationDAO;
            designationDAO=new DesignationDAO();
            designationDTO=designationDAO.getByTitle(title);
            System.out.println("Code at title :"+ title + designationDTO.getCode());

        }catch(DAOException daoException){
            System.out.println(daoException.getMessage());
        }
    }
}

