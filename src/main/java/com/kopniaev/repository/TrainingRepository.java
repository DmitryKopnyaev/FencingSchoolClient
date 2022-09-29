package com.kopniaev.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kopniaev.model.Training;
import com.kopniaev.util.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalTime;
import java.util.List;

public class TrainingRepository implements ConnectServer {

    private ObjectMapper mapper = new ObjectMapper();

    public TrainingRepository() {
        mapper.findAndRegisterModules();
    }

    public List<Training> getAllByApprenticeId(long id) throws IOException {
        BufferedReader reader = connect(Constants.SERVER_NAME + "/training/byapprentice/" + id, "GET", null);
        return mapper.readValue(reader, new TypeReference<List<Training>>() {
        });
    }

    public Training deletById(long id) throws IOException {
        BufferedReader reader = connect(Constants.SERVER_NAME + "/training/" + id, "DELETE", null);
        return mapper.readValue(reader, Training.class);
    }

    public List<LocalTime> getByTrainerIdAndDate(long idTrainer, String date) throws IOException {
        BufferedReader reader = connect(Constants.SERVER_NAME + "/training/listtimes/" + idTrainer + "?date=" + date, "GET", null);
        return mapper.readValue(reader, new TypeReference<List<LocalTime>>() {
        });
    }

    public Training addTraining(long trainerId, long apprenticeId, int gymNumber, String date, String time) throws IOException {
        BufferedReader reader = connect(Constants.SERVER_NAME
                        + "/training/" + trainerId + "/" + apprenticeId
                        + "?gymNumber=" + gymNumber
                        + "&date=" + date
                        + "&start=" + time,
                "POST", null);
        return mapper.readValue(reader, Training.class);
    }

}
