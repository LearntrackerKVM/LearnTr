package com.example.demo.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.data.FriendRequest;


@Repository
public interface FriendRequestRepository extends MongoRepository<FriendRequest, String> {
    List<FriendRequest> findByRecipientIdAndStatus(String recipientId, String status);
}

