package com.kopniaev.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Training {

    private long id;

    @NonNull
    private int numberGym;

    private Trainer trainer;

    private Apprentice apprentice;

    @NonNull
    private LocalDate date;

    @NonNull
    private LocalTime time;

    @Override
    public String toString(){
        return "Дата: " + this.date + " " + this.time + ", Тренер: " + this.trainer.getSurname() + ", Номер зала: " + numberGym;
    }
}
