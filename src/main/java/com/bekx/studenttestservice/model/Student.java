package com.bekx.studenttestservice.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "İsim boş olamaz!")
    @Column(nullable = false)
    private String firstName;

    @NotBlank(message = "Soyisim boş olamaz!")
    @Column(nullable = false)
    private String lastName;

    @NotBlank(message = "Öğrenci numaraso boş olamaz!")
    @Column(nullable = false)
    private String number;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    @JsonManagedReference("student-participations")
    private List<TestParticipation> participations;

    //getter &setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getNumber() { return number; }
    public void setNumber(String number) { this.number = number; }
    public List<TestParticipation> getParticipations() { return participations; }
    public void setParticipations(List<TestParticipation> participations) {this.participations = participations;}
}
