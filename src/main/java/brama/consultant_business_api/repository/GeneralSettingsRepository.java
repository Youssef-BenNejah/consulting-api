package brama.consultant_business_api.repository;

import brama.consultant_business_api.domain.settings.general.model.GeneralSettings;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GeneralSettingsRepository extends MongoRepository<GeneralSettings, String> {
}
