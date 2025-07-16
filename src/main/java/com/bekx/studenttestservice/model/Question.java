package com.bekx.studenttestservice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Table(name = "questions")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Sorunun içeriği boş bırakılamaz!")
    private String content;

    @ManyToOne
    @JoinColumn(name = "test_id", nullable = false)
    @JsonBackReference
    private Test test;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    @JsonManagedReference
    @Valid
    private List<Answer> answers;

    // tek doğru cevap
    @AssertTrue(message = "Her soruda yalnızca bir doğru cevap olmalıdır!")
    public boolean hasOnlyOneCorrectAnswer() {
        if (answers == null) return false;
        long correctCount = answers.stream().filter(Answer::isCorrect).count();
        return correctCount == 1;
    }

    // getter&setter
    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
    public String getContent() {return content;}
    public void setContent(String content) {this.content = content;}
    public Test getTest() {return test;}
    public void setTest(Test test) {this.test = test;}
    public List<Answer> getAnswers() {return answers;}
    public void setAnswers(List<Answer> answers) {this.answers = answers;}
}

