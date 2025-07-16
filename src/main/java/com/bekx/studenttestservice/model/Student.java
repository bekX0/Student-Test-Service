package com.bekx.studenttestservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

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

    //getter &setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getNumber() { return number; }
    public void setNumber(String number) { this.number = number; }
}
