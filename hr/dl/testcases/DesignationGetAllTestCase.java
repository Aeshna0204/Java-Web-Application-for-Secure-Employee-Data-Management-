 package hr.dl.testcases;
import com.thinking.machines.hr.dl.exceptions.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
// import com.thinking.machines.hr.dl.interfaces.dao.*;
// import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.hr.dl.dao.*;
import java.util.Set;

public class DesignationGetAllTestCase {
    public static void main(String gg[]){
        
        try{
            
            DesignationDAO designationDAO = new DesignationDAO();
            Set<DesignationDTOInterface> designations = designationDAO.getAll();
            designations.forEach((designationDTO)->{
            System.out.println("Code: "+designationDTO.getCode()+", Title: "+designationDTO.getTitle());});

        }catch(DAOException daoException){
            System.out.println(daoException.getMessage());
        }
    }
}

