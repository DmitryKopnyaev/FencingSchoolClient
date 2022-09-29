package com.kopniaev;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kopniaev.model.Apprentice;
import com.kopniaev.model.Schedule;
import com.kopniaev.model.Trainer;
import com.kopniaev.model.Training;
import com.kopniaev.repository.TrainerRepository;
import com.kopniaev.repository.TrainerScheduleRepository;
import com.kopniaev.repository.TrainingRepository;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class AddTrainingController {

    public ComboBox<Trainer> comboBoxTrainers;
    public DatePicker datePickerTraining;
    public Button buttonAddTraining;
    public ComboBox<LocalTime> comboBoxTrainingTime;
    public TextField textFieldGymNumber;

    private Apprentice apprentice;

    private Trainer selectedTrainer;
    private LocalDate selectedDate;
    private LocalTime selectedTime;

    private DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HH:mm");

    public void initData(Apprentice apprentice) {
        this.apprentice = apprentice;

        try {
            //отфильтруем тренеров без расписания
            List<Trainer> trainersWithSchedule = new TrainerRepository().getAllTrainers().stream().filter(o -> o.getTrainerSchedule() != null).collect(Collectors.toList());
            this.comboBoxTrainers.setItems(FXCollections.observableList(trainersWithSchedule));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    //очередность добавления тренировки обязятальна: тренер -> день -> номер зала -> время
    public void buttonAddTraining(ActionEvent actionEvent) {
        String gymNumberText = textFieldGymNumber.getText();
        if (gymNumberText.isEmpty())
            App.showAlert("Error", "Введите номер зала", Alert.AlertType.ERROR);
        try {
            int gymNumber = Integer.parseInt(gymNumberText);
            Training training = new TrainingRepository().addTraining(this.selectedTrainer.getId(), this.apprentice.getId(), gymNumber, selectedDate.format(formatterDate), selectedTime.format(formatterTime));
            System.out.println(training);
            App.showAlert("INFORMATION", "Тренеровка добавлена", Alert.AlertType.INFORMATION);
            Stage stage = (Stage) this.buttonAddTraining.getScene().getWindow();
            stage.close();
        } catch (NumberFormatException e) {
            App.showAlert("Error", "Номер зала должен быть целым числом", Alert.AlertType.ERROR);
        } catch (IOException e) {
            App.showAlert("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }


    public void selectTrainer(ActionEvent actionEvent) {
        this.selectedTrainer = this.comboBoxTrainers.getSelectionModel().getSelectedItem();
        try {
            List<Schedule> byTrainerId = new TrainerScheduleRepository().getByTrainerId(this.selectedTrainer.getId());
            List<String> notWorkingDays = byTrainerId.stream().filter(o -> o.getBegin() == null || o.getEnd() == null)
                    .map(o -> o.getWeekDay().toUpperCase()).collect(Collectors.toList());

            List<Training> allByApprenticeId = new TrainingRepository().getAllByApprenticeId(this.apprentice.getId());

            datePickerTraining.setDayCellFactory(picker -> new DateCell() {
                @Override
                public void updateItem(LocalDate date, boolean empty) {
                    super.updateItem(date, empty);
                    if (notWorkingDays.stream().anyMatch(o -> o.equals(date.getDayOfWeek().toString()))
                            || date.isBefore(LocalDate.now())
                            || allByApprenticeId.stream().anyMatch(o -> o.getDate().equals(date))) {
                        setDisable(true);
                        setStyle("-fx-background-color: #ffc0cb");
                    }
                }
            });
        } catch (Exception e) {
            this.selectedTrainer = null;
        }
    }

    public void onMouseClickedDate(MouseEvent mouseEvent) {
        if (this.selectedTrainer == null)
            App.showAlert("Error", "Выберите тренера", Alert.AlertType.ERROR);
    }

    public void selectDate(ActionEvent actionEvent) {
        this.selectedDate = this.datePickerTraining.getValue();

        try {
            List<LocalTime> byTrainerIdAndDate = new TrainingRepository().getByTrainerIdAndDate(this.selectedTrainer.getId(), this.selectedDate.format(formatterDate.ofPattern("dd.MM.yyyy")));
            this.comboBoxTrainingTime.setItems(FXCollections.observableList(byTrainerIdAndDate));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void timeTrainingMouseClicked(MouseEvent mouseEvent) {
        if (this.selectedDate == null)
            App.showAlert("Error", "Выберите дату", Alert.AlertType.ERROR);
    }


    public void selectTrainingTime(ActionEvent actionEvent) {
        this.selectedTime = this.comboBoxTrainingTime.getSelectionModel().getSelectedItem();
    }


    public void gymNumberMouseClicked(MouseEvent mouseEvent) {
        if (this.selectedTime == null)
            App.showAlert("Error", "Выберите время тренеровки", Alert.AlertType.ERROR);
    }
}
