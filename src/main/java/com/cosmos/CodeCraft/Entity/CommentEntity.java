/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cosmos.CodeCraft.Entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "comments")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(columnDefinition = "TEXT")
    private String content;
    
    private String post_type;
    
    @ManyToOne(targetEntity = UserEntity.class)
    @JoinColumn(name = "user_id")
    private UserEntity user;
    
    @ManyToOne(targetEntity = AnswerEntity.class)
    @JoinColumn(name = "answer_entity_id")
    private AnswerEntity answerEntity;
    
    @ManyToOne(targetEntity = QuestionEntity.class)
    @JoinColumn(name = "question_entity_id")
    private QuestionEntity questionEntity;
    
    
    @CreationTimestamp
    @Column(columnDefinition = "TIMESTAMP", updatable = false, nullable = false)
    private LocalDateTime created_at;
    
    @UpdateTimestamp
    @Column(columnDefinition = "TIMESTAMP", nullable = false)
    private LocalDateTime updated_at;
    
}
