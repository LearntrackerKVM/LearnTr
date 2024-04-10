package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.data.Discussion;
import com.example.demo.repository.DiscussionRepository;

import java.util.List;
import java.util.Optional;

@Service
public class DiscussionService {

    @Autowired
    private DiscussionRepository discussionRepository;

    
    public Discussion postDiscussion(Discussion discussion) {
        // Save the discussion to the database
        return discussionRepository.save(discussion);
    }

    
    public Discussion replyToDiscussion(String parentID, Discussion reply) {
        // Set the parentID for the reply and save
        reply.setParentID(parentID);
        return discussionRepository.save(reply);
    }

    public Integer likePost(String discussionID) { 
        Optional<Discussion> discussionOptional = discussionRepository.findById(discussionID);
        if (discussionOptional.isPresent()) {
            Discussion discussion = discussionOptional.get();
            discussion.setLikes(discussion.getLikes() + 1);
            discussionRepository.save(discussion);
            return discussion.getLikes(); // Return the updated likes count
        } else {
            // Discussion not found. Handle accordingly, maybe return null or a specific value indicating not found.
            return null; // Or consider throwing an exception or another indicative value
        }
    }


    public Integer dislikePost(String discussionID) {
        Optional<Discussion> discussionOptional = discussionRepository.findById(discussionID);
        if (discussionOptional.isPresent()) {
            Discussion discussion = discussionOptional.get();
            discussion.setDislikes(discussion.getDislikes() + 1);
            discussionRepository.save(discussion);
            return discussion.getDislikes(); // Return the updated dislikes count
        } else {
            // Discussion not found. Handle accordingly, maybe return null or a specific value indicating not found.
            return null; // Or consider throwing an exception or another indicative value
        }
    }


    
    public List<Discussion> getAllDiscussionsByCourseID(String courseID) {
        return discussionRepository.findByCourseID(courseID);
    }
}
