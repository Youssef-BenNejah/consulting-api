package brama.consultant_business_api.repository;

import brama.consultant_business_api.domain.settings.catalog.model.SettingsCatalog;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SettingsCatalogRepository extends MongoRepository<SettingsCatalog, String> {
}
