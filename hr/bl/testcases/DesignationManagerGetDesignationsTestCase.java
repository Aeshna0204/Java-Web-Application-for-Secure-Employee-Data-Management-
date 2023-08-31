import com.resource.management.hr.bl.exceptions.BLException;
import com.resource.management.hr.bl.interfaces.manager.*;
import com.resource.management.hr.bl.interfaces.pojo.DesignationInterface;
import com.rsource.management.hr.bl.interfaces.pojo.*;
import com.rsource.management.hr.bl.exceptions.*;
import com.rsource.management.hr.bl.pojo.*;
import com.rsource.management.hr.bl.manager.*;
import java.util.*;

class DesignationManagerGetDesignationByCodeTestCase{
    public static void main(String gg[]){
       
        try{

            DesignationManagerInterface designationManager;
            designationManager=DesignationManager.getDesignationManager();
            Set<DesignationInterface>designations;
            designations=designationManager.getDesignations();
            designations.forEach((designation)->{
                System.out.println("code : "+designation.getCode()+" , title : "+designation.getTitle());
            });

        }catch(BLException ble){
            List<String>properties=blException.getProperties();
            properties.forEach((property)->{
                System.out.println(ble.getException(property));
            });
        }
    }
}
