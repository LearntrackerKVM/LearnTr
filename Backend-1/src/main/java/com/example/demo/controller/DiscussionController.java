package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.data.Discussion;
import com.example.demo.service.DiscussionService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/discussions")
@CrossOrigin("*")
public class DiscussionController {

    @Autowired
    private DiscussionService discussionService;

    @PostMapping("/save")
    public Discussion postDiscussion(@RequestBody Discussion discussion) {
        return discussionService.postDiscussion(discussion);
    }

    @PostMapping("/reply/{parentID}")
    public Discussion replyToDiscussion(@PathVariable String parentID, @RequestBody Discussion reply) {
        return discussionService.replyToDiscussion(parentID, reply);
    }

    @PostMapping("/like/{discussionID}")
    public ResponseEntity<?> likePost(@PathVariable String discussionID) {
        Integer likes = discussionService.likePost(discussionID);
        if (likes != null) {
            // Return the updated like count in the response, with a 200 OK status
            return ResponseEntity.ok().body(Map.of("likes", likes));
        } else {
            // Discussion not found, return a 404 Not Found status
            return ResponseEntity.notFound().build();
        }
    }
 
    
    @PostMapping("/dislike/{discussionID}")
    public ResponseEntity<?> dislikePost(@PathVariable String discussionID) {
        Integer dislikes = discussionService.dislikePost(discussionID);
        if (dislikes != null) {
            // Return the updated dislike count in the response, with a 200 OK status
            return ResponseEntity.ok().body(Map.of("dislikes", dislikes));
        } else {
            // Discussion not found, return a 404 Not Found status
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/course/{courseID}")
    public List<Discussion> getAllDiscussionsByCourseID(@PathVariable String courseID) {
        return discussionService.getAllDiscussionsByCourseID(courseID);
    }
}
