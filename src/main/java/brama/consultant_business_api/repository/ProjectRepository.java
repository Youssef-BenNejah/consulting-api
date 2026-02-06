package brama.consultant_business_api.repository;

import brama.consultant_business_api.domain.project.model.Project;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProjectRepository extends MongoRepository<Project, String> {
    boolean existsByStatusId(String statusId);

    boolean existsByProjectTypeId(String projectTypeId);

    boolean existsByPriorityId(String priorityId);

    boolean existsByTagIdsContaining(String tagId);

    boolean existsByContractTypeId(String contractTypeId);
}
