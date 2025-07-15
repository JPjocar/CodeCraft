/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cosmos.CodeCraft.Entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "questions")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    
    @Column(unique = true)
    private String slug;
    
    @Column(columnDefinition = "TEXT")
    private String content;
    
    private int views;
    
    private int score;
    
    @CreationTimestamp
    @Column(columnDefinition = "TIMESTAMP", nullable = false, updatable = false)
    private LocalDateTime created_at;
    
    @UpdateTimestamp
    @Column(columnDefinition = "TIMESTAMP", nullable = false)
    private LocalDateTime updated_at;
    
    
    @OneToMany(targetEntity = AnswerEntity.class, cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy = "question", fetch = FetchType.LAZY)
    private List<AnswerEntity> answers = new ArrayList<>();
    
    @ManyToMany(targetEntity = TagEntity.class, fetch = FetchType.EAGER)
    @JoinTable(name = "questions_tags",
        joinColumns = @JoinColumn(name = "question_entity_id"),
        inverseJoinColumns = @JoinColumn(name = "tags_id"))
    private Set<TagEntity> Tags = new HashSet<>();
    
    
    @OneToMany(
            targetEntity = CommentEntity.class,
            fetch = FetchType.LAZY, mappedBy = "questionEntity",
            cascade = {CascadeType.REMOVE}
    )
    private List<CommentEntity> comments = new ArrayList<>();
    
    
    @ManyToOne(targetEntity = UserEntity.class)
    @JoinColumn(name = "user_id")
    private UserEntity user;
    
    
    
}
