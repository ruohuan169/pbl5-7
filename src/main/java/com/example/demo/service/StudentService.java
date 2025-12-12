package com.example.demo.service;

import com.example.demo.entity.Student;
import com.example.demo.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class StudentService extends BaseService<Student, Long, StudentRepository> {
    
    @Autowired
    public StudentService(StudentRepository repository) {
        super(repository);
        this.repository = repository;
    }
    
    public Page<Student> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }
    
    public Student findByStudentNumber(String studentNumber) {
        return repository.findByStudentNumber(studentNumber);
    }
}