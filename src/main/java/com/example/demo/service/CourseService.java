package com.example.demo.service;

import com.example.demo.entity.Course;
import com.example.demo.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CourseService extends BaseService<Course, Long, CourseRepository> {
    
    @Autowired
    public CourseService(CourseRepository repository) {
        super(repository);
        this.repository = repository;
    }
    
    public Page<Course> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }
    
    public Course findByCourseCode(String courseCode) {
        return repository.findByCourseCode(courseCode);
    }
}