package com.rhapps.show_of_hands.showofhands.model.PollModels;

public class UpDownVote {
    boolean upvoted;
    boolean downvoted;
    String username;

    public UpDownVote(boolean upvoted, boolean downvoted, String username) {
        this.upvoted = upvoted;
        this.downvoted = downvoted;
        this.username = username;
    }
    

    public boolean isUpvoted() {
        return upvoted;
    }

    public void setUpvoted(boolean upvoted) {
        this.upvoted = upvoted;
    }

    public boolean isDownvoted() {
        return downvoted;
    }

    public void setDownvoted(boolean downvoted) {
        this.downvoted = downvoted;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


}
