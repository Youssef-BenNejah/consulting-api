package brama.consultant_business_api;

import brama.consultant_business_api.role.Role;
import brama.consultant_business_api.role.RoleRepository;
import brama.consultant_business_api.user.User;
import brama.consultant_business_api.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class ConsultantBusinessApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConsultantBusinessApiApplication.class, args);
	}
	@Bean
	public CommandLineRunner commandLineRunner(final RoleRepository roleRepository,
											   final UserRepository userRepository,
											   final PasswordEncoder passwordEncoder) {
		return args -> {
			final Role userRole = roleRepository.findFirstByName("USER")
					.orElseGet(() -> {
						final Role role = new Role();
						role.setName("USER");
						role.setCreatedBy("admin");
						return roleRepository.save(role);
					});

			final Role adminRole = roleRepository.findFirstByName("ADMIN")
					.orElseGet(() -> {
						final Role role = new Role();
						role.setName("ADMIN");
						role.setCreatedBy("admin");
						return roleRepository.save(role);
					});

			final String adminEmail = "cherif@gmail.com";
			if (!userRepository.existsByEmailIgnoreCase(adminEmail)) {
				final String primaryPhone = "+10000000000";
				final String fallbackPhone = "+10000000001";
				final String phoneNumber = userRepository.existsByPhoneNumber(primaryPhone)
						? fallbackPhone
						: primaryPhone;
				if (userRepository.existsByPhoneNumber(phoneNumber)) {
					return;
				}

				final List<String> roleIds = new ArrayList<>();
				roleIds.add(userRole.getId());
				roleIds.add(adminRole.getId());

				final User admin = User.builder()
						.firstName("Admin")
						.lastName("User")
						.email(adminEmail)
						.phoneNumber(phoneNumber)
						.password(passwordEncoder.encode("admin123"))
						.roles(roleIds)
						.enabled(true)
						.locked(false)
						.credentialsExpired(false)
						.emailVerified(true)
						.phoneVerified(false)
						.build();
				userRepository.save(admin);
			}
		};
	}
}

