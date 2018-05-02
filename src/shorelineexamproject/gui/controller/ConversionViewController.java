/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shorelineexamproject.gui.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXTextField;
import java.io.BufferedWriter;
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

import shorelineexamproject.be.Values;


import shorelineexamproject.be.ListViewObject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

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

    @FXML
    private JFXButton btnTask;
    @FXML
    private JFXProgressBar prgBar;
    @FXML
    private JFXButton btnPauseTask;

   private ArrayList<String> lstVarSiteName = new ArrayList<String>();
    private ArrayList<String> lstVarAssetSerialNumber = new ArrayList<String>();
    private ArrayList<String> lstVarType = new ArrayList<String>();
    private ArrayList<String> lstVarExternalWorkOrderId = new ArrayList<String>();
    private ArrayList<String> lstVarSystemStatus = new ArrayList<String>();
    private ArrayList<String> lstVarUserStatus = new ArrayList<String>();
    private ArrayList<String> lstVarCreatedOn = new ArrayList<String>();
    private ArrayList<String> lstVarCreatedBy = new ArrayList<String>();
    private ArrayList<String> lstVarName = new ArrayList<String>();
    private ArrayList<String> lstVarPriority = new ArrayList<String>();
    private ArrayList<String> lstVarStatus = new ArrayList<String>();
    private ArrayList<String> lstVarLatestFinishDate = new ArrayList<String>();
    private ArrayList<String> lstVarEarliestStartDate = new ArrayList<String>();
    private ArrayList<String> lstVarLatestStartDate = new ArrayList<String>();
    private ArrayList<String> lstVarEstimatedTime = new ArrayList<String>();



    private ArrayList<String> lstHeader1 = new ArrayList<String>();

    private Window stage;

    //AbsolutePath for the file being read
    private String absolutePath = null;
    //Variables for use with threads
    private Thread thread;
    private final AtomicBoolean running = new AtomicBoolean(false);
    private final AtomicBoolean suspend = new AtomicBoolean(false);

    public ConversionViewController()
    {
        this.thread = new Thread(task);
    }


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
     * Starts a task
     */
    public void start()
    {
        thread.start();
    }

    /**
     * Stops a task
     */
    public void stop()
    {
        task.cancel();
    }

    /**
     * Pauses the task
     */
    public void pause()
    {
        try
        {
            task.wait();
        } catch (InterruptedException ex)
        {
            Logger.getLogger(ConversionViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Resumes a paused task
     */
    public void resume()
    {
        task.notify();
    }

    /**
     * Checks if the task is running
     *
     * @return
     */
    boolean isRunning()
    {
        return running.get();
    }

    /**
     * Checks if the task has been suspended
     *
     * @return
     */
    boolean isSuspended()
    {
        return suspend.get();
    }

    //Placeholder task
    private Task task = new Task()
    {
        @Override
        protected Object call() throws Exception
        {
                for (int i = 0; i < 30; i++)
                {
                    if (isCancelled())
                    {
                        break;
                    }
                    
                    //Task goes here
                    try
                    {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex)
                    {
                        Logger.getLogger(ConversionViewController.class.getName()).log(Level.INFO, "Thread stopped");
                    }
                    System.out.println("Hello");
                    //Task ends here
                }            
            return null;
        }
    };

    //Placeholde start/stop button
    @FXML
    private void clickTask(ActionEvent event) throws InterruptedException
    {
        if (btnTask.getText().equals("Start"))
        {
            start();

            btnTask.setText("Stop");
        } else if (btnTask.getText().equals("Stop"))
        {
            stop();

            btnTask.setText("Start");
        }
    }

    //Placeholder pause/resume button
    @FXML
    private void clickPauseTask(ActionEvent event) throws InterruptedException
    {
        if (btnPauseTask.getText().equals("Pause"))
        {
            pause();

            btnPauseTask.setText("Resume");
        } else if (btnPauseTask.getText().equals("Resume"))
        {
            resume();

            btnPauseTask.setText("Pause");
        }
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
        String header2 = txtTest.getText();
        String header3 = txtTest.getText();
        String header4 = txtTest.getText();
        String header5 = txtTest.getText();
        String header6 = txtTest.getText();
        String header7 = txtTest.getText();
        String header8 = txtTest.getText();
        String header9 = txtTest.getText();
        String header10 = txtTest.getText();
        String header11 = txtTest.getText();
        String header12 = txtTest.getText();
        String header13 = txtTest.getText();
        String header14 = txtTest.getText();
        String header15 = txtTest.getText();

        getXLSXHeaderValues(absolutePath, header1, header2,
                header3, header4, header5, header6, header7,
                header8, header9, header10, header11, header12,
                header13, header14, header15); //??
        //   System.out.println(lstVarSiteName);
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
            String header1, String header2, String header3, String header4, String header5,
            String header6, String header7, String header8, String header9, String header10,
            String header11, String header12, String header13, String header14, String header15)

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
                                    lstVarSiteName.add(r.getCell(colIndex).toString());

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
                                    lstVarSiteName.add(r.getCell(colIndex).toString());
                                    lstVarAssetSerialNumber.add(r.getCell(colIndex).toString());
                                    lstVarType.add(r.getCell(colIndex).toString());
                                    lstVarExternalWorkOrderId.add(r.getCell(colIndex).toString());
                                    lstVarSystemStatus.add(r.getCell(colIndex).toString());
                                    lstVarUserStatus.add(r.getCell(colIndex).toString());
                                    lstVarCreatedOn.add(r.getCell(colIndex).toString());
                                    lstVarCreatedBy.add(r.getCell(colIndex).toString());
                                    lstVarName.add(r.getCell(colIndex).toString());
                                    lstVarPriority.add(r.getCell(colIndex).toString());
                                    lstVarStatus.add(r.getCell(colIndex).toString());
                                    lstVarLatestFinishDate.add(r.getCell(colIndex).toString());
                                    lstVarEarliestStartDate.add(r.getCell(colIndex).toString());
                                    lstVarLatestStartDate.add(r.getCell(colIndex).toString());
                                    lstVarEstimatedTime.add(r.getCell(colIndex).toString());

                                }
//                               
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
        for (int i = 0; i < lstVarSiteName.size(); i++)
        {
            JSONObject obj = new JSONObject();

            obj.put(txtSiteName.getText(), lstVarSiteName.get(i));
            obj.put(txtAssetSerialNumber.getText(), lstVarAssetSerialNumber.get(i));
            obj.put(txtType.getText(), lstVarType.get(i));
            obj.put(txtExternalWorkOrderid.getText(), lstVarExternalWorkOrderId.get(i));
            obj.put(txtSystemStatus.getText(), lstVarSystemStatus.get(i));
            obj.put(txtUserStatus.getText(), lstVarUserStatus.get(i));
            obj.put(txtCreatedOn.getText(), lstVarCreatedOn.get(i));
            obj.put(txtCreatedBy.getText(), lstVarCreatedBy.get(i));
            obj.put(txtName.getText(), lstVarName.get(i));
            obj.put(txtPriority.getText(), lstVarPriority.get(i));
            obj.put(txtStatus.getText(), lstVarStatus.get(i));

            JSONObject obj2 = new JSONObject();
            obj2.put(txtLatestFinishDate.getText(), lstVarLatestFinishDate.get(i));
            obj2.put(txtEarliestStartDate.getText(), lstVarEarliestStartDate.get(i));
            obj2.put(txtLatestStartDate.getText(), lstVarLatestStartDate.get(i));
            obj2.put(txtEstimatedTime.getText(), lstVarEstimatedTime.get(i));

            obj.put("planning", obj2);

            mainjsonArray.put(obj);
        }
        System.out.println(mainjsonArray);
        return mainjsonArray;
    }

}
