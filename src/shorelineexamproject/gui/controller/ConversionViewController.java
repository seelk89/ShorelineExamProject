/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shorelineexamproject.gui.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import static java.util.Locale.filter;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author Jesper
 */
public class ConversionViewController implements Initializable
{

    @FXML
    private JFXButton btnTest;
    @FXML
    private JFXTextField txtTest;
    @FXML
    private JFXListView<?> lstTest;
    @FXML
    private JFXButton btnGet;

    private Window stage;
    @FXML
    private JFXTextField txtJSONName;
    @FXML
    private JFXTextField txtType;
    @FXML
    private JFXTextField txtUserStatus;
    @FXML
    private JFXTextField txtSystemStatus;
    @FXML
    private JFXTextField txtExternalWorkOrderid;
    @FXML
    private JFXTextField txtassetSerialNumber;
    @FXML
    private JFXTextField txtsiteName;
    @FXML
    private JFXTextField txtCreatedOn;
    @FXML
    private JFXTextField txtCreatedBy;
    @FXML
    private JFXTextField txtName;
    @FXML
    private JFXTextField txtPriority;
    @FXML
    private JFXTextField txtStatus;
    @FXML
    private JFXTextField txtPlanning;
    @FXML
    private JFXTextField txtEstimatedTime;
    @FXML
    private JFXTextField txtLatestFinishDate;
    @FXML
    private JFXTextField txtEarliestStartDate;
    @FXML
    private JFXTextField txtLatestStartDate;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // TODO
    }

    /**
     * Gets a XLSX filepath from the txtTest text field and prints out the contents of the file.
     * @param event
     * @throws IOException 
     */
    @FXML
    private void clickTest(ActionEvent event) throws IOException
    {
        String filepath = txtTest.getText();
        
        try
        {
            FileInputStream file = new FileInputStream(new File(filepath));

            //Get the workbook instance for XLS file 
            XSSFWorkbook workbook = new XSSFWorkbook(file);

            //Get first sheet from the workbook
            XSSFSheet sheet = workbook.getSheetAt(0);

            //Iterate through each rows from first sheet
            Iterator<Row> rowIterator = sheet.iterator();
            while (rowIterator.hasNext())
            {
                Row row = rowIterator.next();

                //For each row, iterate through each columns
                Iterator<Cell> cellIterator = row.cellIterator();
                while (cellIterator.hasNext())
                {

                    Cell cell = cellIterator.next();

                    switch (cell.getCellType())
                    {
                        case Cell.CELL_TYPE_BOOLEAN:
                            System.out.print(cell.getBooleanCellValue() + "\t\t");
                            break;
                        case Cell.CELL_TYPE_NUMERIC:
                            System.out.print(cell.getNumericCellValue() + "\t\t");
                            break;
                        case Cell.CELL_TYPE_STRING:
                            System.out.print(cell.getStringCellValue() + "\t\t");
                            break;
                    }
                }
                System.out.println("");
            }
            file.close();
            FileOutputStream out = new FileOutputStream(new File(filepath));
            workbook.write(out);
            out.close();

        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Opens a FileChooser that is only able to get xlsx files. Jesper
     *
     * @param event
     */
    @FXML
    private void clickGet(ActionEvent event)
    {
        String absolutePath = null;

        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose a file");
        //Adds a file filter that will only allow xlsx files (excel output files).
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("(*.xlsx)", "*.xlsx");
        fileChooser.getExtensionFilters().add(extFilter);

        //Opens a window based on setting set above.
        File xlxsFile = fileChooser.showOpenDialog(stage);
        if (xlxsFile != null)
        {
            absolutePath = xlxsFile.getAbsolutePath();
        }

        txtTest.setText(absolutePath);
    }

 /**
     * This method gets the name of the textfield and creates a new file with this name, 
     * and then it adds the obj to the file
     * @param event
     * @throws IOException 
     */
    @FXML
    private void clickCreateJSONFile(ActionEvent event) throws IOException
    {
        //doesn't add .json automatically yet
        String FileName = txtJSONName.getText() + ".json";
        File file = new File(FileName);

//        String content = "This is the content to write into a file, can be an object";
        JSONObject obj = new JSONObject();
        obj.put("String", "thingy");
        obj.put("Integer", 1);
        obj.put("Boolean", "True");

        JSONArray list = new JSONArray();
        list.add("This is the first object in my object of lists");
        list.add("This is the second object in my object of lists");
        list.add("This is the third object in my object of lists");
        obj.put("myJSONArray list", list);

        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        fw.write(obj.toJSONString());
        fw.flush();

        System.out.println("filewriter flushed, JSONfile called: " + FileName + " created");
   
        System.out.println(obj);
    }

}
