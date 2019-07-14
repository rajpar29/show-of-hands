package com.rhapps.show_of_hands.showofhands.model.PollModels;

public class PollOptions{
    public String optionName;
    public int votes;
    public String optionid;

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public String getOptionid() {
        return optionid;
    }

    public void setOptionid(String optionid) {
        this.optionid = optionid;
    }

    public PollOptions(String optionName, int votes, String optionid) {
        this.optionName = optionName;
        this.votes = votes;
        this.optionid = optionid;
    }
}