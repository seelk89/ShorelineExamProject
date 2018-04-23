/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shorelineexamproject.gui.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

/**
 *
 * @author Jesper
 */
public class LoginViewController implements Initializable
{
    
    @FXML
    private ImageView imgLogo;
    @FXML
    private JFXTextField txtUser;
    @FXML
    private JFXTextField txtPassword;
    @FXML
    private JFXButton btnLogin;
    @FXML
    private JFXButton btnCreateUser;
    @FXML
    private Label lblErrorMessage;
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // TODO
    }    

    @FXML
    private void clickLogin(ActionEvent event)
    {
    }

    @FXML
    private void clickCreateUser(ActionEvent event)
    {
    }
    
}
