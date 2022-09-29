package com.kopniaev;

import com.kopniaev.model.User;
import com.kopniaev.repository.UserRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class AuthorizationController {
    @FXML
    public Label labelReg;
    public TextField textFieldLogin;
    public TextField textFieldPassword;

    public void buttonSignIn(ActionEvent actionEvent) {
        String login = textFieldLogin.getText();
        String password = textFieldPassword.getText();
        if (login.isEmpty()) App.showAlert("Error", "Insert login", Alert.AlertType.ERROR);
        else if (textFieldPassword.getText().isEmpty())
            App.showAlert("Error", "Insert password", Alert.AlertType.ERROR);
        else {
            try {
                User userByLoginAndPassword = new UserRepository().getUserByLoginAndPassword(login, password);
                if (userByLoginAndPassword == null)
                    App.showAlert("Error", "Не верный логин или пароль", Alert.AlertType.ERROR);
                else {
                    Stage stageAuthorization = (Stage) labelReg.getScene().getWindow();
                    stageAuthorization.close();

                    Stage stageMain = new Stage();
                    FXMLLoader loaderMain = new FXMLLoader(getClass().getResource("main.fxml"));
                    Parent root = loaderMain.load();
                    stageMain.setScene(new Scene(root));

                    MainController mainController = loaderMain.getController();
                    mainController.initData(userByLoginAndPassword);
                    stageMain.show();

                    App.setPreferences("login", login);
                    App.setPreferences("password", password);

                    this.textFieldLogin.setText("");
                    this.textFieldPassword.setText("");
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void labelReg(MouseEvent mouseEvent) {
        Stage stageAuthorization = (Stage) labelReg.getScene().getWindow();
        stageAuthorization.close();
        try {
            Stage stageMain = new Stage();
            FXMLLoader loaderMain = new FXMLLoader(getClass().getResource("registration.fxml"));
            Parent root = loaderMain.load();
            stageMain.setScene(new Scene(root));
            stageMain.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
