package com.bekx.studenttestservice.dto;

public class StudentTestParticipationDto {
    private Long participationId;
    private String studentName;
    private String testName;

    public StudentTestParticipationDto(Long participationId, String studentName, String testName) {
        this.participationId = participationId;
        this.studentName = studentName;
        this.testName = testName;
    }

    public Long getParticipationId() {
        return participationId;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getTestName() {
        return testName;
    }
}
