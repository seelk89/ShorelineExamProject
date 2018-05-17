/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shorelineexamproject.dal;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.json.JSONArray;

/**
 *
 * @author Elisabeth
 */
public class DAOJSONWriter
{

    /**
     * Gets a JSONArray from the gui layer and creates a jsonfile by creating a new file, giving it a path and filename
     * after which it uses filewriter to write the jsonarray to the file.
     * @param directory
     * @param fileName
     * @param jarray
     * @throws IOException 
     */
    public void CreateJSONFile(String directory, String fileName, JSONArray jarray) throws IOException
    {    
        File file = new File(directory + "\\" + fileName);

        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        fw.write(jarray.toString(4));
        fw.flush();
    }



}
