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
import java.net.URL;
import static java.util.Locale.filter;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.FileChooser;
import javafx.stage.Window;

/**
 * FXML Controller class
 *
 * @author Jesper
 */
public class ConversionViewController implements Initializable
{

    @FXML
    private JFXButton btnTest;
    @FXML
    private JFXTextField txtTest;
    @FXML
    private JFXListView<?> lstTest;
    @FXML
    private JFXButton btnGet;
    
    private Window stage;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // TODO
    }    

    @FXML
    private void clickTest(ActionEvent event)
    {
    }

    /**
     * Opens a FileChooser that is only able to get xlsx files.
     * Jesper
     * @param event 
     */
    @FXML
    private void clickGet(ActionEvent event)
    {
        String absolutePath = null;

        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose a file");
        //Adds a file filter that will only allow xlsx files (excel output files).
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("(*.xlsx)", "*.xlsx");
        fileChooser.getExtensionFilters().add(extFilter);
        
        //Opens a window based on setting set above.
        File xlxsFile = fileChooser.showOpenDialog(stage);
        if (xlxsFile != null) {
            absolutePath = xlxsFile.getAbsolutePath();
        }

        txtTest.setText(absolutePath);
    }
    
}
