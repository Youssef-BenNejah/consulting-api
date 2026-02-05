package brama.consultant_business_api.repository;

import brama.consultant_business_api.domain.document.model.DocumentFile;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DocumentRepository extends MongoRepository<DocumentFile, String> {
}
