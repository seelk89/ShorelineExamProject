/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shorelineexamproject.bll;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import shorelineexamproject.be.Customization;
import shorelineexamproject.be.ListViewObject;
import shorelineexamproject.dal.DAOCustomization;
import shorelineexamproject.dal.DAOJSONWriter;
import shorelineexamproject.dal.DAOXLSXReader;

/**
 *
 * @author Jesper
 */
public class BLL
{

    private DAOXLSXReader daoXLSXReader = new DAOXLSXReader();
    private DAOJSONWriter daoJSONWriter = new DAOJSONWriter();
    private DAOCustomization daoCustomization;

    public BLL() throws IOException
    {
        this.daoCustomization = new DAOCustomization();
    }

    public List<ListViewObject> readXLSXHeaders(String filepath)
    {
        return daoXLSXReader.readXLSXHeaders(filepath);
    }

    public void getXLSXHeaderValues(String filepath,
            String header, ArrayList<String> headerList)
    {
        daoXLSXReader.getXLSXHeaderValues(filepath, header, headerList);
    }

    public String getDate()
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDateTime dateTime = LocalDateTime.now();
        String formattedDate = dateTime.format(formatter);

        return formattedDate;
    }

    /**
     *
     * @param FileName
     * @param objectilist
     * @throws IOException
     */
    public void CreateJSONFile(String directory, String fileName, JSONArray jarray) throws IOException
    {
        daoJSONWriter.CreateJSONFile(directory, fileName, jarray);
    }

    /**
     *
     * @param objectilist
     * @return
     */
    public JSONArray CreateJsonObjects()
    {
        daoJSONWriter.CreateJsonObjects();
        return null;
    }

    public List<Customization> getAllCustomizations()
    {
        return daoCustomization.getAllCustomizations();
    }

    public void addCustomizationToDB(Customization c)
    {
        daoCustomization.addCustomizationToDB(c);
    }

    public void removeCustomizationFromDb(Customization selectedCustomization)
    {
        daoCustomization.removeCustomizationFromDb(selectedCustomization);
    }
}
