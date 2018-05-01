/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shorelineexamproject.gui.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import shorelineexamproject.be.ListViewObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

//import org.apache.sling.commons.json.JSONException;
//import org.apache.sling.commons.json.JSONcellValuesect;
//import com.PojoClassName;
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
    private ListView<ListViewObject> lstHeaders;
    @FXML
    private JFXTextField txtPlanning;
    @FXML
    private JFXTextField txtTest;
    @FXML
    private JFXButton btnTest;

    //variable that we will need to get the input stuff be become output
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
    private String absolutePath = null;
    @FXML
    private JFXButton btnTask;
    @FXML
    private JFXButton btnCancelTask;



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

    }
    //Experimentation
    TaskRunner taskRunner;
    
    private Runnable task = new Runnable()
    {
        @Override
        public void run()
        {
            for (int i = 0; i < 5000; i++)
            {
                try
                {
                    Thread.sleep(1000);
                } catch (InterruptedException ex)
                {
                    Logger.getLogger(ConversionViewController.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println("Hello");
            }
        }

    };

    @FXML
    private void clickTask(ActionEvent event) throws InterruptedException
    {
        if (btnTask.getText().equals("Task"))
        {   
            this.taskRunner = new TaskRunner(task);
            taskRunner.start();
            btnTask.setText("Pause");
        } else if (btnTask.getText().equals("Pause"))
        {
            taskRunner.pause();
            btnTask.setText("Task");
        }
    }

    @FXML
    private void clickCancelTask(ActionEvent event) throws InterruptedException
    {
        taskRunner.interrupt();
    }
    //End of experimentation

    /**
     * Allows dragging from the ListView
     *
     * @param event
     * @throws FileNotFoundException
     */
    @FXML
    private void dragLstHeaders(javafx.scene.input.MouseEvent event) throws FileNotFoundException
    {
        //Allow copy transfer mode
        Dragboard dragBoard = lstHeaders.startDragAndDrop(TransferMode.COPY);

        //Put a string on dragboard
        ClipboardContent content = new ClipboardContent();
        content.putString(getListViewObject());
        dragBoard.setContent(content);

        event.consume();
    }

    /**
     * Activates upon hovering the dragged object over a legitimate input field
     *
     * @param event
     */
    @FXML
    private void overTxtTest(DragEvent event)
    {
        //Accept it only if it is  not dragged from the same node and if it has a string data
        if (event.getGestureSource() != txtTest && event.getDragboard().hasString())
        {
            //Allow for both copying and moving, whatever user chooses
            event.acceptTransferModes(TransferMode.COPY);
        }

        event.consume();
    }

    /**
     * Put the dragged String into the field that it is dropped over
     *
     * @param event
     */
    @FXML
    private void dropTxtTest(DragEvent event)
    {
        //If there is a string on the dragboard, read it and use it
        Dragboard dragBoard = event.getDragboard();
        boolean success = false;
        if (dragBoard.hasString())
        {
            txtTest.setText(dragBoard.getString());
            success = true;
        }

        //let the source know whether the string was successfully transferred and used
        event.setDropCompleted(success);

        event.consume();
    }

    //Placeholder method
    @FXML
    private void clickTest(ActionEvent event)
    {
        String header = txtTest.getText();
        getXLSXHeaderValues(absolutePath, header);
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
     * @param event
     * @throws IOException Takes the String filepath of a xlsx file and finds
     * the headers before putting them into a ListView Jesper
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
                //Creates a ListViewObject Where the Headers of the XLSX file can be put as objects for the listView
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
     * Takes a filepath as a String, a String representation of a header in the
     * currently selected xlsx file and gets values from the rows below the
     * selected header in the currently selected xlsx file. Jesper
     *
     * @param filepath
     * @param header
     */
    private void getXLSXHeaderValues(String filepath, String header)
    {
        try
        {
            FileInputStream file = new FileInputStream(new File(filepath));
            String cellData = null;
            int colIndex = 0;
            int rowIndex = 0;

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
                Cell cell = cellIterator.next();

                switch (cell.getCellType())
                {
                    //Case the cells value is of type double it will be parsed as String and used to compare to the header
                    case Cell.CELL_TYPE_NUMERIC:

                        cellData = String.valueOf(cell.getNumericCellValue());

                        if (cellData.equals(header))
                        {
                            colIndex = cell.getColumnIndex();

                            //runs down the rows and prints out the values
                            while (rowIterator.hasNext())
                            {
                                rowIndex = rowIndex + 1;

                                System.out.println(sheet.getRow(rowIndex).getCell(colIndex));
                            }
                        }

                        break;
                    //Case the cells value is of type String it will be compared to the header
                    case Cell.CELL_TYPE_STRING:

                        cellData = cell.getStringCellValue();

                        if (cellData.equals(header))
                        {
                            colIndex = cell.getColumnIndex();

                            //runs down the rows and prints out the values
                            while (rowIterator.hasNext())
                            {
                                rowIndex = rowIndex + 1;

                                System.out.println(sheet.getRow(rowIndex).getCell(colIndex));
                            }
                        }

                        break;
                }
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
     *
     * Anni This method gets the name of the textfield and creates a new file
     * with this name, and then it adds the obj to the file This method gets the
     * name of the textfield and creates a new file with this name, and then it
     * adds the obj to the file
     *
     * @param event
     * @throws IOException
     */
    @FXML
    private void clickCreateJSONFile(ActionEvent event) throws IOException
    {
        String FileName = txtJSONName.getText() + ".json";
        File file = new File(FileName);

        JSONArray jarray = getJsonObjects(objectilist); 

//        JSONObject obj = new JSONObject();
//        obj.put(txtSiteName.getText(), varSiteName);
//        obj.put(txtAssetSerialNumber.getText(), varAssetSerialNumber);
//        obj.put(txtType.getText(), varType);
//        obj.put(txtExternalWorkOrderid.getText(), varExternalWorkOrderId);
//        obj.put(txtSystemStatus.getText(), varSystemStatus);
//        obj.put(txtUserStatus.getText(), varUserStatus);
//        obj.put(txtCreatedOn.getText(), varCreatedOn);
//        obj.put(txtCreatedBy.getText(), varCreatedBy);
//        obj.put(txtName.getText(), varName);
//        obj.put(txtPriority.getText(), varPriority);
//        obj.put(txtStatus.getText(), varStatus);
////        obj.put("getkey", obj.get(i).getvalue);
//
//        JSONArray list = new JSONArray();
//        list.add(txtLatestFinishDate.getText() + "," + varLatestFinishDate);
//        list.add(txtEarliestStartDate.getText() + "," + varEarliestStartDate);
//        list.add(txtLatestStartDate.getText() + "," + varLatestStartDate);
//        list.add(txtEstimatedTime.getText() + "," + varEstimatedTime);
//
//        obj.put("planning", list);
//
//    
        //the above will be moved to another method
        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        fw.write(jarray.toString(4));
        fw.flush();
        System.out.println("JSONfile called: " + FileName + " created in" + file.getAbsolutePath());

        System.out.println(jarray);

    }

    private List<Object> objectilist = new ArrayList();


    /** Anni
     *     //method below converts to json (for now just loops through a list
    //throws JSONException?, public static string
     * @param objectilist
     * @return 
     */
    public JSONArray getJsonObjects(List<Object> objectilist)
    {
        
        JSONArray mainjsonArray = new JSONArray();
        //This will be used to loop through the excel

        //for (Object objectilist1 : objectilist) //need to enter code here??
        for (int i = 0; i < 10; i++)

        //  int i = 0; i < objectilist.size(); i++
        {
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

            JSONObject obj2 = new JSONObject();
            obj2.put(txtLatestFinishDate.getText(), varLatestFinishDate);
            obj2.put(txtEarliestStartDate.getText(),varEarliestStartDate);
            obj2.put(txtLatestStartDate.getText(), varLatestStartDate);
            obj2.put(txtEstimatedTime.getText(), varEstimatedTime);

           obj.put("planning", obj2);

            
            System.out.println(mainjsonArray);
           mainjsonArray.put(obj);
        }
        return mainjsonArray;
    }
}
