package com.sogon.server.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "diary_entries")
public class Diary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    // 사용자가 지정한 날짜 (DTO의 date와 매핑)
    @Column(name = "diary_date", nullable = false)
    private LocalDate diaryDate;

    // 감정 상태 (DTO의 sentiment와 매핑)
    private String sentiment;

    // AI 벡터 데이터
    @Column(columnDefinition = "vector")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<Double> embedding;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // 내용, 날짜, 감정, 벡터 
    public void update(String content, LocalDate diaryDate, String sentiment, List<Double> embedding) {
        this.content = content;
        this.diaryDate = diaryDate;
        this.sentiment = sentiment;
        this.embedding = embedding;
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}