package com.feedback.feedback.system.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "feedback_comment")
public class FeedbackComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @OneToOne
    @JoinColumn(name = "session_id")
    private FeedbackSession session;

    @Lob
    private String rawText;

    @Lob
    private String nlpSummary;

    // Getters and setters
    public Long getCommentId() { return commentId; }
    public void setCommentId(Long commentId) { this.commentId = commentId; }

    public FeedbackSession getSession() { return session; }
    public void setSession(FeedbackSession session) { this.session = session; }

    public String getRawText() { return rawText; }
    public void setRawText(String rawText) { this.rawText = rawText; }

    public String getNlpSummary() { return nlpSummary; }
    public void setNlpSummary(String nlpSummary) { this.nlpSummary = nlpSummary; }
}
