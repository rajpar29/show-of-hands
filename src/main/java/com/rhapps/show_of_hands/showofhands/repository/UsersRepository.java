package com.rhapps.show_of_hands.showofhands.repository;

import com.rhapps.show_of_hands.showofhands.model.Usermodels.Users;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UsersRepository extends MongoRepository<Users, ObjectId> {
    Optional<Users> findByUsername(String username);
}
