package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.data.User;


@Repository
public interface UserRepository extends MongoRepository<User,String>{

	Optional<User> findByEmail(String email);
	 List<User> findByRole(String role);
	List<User> findAllById(List<String> studentIds);
	    Optional<User> findByUserName(String userName);
	    List<User> findByRoleOrderByRankAsc(String role);

	    @Query("{ '_id': { $in: ?0 } }")
	    List<User> findUsersByIds(List<String> ids);

	 
}
