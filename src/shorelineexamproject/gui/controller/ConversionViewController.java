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
import shorelineexamproject.gui.model.Model;

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
    private JFXTextField txtVarName;
    @FXML
    private JFXTextField txtVarPriority;
    @FXML
    private JFXTextField txtVarEstimatedTime;
    @FXML
    private JFXTextField txtVarLatestFinishDate;
    @FXML
    private JFXTextField txtVarEarliestStartDate;
    @FXML
    private JFXTextField txtVarLatestStartDate;

    private ArrayList<String> lstVarAssetSerialNumber = new ArrayList<String>();
    private ArrayList<String> lstVarType = new ArrayList<String>();
    private ArrayList<String> lstVarExternalWorkOrderid = new ArrayList<String>();
    private ArrayList<String> lstVarSystemStatus = new ArrayList<String>();
    private ArrayList<String> lstVarUserStatus = new ArrayList<String>();
    private ArrayList<String> lstVarName = new ArrayList<String>();
    private ArrayList<String> lstVarPriority = new ArrayList<String>();
    private ArrayList<String> lstVarLatestFinishDate = new ArrayList<String>();
    private ArrayList<String> lstVarEarliestStartDate = new ArrayList<String>();
    private ArrayList<String> lstVarLatestStartDate = new ArrayList<String>();
    private ArrayList<String> lstVarEstimatedTime = new ArrayList<String>();

    private List<Object> objectilist = new ArrayList();

    private Window stage;

    //AbsolutePath for the file being read
    private String absolutePath = null;

    //Variables for use with threads
    private Thread thread = null;
    private Task task = null;
    private final AtomicBoolean suspend = new AtomicBoolean(true);
    private final AtomicBoolean done = new AtomicBoolean(true);

    private Model model = new Model();

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
        task = createNewTask();
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
    private Task createNewTask()
    {
        return new Task()
        {
            boolean wait = true;

            @Override
            protected Object call() throws Exception
            {
                if (wait == true)
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
                    }
                } else
                {

                }
                return null;
            }
        };
    }

    ;
    
                //platform run later for progress bar
//                prgBar.setProgress(task.getProgress());
//                prgBar1.setProgress(task.getProgress());
                
    
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
     * @param event
     * @throws IOException Takes the String filepath of a xlsx file and finds
     * the headers before putting them into a ListView Jesper
     *
     * @param filepath
     */
    private void readXLSXHeaders(String filepath)
    {
        lstHeaders.getItems().addAll(model.readXLSXHeaders(filepath));
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

        String header1 = txtVarAssetSerialNumber.getText();
        String header2 = txtVarType.getText();
        String header3 = txtVarExternalWorkOrderid.getText();
        String header4 = txtVarSystemStatus.getText();
        String header5 = txtVarUserStatus.getText();
        String header6 = txtVarName.getText();
        String header7 = txtVarPriority.getText();
        String header8 = txtVarLatestFinishDate.getText();
        String header9 = txtVarEarliestStartDate.getText();
        String header10 = txtVarLatestStartDate.getText();
        String header11 = txtVarEstimatedTime.getText();

        model.getXLSXHeaderValues(absolutePath, header1, lstVarAssetSerialNumber);
        model.getXLSXHeaderValues(absolutePath, header2, lstVarType);
        model.getXLSXHeaderValues(absolutePath, header3, lstVarExternalWorkOrderid);
        model.getXLSXHeaderValues(absolutePath, header4, lstVarSystemStatus);
        model.getXLSXHeaderValues(absolutePath, header5, lstVarUserStatus);
        model.getXLSXHeaderValues(absolutePath, header6, lstVarName);
        model.getXLSXHeaderValues(absolutePath, header7, lstVarPriority);
        model.getXLSXHeaderValues(absolutePath, header8, lstVarLatestFinishDate);
        model.getXLSXHeaderValues(absolutePath, header9, lstVarEarliestStartDate);
        model.getXLSXHeaderValues(absolutePath, header10, lstVarLatestStartDate);
        model.getXLSXHeaderValues(absolutePath, header11, lstVarEstimatedTime);

        JSONArray jarray = CreateJsonObjects(objectilist);

        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        fw.write(jarray.toString(4));
        fw.flush();
        System.out.println("JSONfile called: " + FileName + " created in" + file.getAbsolutePath());

        System.out.println(jarray);
    }

    /**
     * Anni method below converts to json (for now just loops through a list
     *
     * @param objectilist
     * @return
     */
    public JSONArray CreateJsonObjects(List<Object> objectilist)
    {
        JSONArray mainjsonArray = new JSONArray();
        //This will be used to loop through the excel

        for (int i = 0; i < lstVarType.size(); i++)
        {
            JSONObject obj = new JSONObject();

            obj.put(txtSiteName.getText(), ""); //get from middle of description if possible
            obj.put(txtAssetSerialNumber.getText(), lstVarAssetSerialNumber.get(i));
            obj.put(txtType.getText(), lstVarType.get(i));
            obj.put(txtExternalWorkOrderid.getText(), lstVarExternalWorkOrderid.get(i));
            obj.put(txtSystemStatus.getText(), lstVarSystemStatus.get(i));
            obj.put(txtUserStatus.getText(), lstVarUserStatus.get(i));
            obj.put(txtCreatedOn.getText(), "Datetime Object"); //get datetime object
            obj.put(txtCreatedBy.getText(), "SAP"); //get sap (or login)
            obj.put(txtName.getText(), lstVarName.get(i));  //2 different ones
            obj.put(txtPriority.getText(), lstVarPriority.get(i)); //priority, if empty set low
            obj.put(txtStatus.getText(), "NEW"); //weird thing

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

    @FXML
    private void overTxtTest(DragEvent event)
    {
        if (event.getDragboard().hasString())
        {
            event.acceptTransferModes(TransferMode.COPY);
        }

        event.consume();
    }

    @FXML
    private void dropTxtVarAssetSerialNumber(DragEvent event)
    {
        Dragboard dragBoard = event.getDragboard();
        if (dragBoard.hasString())
        {
            txtVarAssetSerialNumber.setText(dragBoard.getString());
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
}
