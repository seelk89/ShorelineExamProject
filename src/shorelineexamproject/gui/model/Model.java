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
import org.json.JSONArray;
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

    private Model()
    {
    }

    public synchronized static Model getInstance()
    {
        if (firstInstance == null)
        {
            firstInstance = new Model();
        }
        return firstInstance;
    }
    //end of singleton setup
    
    private BLL bll = new BLL();

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
    public void CreateJSONFile(String FileName, JSONArray jarray) throws IOException
    {
        bll.CreateJSONFile(FileName, jarray);
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
}
