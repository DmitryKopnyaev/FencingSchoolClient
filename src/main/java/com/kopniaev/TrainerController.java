package com.kopniaev;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kopniaev.model.Apprentice;
import com.kopniaev.model.Schedule;
import com.kopniaev.model.Trainer;
import com.kopniaev.model.TrainerSchedule;
import com.kopniaev.repository.ApprenticeRepository;
import com.kopniaev.repository.TrainerRepository;
import com.kopniaev.repository.TrainerScheduleRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.ServerException;
import java.time.LocalTime;
import java.util.List;

public class TrainerController {
    @FXML
    public TextField textFieldSurname;
    public TextField textFieldName;
    public TextField textFieldPatronymic;
    public TextField textFieldExperience;
    public TableView<Schedule> tableViewTrainerSchedule;
    public TableColumn<Schedule, String> columnWeekDay;
    public TableColumn<Schedule, LocalTime> columnTrainingStart;
    public TableColumn<Schedule, LocalTime> columnTrainingEnd;

    private Trainer trainer;

    @FXML
    private void initialize() {
        this.columnWeekDay.setCellValueFactory(new PropertyValueFactory<>("weekDay"));
        this.columnTrainingStart.setCellValueFactory(new PropertyValueFactory<>("begin"));
        this.columnTrainingEnd.setCellValueFactory(new PropertyValueFactory<>("end"));
    }

    public void initData(Trainer trainer) {
        this.trainer = trainer;
        if (trainer != null) {
            this.textFieldName.setText(trainer.getName());
            this.textFieldSurname.setText(trainer.getSurname());
            this.textFieldPatronymic.setText(trainer.getPatronymic());
            this.textFieldExperience.setText(String.valueOf(trainer.getExperience()));
        }
        this.updateTrainerScheduleTable();
    }

    public void updateTrainerScheduleTable() {
        try {
            List<Schedule> scheduleList = new TrainerScheduleRepository().getByTrainerId(this.trainer.getId());
            tableViewTrainerSchedule.setItems(FXCollections.observableList(scheduleList));
        } catch (Exception ignored) {
        }
    }

    public void buttonUpdateTrainer(ActionEvent actionEvent) {
        String name = this.textFieldName.getText();
        String surname = this.textFieldSurname.getText();
        String patronymic = this.textFieldPatronymic.getText();
        String experienceText = this.textFieldExperience.getText();
        if (!name.isEmpty() && !surname.isEmpty() && !patronymic.isEmpty() && !experienceText.isEmpty()) {
            this.trainer.setName(name);
            this.trainer.setSurname(surname);
            this.trainer.setPatronymic(patronymic);
            this.trainer.setExperience(Integer.parseInt(experienceText));
            try {
                new TrainerRepository().updateTrainer(this.trainer);
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

    public void buttonDeleteTrainer(ActionEvent actionEvent) {
        try {
            Trainer trainer = new TrainerRepository().deleteTrainerById(this.trainer.getId());
            if (trainer != null) {
                Stage window = (Stage) textFieldName.getScene().getWindow();
                window.close();
            }
        } catch (IOException e) {
            App.showAlert("Error", "Error", Alert.AlertType.ERROR);
            System.out.println(e.getMessage());
        }
    }

    public void buttonAddSchedule(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("addSchedule.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            AddScheduleController controller = loader.getController();
            controller.initData(this.trainer);
            stage.showAndWait();
            this.updateTrainerScheduleTable();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void buttonDeleteSchedule(ActionEvent actionEvent) {
        Schedule selectedItem = this.tableViewTrainerSchedule.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            App.showAlert("Error", "Выберите строку", Alert.AlertType.ERROR);
            return;
        }
        try {
            TrainerSchedule trainerSchedule = new TrainerScheduleRepository().deleteByTrainerIdByWeekDay(this.trainer.getId(), selectedItem.getWeekDay());
            if (trainerSchedule != null)
                this.updateTrainerScheduleTable();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
