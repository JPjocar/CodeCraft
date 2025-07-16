package com.cosmos.CodeCraft.Repository;

import com.cosmos.CodeCraft.Entity.AnswerEntity;
import com.cosmos.CodeCraft.Entity.UserEntity;
import com.cosmos.CodeCraft.Entity.VoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<VoteEntity, Long> {
    public Optional<VoteEntity> findByUserEntityAndAnswerEntity(UserEntity userEntity, AnswerEntity answerEntity);
    public List<VoteEntity> findByUserEntity_UsernameAndAnswerEntity_Id(String username, Long answerId);
}
