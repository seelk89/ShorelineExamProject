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
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
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
import shorelineexamproject.be.ListViewObject;

/**
 * FXML Controller class
 *
 * @author Jesper
 */
public class ConversionViewController implements Initializable
{

    @FXML
    private JFXButton btnGet;
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
    @FXML
    private JFXTextField txtassetSerialNumber;
    @FXML
    private JFXTextField txtsiteName;
    @FXML
    private ListView<ListViewObject> lstHeaders;
       
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

    private Window stage;
    private String listViewObjectString;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        lstHeaders.setCellFactory((ListView<ListViewObject> param) -> new ListCell<ListViewObject>()
        {
            @Override
            protected void updateItem(ListViewObject item, boolean empty)
            {
                super.updateItem(item, empty);
                if (item != null)
                {
                    Text t = new Text(item.getStringObject());

                    setGraphic(t);
                }
            }
        });
        
        lstHeaders.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    /**
     * Gets the selected ListViewObject Jesper
     *
     * @return
     */
    private String getListViewObject()
    {
        return lstHeaders.getSelectionModel().getSelectedItem().getStringObject();
    }

    /**
     * Takes the String filepath of a xlsx file and finds the headers before
     * putting them into a ListView Jesper
     *
     * @param filepath
     */
    private void readXLSXHeaders(String filepath)
    {
        try
        {
            FileInputStream file = new FileInputStream(new File(filepath));

            //Get the workbook instance for xlsx file 
            XSSFWorkbook workbook = new XSSFWorkbook(file);

            //Get first sheet from the workbook
            XSSFSheet sheet = workbook.getSheetAt(0);

            //Gets the first row from the first sheet
            Iterator<Row> rowIterator = sheet.iterator();
            Row row = rowIterator.next();

            //Iterate through the cells of the first row
            Iterator<Cell> cellIterator = row.cellIterator();
            while (cellIterator.hasNext())
            {
                //Creates a ListViewObject Where the Headers of the xlsx file can be put as objects for the listView
                ListViewObject listViewObject = new ListViewObject();

                Cell cell = cellIterator.next();

                switch (cell.getCellType())
                {
                    //Case the cells value is of type double it will be parsed as String before the value is stored in a ListViewObject and then added to the ListView
                    case Cell.CELL_TYPE_NUMERIC:
                        listViewObject.setStringObject(String.valueOf(cell.getNumericCellValue()));
                        lstHeaders.getItems().add(listViewObject);

                        break;
                    //Case the cells value is of type String it will be put into a ListViewObject and then added to the ListView
                    case Cell.CELL_TYPE_STRING:
                        listViewObject.setStringObject(cell.getStringCellValue());
                        lstHeaders.getItems().add(listViewObject);

                        break;
                }
            }

            file.close();
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
        
        //Adds a file filter that will only allow xlsx files (excel output files)
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("(*.xlsx)", "*.xlsx");
        fileChooser.getExtensionFilters().add(extFilter);

        //Opens a window based on settings set above
        File xlsxFile = fileChooser.showOpenDialog(stage);

        if (xlsxFile != null)
        {
            absolutePath = xlsxFile.getAbsolutePath();
        }

        //Clears the ListView and adds headers from xlsx file
        lstHeaders.getItems().clear();
        readXLSXHeaders(absolutePath);
    }

    /**
     * This method gets the name of the textfield and creates a new file
     * with this name, and then it adds the obj to the file
     * This method gets the name of the textfield and creates a new file with
     * this name, and then it adds the obj to the file Anni
     *
     * @param event
     * @throws IOException
     */
    @FXML
    private void clickCreateJSONFile(ActionEvent event) throws IOException
    {
        String FileName = txtJSONName.getText() + ".json";
        File file = new File(FileName);


        //String content = "This is the content to write into a file, can be an object";

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

    @FXML
    private void dragHeaders(MouseEvent event)
    {
        String s = getListViewObject();
        listViewObjectString = s;
        System.out.println(s);  
    }

}
