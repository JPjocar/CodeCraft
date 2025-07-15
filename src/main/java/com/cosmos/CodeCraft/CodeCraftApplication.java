package com.cosmos.CodeCraft;

import com.cosmos.CodeCraft.Entity.PermissionEntity;
import com.cosmos.CodeCraft.Entity.RoleEntity;
import com.cosmos.CodeCraft.Entity.UserEntity;
import com.cosmos.CodeCraft.Repository.UserRepository;
import java.util.List;
import java.util.Set;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class CodeCraftApplication {

	public static void main(String[] args) {
		SpringApplication.run(CodeCraftApplication.class, args);
	}


}
