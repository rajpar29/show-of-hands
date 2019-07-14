package com.rhapps.show_of_hands.showofhands.model.PollModels;

public class PollOptionChoosen {
    String username;
    String optionName;
    String optionId;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public String getOptionId() {
        return optionId;
    }

    public void setOptionId(String optionId) {
        this.optionId = optionId;
    }

    public PollOptionChoosen(String username, String optionName, String optionId) {
        this.username = username;
        this.optionName = optionName;
        this.optionId = optionId;
    }
}
