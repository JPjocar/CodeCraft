package com.cosmos.CodeCraft;

import com.cosmos.CodeCraft.Controller.QuestionController;
import com.cosmos.CodeCraft.Entity.AnswerEntity;
import com.cosmos.CodeCraft.Entity.QuestionEntity;
import com.cosmos.CodeCraft.Repository.QuestionRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CodeCraftApplication {

	public static void main(String[] args) {
		SpringApplication.run(CodeCraftApplication.class, args);
	}
        
        

}
