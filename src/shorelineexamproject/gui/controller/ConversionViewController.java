/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shorelineexamproject.gui.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.net.URL;
import java.util.ResourceBundle;
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
import shorelineexamproject.be.Values;

import shorelineexamproject.be.ListViewObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    
    private Window stage;
    private String absolutePath = null;
    @FXML
    private JFXTextField txtVarSiteName;
    @FXML
    private JFXTextField txtVarAssetSerialNumber;
    @FXML
    private JFXTextField txtVarType;
    @FXML
    private JFXTextField txtVarExternalWorkOrderid;
    @FXML
    private JFXTextField txtVarCreatedOn;
    @FXML
    private JFXTextField txtVarUserStatus;
    @FXML
    private JFXTextField txtVarSystemStatus;
    @FXML
    private JFXTextField txtVarCreatedBy;
    @FXML
    private JFXTextField txtVarName;
    @FXML
    private JFXTextField txtVarPriority;
    @FXML
    private JFXTextField txtVarStatus;
    @FXML
    private JFXTextField txtVarPlanning;
    @FXML
    private JFXTextField txtVarLatestFinishDate;
    @FXML
    private JFXTextField txtVarEarliestStartDate;
    @FXML
    private JFXTextField txtVarLatestStartDate;
    @FXML
    private JFXTextField txtVarEstimatedTime;

    //variables that we will need to get the input stuff to become the output
    private String varSiteName = ""; //is okay if it's always empty
    private String varAssetSerialNumber = "asset.id";
    private String varType = "Order Type";
    private String varExternalWorkOrderId = "Order";
    private String varSystemStatus = "System Status";
    private String varUserStatus = "User Status";
    private String varCreatedOn = "Datetime Object(Date now)";
    private String varCreatedBy = "SAP";
    private String varName = "Opr.short text, if empty then Description 2";
    private String varPriority = "Priority, if not set, Low";
    private String varStatus = "New"; //always new
    private String varLatestFinishDate = "Datetime Object";
    private String varEarliestStartDate = "Datetime Object";
    private String varLatestStartDate = "Datetime Object";
    private String varEstimatedTime = ""; //Hours if exist in the input, else null(?)

    private ArrayList<String> lstHeader1 = new ArrayList<String>();

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

    /**
     * Allows dragging from the ListView
     *
     * @param event
     * @throws FileNotFoundException
     */
    @FXML
    private void dragLstHeaders(javafx.scene.input.MouseEvent event) throws FileNotFoundException
    {
        //Allow any transfer mode
        Dragboard dragBoard = lstHeaders.startDragAndDrop(TransferMode.COPY);

        //For fun code (might be of some use in regards to useability)
//        FileInputStream inputstream = new FileInputStream("ShorelineExamProject/images/SLLogo.png");
//        Image image = new Image(inputstream);
//        dragBoard.setDragView(image);
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
    
    @FXML
    private void clickTest(ActionEvent event)
    {
        String header1 = txtTest.getText();
        getXLSXHeaderValues(absolutePath, header1); //??
        System.out.println(lstHeader1);
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
    private void getXLSXHeaderValues(String filepath,
            String header1)
    /**
     * , String header2, String header3, String header4, String header5, String
     * header6, String header7, String header8, String header9, String header10,
     * String header11, String header12,String header13, String header14, String
     * header15)
     */
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
                        
                        if (cellData.equals(header1))
                        {
                            colIndex = cell.getColumnIndex();

                            //runs down the rows and prints out the values
                            while (rowIterator.hasNext())
                            {
                                Row r = rowIterator.next();
                                if (r != null)
                                {
                                     lstHeader1.add(r.getCell(colIndex).toString());
                                // System.out.println(sheet.getRow(rowIndex).getCell(colIndex));
                               // System.out.println(lstHeader1);
                                }
                               
                                
                            }
                        }
                        
                        break;
//                    //Case the cells value is of type String it will be compared to the header
                    case Cell.CELL_TYPE_STRING:
                        
                        cellData = cell.getStringCellValue();
                        
                        if (cellData.equals(header1))
                        {
                            colIndex = cell.getColumnIndex();

                            //runs down the rows and prints out the values
                            while (rowIterator.hasNext())
                            {
                                rowIndex = rowIndex + 1;
                                 Row r = rowIterator.next();
                                 if (r != null)
                                {
                                     lstHeader1.add(r.getCell(colIndex).toString());
                                // System.out.println(sheet.getRow(rowIndex).getCell(colIndex));
                               // System.out.println(lstHeader1);
                                }
//                                 lstHeader1.add(String.valueOf(sheet.getRow(rowIndex).getCell(colIndex)));
//                                 
//                              //  System.out.println(sheet.getRow(rowIndex).getCell(colIndex)); //??
//                                System.out.println(lstHeader1);
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

        //metodekald/metode
        JSONArray jarray = CreateJsonObjects(objectilist);
        
        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        fw.write(jarray.toString(4));
        fw.flush();
        System.out.println("JSONfile called: " + FileName + " created in" + file.getAbsolutePath());
        
        System.out.println(jarray);
        
    }
    
    private List<Object> objectilist = new ArrayList();

    /**
     * Anni //method below converts to json (for now just loops through a list
     * //throws JSONException?, public static string
     *
     * @param objectilist
     * @return
     */
    public JSONArray CreateJsonObjects(List<Object> objectilist)
    {
        JSONArray mainjsonArray = new JSONArray();
        //This will be used to loop through the excel

        //for (Object objectilist1 : objectilist) //need to enter code here??
        for (int i = 0; i < lstHeader1.size(); i++)
        {
            JSONObject obj = new JSONObject();
            
            obj.put(txtSiteName.getText(), varSiteName);

            String varAssetSerialNumber = lstHeader1.get(i);
           // System.out.println(lstHeader1.get(1));
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
            obj2.put(txtEarliestStartDate.getText(), varEarliestStartDate);
            obj2.put(txtLatestStartDate.getText(), varLatestStartDate);
            obj2.put(txtEstimatedTime.getText(), varEstimatedTime);
            
            obj.put("planning", obj2);
            
            System.out.println(mainjsonArray);
            mainjsonArray.put(obj);
        }
        return mainjsonArray;
    }
    
}
