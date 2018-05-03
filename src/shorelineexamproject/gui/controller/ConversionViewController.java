/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shorelineexamproject.gui.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXProgressBar;
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
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.concurrent.Task;
import javafx.scene.control.ProgressBar;
import org.json.JSONArray;
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
    private JFXButton btnTask;
    @FXML
    private JFXProgressBar prgBar;
    @FXML
    private ProgressBar prgBar1;
    @FXML
    private JFXButton btnPauseTask;
    @FXML
    private JFXTextField txtVarType;
    @FXML
    private JFXTextField txtVarUserStatus;
    @FXML
    private JFXTextField txtVarSystemStatus;
    @FXML
    private JFXTextField txtVarExternalWorkOrderid;
    @FXML
    private JFXTextField txtVarAssetSerialNumber;
    @FXML
    private JFXTextField txtVarSiteName;
    @FXML
    private JFXTextField txtVarCreatedOn;
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
    private JFXTextField txtVarEstimatedTime;
    @FXML
    private JFXTextField txtVarLatestFinishDate;
    @FXML
    private JFXTextField txtVarEarliestStartDate;
    @FXML
    private JFXTextField txtVarLatestStartDate;

    private ArrayList<String> lstVarSiteName = new ArrayList<String>();
    private ArrayList<String> lstVarAssetSerialNumber = new ArrayList<String>();
    private ArrayList<String> lstVarType = new ArrayList<String>();
    private ArrayList<String> lstVarExternalWorkOrderid = new ArrayList<String>();
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

    private Window stage;

    //AbsolutePath for the file being read
    private String absolutePath = null;

    //Variables for use with threads
    private Thread thread = null;
    private final AtomicBoolean suspend = new AtomicBoolean(true);
    private final AtomicBoolean done = new AtomicBoolean(true);

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
        thread = new Thread(task);
        thread.setDaemon(true);
        done.set(false);
        suspend.set(false);
        thread.start();
    }

    /**
     * Stops a task
     */
    public void stop()
    {
        done.set(true);
        task.cancel();
    }

    /**
     * Pauses the task
     */
    public void pause() throws InterruptedException
    {
        suspend.set(true);
        task.wait();
    }

    /**
     * Resumes a paused task
     */
    public void resume()
    {
        suspend.set(false);
        synchronized (thread)
        {
            task.notify();
        }
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

    /**
     * Checks if the task has been done
     *
     * @return
     */
    boolean isDone()
    {
        return done.get();
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
                System.out.println("Hello " + i);
                //Task ends here
                prgBar.setProgress(task.getProgress());
                prgBar1.setProgress(task.getProgress());
            }
            return null;
        }
    };

    //Placeholde start/stop button
    @FXML
    private void clickTask(ActionEvent event) throws InterruptedException
    {
        if (done.get() == true)
        {
            start();

            btnTask.setText("Stop");
        } else if (done.get() == false)
        {
            stop();

            btnTask.setText("Start");
        }
    }

    //Placeholder pause/resume button
    @FXML
    private void clickPauseTask(ActionEvent event) throws InterruptedException
    {
        if (suspend.get() == true)
        {
            pause();

            btnPauseTask.setText("Resume");
        } else if (suspend.get() == false)
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
        if (event.getDragboard().hasString())
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
        if (dragBoard.hasString())
        {
            txtTest.setText(dragBoard.getString());
        }

        event.consume();
    }

    @FXML
    private void dropTxtVarAssetSerialNumber(DragEvent event)
    {
        //If there is a string on the dragboard, read it and use it
        Dragboard dragBoard = event.getDragboard();
        if (dragBoard.hasString())
        {
            txtVarAssetSerialNumber.setText(dragBoard.getString());
        }

        event.consume();
    }

    @FXML
    private void dropTxtVarSiteName(DragEvent event)
    {
        //If there is a string on the dragboard, read it and use it
        Dragboard dragBoard = event.getDragboard();
        if (dragBoard.hasString())
        {
            txtVarSiteName.setText(dragBoard.getString());
        }

        event.consume();
    }

    @FXML
    private void dropTxtVarType(DragEvent event)
    {
        Dragboard dragBoard = event.getDragboard();
        if (dragBoard.hasString())
        {
            txtVarType.setText(dragBoard.getString());
        }

        event.consume();
    }

    @FXML
    private void dropTxtVarUserStatus(DragEvent event)
    {
        Dragboard dragBoard = event.getDragboard();
        if (dragBoard.hasString())
        {
            txtVarUserStatus.setText(dragBoard.getString());
        }

        event.consume();
    }

    @FXML
    private void dropTxtVarSystemStatus(DragEvent event)
    {
        Dragboard dragBoard = event.getDragboard();
        if (dragBoard.hasString())
        {
            txtVarSystemStatus.setText(dragBoard.getString());
        }

        event.consume();
    }

    @FXML
    private void dropTxtVarExternalWorkOrderid(DragEvent event)
    {
        Dragboard dragBoard = event.getDragboard();
        if (dragBoard.hasString())
        {
            txtVarExternalWorkOrderid.setText(dragBoard.getString());
        }

        event.consume();
    }

    @FXML
    private void dropTxtVarCreatedOn(DragEvent event)
    {
        Dragboard dragBoard = event.getDragboard();
        if (dragBoard.hasString())
        {
            txtVarCreatedOn.setText(dragBoard.getString());
        }

        event.consume();
    }

    @FXML
    private void dropTxtVarCreatedBy(DragEvent event)
    {
        Dragboard dragBoard = event.getDragboard();
        if (dragBoard.hasString())
        {
            txtVarCreatedBy.setText(dragBoard.getString());
        }

        event.consume();
    }

    @FXML
    private void dropTxtVarName(DragEvent event)
    {
        Dragboard dragBoard = event.getDragboard();
        if (dragBoard.hasString())
        {
            txtVarName.setText(dragBoard.getString());
        }

        event.consume();
    }

    @FXML
    private void dropTxtVarPriority(DragEvent event)
    {
        Dragboard dragBoard = event.getDragboard();
        if (dragBoard.hasString())
        {
            txtVarPriority.setText(dragBoard.getString());
        }

        event.consume();
    }

    @FXML
    private void dropTxtVarStatus(DragEvent event)
    {
        Dragboard dragBoard = event.getDragboard();
        if (dragBoard.hasString())
        {
            txtVarStatus.setText(dragBoard.getString());
        }

        event.consume();
    }

    @FXML
    private void dropTxtVarEstimatedTime(DragEvent event)
    {
        Dragboard dragBoard = event.getDragboard();
        if (dragBoard.hasString())
        {
            txtVarEstimatedTime.setText(dragBoard.getString());
        }

        event.consume();
    }

    @FXML
    private void dropTxtVarLatestFinishDate(DragEvent event)
    {
        Dragboard dragBoard = event.getDragboard();
        if (dragBoard.hasString())
        {
            txtVarLatestFinishDate.setText(dragBoard.getString());
        }

        event.consume();
    }

    @FXML
    private void dropTxtVarEarliestStartDate(DragEvent event)
    {
        Dragboard dragBoard = event.getDragboard();
        if (dragBoard.hasString())
        {
            txtVarEarliestStartDate.setText(dragBoard.getString());
        }

        event.consume();
    }

    @FXML
    private void dropTxtVarLatestStartDate(DragEvent event)
    {
        Dragboard dragBoard = event.getDragboard();
        if (dragBoard.hasString())
        {
            txtVarLatestStartDate.setText(dragBoard.getString());
        }

        event.consume();
    }

    @FXML
    private void clickTest(ActionEvent event)
    {
        String header1 = txtVarSiteName.getText();
        String header2 = txtVarAssetSerialNumber.getText();
        String header3 = txtVarType.getText();
        String header4 = txtVarExternalWorkOrderid.getText();
        String header5 = txtVarSystemStatus.getText();
        String header6 = txtVarUserStatus.getText();
        String header7 = txtVarCreatedOn.getText();
        String header8 = txtVarCreatedBy.getText();
        String header9 = txtVarName.getText();
        String header10 = txtVarPriority.getText();
        String header11 = txtVarStatus.getText();
        String header12 = txtVarLatestFinishDate.getText();
        String header13 = txtVarEarliestStartDate.getText();
        String header14 = txtVarLatestStartDate.getText();
        String header15 = txtVarEstimatedTime.getText();

        getXLSXHeaderValues(absolutePath, header1, header2,
                header3, header4, header5, header6, header7,
                header8, header9, header10, header11, header12,
                header13, header14, header15);
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

                        ArrayList<String> headerList = new ArrayList<>();
                        headerList.add(header1);
                        headerList.add(header2);
                        headerList.add(header3);
                        headerList.add(header4);
                        headerList.add(header5);
                        headerList.add(header6);
                        headerList.add(header7);
                        headerList.add(header8);
                        headerList.add(header9);
                        headerList.add(header10);
                        headerList.add(header11);
                        headerList.add(header12);
                        headerList.add(header13);
                        headerList.add(header14);
                        headerList.add(header15);
                        
                        cellData = String.valueOf(cell.getNumericCellValue());

                        if (cellData.equals(headerList))
                        {
                            colIndex = cell.getColumnIndex();

                            //runs down the rows and prints out the values
                            while (rowIterator.hasNext())
                            {
                                Row r = rowIterator.next();
                                if (r != null)
                                {
                                    lstVarSiteName.add(r.getCell(colIndex).toString());
                                    lstVarAssetSerialNumber.add(r.getCell(colIndex).toString());
                                    lstVarType.add(r.getCell(colIndex).toString());
                                    lstVarExternalWorkOrderid.add(r.getCell(colIndex).toString());
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

                            }
                        }

                        break;
                    //Case the cells value is of type String it will be compared to the header
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
                                    lstVarExternalWorkOrderid.add(r.getCell(colIndex).toString());
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

        String header1 = txtVarSiteName.getText();
        String header2 = txtVarAssetSerialNumber.getText();
        String header3 = txtVarType.getText();
        String header4 = txtVarExternalWorkOrderid.getText();
        String header5 = txtVarSystemStatus.getText();
        String header6 = txtVarUserStatus.getText();
        String header7 = txtVarCreatedOn.getText();
        String header8 = txtVarCreatedBy.getText();
        String header9 = txtVarName.getText();
        String header10 = txtVarPriority.getText();
        String header11 = txtVarStatus.getText();
        String header12 = txtVarLatestFinishDate.getText();
        String header13 = txtVarEarliestStartDate.getText();
        String header14 = txtVarLatestStartDate.getText();
        String header15 = txtVarEstimatedTime.getText();

        getXLSXHeaderValues(absolutePath, header1, header2,
                header3, header4, header5, header6, header7,
                header8, header9, header10, header11, header12,
                header13, header14, header15);
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
            obj.put(txtExternalWorkOrderid.getText(), lstVarExternalWorkOrderid.get(i));
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
