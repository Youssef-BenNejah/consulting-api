package brama.consultant_business_api.repository;

import brama.consultant_business_api.domain.contact.model.Contact;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ContactRepository extends MongoRepository<Contact, String> {
}
