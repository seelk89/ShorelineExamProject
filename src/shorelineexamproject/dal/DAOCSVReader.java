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
     * Converts a CSV file into a String[][] and gets the String objects on
     * [0][i]
     *
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

            //Reads headers for the header list
            for (int i = 0; i < colCount(filepath); i++)
            {
                ListViewObject listViewObject = new ListViewObject();

                Integer count = headerDuplicates.get(csvArray[0][i]);
                if (count == null)
                {
                    headerDuplicates.put(csvArray[0][i], 1);
                    listViewObject.setStringObject(csvArray[0][i]);
                    lstHeaders.add(listViewObject);
                } else
                {
                    headerDuplicates.put(csvArray[0][i], ++count);
                    listViewObject.setStringObject(String.valueOf(csvArray[0][i]) + " " + count);
                    lstHeaders.add(listViewObject);
                }
            }

        } catch (FileNotFoundException ex)
        {
            Logger.getLogger(DAOCSVReader.class.getName()).log(Level.SEVERE, null, ex);
        }

        return lstHeaders;
    }

    public void getCSVHeaderValues(String filepath,
            String header, ArrayList<String> headerList)
    {
        List<ListViewObject> lstHeaders = new ArrayList();
        String headerWithoutNumeration = header;
        int number = 1;
        int numberCount = 0;

        //Divides the header by whitespace
        String[] stringPart;
        stringPart = header.trim().split("\\s+");

        //Sets number = to the number behind a header, for example, if the header is description 3, it will be set to 3
        if (isInteger(stringPart[stringPart.length - 1]) == true)
        {
            number = Integer.parseInt(stringPart[stringPart.length - 1]);
            headerWithoutNumeration = header.substring(0, header.lastIndexOf(" "));
        }

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

            //Reads headers for the header list
            for (int i = 0; i < colCount(filepath); i++)
            {
                if (csvArray[0][i].equals(headerWithoutNumeration))
                {
                    numberCount = numberCount + 1;
                    if (number == numberCount)
                    {
                        for (int j = 1; j < csvArray.length; j++)
                        {
                            headerList.add(csvArray[j][i]);
                        }
                    }
                }
            }

        } catch (FileNotFoundException ex)
        {
            Logger.getLogger(DAOCSVReader.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Counts the number of columns in a CSV file by counting the , and adding 1
     *
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

    /**
     * Returns true if a string is an Integer
     *
     * @param input
     * @return
     */
    private boolean isInteger(String input)
    {
        try
        {
            Integer.parseInt(input);
            return true;
        } catch (Exception exception)
        {
            return false;
        }
    }
}
