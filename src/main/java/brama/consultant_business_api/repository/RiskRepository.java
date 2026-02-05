package brama.consultant_business_api.repository;

import brama.consultant_business_api.domain.risk.model.Risk;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RiskRepository extends MongoRepository<Risk, String> {
}
