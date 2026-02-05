package brama.consultant_business_api.repository;

import brama.consultant_business_api.domain.projecttype.model.ProjectTypeConfig;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProjectTypeRepository extends MongoRepository<ProjectTypeConfig, String> {
}
