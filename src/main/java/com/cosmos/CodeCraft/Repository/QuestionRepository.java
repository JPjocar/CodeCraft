/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cosmos.CodeCraft.Repository;

import com.cosmos.CodeCraft.Entity.QuestionEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<QuestionEntity, Long>{
    
    @Query("SELECT u FROM QuestionEntity u WHERE u.id = :question_id AND u.user.username = :username")
    public Optional<QuestionEntity> findByIdAndOwner(@Param("question_id") Long question_id, @Param("username") String username);
    
    public Optional<QuestionEntity> findQuestionEntityBySlug(String slug);
}
