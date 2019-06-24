package com.rhapps.show_of_hands.showofhands.model.PollModels;

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
    private List<CommentModel> comments = new ArrayList<>();
    private List<PollOptions> options ;
    private String imageUrl ;
    private List<String> categories ;
    private String username;
    private List<UpDownVote> upvoteOrDownvotedBy = new ArrayList<>();


    public List<UpDownVote> getUpvoteOrDownvotedBy() {
        return upvoteOrDownvotedBy;
    }

    public void setUpvoteOrDownvotedBy(List<UpDownVote> upvoteOrDownvotedBy) {
        this.upvoteOrDownvotedBy = upvoteOrDownvotedBy;
    }


    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getUpvotes() {
        return upvotes;
    }

    public void setUpvotes(int upvotes) {
        this.upvotes = upvotes;
    }

    public int getDownvotes() {
        return downvotes;
    }

    public void setDownvotes(int downvotes) {
        this.downvotes = downvotes;
    }

    public List<CommentModel> getComments() {
        return comments;
    }

    public void setComments(List<CommentModel> comments) {
        this.comments = comments;
    }

    public List<PollOptions> getOptions() {
        return options;
    }

    public void setOptions(List<PollOptions> options) {
        this.options = options;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Polls(String username, String userId, String title, String description, List<PollOptions> options, String imageUrl, List<String> categories) {
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



