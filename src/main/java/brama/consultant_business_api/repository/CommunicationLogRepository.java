package brama.consultant_business_api.repository;

import brama.consultant_business_api.domain.communication.model.CommunicationLog;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CommunicationLogRepository extends MongoRepository<CommunicationLog, String> {
}
