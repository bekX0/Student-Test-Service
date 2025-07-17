package com.bekx.studenttestservice.dto;

public class TestParticipationDto {
    private Long id;
    private String testName;

    public TestParticipationDto(Long id, String testName) {
        this.id = id;
        this.testName = testName;
    }

    public Long getId() { return id; }
    public String getTestName() { return testName; }
}
