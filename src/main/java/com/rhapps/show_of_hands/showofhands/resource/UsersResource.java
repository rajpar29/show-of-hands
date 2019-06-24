package com.rhapps.show_of_hands.showofhands.resource;

import com.rhapps.show_of_hands.showofhands.model.Usermodels.CustomUserDetails;
import com.rhapps.show_of_hands.showofhands.model.Usermodels.Users;
import com.rhapps.show_of_hands.showofhands.repository.UsersRepository;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UsersResource {

    private UsersRepository usersRepository;
    private AuthenticationManager authenticationManager;

    public UsersResource(UsersRepository usersRepository, AuthenticationManager authenticationManager) {
        this.usersRepository = usersRepository;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/allUsers")
    public List<Users> getAllUsers() {
        return usersRepository.findAll();
    }

    @PostMapping("/createUser")
    public UserCreationResult createUser(@RequestParam String username, @RequestParam String password) {
        try {
            usersRepository.insert(new Users(username, password));
            return new UserCreationResult("User Created Successfully", true);
        } catch (DuplicateKeyException e) {
            return new UserCreationResult("Username already taken", false);
        }
    }

    @GetMapping("/getUserId/{username}")
    public String getUserId(@PathVariable("username") String username) {
        return findUserId(username);

    }

    @PostMapping("/userLogin")
    public void userSignIn(@RequestParam("username") String username, @RequestParam("password") String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        Authentication auth = authenticationManager.authenticate(authenticationToken);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(auth);
    }

    public String findUserId(String username) {
        Optional<Users> user = usersRepository.findByUsername(username);
        user.orElseThrow(() -> new UsernameNotFoundException("USername not found"));
        return user.map(CustomUserDetails::new).get().get_id().toString();
    }

}


class UserCreationResult {
    private String responseMessage;
    private boolean isSuccessful;

    UserCreationResult(String responseMessage, boolean isSuccessful) {
        this.responseMessage = responseMessage;
        this.isSuccessful = isSuccessful;
    }

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


}
