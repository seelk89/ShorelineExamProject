/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shorelineexamproject.bll;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import shorelineexamproject.be.ListViewObject;
import shorelineexamproject.dal.DAOXLSXReader;

/**
 *
 * @author Jesper
 */
public class BLL
{

    private DAOXLSXReader daoXLSXReader = new DAOXLSXReader();

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
      //  System.out.println(formattedDate);
        
        return formattedDate;
        
    }
}
