package com.kopniaev;

import com.kopniaev.model.Apprentice;
import com.kopniaev.model.Trainer;
import com.kopniaev.model.User;
import com.kopniaev.repository.ApprenticeRepository;
import com.kopniaev.repository.TrainerRepository;
import com.kopniaev.repository.UserRepository;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class MainController {

    @FXML
    public ListView<Apprentice> listViewApprentices;
    public ListView<Trainer> listViewTrainers;
    public Label labelWelcome;
    private User user;

    public void buttonDeleteUser(ActionEvent actionEvent) {
        try {
            new UserRepository().deleteUserById(this.user.getId());
            this.buttonExit(new ActionEvent());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void buttonExit(ActionEvent actionEvent) {
        Stage stage = (Stage) listViewTrainers.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("authorization.fxml"));
        try{
            stage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            App.setPreferences("login", "");
            App.setPreferences("password", "");
        }
    }

    public void buttonAdd(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("addPerson.fxml"));
        Stage stage = new Stage();
        try {
            stage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        AddPersonController addPersonController = loader.getController();
        addPersonController.initData();
        stage.showAndWait();

        this.updateTrainersList();
        this.updateApprenticeList();
    }

    public void initData(User user) {
        this.user = user;
        this.updateApprenticeList();
        this.updateTrainersList();
        this.labelWelcome.setText("Добро пожаловать, " + this.user.getName());

        this.listViewApprentices.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Apprentice selectedApprentice = listViewApprentices.getSelectionModel().getSelectedItem();
                if(selectedApprentice == null){
                    App.showAlert("Error", "Выберите ученика", Alert.AlertType.ERROR);
                    return;
                }
                //окно apprenticeForm
                FXMLLoader loader = new FXMLLoader(getClass().getResource("apprentice.fxml"));
                Stage stage = new Stage();
                try {
                    stage.setScene(new Scene(loader.load()));
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
                ApprenticeController apprenticeController = loader.getController();
                apprenticeController.initData(selectedApprentice);
                stage.showAndWait();
                MainController.this.updateApprenticeList();
            }
        });

        this.listViewTrainers.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Trainer selectedTrainer = listViewTrainers.getSelectionModel().getSelectedItem();
                if(selectedTrainer == null){
                    App.showAlert("Error", "Выберите тренера", Alert.AlertType.ERROR);
                    return;
                }
                //окно trainerForm
                FXMLLoader loader = new FXMLLoader(getClass().getResource("trainer.fxml"));
                Stage stage = new Stage();
                try {
                    stage.setScene(new Scene(loader.load()));
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
                TrainerController trainerController = loader.getController();
                trainerController.initData(selectedTrainer);
                stage.showAndWait();
                MainController.this.updateTrainersList();
            }
        });
    }

    public void updateApprenticeList() {
        try {
            List<Apprentice> allApprentices = new ApprenticeRepository().getAllApprentices();
            this.listViewApprentices.setItems(FXCollections.observableList(allApprentices));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateTrainersList() {
        try {
            List<Trainer> allTrainers = new TrainerRepository().getAllTrainers();
            this.listViewTrainers.setItems(FXCollections.observableList(allTrainers));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
