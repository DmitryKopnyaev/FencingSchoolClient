package com.kopniaev;

import com.kopniaev.model.User;
import com.kopniaev.repository.UserRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Date;

public class RegistrationController {
    public TextField textFieldLogin;
    public TextField textFieldPassword;
    public TextField textFieldName;
    public Label labelAuthorization;

    public void labelAuthorization(MouseEvent mouseEvent) {
        closeThisAndOpenReg();
    }

    public void buttonRegistration(ActionEvent actionEvent) {
        String login = textFieldLogin.getText();
        String password = textFieldPassword.getText();
        String name = textFieldName.getText();
        if(login.isEmpty() || password.isEmpty() || name.isEmpty())
            App.showAlert("Error", "Не все поля заполнены", Alert.AlertType.ERROR);
        else {
            try {
                User user = new UserRepository().addUser(new User(login, password, name, new Date()));
                closeThisAndOpenReg();
            } catch (IOException e) {
                App.showAlert("Error", e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    public void closeThisAndOpenReg(){
        Stage currentStage = (Stage) labelAuthorization.getScene().getWindow();
        currentStage.close();
        try {
            Stage stageMain = new Stage();
            FXMLLoader loaderMain = new FXMLLoader(getClass().getResource("authorization.fxml"));
            Parent root = loaderMain.load();
            stageMain.setScene(new Scene(root));
            stageMain.show();
        } catch (IOException e) {
            App.showAlert("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }
}
