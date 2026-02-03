package brama.consultant_business_api;

import brama.consultant_business_api.role.Role;
import brama.consultant_business_api.role.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class ConsultantBusinessApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConsultantBusinessApiApplication.class, args);
	}
	@Bean
	public CommandLineRunner commandLineRunner(final RoleRepository roleRepository) {
		return args -> {
			final boolean userRoleExists = roleRepository.existsByName("USER");
			if (!userRoleExists) {
				final Role role = new Role();
				role.setName("USER");
				role.setCreatedBy("admin");
				roleRepository.save(role);
			}
		};
	}
}

