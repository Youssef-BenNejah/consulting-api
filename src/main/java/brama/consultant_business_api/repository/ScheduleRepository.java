package brama.consultant_business_api.repository;

import brama.consultant_business_api.domain.schedule.model.ProjectSchedule;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ScheduleRepository extends MongoRepository<ProjectSchedule, String> {
}
