/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shorelineexamproject.gui.model;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.json.JSONArray;
import shorelineexamproject.be.Customization;
import shorelineexamproject.be.ListViewObject;
import shorelineexamproject.be.LogIn;
import shorelineexamproject.be.TraceLog;
import shorelineexamproject.bll.BLL;
import shorelineexamproject.dal.exceptions.DalException;

/**
 *
 * @author Jesper
 */
public class Model
{

    //start of singleton setup
    private static Model firstInstance = null;
    private BLL bll;

    private Model() throws IOException, DalException
    {
        this.bll = new BLL();
    }

//    private Model()
//    {
//    }
    //not sure if method below is needed
    private ObservableList<Customization> customizationList = FXCollections.observableArrayList();
    private ObservableList<TraceLog> logList = FXCollections.observableArrayList();

    public synchronized static Model getInstance() throws IOException, DalException
    {
        if (firstInstance == null)
        {
            firstInstance = new Model();
        }
        return firstInstance;
    }
    //end of singleton setup

    public List<ListViewObject> readXLSXHeaders(String filepath)
    {
        return bll.readXLSXHeaders(filepath);
    }

    public void getXLSXHeaderValues(String filepath,
            String header, ArrayList<String> headerList)
    {
        bll.getXLSXHeaderValues(filepath, header, headerList);
    }

    public String getDate()
    {
        return bll.getDate();
    }

    /**
     *
     * @param FileName
     * @throws IOException
     */
    public void CreateJSONFile(String directory, String fileName, JSONArray jarray) throws IOException
    {
        bll.CreateJSONFile(directory, fileName, jarray);
    }

    /**
     *
     * @return
     */
    public JSONArray CreateJsonObjects()
    {
        bll.CreateJsonObjects();
        return null;
    }

    public ObservableList<Customization> getAllCustomizations() throws DalException
    {
        customizationList.setAll(bll.getAllCustomizations());
        return customizationList;
    }

    public void addCustomizationToDB(Customization c) throws DalException
    {
        bll.addCustomizationToDB(c);
        getAllCustomizations();
    }

    public void removeCustomizationFromDb(Customization selectedCustomization) throws DalException
    {
        bll.removeCustomizationFromDb(selectedCustomization);
    }

    public boolean UserLogin(String userName, String password) throws DalException
    {
        return bll.UserLogin(userName, password);
    }

    public void addUserToDB(LogIn l) throws DalException
    {
        bll.addUserToDB(l);
    }

    public ObservableList<TraceLog> getTraceLogList()
    {
        return logList;
    }

    public void loadTraceLog() throws DalException
    {
        logList.setAll(bll.getAllTraceLogs());
    }

    public void addTraceLogToDB(TraceLog t) throws DalException
    {
        bll.addTraceLogToDB(t);
    }
}
