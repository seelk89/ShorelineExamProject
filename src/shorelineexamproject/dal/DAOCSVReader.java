/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shorelineexamproject.dal;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import shorelineexamproject.be.ListViewObject;

/**
 *
 * @author Jesper
 */
public class DAOCSVReader
{

    /**
     * Converts a CSV file into a String[][] and gets the String objects on [0][i]
     * @param filepath
     * @return 
     */
    public List<ListViewObject> readCSVHeaders(String filepath)
    {
        List<ListViewObject> lstHeaders = new ArrayList();
        Hashtable<String, Integer> headerDuplicates = new Hashtable<String, Integer>();

        BufferedReader reader;
        try
        {
            reader = new BufferedReader(new FileReader(filepath));

            String line = null;

            List<String[]> lines = new ArrayList<String[]>();
            try
            {
                while ((line = reader.readLine()) != null)
                {
                    lines.add(line.split(","));
                }
            } catch (IOException ex)
            {
                Logger.getLogger(DAOCSVReader.class.getName()).log(Level.SEVERE, null, ex);
            }

            //Convert our list to a String array.
            String[][] csvArray = new String[lines.size()][0];
            lines.toArray(csvArray);

            for (int i = 0; i < colCount(filepath); i++)
            {
                ListViewObject listViewObject = new ListViewObject();
                listViewObject.setStringObject(csvArray[0][i]);
                lstHeaders.add(listViewObject);
            }

        } catch (FileNotFoundException ex)
        {
            Logger.getLogger(DAOCSVReader.class.getName()).log(Level.SEVERE, null, ex);
        }

        return lstHeaders;
    }

    /**
     * Counts the number of columns in a CSV file by counting the , and adding 1
     * @param filepath
     * @return
     * @throws FileNotFoundException 
     */
    private int colCount(String filepath) throws FileNotFoundException
    {
        int colCount = 0;
        BufferedReader reader;

        reader = new BufferedReader(new FileReader(filepath));

        //Count ,
        String firstLine = null;
        List<String> linesBecause = new ArrayList<>();

        try
        {
            while ((firstLine = reader.readLine()) != null)
            {
                linesBecause.add(firstLine);
            }
        } catch (IOException ex)
        {
            Logger.getLogger(DAOCSVReader.class.getName()).log(Level.SEVERE, null, ex);
        }

        firstLine = linesBecause.get(0);
        colCount = firstLine.length() - firstLine.replace(",", "").length() + 1;

        return colCount;
    }
}
