package brama.consultant_business_api.repository;

import brama.consultant_business_api.domain.settings.notification.model.NotificationSettings;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotificationSettingsRepository extends MongoRepository<NotificationSettings, String> {
}
