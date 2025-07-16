package com.bekx.studenttestservice.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

@Entity
@Table(name = "tests")
public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "test", cascade = CascadeType.ALL, orphanRemoval = false)
    @JsonManagedReference
    private List<Question> questions;

    //getter&stter
    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public List<Question> getQuestions() {return questions;}
    public void setQuestions(List<Question> questions) {this.questions = questions;}
}
