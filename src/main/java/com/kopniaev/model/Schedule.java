package com.kopniaev.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Schedule {
    private String weekDay;
    private LocalTime begin;
    private LocalTime end;
}
