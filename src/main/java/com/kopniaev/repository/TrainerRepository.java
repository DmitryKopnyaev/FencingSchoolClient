package com.kopniaev.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kopniaev.model.Trainer;
import com.kopniaev.util.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

public class TrainerRepository implements ConnectServer {
    private ObjectMapper mapper = new ObjectMapper();

    public TrainerRepository() {
        mapper.findAndRegisterModules();
    }

    public List<Trainer> getAllTrainers() throws IOException {
        BufferedReader reader = this.connect(Constants.SERVER_NAME + "/trainer", "GET", null);
        return mapper.readValue(reader, new TypeReference<List<Trainer>>() {
        });
    }

    public Trainer addTrainer(Trainer trainer) throws IOException {
        BufferedReader reader = this.connect(Constants.SERVER_NAME + "/trainer", "POST", mapper.writeValueAsString(trainer));
        return mapper.readValue(reader, Trainer.class);
    }

    public Trainer updateTrainer(Trainer trainer) throws IOException {
        BufferedReader reader = this.connect(Constants.SERVER_NAME + "/trainer", "PUT", mapper.writeValueAsString(trainer));
        return mapper.readValue(reader, Trainer.class);
    }

    public Trainer deleteTrainerById(long id) throws IOException {
        BufferedReader reader = this.connect(Constants.SERVER_NAME + "/trainer/" + id, "DELETE", null);
        return mapper.readValue(reader, Trainer.class);
    }
}
