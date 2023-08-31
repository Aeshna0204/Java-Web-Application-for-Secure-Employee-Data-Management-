package com.resource.management.hr.bl.interfaces.manager;
import com.resource.management.hr.bl.interfaces.pojo.*;
import com.resource.management.hr.bl.exceptions.*;
import java.util.*;
public interface DesignationManagerInterface {
    public void addDesignation(DesignationInterface designation)throws BLException;
    public void updateDesignation(DesignationInterface designation)throws BLException;
    public void removeDesignation(DesignationInterface designation)throws BLException;
    public DesignationInterface getDesignationByCode(int code)throws BLException;
    public DesignationInterface getDesignationByTitle(String title)throws BLException;
    public int getDesignationCount();
    public boolean designationCodeExists(int code);
    public boolean designationTitleExists(String title);
    public Set<DesignationInterface> getDesignations();


}
