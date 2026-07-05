package com.feedback.feedback.system.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "feedback_answer")
public class FeedbackAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long answerId;

    @ManyToOne
    @JoinColumn(name = "session_id")
    private FeedbackSession session;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    private int rating;

    private String answerText;

    // Getters and setters
    public Long getAnswerId() { return answerId; }
    public void setAnswerId(Long answerId) { this.answerId = answerId; }

    public FeedbackSession getSession() { return session; }
    public void setSession(FeedbackSession session) { this.session = session; }

    public Question getQuestion() { return question; }
    public void setQuestion(Question question) { this.question = question; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getAnswerText() { return answerText; }
    public void setAnswerText(String answerText) { this.answerText = answerText; }
}

