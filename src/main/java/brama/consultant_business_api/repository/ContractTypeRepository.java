package brama.consultant_business_api.repository;

import brama.consultant_business_api.domain.contracttype.model.ContractTypeConfig;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ContractTypeRepository extends MongoRepository<ContractTypeConfig, String> {
}
