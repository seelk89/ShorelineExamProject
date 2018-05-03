/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shorelineexamproject.gui.model;

import java.util.ArrayList;
import java.util.List;
import shorelineexamproject.be.ListViewObject;
import shorelineexamproject.bll.BLL;

/**
 *
 * @author Jesper
 */
public class Model
{
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
}
