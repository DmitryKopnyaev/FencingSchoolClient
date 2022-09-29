package com.kopniaev.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Apprentice {
    private long id;

    @NonNull
    private String surname;

    @NonNull
    private String name;

    @NonNull
    private String patronymic;

    @NonNull
    private String phoneNumber;

    @ToString.Exclude
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Training> trainings;

    @Override
    public String toString() {
        return "Фамилия: " + this.surname
                + ", Имя: " + this.name
                + ", Отчество: " + this.patronymic
                + ", Номер телефона: " + this.phoneNumber;
    }
}
