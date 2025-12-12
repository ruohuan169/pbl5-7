package com.example.demo.entity;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "grades")
public class Grade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_id", nullable = false)
    private Long studentId;

    @Column(name = "course_id", nullable = false)
    private Long courseId;

    @Column(precision = 5, scale = 2)
    private BigDecimal score;

    @Column(name = "grade_point", precision = 3, scale = 2)
    private BigDecimal gradePoint;

    @Column(name = "letter_grade")
    private String letterGrade;

    @Column(name = "exam_date")
    private java.sql.Date examDate;

    private String semester;

    @Column(name = "academic_year")
    private String academicYear;

    @Column(name = "created_at")
    private java.sql.Timestamp createdAt;

    // Constructors
    public Grade() {}

    public Grade(Long studentId, Long courseId) {
        this.studentId = studentId;
        this.courseId = courseId;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public BigDecimal getGradePoint() {
        return gradePoint;
    }

    public void setGradePoint(BigDecimal gradePoint) {
        this.gradePoint = gradePoint;
    }

    public String getLetterGrade() {
        return letterGrade;
    }

    public void setLetterGrade(String letterGrade) {
        this.letterGrade = letterGrade;
    }

    public java.sql.Date getExamDate() {
        return examDate;
    }

    public void setExamDate(java.sql.Date examDate) {
        this.examDate = examDate;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
    }

    public java.sql.Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(java.sql.Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}