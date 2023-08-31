package com.resource.management.hr.pl.model;
import com.resource.management.hr.bl.interfaces.manager.*;
import com.resource.management.hr.bl.interfaces.pojo.*;
import com.resource.management.hr.bl.manager.*;
import com.resource.management.hr.bl.pojo.*;
import com.resource.management.hr.bl.exceptions.*;
import java.awt.*;
import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.table.*;

public class DesignationModel extends AbstractTableModel{

    private DesignationManagerInterface designationManager;
    private List<DesignationInterface>designations;
    String[] columnTitle;
    public DesignationModel(){
        this.populateDataStructure();
    }

     public int getRowCount(){
        return designations.size();
    }

    public int getColumnCount(){
        return columnTitle.length;
    }

    public String getColumnName(int columnIndex){
       return columnTitle[columnIndex];
    }


    public Object getValueAt(int rowIndex,int columnIndex){
        if(columnIndex==0)return rowIndex+1;
        return this.designations.get(rowIndex).getTitle();
    }

    public boolean isCellEditable(int rowIndex,int columnIndex){
        return false;
    }

    public Class getColumnClass(int columnIndex){
        
           if(columnIndex==0)return Integer.class;
           return String.class;
       
    }


    private void populateDataStructure(){
        this.columnTitle=new String[2];
        this.columnTitle[0]="S.no";
        this.columnTitle[1]="Designations";
        try{
        designationManager= DesignationManager.getDesignationManager();
        }catch(BLException ble){
            // ????
        }
        
        Set<DesignationInterface>blDesignations=designationManager.getDesignations();
        this.designations=new LinkedList<>();
        for(DesignationInterface designation:blDesignations){
            designations.add(designation);
        }
        Collections.sort(this.designations,new Comparator<DesignationInterface>(){
            public int compare(DesignationInterface left,DesignationInterface right){
                return left.getTitle().toUpperCase().compareTo(right.getTitle().toUpperCase());
            }
        });
    
    }


    //  Application Specific Methods

    // Add method

    public void add(DesignationInterface designation)throws BLException{

        designationManager.addDesignation(designation);
        this.designations.add(designation);
        Collections.sort(this.designations,new Comparator<DesignationInterface>(){
            public int compare(DesignationInterface left,DesignationInterface right){
                return left.getTitle().toUpperCase().compareTo(right.getTitle().toUpperCase());
            }
        });

        fireTableDataChanged();
    }

    // for displaying the designation that is added
    public int indexOfDesignation(DesignationInterface designation)throws BLException{
        Iterator<DesignationInterface> iterators=this.designations.iterator();
        DesignationInterface d;
        int index=0;
        while(iterators.hasNext()){
            d=iterators.next();
            if(d.equals(designation)){
                return index;      
            }
            index++;
        }
        BLException blException=new BLException();
        blException.setGenericException("Invalid Designation"+designation.getTitle());
        throw blException;
    }

    // for searching

    public int indexOfTitle(String title, boolean partialLeftSearch)throws BLException{
        Iterator<DesignationInterface> iterators=this.designations.iterator();
        DesignationInterface d;
        int index=0;
        while(iterators.hasNext()){
            d=iterators.next();
            if(partialLeftSearch)
            {
            if(d.getTitle().toUpperCase().startsWith(title.toUpperCase())){
                return index;      
            }
             }
             else{
                if(d.getTitle().toUpperCase().equals(title.toUpperCase())){
                    return index;
                }
             }
            index++;
        }
        BLException blException=new BLException();
        blException.setGenericException("Invalid Title");
        throw blException;
    }

    // update method

    public void update(DesignationInterface designation)throws BLException{

        designationManager.updateDesignation(designation);
        this.designations.remove(indexOfDesignation(designation));
        designations.add(designation);
        Collections.sort(this.designations,new Comparator<DesignationInterface>(){
            public int compare(DesignationInterface left,DesignationInterface right){
                return left.getTitle().toUpperCase().compareTo(right.getTitle().toUpperCase());
            }
        });
        fireTableDataChanged();

    }


    public void remove(int code)throws BLException{

        // this function need to be tally it is corrected by you

        DesignationInterface ds=new Designation();
        ds.setCode(code);
        designationManager.removeDesignation(ds);
        int index=0;
        Iterator<DesignationInterface> iterator= this.designations.iterator();
        while(iterator.hasNext()){
            if(iterator.next().getCode()==code)break;
            index++;
        }
        if(index==this.designations.size()){
            BLException blException=new BLException();
            blException.setGenericException("Invalid Code");
            throw blException;
        }
        this.designations.remove(index);
        fireTableDataChanged();

    }


    public DesignationInterface getDesignationAt(int index)throws BLException{
        if(index<0 || index>=this.designations.size()){
            BLException blException=new BLException();
            blException.setGenericException("Invalid index");
            throw blException;
        }
        return this.designations.get(index);
    }












}


class psp{
    DesignationModel designationModel=new DesignationModel();
}
