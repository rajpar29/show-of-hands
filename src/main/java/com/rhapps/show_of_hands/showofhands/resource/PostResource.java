package com.rhapps.show_of_hands.showofhands.resource;


import com.mongodb.MongoException;
import com.rhapps.show_of_hands.showofhands.config.SecurityConfiguration;
import com.rhapps.show_of_hands.showofhands.model.PollModels.CommentModel;
import com.rhapps.show_of_hands.showofhands.model.PollModels.PollOptions;
import com.rhapps.show_of_hands.showofhands.model.PollModels.Polls;
import com.rhapps.show_of_hands.showofhands.model.PollModels.UpDownVote;
import com.rhapps.show_of_hands.showofhands.model.Usermodels.CustomUserDetails;
import com.rhapps.show_of_hands.showofhands.model.Usermodels.Users;
import com.rhapps.show_of_hands.showofhands.repository.PollsRepository;
import com.rhapps.show_of_hands.showofhands.repository.UsersRepository;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/polls")
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
    }

    @GetMapping("/allPolls")
    public List<Polls> getPolls(){
        return pollsRepository.findAll();
    }

    @PostMapping("/makeComment/{postId}")
    public Polls makeComment(@RequestParam("comment") String comment, @PathVariable("postId") String postId) {
        Polls poll = pollsRepository.findById(new ObjectId(postId)).orElseThrow(() -> new MongoException("No Found"));
        List<CommentModel> oldCommetns = poll.getComments();
        oldCommetns.add(new CommentModel(comment, SecurityConfiguration.getUser()));
        poll.setComments(oldCommetns);
        pollsRepository.save(poll);
        return poll;
    }

    @GetMapping("/upvote/{postId}")
    public Polls upvote(@PathVariable("postId") String postId) {
        Polls poll = pollsRepository.findById(new ObjectId(postId)).orElseThrow(() -> new MongoException("No Found"));
        return findIfUserUpvotedOrDownvoted(poll, true);
    }

    @GetMapping("/downvote/{postId}")
    public Polls downvote(@PathVariable("postId") String postId) {
        Polls poll = pollsRepository.findById(new ObjectId(postId)).orElseThrow(() -> new MongoException("No Found"));
        return findIfUserUpvotedOrDownvoted(poll, false);
    }

    public Polls findIfUserUpvotedOrDownvoted(Polls poll, boolean isUpvote) {
        List<UpDownVote> upDownVotesList = poll.getUpvoteOrDownvotedBy();
        for (int i = 0; i < upDownVotesList.size(); i++) {
            if (upDownVotesList.get(i).getUsername().equals(SecurityConfiguration.getUser())) {
                if (isUpvote) {
                    if (upDownVotesList.get(i).isUpvoted()) {
                        poll.setUpvotes(poll.getUpvotes() - 1);
                        upDownVotesList.remove(i);
                    } else {
                        poll.setDownvotes(poll.getDownvotes() - 1);
                        poll.setUpvotes(poll.getUpvotes() + 1);
                        upDownVotesList.set(i, new UpDownVote(isUpvote, !isUpvote, SecurityConfiguration.getUser()));


                    }
                } else {
                    if (upDownVotesList.get(i).isDownvoted()) {
                        poll.setDownvotes(poll.getDownvotes() - 1);
                        System.out.println(upDownVotesList.toString());
                        System.out.println(upDownVotesList.get(i));
                        upDownVotesList.remove(i);
                    } else {
                        poll.setUpvotes(poll.getUpvotes() - 1);
                        poll.setDownvotes(poll.getDownvotes() + 1);
                        upDownVotesList.set(i, new UpDownVote(isUpvote, !isUpvote, SecurityConfiguration.getUser()));
                    }
                }
                poll.setUpvoteOrDownvotedBy(upDownVotesList);
                pollsRepository.save(poll);
                return poll;

            }
        }
        upDownVotesList.add(new UpDownVote(isUpvote, !isUpvote, SecurityConfiguration.getUser()));
        if (isUpvote) {
            poll.setUpvotes(poll.getUpvotes() + 1);
        } else {
            poll.setDownvotes(poll.getDownvotes() + 1);
        }
        poll.setUpvoteOrDownvotedBy(upDownVotesList);
        pollsRepository.save(poll);
        return poll;
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




