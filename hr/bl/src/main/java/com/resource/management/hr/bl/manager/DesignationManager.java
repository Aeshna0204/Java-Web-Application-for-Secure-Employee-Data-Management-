package com.resource.management.hr.bl.manager;

import com.resource.management.hr.bl.interfaces.pojo.*;
import com.resource.management.hr.bl.interfaces.manager.*;
import com.resource.management.hr.bl.exceptions.*;
import com.resource.management.hr.bl.pojo.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.hr.dl.dao.*;
import com.thinking.machines.hr.dl.exceptions.*;
import java.util.*;

public class DesignationManager implements DesignationManagerInterface {

    private Map<Integer, DesignationInterface> codeWiseDesignationsMap;
    private Map<String, DesignationInterface> titleWiseDesignationsMap;
    private Set<DesignationInterface> designationsSet;
    private static DesignationManager designationManager = null;

    // to make object only once we set the constructor private otherwise user will
    // create multiple objects
    private DesignationManager() throws BLException {
        populateDataStructures();
    }

    private void populateDataStructures() throws BLException {
        this.codeWiseDesignationsMap = new HashMap<>();
        this.titleWiseDesignationsMap = new HashMap<>();
        this.designationsSet = new TreeSet<>();
        try {

            Set<DesignationDTOInterface> dlDesignations;
            dlDesignations = new DesignationDAO().getAll();
            DesignationInterface designation;
            for (DesignationDTOInterface dlDesignation : dlDesignations) {
                designation = new Designation();
                designation.setCode(dlDesignation.getCode());
                designation.setTitle(dlDesignation.getTitle());
                this.codeWiseDesignationsMap.put(Integer.valueOf(designation.getCode()), designation);
                this.titleWiseDesignationsMap.put(designation.getTitle().toUpperCase(), designation);
                this.designationsSet.add(designation);
            }

        } catch (DAOException daoe) {
            BLException ble = new BLException();
            ble.setGenericException(daoe.getMessage());
            throw ble;

        }
    }

    // constructor private kiya toh object nahi ban skta but ek object to chahiye
    // isliye iss method se ek hi object banega
    public static DesignationManagerInterface getDesignationManager() throws BLException {
        if (designationManager == null)
            designationManager = new DesignationManager();
        return designationManager;
    }

    public void addDesignation(DesignationInterface designation) throws BLException {

        BLException blException = new BLException();
        if (designation == null) {
            blException.setGenericException("Designation is required");
            throw blException;
        }
        int code = designation.getCode();
        String title = designation.getTitle();
        if (code != 0) {
            blException.addException("code", "Code should be zero");
        }
        if (title == null) {
            blException.addException("title", "title required");
            title = "";
        } else {
            title = title.trim();
            if (title.length() == 0)
                blException.addException("title", "title required");

        }
        if (title.length() > 0) {
            if (this.titleWiseDesignationsMap.containsKey(title.toUpperCase()))
                ;
            {
                blException.addException("title", "title already exists");
            }

        }
        if (blException.hasExceptions()) {
            throw blException;
        }
        // validations end here

        try {
            DesignationDTOInterface designationDTO;
            designationDTO = new DesignationDTO();
            designationDTO.setTitle(title);
            DesignationDAOInterface designationDAO = new DesignationDAO();
            designationDAO.add(designationDTO);
            code = designationDTO.getCode();
            designation.setCode(code);
            Designation dsDesignation = new Designation();
            dsDesignation.setCode(code);
            dsDesignation.setTitle(title);
            codeWiseDesignationsMap.put(Integer.valueOf(code), dsDesignation);
            titleWiseDesignationsMap.put(title.toUpperCase(), dsDesignation);
            designationsSet.add(dsDesignation);
        } catch (DAOException daoException) {
            blException.setGenericException(daoException.getMessage());
        }

    }

    // -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    public void updateDesignation(DesignationInterface designation) throws BLException {

        BLException blException = new BLException();
        if (designation == null) {
            blException.setGenericException("Designation Required");
            throw blException;
        }
        int code = designation.getCode();
        String title = designation.getTitle();
        if (code <= 0)
            blException.addException("code", "Invalid Code: " + code);
        if (code > 0) {
            if (this.codeWiseDesignationsMap.containsKey(code) == false) {
                blException.addException("code", "Invalid Code");
                throw blException;
            }
        }

        if (title == null) {
            blException.addException("title", "Title Required");
            title = "";
        } else {
            title = title.trim();
            if (title.length() == 0)
                blException.addException("title", "Title Required");
        }

        if (title.length() > 0) {
            // new title already exists
            DesignationInterface d = titleWiseDesignationsMap.get(title.toUpperCase());
            if (d != null && d.getCode() != code)
                blException.addException("title", "Designation: " + title + " exists.");
        }
        if (blException.hasExceptions()) {
            throw blException;
        }

        try{
            DesignationInterface dsDesignation=codeWiseDesignationsMap.get(code);
            DesignationDTOInterface dlDesignation=new DesignationDTO();
            dlDesignation.setCode(code);
            dlDesignation.setTitle(title);
            new DesignationDAO().update(dlDesignation);

            // remove the old one from all datastructure
            codeWiseDesignationsMap.remove(code);
            titleWiseDesignationsMap.remove(dsDesignation.getTitle().toUpperCase());
            designationsSet.remove(dsDesignation);

            // update the ds object
            dsDesignation.setTitle(title);
            // update the ds
            codeWiseDesignationsMap.put(code,dsDesignation);
            titleWiseDesignationsMap.put(title.toUpperCase(),dsDesignation);
            designationsSet.add(dsDesignation);
        }catch(DAOException daoException){
            blException.setGenericException(daoException.getMessage());
        }

    }
// ----------------------------------------------------------------------------------------------------------------------------------------------------------

    public void removeDesignation(DesignationInterface designation) throws BLException {
        
        BLException blException = new BLException();
        if (designation == null) {
            blException.setGenericException("Designation Required");
            throw blException;
        }
        int code = designation.getCode();
        if (code <= 0){
            blException.addException("code", "Invalid Code: " + code);
            throw blException;
        }
            
        if (code > 0) {
            if (this.codeWiseDesignationsMap.containsKey(code) == false) {
                blException.addException("code", "Invalid Code");
                throw blException;
            }
        }

        try{
            DesignationInterface dsDesignation=codeWiseDesignationsMap.get(code);
            new DesignationDAO().delete(code);
            codeWiseDesignationsMap.remove(code);
            titleWiseDesignationsMap.remove(dsDesignation.getTitle().toUpperCase());
            designationsSet.remove(dsDesignation);
        }catch(DAOException daoException){
            blException.setGenericException(daoException.getMessage());
            throw blException;
        }

    }

    // ------------------------------------------------------------------------------------------------------------
    public DesignationInterface getDesignationByCode(int code) throws BLException {
        
        DesignationInterface designation;
        designation=codeWiseDesignationsMap.get(code);
        if(designation==null){
            BLException blException;
            blException=new BLException();
            blException.addException("code","Invalid code");
            throw blException;
        }
        DesignationInterface d=new Designation();
        d.setCode(designation.getCode());
        d.setTitle(designation.getTitle());
        return d;
    }
// modifier nahi diya isliye package k ander use ho skta or package k ander hi use krr rhe

     DesignationInterface getDSDesignationByCode(int code){
        
        DesignationInterface designation;
        designation=codeWiseDesignationsMap.get(code);
        return designation;
    }


// -----------------------------------------------------------------------------------------------------------------------------------------

    public DesignationInterface getDesignationByTitle(String title) throws BLException {
       
        DesignationInterface designation;
        designation=titleWiseDesignationsMap.get(title.toUpperCase());
        if(designation==null){
            BLException blException=new BLException();
            blException.addException("title","Invalid designation"+title);
            throw blException;
        }
        DesignationInterface d=new Designation();
        d.setCode(designation.getCode());
        d.setTitle(designation.getTitle());
        return d;

    }

    public int getDesignationCount(){
        return designationsSet.size();
    }

    public boolean designationCodeExists(int code){
        return codeWiseDesignationsMap.containsKey(code);
    }

    public boolean designationTitleExists(String title) {
       return titleWiseDesignationsMap.containsKey(title.toUpperCase());
    }

    public Set<DesignationInterface> getDesignations(){
        Set<DesignationInterface>designations;
        designations=new TreeSet<>();
        designationsSet.forEach((designation)->{
            DesignationInterface d=new Designation();
            d.setCode(designation.getCode());
            d.setTitle(designation.getTitle());
            designations.add(d);
          
        });
          return designations;
    }

}
