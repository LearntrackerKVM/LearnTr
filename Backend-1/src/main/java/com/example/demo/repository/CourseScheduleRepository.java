

package com.example.demo.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.data.Courseschedule;

@Repository
public interface CourseScheduleRepository extends CrudRepository<Courseschedule,Integer> {

	   List<Courseschedule> findByCourseId(String courseId); 
	   List<Courseschedule> findByCourseIdIn(List<String> courseIds);
}
