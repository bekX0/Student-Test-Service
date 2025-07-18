package com.bekx.studenttestservice.dto;

public class AnswerSubmissionDto {
    private Long studentId;
    private Long questionId;
    private Long answerId;
    private Long participationId;

    public AnswerSubmissionDto(Long studentId, Long questionId, Long answerId, Long participationId) {
        this.studentId = studentId;
        this.questionId = questionId;
        this.answerId = answerId;
        this.participationId = participationId;
    }

    // getter&setter
    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public Long getQuestionId() { return questionId; }
    public void setQuestionId(Long questionId) { this.questionId = questionId; }

    public Long getAnswerId() { return answerId; }
    public void setAnswerId(Long answerId) { this.answerId = answerId; }

    public Long getParticipationId() { return participationId; }
    public void setParticipationId(Long participationId) { this.participationId = participationId; }
}
