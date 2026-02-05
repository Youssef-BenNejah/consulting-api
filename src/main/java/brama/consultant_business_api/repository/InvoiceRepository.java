package brama.consultant_business_api.repository;

import brama.consultant_business_api.domain.invoice.model.Invoice;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface InvoiceRepository extends MongoRepository<Invoice, String> {
}
