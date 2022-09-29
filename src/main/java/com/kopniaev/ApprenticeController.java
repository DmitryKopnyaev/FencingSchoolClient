package com.kopniaev;

import com.kopniaev.model.Apprentice;
import com.kopniaev.model.Training;
import com.kopniaev.repository.ApprenticeRepository;
import com.kopniaev.repository.TrainingRepository;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.ServerException;
import java.util.List;

public class ApprenticeController {
    public TextField textFieldSurname;
    public TextField textFieldName;
    public TextField textFieldPatronimic;
    public TextField textFieldPhoneNumber;
    public ListView<Training> listViewListOfTrainings;
    private Apprentice apprentice;

    public void initData(Apprentice apprentice) {
        this.apprentice = apprentice;
        if (apprentice != null) {
            this.textFieldName.setText(apprentice.getName());
            this.textFieldSurname.setText(apprentice.getSurname());
            this.textFieldPatronimic.setText(apprentice.getPatronymic());
            this.textFieldPhoneNumber.setText(apprentice.getPhoneNumber());
            this.updateTrainings();
        }
    }

    public void updateTrainings() {
        try {
            List<Training> allByApprenticeId = new TrainingRepository().getAllByApprenticeId(this.apprentice.getId());
            this.listViewListOfTrainings.setItems(FXCollections.observableList(allByApprenticeId));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void buttonUpdateApprentice(ActionEvent actionEvent) {
        String name = this.textFieldName.getText();
        String surname = this.textFieldSurname.getText();
        String patronymic = this.textFieldPatronimic.getText();
        String phoneNumber = this.textFieldPhoneNumber.getText();
        if (!name.isEmpty() && !surname.isEmpty() && !patronymic.isEmpty() && !phoneNumber.isEmpty()) {
            this.apprentice.setName(name);
            this.apprentice.setSurname(surname);
            this.apprentice.setPatronymic(patronymic);
            this.apprentice.setPhoneNumber(phoneNumber);
            try {
                new ApprenticeRepository().updateApprentice(this.apprentice);
            } catch (ServerException e) {
                App.showAlert("Error", e.getMessage(), Alert.AlertType.ERROR);
            } catch (IOException e) {
                App.showAlert("Error", "Error", Alert.AlertType.ERROR);
                System.out.println(e.getMessage());
            }
        } else {
            App.showAlert("Error", "Не все поля заполнены", Alert.AlertType.ERROR);
        }
    }

    public void buttonDeleteApprentice(ActionEvent actionEvent) {
        try {
            Apprentice apprentice = new ApprenticeRepository().deleteApprenticeById(this.apprentice.getId());
            if (apprentice != null) {
                Stage window = (Stage) textFieldName.getScene().getWindow();
                window.close();
            }
        } catch (IOException e) {
            App.showAlert("Error", "Error", Alert.AlertType.ERROR);
            System.out.println(e.getMessage());
        }
    }

    public void buttonAddTraining(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("addTraining.fxml"));
        Stage stage = new Stage();
        try {
            stage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        AddTrainingController addTrainingController = loader.getController();
        addTrainingController.initData(this.apprentice);
        stage.showAndWait();
        this.updateTrainings();
    }

    public void buttonDeleteTraining(ActionEvent actionEvent) {
        Training selectedTraining = this.listViewListOfTrainings.getSelectionModel().getSelectedItem();
        if (selectedTraining == null)
            App.showAlert("Error", "Выберите тренеровку", Alert.AlertType.ERROR);
        else {
            try {
                new TrainingRepository().deletById(selectedTraining.getId());
                this.updateTrainings();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
