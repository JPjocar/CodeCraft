/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cosmos.CodeCraft.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "votes")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VoteEntity {
     
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = AnswerEntity.class)
    private AnswerEntity answerEntity;

    @ManyToOne(targetEntity = UserEntity.class)
    private UserEntity userEntity;

    //@ManyToOne(targetEntity = QuestionEntity.class)
    //private QuestionEntity questionEntity;
    
    private int point;
    
}
