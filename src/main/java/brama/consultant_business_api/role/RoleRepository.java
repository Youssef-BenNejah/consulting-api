package brama.consultant_business_api.role;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RoleRepository extends MongoRepository<Role, String> {
    Optional<Role> findFirstByName(String name);
    boolean existsByName(String name);
}

