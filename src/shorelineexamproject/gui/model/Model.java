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
import shorelineexamproject.bll.BLL;

/**
 *
 * @author Jesper
 */
public class Model
{

    //start of singleton setup
    private static Model firstInstance = null;
    private BLL bll;

    public Model() throws IOException
    {
        this.bll = new BLL();
    }

//    private Model()
//    {
//    }
    
    //not sure if method below is needed
    public ObservableList<Customization> cList = FXCollections.observableArrayList();

    public synchronized static Model getInstance() throws IOException
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

    public List<ListViewObject> readCSVHeaders(String filepath)
    {
        return bll.readCSVHeaders(filepath);
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
}
