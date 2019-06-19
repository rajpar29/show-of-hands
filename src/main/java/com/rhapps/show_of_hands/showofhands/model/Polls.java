package com.rhapps.show_of_hands.showofhands.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "posts")
public class Polls {
    @Id
    private ObjectId _id;
    private String userId;
    private String title;
    private String description;
    private int upvotes;
    private int downvotes ;
    private List<String> comments = new ArrayList<>();
    private List<PollOptions> options ;
    private String imageUrl ;
    private List<String> categories ;
    private String username;

    public Polls(String username,String userId, String title, String description, List<PollOptions> options, String imageUrl, List<String> categories) {
        this.username = username;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.upvotes = 0;
        this.downvotes = 0;
        this.options = options;
        this.imageUrl = imageUrl;
        this.categories = categories;
    }

}



