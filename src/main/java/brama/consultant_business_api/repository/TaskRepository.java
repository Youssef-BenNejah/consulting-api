package brama.consultant_business_api.repository;

import brama.consultant_business_api.domain.task.model.Task;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TaskRepository extends MongoRepository<Task, String> {
}
