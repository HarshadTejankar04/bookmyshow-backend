package com.cfs.bms;

import com.cfs.bms.model.User;
import com.cfs.bms.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class BmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(BmsApplication.class, args);
    }

    @Bean
    public CommandLineRunner seedAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findByEmail("admin@bms.com").isEmpty()) {
                User admin = new User();
                admin.setName("Default Admin");
                admin.setEmail("admin@bms.com");
                admin.setPhoneNumber("0000000000");
                admin.setPassword(passwordEncoder.encode("Admin@123"));
                admin.setRole("ROLE_ADMIN");
                userRepository.save(admin);
            }
        };
    }
}
