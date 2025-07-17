package com.bekx.studenttestservice.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

@Entity
@Table(name = "tests")
public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Test için bir isim girmelisin!")
    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TestType type;

    @OneToMany(mappedBy = "test", cascade = CascadeType.ALL, orphanRemoval = false)
    @JsonManagedReference
    @Valid
    private List<Question> questions;

    @OneToMany(mappedBy = "test", cascade = CascadeType.ALL)
    @JsonManagedReference("test-participations")
    private List<TestParticipation> participations;

    //getter&stter
    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public List<Question> getQuestions() {return questions;}
    public void setQuestions(List<Question> questions) {this.questions = questions;}
    public List<TestParticipation> getParticipations() {return participations;}
    public void setParticipations(List<TestParticipation> participations) {this.participations = participations;}
    public TestType getType() {return type;}
    public void setType(TestType type) {this.type = type;}
}
