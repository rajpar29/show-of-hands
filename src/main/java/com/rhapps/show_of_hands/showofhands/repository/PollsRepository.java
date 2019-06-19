package com.rhapps.show_of_hands.showofhands.repository;

import com.rhapps.show_of_hands.showofhands.model.Polls;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PollsRepository extends MongoRepository<Polls, ObjectId> {

}
