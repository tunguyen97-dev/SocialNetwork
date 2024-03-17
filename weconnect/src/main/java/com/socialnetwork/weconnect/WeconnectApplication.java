package com.socialnetwork.weconnect;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.socialnetwork.weconnect.Service.AuthenticationService;
import com.socialnetwork.weconnect.Service.FilesStorageService;

import lombok.RequiredArgsConstructor;


@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@RequiredArgsConstructor
public class WeconnectApplication {

	private final FilesStorageService filesStorageService;
	public static void main(String[] args) {
		SpringApplication.run(WeconnectApplication.class, args);
	}
	@Bean
	public CommandLineRunner commandLineRunner(
			AuthenticationService service
	) {
		return args -> {
			filesStorageService.deleteAll();
			filesStorageService.init();
//			var admin = RegisterRequest.builder()
//					.firstname("Admin")
//					.lastname("Admin")
//					.email("admin@mail.com")
//					.password("123456")
//					.role(Role.ADMIN)
//					.build();
//			System.out.println("Admin token: " + service.register(admin).getAccessToken());
//
//			var manager = RegisterRequest.builder()
//					.firstname("Admin")
//					.lastname("Admin")
//					.email("manager@mail.com")
//					.password("123456")
//					.role(Role.MANAGER)
//					.build();
//			System.out.println("Manager token: " + service.register(manager).getAccessToken());

		};
	}
}
