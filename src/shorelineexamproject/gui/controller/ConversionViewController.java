/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shorelineexamproject.gui.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import java.io.File;
import java.io.FileNotFoundException;
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
import shorelineexamproject.be.ListViewObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;
import shorelineexamproject.dal.DAOCustomization;
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
    @FXML
    private JFXComboBox<?> cbxUser;
    @FXML
    private JFXComboBox<?> cbxCustomization;
    @FXML
    private JFXButton btnCreateJson;
    @FXML
    private ProgressIndicator prgConversion;
    @FXML
    private Label lblConversionComplete;

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

    private ArrayList<String> lstVarDescription2 = new ArrayList<String>();

    private Window stage;

    //AbsolutePath for the file being read
    private ArrayList<String> lstAbsolutePaths = new ArrayList<String>();

    //Variables for use with threads
    private Thread thread = null;
    private final AtomicBoolean suspend = new AtomicBoolean(false);
    private final AtomicBoolean done = new AtomicBoolean(false);
    
    //Indicates progress of a given task
    double progress = 1;
    int filesDone = 0;

    //doing jeppes stuff
    private boolean stopped = false;
    private boolean paused = false;

    Model model;
    
     public ConversionViewController() throws IOException
    {
        this.model = new Model();
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

//jeppes stuff
    private final Task task = new Task()
    {
        @Override
        protected Object call() throws Exception
        {

            while (!stopped)
            {
                synchronized (this)
                {
                    while (paused)
                    {
                        wait();
                    }
                }
                
                String fileName = null;
                
                for (int i = 0; i < lstAbsolutePaths.size(); i++)
                {
                    //Fills the list with the values below the headers in a given file
                    fillListsWithExcel(lstAbsolutePaths.get(i));
                    
                    if(i > 0)
                    {
                        fileName = txtJSONName.getText() + "_" + (i + 1) + ".json";
                    } else
                    {
                        fileName = txtJSONName.getText() + ".json";
                    }
                    
                    JSONArray jarray = CreateJsonObjects();
                    model.CreateJSONFile(fileName, jarray);
                    
                    filesDone = i + 1;
                    System.out.println(filesDone);

                    lstVarAssetSerialNumber.clear();
                    lstVarType.clear();
                    lstVarExternalWorkOrderid.clear();
                    lstVarSystemStatus.clear();
                    lstVarUserStatus.clear();
                    lstVarName.clear();
                    lstVarPriority.clear();
                    lstVarLatestFinishDate.clear();
                    lstVarEarliestStartDate.clear();
                    lstVarLatestStartDate.clear();
                    lstVarEstimatedTime.clear();

                    lstVarDescription2.clear();
                }
                stopped = true;
                stop();
            }
            return null;
        }
    };

    //platform run later for progress bar
//                prgBar.setProgress(task.getProgress());
//                prgBar1.setProgress(task.getProgress());
    /**
     * Starts a task
     */
    public void start()
    {
        stopped = false;
        paused = false;
        
        if (thread == null)
        {
            thread = new Thread(task);
        }

        thread.setDaemon(true);
        thread.start();

        while(!stopped)
        {
            prgConversion.setProgress(progress);
            lblConversionComplete.setText(filesDone + " Files done");
        }
    }

    /**
     * Stops a task
     */
    public void stop()
    {
        stopped = true;
    }

    /**
     * Suspends task
     */
    private void suspendTask()
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
     * Pauses the task
     *
     * @throws java.lang.InterruptedException
     */
    public synchronized void pause() throws InterruptedException
    {
        suspend.set(true);
    }

    /**
     * Resumes a paused task
     */
    public void resume()
    {
        suspend.set(false);
        synchronized (thread)
        {
            thread.notifyAll();
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

    //Placeholde start/stop button
    @FXML
    private void clickTask(ActionEvent event) throws InterruptedException
    {
        if ("Start".equals(btnTask.getText()))
        {
            start();

            btnTask.setText("Stop");
        } else if ("Stop".equals(btnTask.getText()))
        {
            stop();
            thread = null;

            btnTask.setText("Start");
            System.out.println("stopped");
        }
    }

    //Placeholder pause/resume button
    //jeppes stuff
    @FXML
    private void clickPauseTask(ActionEvent event) throws InterruptedException
    {
        paused = !paused;
        if (!paused)
        {
            synchronized (task)
            {
                task.notify();

            }

            btnPauseTask.setText("Pause");
        } else
        {
            btnPauseTask.setText("Resume");
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
        List<File> files = fileChooser.showOpenMultipleDialog(new Stage());
        if (files.size() > 0)
        {
            files.forEach((File file) ->
            {
                lstAbsolutePaths.add(file.getAbsolutePath());
            });
        }

        //Clears the ListView and adds headers from xlsx file
        lstHeaders.getItems().clear();
        readXLSXHeaders(lstAbsolutePaths.get(0));
    }

    /**
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
        //fillListsWithExcel();

        String FileName = txtJSONName.getText() + ".json";

        JSONArray jarray = CreateJsonObjects();
        model.CreateJSONFile(FileName, jarray);
    }

    private void fillListsWithExcel(String absolutePath)
    {
        model.getXLSXHeaderValues(absolutePath, txtVarAssetSerialNumber.getText(), lstVarAssetSerialNumber);
        model.getXLSXHeaderValues(absolutePath, txtVarType.getText(), lstVarType);
        model.getXLSXHeaderValues(absolutePath, txtVarExternalWorkOrderid.getText(), lstVarExternalWorkOrderid);
        model.getXLSXHeaderValues(absolutePath, txtVarSystemStatus.getText(), lstVarSystemStatus);
        model.getXLSXHeaderValues(absolutePath, txtVarUserStatus.getText(), lstVarUserStatus);
        model.getXLSXHeaderValues(absolutePath, txtVarName.getText(), lstVarName);
        model.getXLSXHeaderValues(absolutePath, txtVarPriority.getText(), lstVarPriority);
        model.getXLSXHeaderValues(absolutePath, txtVarLatestFinishDate.getText(), lstVarLatestFinishDate);
        model.getXLSXHeaderValues(absolutePath, txtVarEarliestStartDate.getText(), lstVarEarliestStartDate);
        model.getXLSXHeaderValues(absolutePath, txtVarLatestStartDate.getText(), lstVarLatestStartDate);
        model.getXLSXHeaderValues(absolutePath, txtVarEstimatedTime.getText(), lstVarEstimatedTime);

        model.getXLSXHeaderValues(absolutePath, "Description 2", lstVarDescription2);
    }

    /**
     * Anni method below converts excel to json by looping through a list that
     * adds objects to an array.
     *
     * @param objectilist
     * @return
     */
    public JSONArray CreateJsonObjects()
    {
        JSONArray mainjsonArray = new JSONArray();
        //This will be used to loop through the excel
//what can we use instead of lstVarType? objectilist? then it gets empty

        //For indicating progress
        progress = progress / lstVarType.size();
        System.out.println(progress);
        
        for (int i = 0; i < lstVarType.size(); i++)
        {
            JSONObject obj = new JSONObject();

            obj.put(txtSiteName.getText(), ""); //get from middle of description if possible
            obj.put(txtAssetSerialNumber.getText(), lstVarAssetSerialNumber.get(i));
            obj.put(txtType.getText(), lstVarType.get(i));
            obj.put(txtExternalWorkOrderid.getText(), lstVarExternalWorkOrderid.get(i));
            obj.put(txtSystemStatus.getText(), lstVarSystemStatus.get(i));
            obj.put(txtUserStatus.getText(), lstVarUserStatus.get(i));
            obj.put(txtCreatedOn.getText(), model.getDate());
            obj.put(txtCreatedBy.getText(), "SAP"); //get sap (or login, ask po)

            if ("".equals(lstVarName.get(i)))
            {
                obj.put(txtName.getText(), lstVarDescription2.get(i));  //2 different ones
            } else
            {
                obj.put(txtName.getText(), lstVarName.get(i));
            }

            if ("".equals(lstVarPriority.get(i)))
            {
                String priority = "Low";
                obj.put(txtPriority.getText(), priority); //priority, if empty set low
            } else
            {
                obj.put(txtPriority.getText(), lstVarPriority.get(i));
            }

            obj.put(txtStatus.getText(), "NEW"); //weird thing

            JSONObject obj2 = new JSONObject();
            obj2.put(txtLatestFinishDate.getText(), lstVarLatestFinishDate.get(i));
            obj2.put(txtEarliestStartDate.getText(), lstVarEarliestStartDate.get(i));
            obj2.put(txtLatestStartDate.getText(), lstVarLatestStartDate.get(i));
            obj2.put(txtEstimatedTime.getText(), lstVarEstimatedTime.get(i));

            obj.put("planning", obj2);

            mainjsonArray.put(obj);
            
            progress = progress + progress;
            
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
