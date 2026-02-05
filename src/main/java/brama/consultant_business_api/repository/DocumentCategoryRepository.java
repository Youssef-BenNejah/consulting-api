package brama.consultant_business_api.repository;

import brama.consultant_business_api.domain.documentcategory.model.DocumentCategory;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DocumentCategoryRepository extends MongoRepository<DocumentCategory, String> {
}
