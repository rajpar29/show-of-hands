package com.rhapps.show_of_hands.showofhands.model.Usermodels;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Document(collection = "users")
public class Users{

    @Id
    private ObjectId _id;
    private String username;
    private String password;

    public Users(){}
    public Users(Users users) {
        this.password = users.password;
        this._id = users._id;
        this.username = users.username;
    }

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Users(String username, String password) {
        this.username = username;
        this.password = new BCryptPasswordEncoder().encode(password);

    }


}
