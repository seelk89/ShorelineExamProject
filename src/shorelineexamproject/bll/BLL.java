/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shorelineexamproject.bll;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import shorelineexamproject.be.Customization;
import shorelineexamproject.be.ListViewObject;
import shorelineexamproject.dal.DAOCSVReader;
import shorelineexamproject.be.LogIn;
import shorelineexamproject.be.TraceLog;
import shorelineexamproject.dal.DAOCustomization;
import shorelineexamproject.dal.DAOJSONWriter;
import shorelineexamproject.dal.DAOLogIn;
import shorelineexamproject.dal.DAOXLSXReader;
import shorelineexamproject.dal.DAOTraceLog;
import shorelineexamproject.dal.StrategyConversion;
import shorelineexamproject.dal.exceptions.DalException;

/**
 *
 * @author Jesper
 */
public class BLL
{
    private DAOXLSXReader daoXLSXReader = new DAOXLSXReader();
    private DAOCSVReader daoCSVReader = new DAOCSVReader();
    private DAOJSONWriter daoJSONWriter = new DAOJSONWriter();
    private DAOCustomization daoCustomization;
    private DAOLogIn daoLogIn;
    private DAOTraceLog daoTraceLog;
    private StrategyConversion strategyConversion;

    public BLL() throws IOException, DalException
    {
        this.daoCustomization = new DAOCustomization();
        this.daoLogIn = new DAOLogIn();
        this.daoTraceLog = new DAOTraceLog();
    }

//    public List<ListViewObject> headers(String filepath)
//    {
//        return strategyConversion.headers(filepath);
//    }
//
//    public void valuesInHeaderColumn(String filepath,
//            String header, ArrayList<String> headerList)
//    {
//        strategyConversion.valuesInHeaderColumn(filepath, header, headerList);
//    }
    
    public List<ListViewObject> readXLSXHeaders(String filepath)
    {
        return daoXLSXReader.readXLSXHeaders(filepath);
    }

    public void getXLSXHeaderValues(String filepath,
            String header, ArrayList<String> headerList)
    {
        daoXLSXReader.getXLSXHeaderValues(filepath, header, headerList);
    }
    
    public List<ListViewObject>readCSVHeaders(String filepath)
    {
        return daoCSVReader.readCSVHeaders(filepath);
    }
    
    public void getCSVHeaderValues(String filepath,
            String header, ArrayList<String> headerList)
    {
        daoCSVReader.getCSVHeaderValues(filepath, header, headerList);
    }

    /**
     * Gets the current date and formats it the way it is needed.
     * @return 
     */
    public String getDate()
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDateTime dateTime = LocalDateTime.now();
        String formattedDate = dateTime.format(formatter);

        return formattedDate;
    }

    public void CreateJSONFile(String directory, String fileName, JSONArray jarray) throws IOException
    {
        daoJSONWriter.CreateJSONFile(directory, fileName, jarray);
    }

    public List<Customization> getAllCustomizations() throws DalException
    {
        return daoCustomization.getAllCustomizations();
    }

    public void addCustomizationToDB(Customization c) throws DalException
    {
        daoCustomization.addCustomizationToDB(c);
    }

    public void removeCustomizationFromDb(Customization selectedCustomization) throws DalException
    {
        daoCustomization.removeCustomizationFromDb(selectedCustomization);
    }

    public boolean UserLogin(String userName, String password) throws DalException
    {
        return daoLogIn.UserLogin(userName, password);
    }

    public void addUserToDB(LogIn l) throws DalException
    {
        daoLogIn.addUserToDB(l);
    }

    public List<TraceLog> getAllTraceLogs() throws DalException
    {
        return daoTraceLog.getAllTraceLogs();
    }
    
     public void addTraceLogToDB(TraceLog t) throws DalException
    {
        daoTraceLog.addTraceLogToDB(t);
    }
}
