package com.cosmos.CodeCraft;


import com.cosmos.CodeCraft.Entity.TagEntity;
import com.cosmos.CodeCraft.Repository.TagRepository;
import java.time.LocalDateTime;
import java.util.Date;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CodeCraftApplication {

	public static void main(String[] args) {
		SpringApplication.run(CodeCraftApplication.class, args);
	}
        
        
//        @Bean
//        CommandLineRunner iniciar(TagRepository tagRepository){
//            return (args) -> {
//                TagEntity java = new TagEntity(1l, "java", "description java", LocalDateTime.now(), LocalDateTime.now());
//                tagRepository.save(java);
//                
//            };
//        }
}
