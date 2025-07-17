package com.bekx.studenttestservice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "test_participations")
public class TestParticipation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "student_id")
    @JsonBackReference("student-participations")
    private Student student;

    @ManyToOne(optional = false)
    @JoinColumn(name = "test_id")
    @JsonBackReference("test-participations")
    private Test test;

    private LocalDateTime participatedAt = LocalDateTime.now();

    //getter&setter
    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
    public Student getStudent() {return student;}
    public void setStudent(Student student) {this.student = student;}
    public Test getTest() {return test;}
    public void setTest(Test test) {this.test = test;}
}
