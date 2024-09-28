package com.bloomtech.socialfeed.repositories;

import com.bloomtech.socialfeed.models.User;
import com.bloomtech.socialfeed.validators.UserInfoValidator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserRepository {
    private static final String USER_DATA_PATH = "src/resources/UserData.json";

    private static final UserInfoValidator userInfoValidator = new UserInfoValidator();

    public UserRepository() {
    }


    public List<User> getAllUsers() {
        List<User> allUsers = new ArrayList<>();
        Gson gson = new Gson();

        try {
            // Check if the file exists
            Reader reader = Files.newBufferedReader(Paths.get(USER_DATA_PATH));

            // Parse the JSON into an array of User objects
            User[] usersArray = gson.fromJson(reader, User[].class);

            if (usersArray != null) {
                allUsers = new ArrayList<>(Arrays.asList(usersArray));
            }

            reader.close();
        } catch (IOException e) {
            System.err.println("Failed to read user data: " + e.getMessage());
        }

        return allUsers;
    }

    public Optional<User> findByUsername(String username) {
        return getAllUsers()
                .stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst();
    }

    public void save(User user) {
        List<User> allUsers = getAllUsers();

        Optional<User> existingUser = allUsers.stream()
                .filter(u -> u.getUsername().equals(user.getUsername()))
                .findFirst();

        if (existingUser.isPresent()) {
            throw new RuntimeException("User with name: " + user.getUsername() + " already exists!");
        }
        allUsers.add(user);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (FileWriter writer = new FileWriter(USER_DATA_PATH)) {
            gson.toJson(allUsers, writer);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write user data: " + e.getMessage());
        }
    }
}

