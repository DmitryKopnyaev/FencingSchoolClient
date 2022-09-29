package com.kopniaev.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class User {
    private long id;

    @NonNull
    private String login;

    @NonNull
    private String password;

    @NonNull
    private String name;

    @NonNull
    private Date regDate;
}
