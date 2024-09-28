package com.bloomtech.socialfeed.observerpattern;

import com.bloomtech.socialfeed.App;
import com.bloomtech.socialfeed.models.Post;
import com.bloomtech.socialfeed.models.User;
import com.bloomtech.socialfeed.repositories.PostRepository;
import com.bloomtech.socialfeed.repositories.UserRepository;
import com.bloomtech.socialfeed.services.UserService;

import java.sql.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SourceFeed implements Source {
    private final PostRepository postRepository = new PostRepository();
    private final UserRepository userRepository = new UserRepository();
    private final UserService userService = new UserService(userRepository);

    private List<Post> posts;

    private List<Observer> observers;

    public SourceFeed() {
        this.observers = new ArrayList<>();
        this.posts = new ArrayList<>();
    }

    public void getAllPosts() {
        postRepository.getAllPosts();
    }

    public Post addPost(User user, String body) {
        Post post = new Post(user.getUsername(),
                LocalDateTime.now(),
                body);
        this.posts = postRepository.addPost(post);

//        if (!userRepository.getAllUsers().contains(user)) {
//            userService.save(user);
//        }

        List<String> followerNames = user.getFollowing();

        List<User> followers = new ArrayList<>();

        for (String followerName : followerNames) {
            followers.add(userService.getUserByUsername(followerName));
        }

        for (User follower : followers) {
            OUserFeed userFeed = new OUserFeed(follower);

            this.attach(userFeed);
        }

        this.updateAll();

        return post;
    }

    public List<Observer> getObservers() {
        return observers;
    }

    public List<Post> getPosts() {
        return posts;
    }


    @Override
    public void attach(Observer o) {
        observers.add(o);
    }

    @Override
    public void detach(Observer o) {
        observers.remove(o);
    }

    @Override
    public void updateAll() {
        for (Observer o : observers) {
            o.update();
        }
    }
}
