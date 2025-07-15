/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cosmos.CodeCraft.Repository;

import com.cosmos.CodeCraft.Entity.AnswerEntity;
import java.util.List;
import java.util.Optional;

import com.cosmos.CodeCraft.Entity.QuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerRepository extends JpaRepository<AnswerEntity, Long>{
    @Query("SELECT a FROM AnswerEntity a WHERE a.question = :question AND a.is_correct = true AND a.id != :id")
    public List<AnswerEntity> findByQuestionAndIsCorrectTrueAndIdNot(@Param("question") QuestionEntity question, @Param("id") Long id);
    
}
