package com.bekx.studenttestservice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "answers")
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Cevap içeriği boş bırakılamaz!")
    private String content;

    private boolean isCorrect;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    @JsonBackReference
    private Question question;

    // getter&setter
    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
    public String getContent() {return content;}
    public void setContent(String content) {this.content = content;}
    public boolean isCorrect() {return isCorrect;}
    public void setCorrect(boolean isCorrect) {this.isCorrect = isCorrect;}
    public Question getQuestion() {return question;}
    public void setQuestion(Question question) {this.question = question;}
}
