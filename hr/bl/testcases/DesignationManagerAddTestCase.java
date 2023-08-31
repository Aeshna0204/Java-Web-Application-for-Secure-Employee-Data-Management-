import com.resource.management.hr.bl.managers.*;
import com.resource.management.hr.bl.interfaces.managers.*;
import com.resource.management.hr.bl.interfaces.pojo.*;
import com.resource.management.hr.bl.pojo.*;
import com.resource.management.hr.bl.exceptions.*;
import java.util.*;

class DesignationManagerAddTestCase {
    public static void main(String args[]) {
        DesignationInterface designation = new Designation();
        designation.setTitle("Manager");
        try {
            DesignationManagerInterface designationManager;
            designationManager = DesignationManager.getDesignationManager();
            designationManager.addDesignation(designation);
            System.out.println("Designation added with code as : " + designation.getCode());
        } catch (BLException blException) {
            if (blException.hasGenericException())
                System.out.println(blException.getGenericException());
            List<String> properties = blException.getProperties();
            for (String property : properties) {
                System.out.println(blException.getException(property));
            }
        }
    }
}