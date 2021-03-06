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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import shorelineexamproject.dal.exceptions.DalException;
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
    private Label lblError;

    //Tooltip creation
    private Tooltip directoryTooltip;

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
    private String directory = null;

    //AbsolutePath for the file being read
    private ArrayList<String> lstAbsolutePaths = new ArrayList<String>();

    //Variables for threads
    private Thread thread = null;

    //Task related instancefields
    private boolean btnPressed = false;
    private boolean stopped = false;
    private volatile boolean paused = false;

    private LoginViewController parent;

    private Model model;
    TaskClass taskClass = null;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        try
        {
            this.model = new Model();
        } catch (IOException | DalException ex)
        {
            Logger.getLogger(ConversionViewController.class.getName()).log(Level.SEVERE, null, ex);
        }

        try
        {
            cbxCustomizationInitialize();
        } catch (DalException ex)
        {
            Logger.getLogger(ConversionViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        lstHeadersInitialize();

        //Tooltip creations
        directoryTooltip = new Tooltip();

        String desktopPath = System.getProperty("user.home") + "\\Desktop";
        directory = desktopPath;

        directoryTooltip.setText(directory);
        btnFileLocation.setTooltip(directoryTooltip);

        btnPauseTask.setDisable(true);
        prgConversion.setVisible(false);
    }

    /**
     * Initializes the list of headers we get from the input file
     */
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

    /**
     * Initializes the cbxCustomizations by adding a listener that checks for a
     * chosen customization and fills out the textfields if one is chosen
     */
    private void cbxCustomizationInitialize() throws DalException
    {
        cbxCustomization.setItems(model.getAllCustomizations());
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
     * taskClass for converting xlsx/csv to JSON. It runs while !stopped
     * !paused, it also updates the progressbar, and at the end it clears the
     * txtfields so it can get ready for a new conversion
     */
    public class TaskClass
    {

        public final Task task = new Task()
        {
            @Override
            protected Object call() throws Exception
            {
                while (!stopped)
                {
                    String fileName = null;

                    for (int i = 0; i < lstAbsolutePaths.size(); i++)
                    {
                        while (paused)
                        {
                            Thread.sleep(750);
                        }

                        if (isCancelled())
                        {
                            break;
                        }

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
                    break;
                }
                return null;
            }
        };
    }

    /**
     * Starts a taskClass, gets progressbar info and saves a taskClass
     */
    private void start()
    {
        if (lstAbsolutePaths.size() > 0)
        {
            if (!"".equals(txtVarAssetSerialNumber.getText())
                    && !"".equals(txtVarType.getText())
                    && !"".equals(txtVarExternalWorkOrderid.getText())
                    && !"".equals(txtVarSystemStatus.getText())
                    && !"".equals(txtVarUserStatus.getText())
                    && !"".equals(txtVarName.getText())
                    && !"".equals(txtVarPriority.getText())
                    && !"".equals(txtVarLatestFinishDate.getText())
                    && !"".equals(txtVarEarliestStartDate.getText())
                    && !"".equals(txtVarLatestStartDate.getText())
                    && !"".equals(txtVarEstimatedTime.getText()))
            {
                if (!"".equals(txtJSONName.getText()))
                {
                    btnPressed = true;
                    
                    taskClass = new TaskClass();

                    stopped = false;
                    paused = false;

                    btnPauseTask.setDisable(false);
                    prgConversion.setVisible(true);

                    //Binds the progress bar to the taskClass
                    prgConversion.progressProperty().unbind();
                    prgConversion.setProgress(0);
                    prgConversion.progressProperty().bind(taskClass.task.progressProperty());

                    //Gets a message from the taskClass
                    taskClass.task.messageProperty().addListener(new ChangeListener<String>()
                    {
                        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
                        {
                            lblConversionComplete.setText(newValue);
                            if (prgConversion.getProgress() == 1)
                            {
                                btnTask.setText("Start");
                                btnPauseTask.setText("Pause");
                                btnPauseTask.setDisable(true);

                                stopped = true;
                                thread = null;
                            }
                        }
                    });

                    if (thread == null)
                    {
                        thread = new Thread(taskClass.task);

                        //Daemon, stops the taskClass if the main thread is stopped
                        thread.setDaemon(true);

                        thread.start();
                    }
                    SaveTraceLog();
                } else
                {
                    lblError.setText("Name your conversion");
                }
            } else
            {
                lblError.setText("Get headers from the files");
            }
        } else
        {
            lblError.setText("Get files to read");
        }
    }

    /**
     * If this button is clicked it starts/stops a taskClass. Stops a task
     */
    public synchronized void stop()
    {
        stopped = true;
        taskClass.task.cancel();
        thread = null;
    }

    /**
     * If this button is clicked it starts/stops a task.
     *
     * @param event
     * @throws InterruptedException
     */
    @FXML
    private void clickTask(ActionEvent event) throws InterruptedException
    {
        if ("Start".equals(btnTask.getText()))
        {
            start();
            
            if (btnPressed == true)
            {
                btnTask.setText("Stop");
                btnPressed = false;
            }
        } else if ("Stop".equals(btnTask.getText()))
        {
            stop();
            thread = null;

            btnTask.setText("Start");
        }
    }

    /**
     * pauses or resumes a taskClass
     *
     * @param event
     * @throws InterruptedException
     */
    @FXML
    private synchronized void clickPauseTask(ActionEvent event) throws InterruptedException
    {
        paused = !paused;
        if (!paused)
        {
            btnPauseTask.setText("Pause");
        } else
        {
            btnPauseTask.setText("Resume");
        }
    }

    /**
     * Opens a FileChooser that is only able to get xlsx and csv files.
     *
     * @param event
     */
    @FXML
    private void clickGet(ActionEvent event)
    {
        lstAbsolutePaths.clear();

        prgConversion.progressProperty().unbind();
        prgConversion.setProgress(0);

        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose a file");

        //Adds a file filter that will only allow xlsx or csv files
        FileChooser.ExtensionFilter xlsxFilter = new FileChooser.ExtensionFilter("(*.xlsx)", "*.xlsx");
        FileChooser.ExtensionFilter csvFilter = new FileChooser.ExtensionFilter("(*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(xlsxFilter);
        fileChooser.getExtensionFilters().add(csvFilter);

        //Opens a window based on settings set above
        List<File> files = fileChooser.showOpenMultipleDialog(new Stage());
        if (files != null && files.size() > 0)
        {
            files.forEach((File file) ->
            {
                lstAbsolutePaths.add(file.getAbsolutePath());
            });

            //Clears the ListView and adds headers from xlsx file
            lstHeaders.getItems().clear();
            lstHeaders.getItems().addAll(model.headers(lstAbsolutePaths.get(0)));
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
            model.valuesInHeaderColumn(absolutePath, txtVarAssetSerialNumber.getText(), lstVarAssetSerialNumber);
            model.valuesInHeaderColumn(absolutePath, txtVarType.getText(), lstVarType);
            model.valuesInHeaderColumn(absolutePath, txtVarExternalWorkOrderid.getText(), lstVarExternalWorkOrderId);
            model.valuesInHeaderColumn(absolutePath, txtVarSystemStatus.getText(), lstVarSystemStatus);
            model.valuesInHeaderColumn(absolutePath, txtVarUserStatus.getText(), lstVarUserStatus);
            model.valuesInHeaderColumn(absolutePath, txtVarName.getText(), lstVarName);
            model.valuesInHeaderColumn(absolutePath, txtVarPriority.getText(), lstVarPriority);
            model.valuesInHeaderColumn(absolutePath, txtVarLatestFinishDate.getText(), lstVarLatestFinishDate);
            model.valuesInHeaderColumn(absolutePath, txtVarEarliestStartDate.getText(), lstVarEarliestStartDate);
            model.valuesInHeaderColumn(absolutePath, txtVarLatestStartDate.getText(), lstVarLatestStartDate);
            model.valuesInHeaderColumn(absolutePath, txtVarEstimatedTime.getText(), lstVarEstimatedTime);

            model.valuesInHeaderColumn(absolutePath, "Description 2", lstVarDescription2);
        } else if (absolutePath.endsWith(".csv"))
        {
            model.valuesInHeaderColumn(absolutePath, txtVarAssetSerialNumber.getText(), lstVarAssetSerialNumber);
            model.valuesInHeaderColumn(absolutePath, txtVarType.getText(), lstVarType);
            model.valuesInHeaderColumn(absolutePath, txtVarExternalWorkOrderid.getText(), lstVarExternalWorkOrderId);
            model.valuesInHeaderColumn(absolutePath, txtVarSystemStatus.getText(), lstVarSystemStatus);
            model.valuesInHeaderColumn(absolutePath, txtVarUserStatus.getText(), lstVarUserStatus);
            model.valuesInHeaderColumn(absolutePath, txtVarName.getText(), lstVarName);
            model.valuesInHeaderColumn(absolutePath, txtVarPriority.getText(), lstVarPriority);
            model.valuesInHeaderColumn(absolutePath, txtVarLatestFinishDate.getText(), lstVarLatestFinishDate);
            model.valuesInHeaderColumn(absolutePath, txtVarEarliestStartDate.getText(), lstVarEarliestStartDate);
            model.valuesInHeaderColumn(absolutePath, txtVarLatestStartDate.getText(), lstVarLatestStartDate);
            model.valuesInHeaderColumn(absolutePath, txtVarEstimatedTime.getText(), lstVarEstimatedTime);

            model.valuesInHeaderColumn(absolutePath, "\"Description\" 2", lstVarDescription2);
        }
    }


    private String getDate()
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDateTime dateTime = LocalDateTime.now();
        String formattedDate = dateTime.format(formatter);

        return formattedDate;
    }

    public JSONArray CreateJsonObjects()
    {
        return CreateJsonObjects(
                lstVarAssetSerialNumber, lstVarType, lstVarExternalWorkOrderId, lstVarSystemStatus,
                lstVarUserStatus, lstVarName, lstVarDescription2, lstVarPriority,
                lstVarLatestFinishDate, lstVarEarliestStartDate, lstVarLatestStartDate, lstVarEstimatedTime
        );
    }

    /**
     * Converts excel or csv to json by looping through a list that adds objects
     * to an array that is then sent to the db.
     *
     * @return
     */
    public JSONArray CreateJsonObjects(
            List<String> lstVarAssetSerialNumber1,
            List<String> lstVarType1,
            List<String> lstVarExternalWorkOrderId1,
            List<String> lstVarSystemStatus1,
            List<String> lstVarUserStatus1,
            List<String> lstVarName1,
            List<String> lstVarDescription21,
            List<String> lstVarPriority1,
            List<String> lstVarLatestFinishDate1,
            List<String> lstVarEarliestStartDate1,
            List<String> lstVarLatestStartDate1,
            List<String> lstVarEstimatedTime1
    )
    {
        JSONArray mainjsonArray = new JSONArray();

        for (int i = 0; i < lstVarType1.size(); i++)
        {
            JSONObject obj = new JSONObject();

            obj.put("siteName", "");
            obj.put("assetSerialNumber", lstVarAssetSerialNumber1.get(i));
            obj.put("type", lstVarType1.get(i));
            obj.put("externalWorkOrderId", lstVarExternalWorkOrderId1.get(i));
            obj.put("systemStatus", lstVarSystemStatus1.get(i));
            obj.put("userStatus", lstVarUserStatus1.get(i));
            obj.put("createdOn", getDate());
            obj.put("createdBy", "SAP");

            if ("".equals(lstVarName1.get(i)))
            {
                obj.put("name", lstVarDescription21.get(i));
            } else
            {
                obj.put("name", lstVarName1.get(i));
            }

            if ("".equals(lstVarPriority1.get(i)))
            {
                String priority = "Low";
                obj.put("priority", priority);
            } else
            {
                obj.put("priority", lstVarPriority1.get(i));
            }

            obj.put("status", "NEW");

            JSONObject obj2 = new JSONObject();
            obj2.put("latestFinishDate", lstVarLatestFinishDate1.get(i));
            obj2.put("earliestStartDate", lstVarEarliestStartDate1.get(i));
            obj2.put("latestStartDate", lstVarLatestStartDate1.get(i));
            obj2.put("estimatedTime", lstVarEstimatedTime1.get(i));

            obj.put("planning", obj2);

            mainjsonArray.put(obj);
        }
        return mainjsonArray;
    }

    /**
     * Chooses where to save the converted file.
     *
     * @param event
     */
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

            directory = selectedDirectory.getAbsolutePath(); //null(not anymore?)
            btnFileLocation.setText("File location");

            directoryTooltip.setText(directory); //null
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
     * Gets the selected ListViewObject
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

    /**
     * allows dragging
     *
     * @param event
     */
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

    /**
     * Saves a new customization to the db and updates the boxlist
     *
     * @param event
     */
    @FXML
    private void clickSaveCustomization(ActionEvent event)
    {
        try
        {
            Customization c = new Customization();

            c.setUser(lblUser.getText());
            c.setDateOfCreation(getDate());
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
        } catch (DalException ex)
        {
            Logger.getLogger(ConversionViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Customization getSelectedCustomization()
    {
        return cbxCustomization.getSelectionModel().getSelectedItem();
    }

    @FXML
    private void clickDeleteCustomization(ActionEvent event) throws DalException
    {
        model.removeCustomizationFromDb(getSelectedCustomization());

    }

    /**
     * Opens the LogView so we can see a log of what has been done.
     *
     * @param event
     * @throws IOException
     */
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

    /**
     * Saves a tracelog to the db.
     *
     * @param event
     */
    private void SaveTraceLog()
    {
        try
        {
            TraceLog t = new TraceLog();

            t.setUser(lblUser.getText());
            t.setFileName(txtJSONName.getText() + ".json");
            t.setCustomization(txtJSONName.getText() + "Customization");
            t.setDate(getDate());

            model.addTraceLogToDB(t);
        } catch (DalException ex)
        {
            Logger.getLogger(ConversionViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
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
}
