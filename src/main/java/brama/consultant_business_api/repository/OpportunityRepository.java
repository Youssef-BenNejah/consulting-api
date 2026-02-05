package brama.consultant_business_api.repository;

import brama.consultant_business_api.domain.opportunity.model.Opportunity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OpportunityRepository extends MongoRepository<Opportunity, String> {
}
