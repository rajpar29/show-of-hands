package com.rhapps.show_of_hands.showofhands.resource;


import com.rhapps.show_of_hands.showofhands.config.SecurityConfiguration;
import com.rhapps.show_of_hands.showofhands.model.CustomUserDetails;
import com.rhapps.show_of_hands.showofhands.model.PollOptions;
import com.rhapps.show_of_hands.showofhands.model.Polls;
import com.rhapps.show_of_hands.showofhands.model.Users;
import com.rhapps.show_of_hands.showofhands.repository.PollsRepository;
import com.rhapps.show_of_hands.showofhands.repository.UsersRepository;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/posts")
public class PostResource {
    private PollsRepository pollsRepository;
    private UsersRepository usersRepository;


    public PostResource(PollsRepository pollsRepository, UsersRepository usersRepository) {
        this.pollsRepository = pollsRepository;
        this.usersRepository = usersRepository;
    }


    @PostMapping("/createPost")
    public void createPost(@RequestParam("title") String title,
                           @RequestParam("description") String description,
                           @RequestParam("imageUrl") String imageUrl,
                           @RequestParam("options") String optionString,
                           @RequestParam("categories") String categoriesString) {
        String username = SecurityConfiguration.getUser();
        List<PollOptions> options = fetchOptions(optionString);
        List<String> categories = fetchCategories(categoriesString);


        Polls poll = new Polls(
                username,
                findUserId(username),
                title,
                description,
                options,
                imageUrl,
                categories
        );
          pollsRepository.insert(poll);
        System.out.println();
    }


    private String findUserId(String username) {
        Optional<Users> user = usersRepository.findByUsername(username);
        user.orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        return user.map(CustomUserDetails::new).get().get_id().toString();
    }

    private ArrayList<PollOptions> fetchOptions(String optionString) {
        try {
            ArrayList<PollOptions> tempOptionsList = new ArrayList<>();
            JSONObject obj = new JSONObject(optionString);
            JSONArray tempOptions = obj.getJSONArray("options");
            for (int i = 0; i < tempOptions.length(); i++) {
                tempOptionsList.add(new PollOptions(tempOptions.getString(i), 0));
            }
            return tempOptionsList;
        } catch (JSONException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }


    private ArrayList<String> fetchCategories(String categoryString) {
        try {
            ArrayList<String> tempCategoriesList = new ArrayList<>();
            JSONObject obj = new JSONObject(categoryString);
            JSONArray tempCategories = obj.getJSONArray("categories");
            for (int i = 0; i < tempCategories.length(); i++) {
                tempCategoriesList.add(tempCategories.getString(i));
            }
            return tempCategoriesList;
        } catch (JSONException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }


}


