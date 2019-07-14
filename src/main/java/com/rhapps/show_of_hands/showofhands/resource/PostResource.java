package com.rhapps.show_of_hands.showofhands.resource;


import com.mongodb.MongoException;
import com.rhapps.show_of_hands.showofhands.config.SecurityConfiguration;
import com.rhapps.show_of_hands.showofhands.model.PollModels.*;
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

@CrossOrigin(origins = "*",allowCredentials = "true")
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
    public List<Polls> getPolls() {
        return pollsRepository.findAll();
    }

    @PostMapping("/makeComment/{postId}")
    public Polls makeComment(@RequestParam("comment") String comment, @PathVariable("postId") String postId) {
        Polls poll = pollsRepository.findById(new ObjectId(postId)).orElseThrow(() -> new MongoException("No Found"));
        List<CommentModel> oldCommetns = poll.getComments();
        oldCommetns.add(new CommentModel(SecurityConfiguration.getUser(), comment));
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

    @PostMapping("chooseOption/{postId}")
    public Polls chooseOption(@PathVariable("postId") String postId,
                              @RequestParam("optionName") String optionName,
                              @RequestParam("optionid") String optionId){
        Polls poll = pollsRepository.findById(new ObjectId(postId)).orElseThrow(() -> new MongoException("No Found"));
       return findIfUserAlreadyChooseAnOption(poll, optionName, optionId);
    }

    public Polls findIfUserAlreadyChooseAnOption(Polls poll, String optionName, String optionId){
        List<PollOptionChoosen> optionVotedList = poll.getOptionChoosen();
        String currentUser = SecurityConfiguration.getUser();
        boolean isValidOption = false;

        for(PollOptions option : poll.getOptions()){
            if(option.optionName.equals(optionName) && option.optionid.equals(optionId)){
                isValidOption = true;
            }
        }
        if(!isValidOption){
            throw new RuntimeException("No Option Available");
        }
        for(int i = 0; i<optionVotedList.size(); i++ ){
            if(optionVotedList.get(i).getUsername().equals(currentUser)){
                List<PollOptions> pollOptions = poll.getOptions();
                optionVotedList.get(i).setOptionName(optionName);
                optionVotedList.get(i).setOptionId(optionId);
                poll.setOptionChoosen(optionVotedList);
                pollsRepository.save(poll);
                return poll;
            }
        }

        optionVotedList.add(new PollOptionChoosen(currentUser,optionName,optionId));
        poll.setOptionChoosen(optionVotedList);
        pollsRepository.save(poll);
        return poll;


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
                JSONObject optionObj = tempOptions.getJSONObject(i);
                tempOptionsList.add(new PollOptions(optionObj.get("optionName").toString(),0, optionObj.get("optionId").toString()));
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




