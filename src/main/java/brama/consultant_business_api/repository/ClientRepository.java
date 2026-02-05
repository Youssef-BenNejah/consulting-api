package brama.consultant_business_api.repository;

import brama.consultant_business_api.domain.client.model.Client;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ClientRepository extends MongoRepository<Client, String> {
}
