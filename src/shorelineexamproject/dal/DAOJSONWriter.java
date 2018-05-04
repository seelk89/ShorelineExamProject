/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shorelineexamproject.dal;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.json.JSONArray;
import org.json.JSONObject;
import shorelineexamproject.dal.DAOXLSXReader;
import shorelineexamproject.gui.model.Model;

/**
 *
 * @author Elisabeth
 */
public class DAOJSONWriter
{

    /**
     * 
     * @param FileName
     * @param objectilist
     * @throws IOException 
     */
    public void CreateJSONFile(String FileName, JSONArray jarray) throws IOException
    {    
        File file = new File(FileName);
        //JSONArray jarray = CreateJsonObjects();

        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        fw.write(jarray.toString(4));
        fw.flush();
        System.out.println("JSONfile called: " + FileName + " created in" + file.getAbsolutePath());

//        System.out.println(jarray);
    }

    /**
     * 
     * @param objectilist
     * @return 
     */
    public JSONArray CreateJsonObjects()
    {
        JSONArray mainjsonArray = new JSONArray();
        //This will be used to loop through the excel

//        for (int i = 0; i < 4; i++)
//        {
//            JSONObject obj = new JSONObject();
//
////            obj.put(txtSiteName.getText(), ""); //get from middle of description if possible
////            obj.put(txtAssetSerialNumber.getText(), lstVarAssetSerialNumber.get(i));
////            obj.put(txtType.getText(), lstVarType.get(i));
////            obj.put(txtExternalWorkOrderid.getText(), lstVarExternalWorkOrderid.get(i));
////            obj.put(txtSystemStatus.getText(), lstVarSystemStatus.get(i));
////            obj.put(txtUserStatus.getText(), lstVarUserStatus.get(i));
////            obj.put(txtCreatedOn.getText(), model.getDate());
////            obj.put(txtCreatedBy.getText(), "SAP"); //get sap (or login, ask po)
////            obj.put(txtName.getText(), lstVarName.get(i));  //opr short text or description 2
////            obj.put(txtPriority.getText(), lstVarPriority.get(i)); //priority, if empty set low
////            obj.put(txtStatus.getText(), "NEW"); //always new
////
////            JSONObject obj2 = new JSONObject();
////            obj2.put(txtLatestFinishDate.getText(), lstVarLatestFinishDate.get(i));
////            obj2.put(txtEarliestStartDate.getText(), lstVarEarliestStartDate.get(i));
////            obj2.put(txtLatestStartDate.getText(), lstVarLatestStartDate.get(i));
////            obj2.put(txtEstimatedTime.getText(), lstVarEstimatedTime.get(i));
////
////            obj.put("planning", obj2);
//            mainjsonArray.put(obj);
//        }
        return mainjsonArray;
    }

}
