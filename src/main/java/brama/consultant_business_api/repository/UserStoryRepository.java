package brama.consultant_business_api.repository;

import brama.consultant_business_api.domain.userstory.model.UserStory;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserStoryRepository extends MongoRepository<UserStory, String> {
}
