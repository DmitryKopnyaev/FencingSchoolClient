package com.kopniaev.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.kopniaev.util.LocalTimeDeserializer;
import com.kopniaev.util.LocalTimeSerializer;
import lombok.*;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class TrainerSchedule {
    private long id;

    @NonNull
    @ToString.Exclude
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Trainer trainer;

    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonSerialize(using = LocalTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:SS")
    private LocalTime mondayStart;

    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonSerialize(using = LocalTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:SS")
    private LocalTime mondayEnd;

    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonSerialize(using = LocalTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:SS")
    private LocalTime tuesdayStart;

    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonSerialize(using = LocalTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:SS")
    private LocalTime tuesdayEnd;

    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonSerialize(using = LocalTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:SS")
    private LocalTime wednesdayStart;

    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonSerialize(using = LocalTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:SS")
    private LocalTime wednesdayEnd;

    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonSerialize(using = LocalTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:SS")
    private LocalTime thursdayStart;

    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonSerialize(using = LocalTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:SS")
    private LocalTime thursdayEnd;

    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonSerialize(using = LocalTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:SS")
    private LocalTime fridayStart;

    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonSerialize(using = LocalTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:SS")
    private LocalTime fridayEnd;

    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonSerialize(using = LocalTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:SS")
    private LocalTime saturdayStart;

    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonSerialize(using = LocalTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:SS")
    private LocalTime saturdayEnd;

    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonSerialize(using = LocalTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:SS")
    private LocalTime sundayStart;

    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonSerialize(using = LocalTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:SS")
    private LocalTime sundayEnd;

    public List<Schedule> scheduleList() {
        String[] days = {"monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday"};
        List<Schedule> list = new ArrayList<>();
        for (String day : days) {
            try {
                LocalTime dayStart = (LocalTime) this.getClass().getDeclaredField(day + "Start").get(this);
                LocalTime dayEnd = (LocalTime) this.getClass().getDeclaredField(day + "End").get(this);
                list.add(new Schedule(day, dayStart, dayEnd));
            } catch (IllegalAccessException | NoSuchFieldException ignored) {
            }
        }
        return list;
    }

    public void addSchedule(String weekDay, String start, String end) {
        weekDay = weekDay.toLowerCase();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        try {
            this.getClass().getDeclaredField(weekDay+ "Start").set(this,LocalTime.parse(start,formatter));
            this.getClass().getDeclaredField(weekDay+ "End").set(this,LocalTime.parse(end,formatter));
        } catch (IllegalAccessException ignored) {
        }
        catch (NoSuchFieldException e){
            throw new IllegalArgumentException("No such day of week");
        }
    }
}
