package com.thinking.machines.hr.dl.dao;

import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.hr.dl.dao.*;
import com.thinking.machines.hr.dl.exceptions.*;
import java.util.*;
import java.io.*;

public class DesignationDAO implements DesignationDAOInterface {

    private final static String FILE_NAME = "designation.data";

    public void add(DesignationDTOInterface designationDTO) throws DAOException {
        if (designationDTO == null)
            throw new DAOException("Designation is null");
        String title = designationDTO.getTitle();
        if (title == null)
            throw new DAOException("Designation is null");
        title = title.trim();// to remove right and left spaces

        if (title.length() == 0)
            throw new DAOException("Length of Designation is zero");

        try {
            File file = new File(FILE_NAME);
            RandomAccessFile randomAccessFile;
            randomAccessFile = new RandomAccessFile(file, "rw");
            int lastGeneratedCode = 0;
            int recordCount = 0;
            String lastGeneratedCodeString = "";
            String recordCountString = "";

            if (randomAccessFile.length() == 0) {

                lastGeneratedCodeString = "0";
                while (lastGeneratedCodeString.length() < 10)
                    lastGeneratedCodeString += " ";
                recordCountString = "0";
                while (recordCountString.length() < 10)
                    recordCountString += " ";
                randomAccessFile.writeBytes(lastGeneratedCodeString);
                randomAccessFile.writeBytes("\n");
                randomAccessFile.writeBytes(recordCountString);
                randomAccessFile.writeBytes("\n");

            } else {
                lastGeneratedCodeString = randomAccessFile.readLine().trim();
                recordCountString = randomAccessFile.readLine().trim();
                lastGeneratedCode = Integer.parseInt(lastGeneratedCodeString);
                recordCount = Integer.parseInt(recordCountString);
            }
            int fCode;
            String fTitle;
            while (randomAccessFile.getFilePointer() < randomAccessFile.length()) {
                fCode = Integer.parseInt(randomAccessFile.readLine());
                fTitle = randomAccessFile.readLine();
                if (fTitle.equalsIgnoreCase(title)) {
                    randomAccessFile.close();
                    throw new DAOException("Designation: " + title + " already exists");

                }

            }

            int code = lastGeneratedCode + 1;
            randomAccessFile.writeBytes(String.valueOf(code));
            randomAccessFile.writeBytes("\n");
            randomAccessFile.writeBytes(title);
            randomAccessFile.writeBytes("\n");
            designationDTO.setCode(code);
            lastGeneratedCode++;
            recordCount++;

            lastGeneratedCodeString = String.valueOf(lastGeneratedCode);
            while (lastGeneratedCodeString.length() < 10)
                lastGeneratedCodeString += "";
            randomAccessFile.seek(0);
            randomAccessFile.writeBytes(lastGeneratedCodeString);
            randomAccessFile.writeBytes("\n");
            randomAccessFile.close();
        } catch (IOException ioException) {
            throw new DAOException(ioException.getMessage());
        }

        // ---------------------------------------------------------

    }

    public void update(DesignationDTOInterface designationDTO) throws DAOException {

        if (designationDTO == null)
            throw new DAOException("Designation is null");
        int code = designationDTO.getCode();
        if (code <= 0)
            throw new DAOException("Invalid Code" + code);
        String title = designationDTO.getTitle();
        if (title == null)
            throw new DAOException("designation title is null");
        title = title.trim();
        if (title.length() == 0)
            throw new DAOException("title cannot be of zero length");

        try {

            File file = new File(FILE_NAME);
            if (file.exists() == false)
                throw new DAOException("Inavlid code" + code);
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
            if (randomAccessFile.length() == 0) {
                randomAccessFile.close();
                throw new DAOException("Invalid Code " + code);
            }
            int fCode;
            String fTitle;
            randomAccessFile.readLine();
            randomAccessFile.readLine();
            boolean found = false;

            while (randomAccessFile.getFilePointer() < randomAccessFile.length()) {
                fCode = Integer.parseInt(randomAccessFile.readLine());

                if (fCode == code) {
                    found = true;
                    break;
                }
                randomAccessFile.readLine();

            }
            if (found == false) {
                randomAccessFile.close();
                throw new DAOException("Invalid Code: " + code);
            }

            randomAccessFile.seek(0);
            randomAccessFile.readLine();
            randomAccessFile.readLine();
            while (randomAccessFile.getFilePointer() < randomAccessFile.length()) {
                fCode = Integer.parseInt(randomAccessFile.readLine());
                fTitle = randomAccessFile.readLine();
                if (fCode != code && title.equalsIgnoreCase(fTitle)) {
                    randomAccessFile.close();
                    throw new DAOException("Title " + title + " already exists");

                }
            }
            File tmpFile = new File("tmp.data");
            if (tmpFile.exists())
                tmpFile.delete();
            RandomAccessFile tmprandom = new RandomAccessFile(tmpFile, "rw");
            randomAccessFile.seek(0);
            tmprandom.writeBytes(randomAccessFile.readLine());
            tmprandom.writeBytes("\n");
            tmprandom.writeBytes(randomAccessFile.readLine());
            tmprandom.writeBytes("\n");
            while (randomAccessFile.getFilePointer() < randomAccessFile.length()) {
                fCode = Integer.parseInt(randomAccessFile.readLine());
                fTitle = randomAccessFile.readLine();
                if (code != fCode) {
                    tmprandom.writeBytes(String.valueOf(fCode));
                    tmprandom.writeBytes("\n");
                    tmprandom.writeBytes(fTitle);
                    tmprandom.writeBytes("\n");
                } else {
                    tmprandom.writeBytes(String.valueOf(code));
                    tmprandom.writeBytes("\n");
                    tmprandom.writeBytes(title);
                    tmprandom.writeBytes("\n");
                }
            }
            randomAccessFile.seek(0);
            tmprandom.seek(0);
            while (tmprandom.getFilePointer() < tmprandom.length()) {
                randomAccessFile.writeBytes(tmprandom.readLine());
                randomAccessFile.writeBytes("\n");
            }
            randomAccessFile.setLength(tmprandom.length());
            tmprandom.setLength(0);
            randomAccessFile.close();
            tmprandom.close();

        } catch (IOException ioe) {
            throw new DAOException(ioe.getMessage());
        }

    }

    public void delete(int code) throws DAOException { 

        if (code <= 0)
            throw new DAOException("Invalid code: " + code);
        try {
            String fTitle = "";
            File file = new File(FILE_NAME);
            if (!file.exists()) throw new DAOException("Invalid Code: " + code);
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
            if (randomAccessFile.length() == 0) {
                randomAccessFile.close();
                throw new DAOException("Invalid Code: " + code);
            }
            int fCode;
            randomAccessFile.readLine();
            int recordCount = Integer.parseInt(randomAccessFile.readLine().trim());
            boolean found = false;
            while (randomAccessFile.getFilePointer() < randomAccessFile.length()) {
                fCode = Integer.parseInt(randomAccessFile.readLine());
                fTitle = randomAccessFile.readLine();
                if (fCode == code) {
                    found = true;
                    break;
                }
            }
            if (found == false) {
                randomAccessFile.close();
                throw new DAOException("Invalid Code: " + code);
            }
            if(new EmployeeDAO().isDesignationAlloted(code)){
                randomAccessFile.close();
                throw new DAOException("Employee exists with designation: "+fTitle);
            }
            
            File tmpFile = new File("tmp.data");
            if (tmpFile.exists())
                tmpFile.delete();
            RandomAccessFile tmprandom = new RandomAccessFile(tmpFile, "rw");
            randomAccessFile.seek(0);
            tmprandom.writeBytes(randomAccessFile.readLine());
            tmprandom.writeBytes("\n");
            tmprandom.writeBytes(randomAccessFile.readLine());
            tmprandom.writeBytes("\n");
            while (randomAccessFile.getFilePointer() < randomAccessFile.length()) {
                fCode = Integer.parseInt(randomAccessFile.readLine());
                fTitle = randomAccessFile.readLine();
                if (code != fCode) {
                    tmprandom.writeBytes(String.valueOf(fCode));
                    tmprandom.writeBytes("\n");
                    tmprandom.writeBytes(fTitle);
                    tmprandom.writeBytes("\n");
                }
            }
            randomAccessFile.seek(0);
            tmprandom.seek(0);
            randomAccessFile.writeBytes(tmprandom.readLine());
            randomAccessFile.writeBytes("\n");
            tmprandom.readLine();
            String recordCountString = String.valueOf(recordCount - 1);
            while (recordCountString.length() < 10)
                recordCountString += " ";
            randomAccessFile.writeBytes(recordCountString);
            randomAccessFile.writeBytes("\n");
            while (tmprandom.getFilePointer() < tmprandom.length()) {
                randomAccessFile.writeBytes(tmprandom.readLine());
                randomAccessFile.writeBytes("\n");
            }
            randomAccessFile.setLength(tmprandom.length());
            tmprandom.setLength(0);
            randomAccessFile.close();
            tmprandom.close();
        } catch (IOException ioe) {
            throw new DAOException(ioe.getMessage());
        }

    }

    public Set<DesignationDTOInterface> getAll() throws DAOException {

        Set<DesignationDTOInterface> designations = new TreeSet<>();
        try {
            File file = new File(FILE_NAME);
            if (file.exists() == false)
                return designations;
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
            if (randomAccessFile.length() == 0) {
                randomAccessFile.close();
                return designations;
            }
            randomAccessFile.readLine();
            randomAccessFile.readLine();
            DesignationDTOInterface designationDTO;
            while (randomAccessFile.getFilePointer() < randomAccessFile.length()) {
                designationDTO = new DesignationDTO();
                designationDTO.setCode(Integer.parseInt(randomAccessFile.readLine()));
                designationDTO.setTitle(randomAccessFile.readLine());
                designations.add(designationDTO);
            }
            randomAccessFile.close();
            return designations;

        } catch (IOException ioException) {
            throw new DAOException(ioException.getMessage());
        }

    }

    // -------------------------------------------------------------------------------------
    public DesignationDTOInterface getByCode(int code) throws DAOException {

        if (code <= 0)
            throw new DAOException("invalid code" + code);
        try {
            File file = new File(FILE_NAME);
            if (file.exists() == false)
                throw new DAOException("Invalid code " + code);
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
            if (file.length() == 0) {
                randomAccessFile.close();
                throw new DAOException("invalid code" + code);
            }
            randomAccessFile.readLine();
            int recordCount = Integer.parseInt(randomAccessFile.readLine().trim());
            if (recordCount == 0) {
                randomAccessFile.close();
                throw new DAOException("Invalid code: " + code);
            }
            int fCode;
            String fTitle;
            while (randomAccessFile.getFilePointer() < randomAccessFile.length()) {
                fCode = Integer.parseInt(randomAccessFile.readLine().trim());
                fTitle = randomAccessFile.readLine().trim();
                if (fCode == code) {
                    randomAccessFile.close();
                    DesignationDTOInterface designationDTO = new DesignationDTO();
                    designationDTO.setCode(fCode);
                    designationDTO.setTitle(fTitle);
                    return designationDTO;
                }
            }
            randomAccessFile.close();
            throw new DAOException("Invalid code: " + code);

        } catch (IOException ioException) {
            throw new DAOException(ioException.getMessage());
        }

    }
    // ---------------------------------------------------------------------

    public DesignationDTOInterface getByTitle(String title) throws DAOException {

        if (title == null || title.trim().length() == 0)
            throw new DAOException("invalid Title");
        title = title.trim();
        try {
            File file = new File(FILE_NAME);
            if (file.exists() == false)
                throw new DAOException("Invalid title " + title);
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
            if (file.length() == 0) {
                randomAccessFile.close();
                throw new DAOException("invalid title" + title);
            }
            randomAccessFile.readLine();
            int recordCount = Integer.parseInt(randomAccessFile.readLine().trim());
            if (recordCount == 0) {
                randomAccessFile.close();
                throw new DAOException("Invalid title: " + title);
            }
            int fCode;
            String fTitle;
            while (randomAccessFile.getFilePointer() < randomAccessFile.length()) {
                fCode = Integer.parseInt(randomAccessFile.readLine().trim());
                fTitle = randomAccessFile.readLine().trim();
                if (title.equalsIgnoreCase(fTitle)) {
                    randomAccessFile.close();
                    DesignationDTOInterface designationDTO = new DesignationDTO();
                    designationDTO.setCode(fCode);
                    designationDTO.setTitle(fTitle);
                    return designationDTO;
                }
            }
            randomAccessFile.close();
            throw new DAOException("Invalid title: " + title);

        } catch (IOException ioException) {
            throw new DAOException(ioException.getMessage());
        }
        // -----------------------------------------------------------------------------------------------------------------------------

    }

    public boolean codeExists(int code) throws DAOException {

        if (code <= 0)
            return false;
        try {
            File file = new File(FILE_NAME);
            if (file.exists() == false)
                return false;
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
            if (file.length() == 0) {
                randomAccessFile.close();
                return false;
            }
            randomAccessFile.readLine();
            int recordCount = Integer.parseInt(randomAccessFile.readLine().trim());
            if (recordCount == 0) {
                randomAccessFile.close();
                return false;
            }
            int fCode;
            while (randomAccessFile.getFilePointer() < randomAccessFile.length()) {
                fCode = Integer.parseInt(randomAccessFile.readLine().trim());
                randomAccessFile.readLine().trim();
                if (fCode == code) {
                    randomAccessFile.close();
                    return true;
                }
            }
            randomAccessFile.close();
            return false;

        } catch (IOException ioException) {
            throw new DAOException(ioException.getMessage());
        }

    }

    // -------------------------------------------------------------------------------------------------
    public boolean titleExists(String title) throws DAOException {

        if (title == null || title.trim().length() == 0)
            return false;
        title = title.trim();
        try {
            File file = new File(FILE_NAME);
            if (file.exists() == false)
                return false;
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
            if (file.length() == 0) {
                randomAccessFile.close();
                return false;
            }
            randomAccessFile.readLine();
            int recordCount = Integer.parseInt(randomAccessFile.readLine().trim());
            if (recordCount == 0) {
                randomAccessFile.close();
                return false;
            }
            String fTitle;
            while (randomAccessFile.getFilePointer() < randomAccessFile.length()) {
                randomAccessFile.readLine();
                fTitle = randomAccessFile.readLine();

                if (title.equalsIgnoreCase(fTitle)) {
                    randomAccessFile.close();
                    return true;
                }
            }
            randomAccessFile.close();
            return false;

        } catch (IOException ioException) {
            throw new DAOException(ioException.getMessage());
        }

    }

    // -------------------------------------------------------------------------------------------------------------------------------------
    public int getCount() throws DAOException {

        try {

            File file = new File(FILE_NAME);
            if (file.exists() == false)
                return 0;
            RandomAccessFile randomAccessFile;
            randomAccessFile = new RandomAccessFile(file, "rw");
            if (randomAccessFile.length() == 0) {
                randomAccessFile.close();
                return 0;
            }
            randomAccessFile.readLine();
            int recordCount;
            recordCount = Integer.parseInt(randomAccessFile.readLine().trim());
            randomAccessFile.close();
            return recordCount;

        } catch (IOException ioException) {
            throw new DAOException(ioException.getMessage());
        }

    }

}
