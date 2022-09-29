package com.kopniaev.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kopniaev.model.Schedule;
import com.kopniaev.model.TrainerSchedule;
import com.kopniaev.util.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

public class TrainerScheduleRepository implements ConnectServer {
    private ObjectMapper mapper = new ObjectMapper();

    public TrainerScheduleRepository() {
        this.mapper.findAndRegisterModules();
    }

    public List<Schedule> getByTrainerId(long id) throws IOException {
        BufferedReader reader = this.connect(Constants.SERVER_NAME + "/schedule/list/" + id, "GET", null);
        return this.mapper.readValue(reader, new TypeReference<List<Schedule>>() {
        });
    }

    public TrainerSchedule addByTrainerId(long id, String weekDay, String start, String end) throws IOException {
        BufferedReader reader = this.connect(Constants.SERVER_NAME + "/schedule/" + id + "?weekDay=" + weekDay + "&start=" + start + "&end=" + end, "POST", null);
        System.out.println(Constants.SERVER_NAME + "/schedule/" + id + "?weekDay=" + weekDay + "&start=" + start + "&end=" + end);
        return this.mapper.readValue(reader, TrainerSchedule.class);
    }

    public TrainerSchedule deleteByTrainerIdByWeekDay(long id, String weekDay) throws IOException {
        BufferedReader reader = this.connect(Constants.SERVER_NAME + "/schedule/" + id + "?weekDay=" + weekDay, "DELETE", null);
        return this.mapper.readValue(reader, TrainerSchedule.class);
    }
}
