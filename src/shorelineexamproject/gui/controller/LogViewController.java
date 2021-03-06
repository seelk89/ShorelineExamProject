/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shorelineexamproject.gui.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import shorelineexamproject.be.TraceLog;
import shorelineexamproject.dal.exceptions.DalException;
import shorelineexamproject.gui.model.Model;

/**
 * FXML Controller class
 *
 * @author Elisabeth
 */
public class LogViewController implements Initializable
{

    private LoginViewController parent;
    @FXML
    private TableView<TraceLog> tableLog;
    @FXML
    private TableColumn<TraceLog, String> columnUser;
    @FXML
    private TableColumn<TraceLog, String> columnFileName;
    @FXML
    private TableColumn<TraceLog, String> columnCustomization;
    @FXML
    private TableColumn<TraceLog, String> columnDate;

    private Model model;

    public LogViewController() throws IOException, DalException
    {
        this.model = Model.getInstance();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        columnUser.setCellValueFactory(
                new PropertyValueFactory("user"));
        columnFileName.setCellValueFactory(
                new PropertyValueFactory("fileName"));
        columnCustomization.setCellValueFactory(
                new PropertyValueFactory("customization"));
        columnDate.setCellValueFactory(
                new PropertyValueFactory("date"));

        tableLog.setItems(model.getTraceLogList());
        try
        {
            model.loadTraceLog();
        } catch (DalException ex)
        {
            Logger.getLogger(LogViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setParentWindowController(LoginViewController parent)
    {
        this.parent = parent;
    }

}
