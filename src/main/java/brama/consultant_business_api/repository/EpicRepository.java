package brama.consultant_business_api.repository;

import brama.consultant_business_api.domain.epic.model.Epic;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EpicRepository extends MongoRepository<Epic, String> {
}
