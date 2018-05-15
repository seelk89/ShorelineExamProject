/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shorelineexamproject.gui.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

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
    private void clickLogin(ActionEvent event) throws IOException
    {
        Stage stage = new Stage();

        FXMLLoader fxLoader = new FXMLLoader(getClass().getResource("/shorelineexamproject/gui/view/ConversionView.fxml"));

        Parent root = fxLoader.load();

        ConversionViewController controller = fxLoader.getController();
        controller.setParentWindowController(this);

        Scene scene = new Scene(root);
        stage.setTitle("Conversion");
        stage.setScene(scene);
        stage.show();

        Stage window = (Stage) btnLogin.getScene().getWindow();
        window.close();

    }

    @FXML
    private void clickCreateUser(ActionEvent event)
    {
    }

}
