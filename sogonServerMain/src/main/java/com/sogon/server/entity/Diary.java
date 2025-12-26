package com.sogon.server.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name="diaries")
@Data
public class Diary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="diary_id")
    private Long diaryId;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    @Column(columnDefinition="TEXT", nullable=false)
    private String content;

    // private String embedding;

    private String sentiment;

    @Column(name="written_date", nullable=false) 
    private LocalDate writtenDate;

    @Column(name="created_at", updatable=false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}