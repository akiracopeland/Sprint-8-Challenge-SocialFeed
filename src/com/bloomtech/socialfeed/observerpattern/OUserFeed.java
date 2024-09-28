package com.bloomtech.socialfeed.observerpattern;

import com.bloomtech.socialfeed.App;
import com.bloomtech.socialfeed.models.Post;
import com.bloomtech.socialfeed.models.User;

import java.util.ArrayList;
import java.util.List;

public class OUserFeed implements Observer {
    private User user;
    private List<Post> feed;



    public OUserFeed(User user) {
        this.user = user;
        this.feed = new ArrayList<>();
    }

    public User getUser() {
        return user;
    }

    public List<Post> getFeed() {
        return feed;
    }

    @Override
    public void update() {

        SourceFeed sourceFeed = App.sourceFeed;

        this.feed.clear();

        List<Post> allPosts = sourceFeed.getPosts();

        for (Post post : allPosts) {
            if (this.user.getFollowing().contains(post.getUsername())) {
                this.feed.add(post);
            }
        }

    }

}
