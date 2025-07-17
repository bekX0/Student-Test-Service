package com.bekx.studenttestservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Table(name = "student_answers")
public class StudentAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Student student;

    @ManyToOne(optional = false)
    private Question question;

    @ManyToOne(optional = false)
    private Answer selectedAnswer;

    @ManyToOne(optional = false)
    private TestParticipation participation;

    public boolean isCorrect() {
        return selectedAnswer != null && selectedAnswer.isCorrect();
    }

    // getter&setter
    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
    public Student getStudent() {return student;}
    public void setStudent(Student student) {this.student = student;}
    public Question getQuestion() {return question;}
    public void setQuestion(Question question) {this.question = question;}
    public Answer getSelectedAnswer() {return selectedAnswer;}
    public void setSelectedAnswer(Answer selectedAnswer) {this.selectedAnswer = selectedAnswer;}
    public TestParticipation getParticipation() {return participation;}
    public void setParticipation(TestParticipation participation) {this.participation = participation;}
}

