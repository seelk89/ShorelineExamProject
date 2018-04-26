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
    @FXML
    private JFXTextField txtAssetSerialNumber;
    @FXML
    private JFXTextField txtSiteName;
    //variable that we will need to get the input stuff be become output

//        String varSiteName;
//        String varAssetSerialNumber;
//        String varType;
//        String varExternalWorkOrderId;
//        String varSystemStatus;
//        String varUserStatus;
//        String varCreatedOn;
//        String varCreatedBy;
//        String varName;
//        String varPriority;
//        String varStatus; //always new
//       
//        String varLatestFinishDate ;
//        String varEarliestStartDate;
//        String varLatestStartDate;
//        String varEstimatedTime;
    private String varSiteName = "";
    private String varAssetSerialNumber = "asset.id";
    private String varType = "Order Type";
    private String varExternalWorkOrderId = "Order";
    private String varSystemStatus = "System Status";
    private String varUserStatus = "User Status";
    private String varCreatedOn = "Datetime Object(Date now)";
    private String varCreatedBy = "SAP";
    private String varName = "Opr.short text, if empty then Description 2";
    private String varPriority = "priority, if not set, Low";
    private String varStatus = "New"; //always new

    private String varLatestFinishDate = "find Datetime Object";
    private String varEarliestStartDate = "find Datetime Object";
    private String varLatestStartDate = "find Datetime Object";
    private String varEstimatedTime = "Hours if exist in the input, else null(?)";

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // TODO
    }

    /**
     *
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
     * Anni This method gets the name of the textfield and creates a new file
     * with this name, and then it adds the obj to the file
     *
     * @param event
     * @throws IOException
     */
    @FXML
    private void clickCreateJSONFile(ActionEvent event) throws IOException
    {
        String FileName = txtJSONName.getText() + ".json";
        File file = new File(FileName);

        JSONObject obj = new JSONObject();
        obj.put(txtSiteName.getText(), varSiteName);
        obj.put(txtAssetSerialNumber.getText(), varAssetSerialNumber);
        obj.put(txtType.getText(), varType);
        obj.put(txtExternalWorkOrderid.getText(), varExternalWorkOrderId);
        obj.put(txtSystemStatus.getText(), varSystemStatus);
        obj.put(txtUserStatus.getText(), varUserStatus);
        obj.put(txtCreatedOn.getText(), varCreatedOn);
        obj.put(txtCreatedBy.getText(), varCreatedBy);
        obj.put(txtName.getText(), varName);
        obj.put(txtPriority.getText(), varPriority);
        obj.put(txtStatus.getText(), varStatus);

        JSONArray list = new JSONArray();
        list.add(txtLatestFinishDate.getText() + "," + varLatestFinishDate);
        list.add(txtEarliestStartDate.getText() + "," + varEarliestStartDate);
        list.add(txtLatestStartDate.getText() + "," + varLatestStartDate);
        list.add(txtEstimatedTime.getText() + "," + varEstimatedTime);

        obj.put("planning", list);

        JSONObject obj1 = new JSONObject();
        obj1.put(txtSiteName.getText(), varSiteName);
        obj1.put(txtAssetSerialNumber.getText(), varAssetSerialNumber);
        obj1.put(txtType.getText(), varType);
        obj1.put(txtExternalWorkOrderid.getText(), varExternalWorkOrderId);
        obj1.put(txtSystemStatus.getText(), varSystemStatus);
        obj1.put(txtUserStatus.getText(), varUserStatus);
        obj1.put(txtCreatedOn.getText(), varCreatedOn);
        obj1.put(txtCreatedBy.getText(), varCreatedBy);
        obj1.put(txtName.getText(), varName);
        obj1.put(txtPriority.getText(), varPriority);
        obj1.put(txtStatus.getText(), varStatus);

        JSONArray list1 = new JSONArray();
        list1.add(txtLatestFinishDate.getText() + "," + varLatestFinishDate);
        list1.add(txtEarliestStartDate.getText() + "," + varEarliestStartDate);
        list1.add(txtLatestStartDate.getText() + "," + varLatestStartDate);
        list1.add(txtEstimatedTime.getText() + "," + varEstimatedTime);

        obj1.put("planning1", list1);

        //the above will be moved to another method
        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        fw.write(obj.toJSONString()); //to be deleted
        fw.write(obj.toJSONString());
        fw.flush();

        System.out.println("JSONfile called: " + FileName + " created in" + file.getAbsolutePath());

        System.out.println(obj1 + "/n" + obj);
    }

}
