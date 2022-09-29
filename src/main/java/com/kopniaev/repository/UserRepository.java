package com.kopniaev.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kopniaev.model.User;
import com.kopniaev.util.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class UserRepository implements ConnectServer {
    public User getUserByLoginAndPassword(String login, String password) throws IOException {
        try (BufferedReader reader = connect(Constants.SERVER_NAME
                        + "/user?login=" + URLEncoder.encode(login, StandardCharsets.UTF_8)
                        + "&password=" + URLEncoder.encode(password, StandardCharsets.UTF_8),
                "POST", null)) {
            return new ObjectMapper().readValue(reader, User.class);
        }
    }

    public User addUser(User user) throws IOException {
        try (BufferedReader reader = connect(Constants.SERVER_NAME + "/reg",
                "POST", new ObjectMapper().writeValueAsString(user))) {
            return new ObjectMapper().readValue(reader, User.class);
        }
    }

    public User deleteUserById(long id) throws IOException {
        try (BufferedReader reader = connect(Constants.SERVER_NAME + "/user?id=" + id,
                "DELETE", null)) {
            return new ObjectMapper().readValue(reader, User.class);
        }
    }
}
