package com.rhapps.show_of_hands.showofhands.repository;

import com.rhapps.show_of_hands.showofhands.model.PollModels.Polls;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PollsRepository extends MongoRepository<Polls, ObjectId> {
    public List<Polls> findAllByOrderByUpvotesDesc();

    public List<Polls> findAllByOrderByPollMetaAsc();
}
