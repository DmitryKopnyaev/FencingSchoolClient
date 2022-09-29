package com.kopniaev;

import com.kopniaev.model.Trainer;
import com.kopniaev.model.TrainerSchedule;
import com.kopniaev.repository.TrainerScheduleRepository;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddScheduleController {

    private Trainer trainer;

    @FXML
    public ComboBox<String> comboBoxWeekDay;
    public ComboBox<LocalTime> comboBoxTimeBegin;
    public ComboBox<LocalTime> comboBoxTimeEnd;


    public void initData(Trainer trainer) {
        this.trainer = trainer;
        
        String[] days = {"monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday"};
        comboBoxWeekDay.setItems(FXCollections.observableList(Arrays.asList(days)));

        LocalTime midnight = LocalTime.parse("00:00", DateTimeFormatter.ofPattern("HH:mm"));
        List<LocalTime> time = new ArrayList<>();
        for (int i = 0; i < 48; i ++) {
            time.add(midnight);
            midnight = midnight.plusMinutes(30);
        }

        comboBoxTimeBegin.setItems(FXCollections.observableList(time));
        comboBoxTimeEnd.setItems(FXCollections.observableList(time));
    }

    public void buttonAdd(ActionEvent actionEvent) {
        String weekDay = this.comboBoxWeekDay.getSelectionModel().getSelectedItem();
        LocalTime timeBegin = this.comboBoxTimeBegin.getSelectionModel().getSelectedItem();
        LocalTime timeEnd = this.comboBoxTimeEnd.getSelectionModel().getSelectedItem();
        if(weekDay==null || timeBegin ==null || timeEnd == null){
            App.showAlert("Error", "Вы выбрали не всё", Alert.AlertType.ERROR);
            return;
        }
        if(timeBegin.isAfter(timeEnd) || timeBegin.equals(timeEnd)){
            App.showAlert("Error", "Время окончания тренеровки должно быть позже времени начала", Alert.AlertType.ERROR);
            return;
        }
        try {
            TrainerSchedule trainerSchedule = new TrainerScheduleRepository().addByTrainerId(this.trainer.getId(), weekDay, timeBegin.toString(), timeEnd.toString());
            System.out.println(trainerSchedule);
            Stage stage = (Stage) this.comboBoxTimeBegin.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
