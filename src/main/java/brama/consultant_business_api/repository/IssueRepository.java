package brama.consultant_business_api.repository;

import brama.consultant_business_api.domain.issue.model.Issue;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IssueRepository extends MongoRepository<Issue, String> {
}
