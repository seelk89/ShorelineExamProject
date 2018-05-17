/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shorelineexamproject.gui.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
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
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import shorelineexamproject.be.LogIn;
import shorelineexamproject.dal.exceptions.DalException;
import shorelineexamproject.gui.model.Model;

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
    private JFXPasswordField txtPassword;
    @FXML
    private JFXButton btnLogin;
    @FXML
    private JFXButton btnCreateUser;
    @FXML
    private Label lblErrorMessage;
    @FXML
    private Label lblError;
    private Model model;

    public LoginViewController() throws IOException, DalException
    {
        this.model = Model.getInstance();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // TODO
    }

    @FXML
    private void clickLogin(ActionEvent event) throws IOException
    {
        String userName = txtUser.getText();
        String password = txtPassword.getText();

//comment in the if statement if you want login to work, remember both {start and end bracket}
//        if (model.UserLogin(userName, password) == true)
//        {
        Stage stage = new Stage();

        FXMLLoader fxLoader = new FXMLLoader(getClass().getResource("/shorelineexamproject/gui/view/ConversionView.fxml"));

        Parent root = fxLoader.load();

        ConversionViewController controller = fxLoader.getController();
        controller.setParentWindowController(this);
        controller.lblUser.setText(userName);

        Scene scene = new Scene(root);
        stage.setTitle("Conversion");
        stage.setScene(scene);
        stage.show();

        Stage window = (Stage) btnLogin.getScene().getWindow();
        window.close();
//        }
        //could make login button red and make a tooltip appear instead, cant make this look nice
        lblError.setText("Wrong Username/password");
        lblError.setTextFill(Color.web("#ff0000"));
    }

    @FXML
    private void clickCreateUser(ActionEvent event) throws DalException
    {
        String userName = txtUser.getText();
        String password = txtPassword.getText();
//        if (model.UserLogin(username, password) == true) //if db doesn't already contain this username, make it
//        {

        LogIn l = new LogIn();
        l.setUserName(userName);
        l.setPassword(password);

        model.addUserToDB(l);

        lblError.setText("User Created");
        lblError.setTextFill(Color.web("#0b6074"));
//        }
    }

}
