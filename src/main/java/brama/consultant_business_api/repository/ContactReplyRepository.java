package brama.consultant_business_api.repository;

import brama.consultant_business_api.domain.contact.model.ContactReply;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ContactReplyRepository extends MongoRepository<ContactReply, String> {
    List<ContactReply> findByContactId(String contactId);
}
