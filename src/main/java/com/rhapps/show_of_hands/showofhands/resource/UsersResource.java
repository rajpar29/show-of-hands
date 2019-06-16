package com.rhapps.show_of_hands.showofhands.resource;

import com.mongodb.DuplicateKeyException;
import com.rhapps.show_of_hands.showofhands.model.Users;
import com.rhapps.show_of_hands.showofhands.repository.UsersRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("users")
public class UsersResource {

    private UsersRepository usersRepository;

    public UsersResource(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @GetMapping("allUsers")
    public List<Users> getAllUsers() {
        return usersRepository.findAll();
    }

    @PostMapping("createUser")
    public UserCreationResult createUser(@RequestParam String username, @RequestParam String password) {
        try {
            usersRepository.insert(new Users(username, password));
            return new UserCreationResult("User Created Successfully", true);
        } catch (DuplicateKeyException e) {
            return new UserCreationResult("Username already taken", true);
        }
    }
}

class UserCreationResult {
    private String responseMessage;
    private boolean isSuccessful;

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public void setSuccessful(boolean successful) {
        isSuccessful = successful;
    }

    UserCreationResult(String responseMessage, boolean isSuccessful) {
        this.responseMessage = responseMessage;
        this.isSuccessful = isSuccessful;
    }


}
