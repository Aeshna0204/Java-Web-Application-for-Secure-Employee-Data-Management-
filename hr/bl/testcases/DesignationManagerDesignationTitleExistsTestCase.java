import com.resource.management.hr.bl.exceptions.BLException;
import com.resource.management.hr.bl.interfaces.manager.*;
import com.rsource.management.hr.bl.interfaces.pojo.*;
import com.rsource.management.hr.bl.exceptions.*;
import com.rsource.management.hr.bl.pojo.*;
import com.rsource.management.hr.bl.manager.*;
import java.util.*;

class DesignationManagerGetDesignationByCodeTestCase{
    public static void main(String gg[]){
        String title=(gg[0]);
        try{

            DesignationManagerInterface designationManager;
            designationManager=DesignationManager.getDesignationManager();
            System.out.println("title Exists: "+designationManager.designationTitleExists(title));

        }catch(BLException ble){
            List<String>properties=blException.getProperties();
            properties.forEach((property)->{
                System.out.println(ble.getException(property));
            });
        }
    }
}
