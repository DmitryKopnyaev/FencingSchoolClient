package com.kopniaev;

import com.kopniaev.model.Apprentice;
import com.kopniaev.model.Trainer;
import com.kopniaev.repository.ApprenticeRepository;
import com.kopniaev.repository.TrainerRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class AddPersonController {

    @FXML
    public TextField textFieldSurname;
    public TextField textFieldName;
    public TextField textFieldPatronimic;
    public TextField textFieldPhoneNumber;
    public RadioButton radioButtonTrainer;
    public RadioButton radioButtonApprentice;
    public Label changedLabel;

    public ToggleGroup toggleGroup = new ToggleGroup();

    public void initData() {
        this.radioButtonApprentice.setToggleGroup(toggleGroup);
        this.radioButtonTrainer.setToggleGroup(toggleGroup);
        this.radioButtonApprentice.setSelected(true);
    }

    public void buttonCreate(ActionEvent actionEvent) {
        String surname = textFieldSurname.getText();
        String name = textFieldName.getText();
        String patronimic = textFieldPatronimic.getText();
        String some = textFieldPhoneNumber.getText();

        if (surname.isEmpty())
            App.showAlert("Error", "Insert surname", Alert.AlertType.ERROR);
        else if (name.isEmpty())
            App.showAlert("Error", "Insert name", Alert.AlertType.ERROR);
        else if (patronimic.isEmpty())
            App.showAlert("Error", "Insert patronimic", Alert.AlertType.ERROR);
        else if (some.isEmpty())
            App.showAlert("Error", "Insert phone number", Alert.AlertType.ERROR);
        else {
            RadioButton selectedToggle = (RadioButton) toggleGroup.getSelectedToggle();
            String text = selectedToggle.getText();
            if (text.equals("Тренер")) {
                try {
                    new TrainerRepository().addTrainer(new Trainer(surname, name, patronimic, Integer.parseInt(some)));
                    App.showAlert("Information", "Тренер добавлен", Alert.AlertType.INFORMATION);
                } catch (NumberFormatException e) {
                    App.showAlert("Error", "Не корректное значение опыта работы", Alert.AlertType.ERROR);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    App.showAlert("Error", "Такой тренер уже есть", Alert.AlertType.ERROR);
                }
            } else if (text.equals("Ученик")) {
                try {
                    new ApprenticeRepository().addApprentice(new Apprentice(surname, name, patronimic, some));
                    App.showAlert("Information", "Ученик добавлен", Alert.AlertType.INFORMATION);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    App.showAlert("Error", "Такой ученик уже есть", Alert.AlertType.ERROR);
                }
            }
            Stage stageAddPerson = (Stage) textFieldName.getScene().getWindow();
            stageAddPerson.close();
        }
    }

    public void trainerSelected(ActionEvent actionEvent) {
        this.changedLabel.setText("Стаж");
    }

    public void apprenticeSelected(ActionEvent actionEvent) {
        this.changedLabel.setText("Номер телефона");
    }
}
