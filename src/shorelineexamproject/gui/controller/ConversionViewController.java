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
import java.util.concurrent.atomic.AtomicInteger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Tooltip;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;
import shorelineexamproject.be.Customization;
import shorelineexamproject.be.TraceLog;
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
    private JFXComboBox<Customization> cbxCustomization;
    @FXML
    private ProgressIndicator prgConversion;
    @FXML
    private Label lblConversionComplete;
    @FXML
    private JFXButton btnFileLocation;
    @FXML
    private JFXButton btnSaveCustomization;
    @FXML
    private JFXButton btnDeleteCustomization;
    @FXML
    protected Label lblUser;
    @FXML
    private JFXButton btnOpenTraceLogView;
    @FXML
    private JFXButton btnSaveTraceLog;
    @FXML
    private Label lblError;

    //Tooltip creations
    private Tooltip directoryTooltip = new Tooltip();

    private Tooltip assetSerialNumberTooltip = new Tooltip();
    private Tooltip typeTooltip = new Tooltip();
    private Tooltip externalWorkOrderIdTooltip = new Tooltip();
    private Tooltip systemStatusTooltip = new Tooltip();
    private Tooltip userStatusTooltip = new Tooltip();
    private Tooltip nameTooltip = new Tooltip();
    private Tooltip priorityTooltip = new Tooltip();
    private Tooltip latestFinishDateTooltip = new Tooltip();
    private Tooltip earliestStartDateTooltip = new Tooltip();
    private Tooltip latestStartDateTooltip = new Tooltip();
    private Tooltip estimatedTimeTooltip = new Tooltip();

    private ArrayList<String> lstVarAssetSerialNumber = new ArrayList<String>();
    private ArrayList<String> lstVarType = new ArrayList<String>();
    private ArrayList<String> lstVarExternalWorkOrderId = new ArrayList<String>();
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

    //Variables for folder selection
    boolean directoryChosen = false;
    String directory = null;

    //AbsolutePath for the file being read
    private ArrayList<String> lstAbsolutePaths = new ArrayList<String>();

    //Variables for use with threads
    private Thread thread = null;
    private final AtomicBoolean suspend = new AtomicBoolean(false);

    //Indicates progress of a given task
    double progress = 1;
    AtomicInteger filesDone = new AtomicInteger();

    //doing jeppes stuff
    private boolean stopped = false;
    private boolean paused = false;
    private LoginViewController parent;

    private Model model;

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
        tooltiplist();
        cbxCustomizationInitialize();
        lstHeadersInitialize();
    }

    private void tooltiplist()
    {
        //Tooltip setup
        assetSerialNumberTooltip.setText("Asset id");
        txtVarAssetSerialNumber.setTooltip(assetSerialNumberTooltip);
        typeTooltip.setText("Order type");
        txtVarType.setTooltip(typeTooltip);
        externalWorkOrderIdTooltip.setText("Order");
        txtVarExternalWorkOrderid.setTooltip(externalWorkOrderIdTooltip);
        systemStatusTooltip.setText("System status");
        txtVarSystemStatus.setTooltip(systemStatusTooltip);
        userStatusTooltip.setText("User status");
        txtVarUserStatus.setTooltip(userStatusTooltip);
        nameTooltip.setText("Opr. short text, if it is empty then it will be set to Description 2");
        txtVarName.setTooltip(nameTooltip);
        priorityTooltip.setText("Priority, will be set to Low if there is no value to get");
        txtVarPriority.setTooltip(priorityTooltip);
        latestFinishDateTooltip.setText("LatestFinishDate");
        txtVarLatestFinishDate.setTooltip(latestFinishDateTooltip);
        earliestStartDateTooltip.setText("EarliestStartDate");
        txtVarEarliestStartDate.setTooltip(earliestStartDateTooltip);
        latestStartDateTooltip.setText("LatestStartDate");
        txtVarLatestStartDate.setTooltip(latestStartDateTooltip);
        estimatedTimeTooltip.setText("EastimatedTime");
        txtVarEstimatedTime.setTooltip(estimatedTimeTooltip);
    }

    private void lstHeadersInitialize()
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

    private void cbxCustomizationInitialize()
    {
        cbxCustomization.setItems(FXCollections.observableArrayList(model.getAllCustomizations()));

        cbxCustomization.valueProperty().addListener((observable, oldValue, newValue) ->
        {
            txtVarAssetSerialNumber.setText(cbxCustomization.getSelectionModel().getSelectedItem().getAssetSerialNumber());
            txtVarType.setText(cbxCustomization.getSelectionModel().getSelectedItem().getType());
            txtVarExternalWorkOrderid.setText(cbxCustomization.getSelectionModel().getSelectedItem().getExternalWorkOrderId());
            txtVarSystemStatus.setText(cbxCustomization.getSelectionModel().getSelectedItem().getSystemStatus());
            txtVarUserStatus.setText(cbxCustomization.getSelectionModel().getSelectedItem().getUserStatus());
            txtVarName.setText(cbxCustomization.getSelectionModel().getSelectedItem().getName());
            txtVarPriority.setText(cbxCustomization.getSelectionModel().getSelectedItem().getPriority());
            txtVarLatestFinishDate.setText(cbxCustomization.getSelectionModel().getSelectedItem().getLatestFinishDate());
            txtVarEarliestStartDate.setText(cbxCustomization.getSelectionModel().getSelectedItem().getEarliestStartDate());
            txtVarLatestStartDate.setText(cbxCustomization.getSelectionModel().getSelectedItem().getLatestStartDate());
            txtVarEstimatedTime.setText(cbxCustomization.getSelectionModel().getSelectedItem().getEstimatedTime());
        });
    }

    /**
     * task w. jeppes help
     */
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

                    if (i > 0)
                    {
                        fileName = txtJSONName.getText() + "_" + (i + 1) + ".json";
                    } else
                    {
                        fileName = txtJSONName.getText() + ".json";
                    }

                    JSONArray jarray = CreateJsonObjects();
                    model.CreateJSONFile(directory, fileName, jarray);

                    //For progress
                    updateProgress(i + 1, lstAbsolutePaths.size());
                    updateMessage((i + 1) + " Files done");

                    lstVarAssetSerialNumber.clear();
                    lstVarType.clear();
                    lstVarExternalWorkOrderId.clear();
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
            }
            return null;
        }
    };

    /**
     * Starts a task
     */
    public void start()
    {
        stopped = false;
        paused = false;

        //Binds the progress bar to the task
        prgConversion.progressProperty().unbind();
        prgConversion.progressProperty().bind(task.progressProperty());

        //Gets a message from the task
        task.messageProperty().addListener(new ChangeListener<String>()
        {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
            {
                lblConversionComplete.setText(newValue);
            }
        });

        if (thread == null)
        {
            thread = new Thread(task);
        }

        //Daemon, stops the task if the main thread is stopped
        thread.setDaemon(true);

        thread.start();
    }

    /**
     * Stops a task
     */
    public void stop()
    {
        stopped = true;
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
     * Opens a FileChooser that is only able to get xlsx files. Jesper
     *
     * @param event
     */
    @FXML
    private void clickGet(ActionEvent event)
    {
        lstAbsolutePaths.clear();

        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose a file");

        //Adds a file filter that will only allow xlsx or csv files
        FileChooser.ExtensionFilter xlsxFilter = new FileChooser.ExtensionFilter("(*.xlsx)", "*.xlsx");
        FileChooser.ExtensionFilter csvFilter = new FileChooser.ExtensionFilter("(*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(xlsxFilter);
        fileChooser.getExtensionFilters().add(csvFilter);

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
        if (lstAbsolutePaths.get(0).endsWith(".xlsx"))
        {
            lstHeaders.getItems().addAll(model.readXLSXHeaders(lstAbsolutePaths.get(0)));
        } else if (lstAbsolutePaths.get(0).endsWith(".csv"))
        {
            lstHeaders.getItems().addAll(model.readCSVHeaders(lstAbsolutePaths.get(0)));
        }
    }

    /**
     * Fills the list fields with the items gotten from the file
     *
     * @param absolutePath
     */
    private void fillListsWithExcel(String absolutePath)
    {
        if (absolutePath.endsWith(".xlsx"))
        {
            model.getXLSXHeaderValues(absolutePath, txtVarAssetSerialNumber.getText(), lstVarAssetSerialNumber);
            model.getXLSXHeaderValues(absolutePath, txtVarType.getText(), lstVarType);
            model.getXLSXHeaderValues(absolutePath, txtVarExternalWorkOrderid.getText(), lstVarExternalWorkOrderId);
            model.getXLSXHeaderValues(absolutePath, txtVarSystemStatus.getText(), lstVarSystemStatus);
            model.getXLSXHeaderValues(absolutePath, txtVarUserStatus.getText(), lstVarUserStatus);
            model.getXLSXHeaderValues(absolutePath, txtVarName.getText(), lstVarName);
            model.getXLSXHeaderValues(absolutePath, txtVarPriority.getText(), lstVarPriority);
            model.getXLSXHeaderValues(absolutePath, txtVarLatestFinishDate.getText(), lstVarLatestFinishDate);
            model.getXLSXHeaderValues(absolutePath, txtVarEarliestStartDate.getText(), lstVarEarliestStartDate);
            model.getXLSXHeaderValues(absolutePath, txtVarLatestStartDate.getText(), lstVarLatestStartDate);
            model.getXLSXHeaderValues(absolutePath, txtVarEstimatedTime.getText(), lstVarEstimatedTime);

            model.getXLSXHeaderValues(absolutePath, "Description 2", lstVarDescription2);
        } else if (absolutePath.endsWith(".csv"))
        {
            model.getCSVHeaderValues(absolutePath, txtVarAssetSerialNumber.getText(), lstVarAssetSerialNumber);
            model.getCSVHeaderValues(absolutePath, txtVarType.getText(), lstVarType);
            model.getCSVHeaderValues(absolutePath, txtVarExternalWorkOrderid.getText(), lstVarExternalWorkOrderId);
            model.getCSVHeaderValues(absolutePath, txtVarSystemStatus.getText(), lstVarSystemStatus);
            model.getCSVHeaderValues(absolutePath, txtVarUserStatus.getText(), lstVarUserStatus);
            model.getCSVHeaderValues(absolutePath, txtVarName.getText(), lstVarName);
            model.getCSVHeaderValues(absolutePath, txtVarPriority.getText(), lstVarPriority);
            model.getCSVHeaderValues(absolutePath, txtVarLatestFinishDate.getText(), lstVarLatestFinishDate);
            model.getCSVHeaderValues(absolutePath, txtVarEarliestStartDate.getText(), lstVarEarliestStartDate);
            model.getCSVHeaderValues(absolutePath, txtVarLatestStartDate.getText(), lstVarLatestStartDate);
            model.getCSVHeaderValues(absolutePath, txtVarEstimatedTime.getText(), lstVarEstimatedTime);

            model.getCSVHeaderValues(absolutePath, "\"Description\" 2", lstVarDescription2);
        }
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

        for (int i = 0; i < lstVarType.size(); i++)
        {
            JSONObject obj = new JSONObject();

            obj.put("siteName", ""); //get from middle of description if possible
            obj.put("assetSerialNumber", lstVarAssetSerialNumber.get(i));
            obj.put("type", lstVarType.get(i));
            obj.put("externalWorkOrderId", lstVarExternalWorkOrderId.get(i));
            obj.put("systemStatus", lstVarSystemStatus.get(i));
            obj.put("userStatus", lstVarUserStatus.get(i));
            obj.put("createdOn", model.getDate());
            obj.put("createdBy", "SAP"); //get sap (or login, ask po)

            if ("".equals(lstVarName.get(i)))
            {
                obj.put("name", lstVarDescription2.get(i));  //2 different ones
            } else
            {
                obj.put("name", lstVarName.get(i));
            }

            if ("".equals(lstVarPriority.get(i)))
            {
                String priority = "Low";
                obj.put("priority", priority); //priority, if empty set low
            } else
            {
                obj.put("priority", lstVarPriority.get(i));
            }

            obj.put("status", "NEW"); //weird thing

            JSONObject obj2 = new JSONObject();
            obj2.put("latestFinishDate", lstVarLatestFinishDate.get(i));
            obj2.put("earliestStartDate", lstVarEarliestStartDate.get(i));
            obj2.put("latestStartDate", lstVarLatestStartDate.get(i));
            obj2.put("estimatedTime", lstVarEstimatedTime.get(i));

            obj.put("planning", obj2);

            mainjsonArray.put(obj);

        }
        System.out.println(mainjsonArray);
        return mainjsonArray;
    }

    @FXML
    private void clickFileLocation(ActionEvent event)
    {
        if (directoryChosen == false)
        {
            DirectoryChooser chooser = new DirectoryChooser();
            chooser.setTitle("File output location");
            File defaultDirectory = new File("c:/");
            chooser.setInitialDirectory(defaultDirectory);
            File selectedDirectory = chooser.showDialog(stage);

            directory = selectedDirectory.getAbsolutePath();
            System.out.println(directory);
            btnFileLocation.setText("File location");

            directoryTooltip.setText(directory);
            btnFileLocation.setTooltip(directoryTooltip);

            directoryTooltip.setText(directory);
            btnFileLocation.setTooltip(directoryTooltip);

            directoryChosen = true;
        } else
        {
            try
            {
                Runtime.getRuntime().exec("explorer.exe /select," + directory);
            } catch (IOException ex)
            {
                Logger.getLogger(ConversionViewController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
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

    @FXML
    private void clickSaveCustomization(ActionEvent event)
    {
        Customization c = new Customization();

        c.setUser("Sap"); //need to do login, get label
        c.setDateOfCreation(model.getDate()); //get date object
        c.setNameOfCustomization(txtJSONName.getText() + "Customization");
        c.setAssetSerialNumber(txtVarAssetSerialNumber.getText());
        c.setType(txtVarType.getText());
        c.setExternalWorkOrderId(txtVarExternalWorkOrderid.getText());
        c.setSystemStatus(txtVarSystemStatus.getText());
        c.setUserStatus(txtVarUserStatus.getText());
        c.setName(txtVarName.getText());
        c.setPriority(txtVarPriority.getText());
        c.setStatus(txtVarSystemStatus.getText());
        c.setLatestFinishDate(txtVarLatestFinishDate.getText());
        c.setEarliestStartDate(txtVarEarliestStartDate.getText());
        c.setLatestStartDate(txtVarLatestStartDate.getText());
        c.setEstimatedTime(txtVarEstimatedTime.getText());

        model.addCustomizationToDB(c);
        System.out.println("Conversion saved");
        cbxCustomizationInitialize(); //get a nullpointer but it works?
    }

    public Customization getSelectedCustomization()
    {
        return cbxCustomization.getSelectionModel().getSelectedItem();
    }

    @FXML
    private void clickDeleteCustomization(ActionEvent event)
    {
        model.removeCustomizationFromDb(getSelectedCustomization());
        System.out.println("Customization deleted");
        cbxCustomizationInitialize(); //get a nullpointer but it works?
    }

    /**
     * It makes the mainwindow able to open the other windows.
     *
     * @param parent
     */
    public void setParentWindowController(LoginViewController parent)
    {
        this.parent = parent;
    }

    @FXML
    private void clickOpenTraceLogViev(ActionEvent event) throws IOException
    {
        Stage stage = new Stage();

        FXMLLoader fxLoader = new FXMLLoader(getClass().getResource("/shorelineexamproject/gui/view/LogView.fxml"));

        Parent root = fxLoader.load();

        LogViewController controller = fxLoader.getController();
        controller.setParentWindowController(parent);

        Scene scene = new Scene(root);
        stage.setTitle("Conversion");
        stage.setScene(scene);
        stage.show();

        Stage window = (Stage) btnOpenTraceLogView.getScene().getWindow();
    }

    @FXML
    private void clickSaveTraceLog(ActionEvent event)
    {
        TraceLog t = new TraceLog();

        t.setUser(lblUser.getText()); //need to do login, get label
        t.setFileName(txtJSONName.getText() + ".json");
        t.setCustomization("some customization"); //if old conversion, get name of that, else get new name
        t.setDate(model.getDate());
        t.setError("Error message");

        model.addTraceLogToDB(t);
        System.out.println("TraceLog saved");
    }

}
