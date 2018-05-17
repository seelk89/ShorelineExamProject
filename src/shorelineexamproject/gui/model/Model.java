/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shorelineexamproject.gui.model;

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

/**
 *
 * @author Jesper
 */
public class Model
{
 private BLL bll;

    public Model() throws IOException
    {
        this.bll = new BLL();
    }

    //start of singleton setup
    private static Model firstInstance = null;
   
    public synchronized static Model getInstance() throws IOException
    {
        if (firstInstance == null)
        {
            firstInstance = new Model();
        }
        return firstInstance;
    }
    //end of singleton setup

    public ObservableList<Customization> cList = FXCollections.observableArrayList();
    private ObservableList<TraceLog> logList = FXCollections.observableArrayList();

    public List<ListViewObject> readXLSXHeaders(String filepath)
    {
        return bll.readXLSXHeaders(filepath);
    }

    public List<ListViewObject> readCSVHeaders(String filepath)
    {
        return bll.readCSVHeaders(filepath);
    }

    public void getXLSXHeaderValues(String filepath,
            String header, ArrayList<String> headerList)
    {
        bll.getXLSXHeaderValues(filepath, header, headerList);
    }

    public void getCSVHeaderValues(String filepath,
            String header, ArrayList<String> headerList)
    {
        bll.getCSVHeaderValues(filepath, header, headerList);
    }

    public String getDate()
    {
        return bll.getDate();
    }

    public void CreateJSONFile(String directory, String fileName, JSONArray jarray) throws IOException
    {
        bll.CreateJSONFile(directory, fileName, jarray);
    }

    public List<Customization> getAllCustomizations()
    {
        return bll.getAllCustomizations();
    }

    public void addCustomizationToDB(Customization c)
    {
        bll.addCustomizationToDB(c);
    }

    public void removeCustomizationFromDb(Customization selectedCustomization)
    {
        bll.removeCustomizationFromDb(selectedCustomization);
    }

    public boolean UserLogin(String userName, String password)
    {
        return bll.UserLogin(userName, password);
    }

    public void addUserToDB(LogIn l)
    {
        bll.addUserToDB(l);
    }

    public ObservableList<TraceLog> getTraceLogList()
    {
        return logList;
    }

    public void loadTraceLog()
    {
        logList.setAll(bll.getAllTraceLogs());
    }

    public void addTraceLogToDB(TraceLog t)
    {
        bll.addTraceLogToDB(t);
    }
}
