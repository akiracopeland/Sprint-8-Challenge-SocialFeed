package com.bloomtech.socialfeed.repositories;

import com.bloomtech.socialfeed.helpers.LocalDateTimeAdapter;
import com.bloomtech.socialfeed.models.Post;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PostRepository {
    private static final String POST_DATA_PATH = "src/resources/PostData.json";

    public PostRepository() {
    }

    private Gson getGsonWithLocalDateTimeAdapter() {
        return new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .setPrettyPrinting()
                .create();
    }

    public List<Post> getAllPosts() {
        List<Post> allPosts = new ArrayList<>();
        Gson gson = getGsonWithLocalDateTimeAdapter();

        try {
            Reader reader = Files.newBufferedReader(Paths.get(POST_DATA_PATH));

            // Convert JSON back to list of posts
            Post[] postsArray = gson.fromJson(reader, Post[].class);
            if (postsArray != null) {
                allPosts = new ArrayList<>(Arrays.asList(postsArray));
            }

            reader.close();
        } catch (Exception e) {
            System.err.println("Failed to read post data: " + e.getMessage());
        }

        return allPosts;
    }

    public List<Post> findByUsername(String username) {
        return getAllPosts()
                .stream()
                .filter(p -> p.getUsername().equals(username))
                .collect(Collectors.toList());
    }

    public List<Post> addPost(Post post) {
        List<Post> allPosts = getAllPosts();

        // Add new post to the list
        allPosts.add(post);

        // Write updated list of posts back to the JSON file
        Gson gson = getGsonWithLocalDateTimeAdapter();

        try (FileWriter writer = new FileWriter(POST_DATA_PATH)) {
            gson.toJson(allPosts, writer);
        } catch (Exception e) {
            System.err.println("Failed to write post data: " + e.getMessage());
        }

        // Return updated list of posts
        return allPosts;
    }
}
