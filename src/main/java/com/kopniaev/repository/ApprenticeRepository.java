package com.kopniaev.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kopniaev.model.Apprentice;
import com.kopniaev.util.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

public class ApprenticeRepository implements ConnectServer {
    public List<Apprentice> getAllApprentices() throws IOException {
        BufferedReader reader = this.connect(Constants.SERVER_NAME + "/apprentice", "GET", null);
        return new ObjectMapper().readValue(reader, new TypeReference<List<Apprentice>>() {
        });
    }

    public Apprentice addApprentice(Apprentice apprentice) throws IOException {
        BufferedReader reader = this.connect(Constants.SERVER_NAME + "/apprentice", "POST", new ObjectMapper().writeValueAsString(apprentice));
        return new ObjectMapper().readValue(reader, Apprentice.class);
    }

    public Apprentice updateApprentice(Apprentice apprentice) throws IOException {
        BufferedReader reader = this.connect(Constants.SERVER_NAME + "/apprentice", "PUT", new ObjectMapper().writeValueAsString(apprentice));
        return new ObjectMapper().readValue(reader, Apprentice.class);
    }

    public Apprentice deleteApprenticeById(long id) throws IOException{
        BufferedReader reader = this.connect(Constants.SERVER_NAME + "/apprentice/"+ id, "DELETE", null);
        return new ObjectMapper().readValue(reader, Apprentice.class);
    }
}
