package brama.consultant_business_api.repository;

import brama.consultant_business_api.domain.milestone.model.Milestone;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MilestoneRepository extends MongoRepository<Milestone, String> {
}
