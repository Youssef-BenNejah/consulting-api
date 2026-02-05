package brama.consultant_business_api.integration.support;

import brama.consultant_business_api.domain.client.dto.request.ClientCreateRequest;
import brama.consultant_business_api.domain.client.dto.request.ClientUpdateRequest;
import brama.consultant_business_api.domain.communication.dto.request.CommunicationLogCreateRequest;
import brama.consultant_business_api.domain.communication.enums.CommunicationType;
import brama.consultant_business_api.domain.contact.dto.request.ContactCreateRequest;
import brama.consultant_business_api.domain.contact.dto.request.ContactReplyCreateRequest;
import brama.consultant_business_api.domain.contact.dto.request.ContactUpdateRequest;
import brama.consultant_business_api.domain.contact.enums.ContactStatus;
import brama.consultant_business_api.domain.contracttype.dto.request.ContractTypeCreateRequest;
import brama.consultant_business_api.domain.document.dto.request.DocumentCreateRequest;
import brama.consultant_business_api.domain.document.enums.DocumentCategoryKey;
import brama.consultant_business_api.domain.documentcategory.dto.request.DocumentCategoryCreateRequest;
import brama.consultant_business_api.domain.invoice.dto.request.InvoiceCreateRequest;
import brama.consultant_business_api.domain.invoice.dto.request.InvoiceItemRequest;
import brama.consultant_business_api.domain.invoice.dto.request.InvoiceStatusUpdateRequest;
import brama.consultant_business_api.domain.invoice.dto.request.InvoiceUpdateRequest;
import brama.consultant_business_api.domain.invoice.enums.InvoiceStatus;
import brama.consultant_business_api.domain.project.dto.request.ProjectCreateRequest;
import brama.consultant_business_api.domain.project.dto.request.ProjectUpdateRequest;
import brama.consultant_business_api.domain.project.enums.HealthStatus;
import brama.consultant_business_api.domain.project.enums.ProjectStatus;
import brama.consultant_business_api.domain.project.enums.ProjectType;
import brama.consultant_business_api.domain.projecttype.dto.request.ProjectTypeCreateRequest;
import brama.consultant_business_api.domain.schedule.dto.request.ScheduleCreateRequest;
import brama.consultant_business_api.domain.schedule.dto.request.ScheduleUpdateRequest;

import java.time.LocalDate;
import java.util.List;

public final class TestDataFactory {
    private TestDataFactory() {
    }

    public static ClientCreateRequest clientCreateRequest() {
        return ClientCreateRequest.builder()
                .name("Acme Corp")
                .industry("Technology")
                .country("FR")
                .primaryContact("Jean Dupont")
                .email("jean.dupont@acme.fr")
                .phone("+33123456789")
                .contractType("Enterprise")
                .notes("Important client")
                .tags(List.of("vip", "europe"))
                .build();
    }

    public static ClientUpdateRequest clientUpdateRequest() {
        return ClientUpdateRequest.builder()
                .industry("FinTech")
                .notes("Updated notes")
                .build();
    }

    public static ProjectCreateRequest projectCreateRequest(final String clientId, final String clientName) {
        return ProjectCreateRequest.builder()
                .projectId("PRJ-2026-001")
                .clientId(clientId)
                .clientName(clientName)
                .name("Migration Project")
                .description("Core system migration")
                .startDate(LocalDate.now().minusDays(5))
                .endDate(LocalDate.now().plusDays(30))
                .status(ProjectStatus.DELIVERY)
                .type(ProjectType.FIXED)
                .clientBudget(120000D)
                .vendorCost(40000D)
                .internalCost(30000D)
                .healthStatus(HealthStatus.GREEN)
                .progress(20)
                .build();
    }

    public static ProjectUpdateRequest projectUpdateRequest() {
        return ProjectUpdateRequest.builder()
                .progress(40)
                .healthStatus(HealthStatus.AMBER)
                .status(ProjectStatus.REVIEW)
                .build();
    }


    public static InvoiceCreateRequest invoiceCreateRequest(final String projectId, final String projectName, final String clientId, final String clientName) {
        final InvoiceItemRequest item1 = InvoiceItemRequest.builder()
                .description("Consulting days")
                .quantity(5D)
                .unitPrice(800D)
                .total(4000D)
                .build();
        final InvoiceItemRequest item2 = InvoiceItemRequest.builder()
                .description("Workshops")
                .quantity(2D)
                .unitPrice(1200D)
                .total(2400D)
                .build();
        return InvoiceCreateRequest.builder()
                .invoiceNumber("INV-2026-001")
                .type("client")
                .projectId(projectId)
                .projectName(projectName)
                .clientId(clientId)
                .clientName(clientName)
                .clientAddress("10 Rue de Paris")
                .date(LocalDate.now().minusDays(1))
                .dueDate(LocalDate.now().plusDays(14))
                .amount(6400D)
                .currency("EUR")
                .status(InvoiceStatus.SENT)
                .notes("Net 14 days")
                .items(List.of(item1, item2))
                .build();
    }

    public static InvoiceUpdateRequest invoiceUpdateRequest() {
        return InvoiceUpdateRequest.builder()
                .notes("Updated invoice notes")
                .build();
    }

    public static InvoiceStatusUpdateRequest invoiceStatusUpdateRequest() {
        return InvoiceStatusUpdateRequest.builder()
                .status(InvoiceStatus.PAID)
                .build();
    }

    public static DocumentCreateRequest documentCreateRequest(final String projectId, final String projectName, final String clientId, final String clientName) {
        return DocumentCreateRequest.builder()
                .name("contract.pdf")
                .category(DocumentCategoryKey.CONTRACT)
                .clientId(clientId)
                .clientName(clientName)
                .projectId(projectId)
                .projectName(projectName)
                .uploadedBy("System")
                .uploadedAt(LocalDate.now())
                .size("12KB")
                .fileType("PDF")
                .build();
    }

    public static DocumentCategoryCreateRequest documentCategoryCreateRequest() {
        return DocumentCategoryCreateRequest.builder()
                .name("Contract")
                .key("contract")
                .color("#0055FF")
                .build();
    }

    public static ProjectTypeCreateRequest projectTypeCreateRequest() {
        return ProjectTypeCreateRequest.builder()
                .name("Fixed Price")
                .key("fixed")
                .description("Fixed scope engagement")
                .build();
    }

    public static ContractTypeCreateRequest contractTypeCreateRequest() {
        return ContractTypeCreateRequest.builder()
                .name("Enterprise")
                .key("enterprise")
                .description("Enterprise agreement")
                .build();
    }

    public static CommunicationLogCreateRequest communicationLogCreateRequest(final String clientId, final String clientName,
                                                                              final String projectId, final String projectName) {
        return CommunicationLogCreateRequest.builder()
                .clientId(clientId)
                .clientName(clientName)
                .projectId(projectId)
                .projectName(projectName)
                .date(LocalDate.now())
                .type(CommunicationType.MEETING)
                .summary("Weekly sync")
                .actionItems(List.of("Send summary", "Prepare slides"))
                .participants(List.of("Alice", "Bob"))
                .build();
    }

    public static ContactCreateRequest contactCreateRequest() {
        return ContactCreateRequest.builder()
                .name("Nadia")
                .email("nadia@example.com")
                .company("Nadia Co")
                .service("Consulting")
                .budget("5000")
                .message("Need project estimate")
                .build();
    }

    public static ContactUpdateRequest contactUpdateRequest() {
        return ContactUpdateRequest.builder()
                .status(ContactStatus.IN_PROGRESS)
                .isRead(true)
                .build();
    }

    public static ContactReplyCreateRequest contactReplyCreateRequest() {
        return ContactReplyCreateRequest.builder()
                .message("Thanks, we will follow up shortly.")
                .build();
    }

    public static ScheduleCreateRequest scheduleCreateRequest(final String projectId, final String projectName,
                                                              final String clientId, final String clientName) {
        return ScheduleCreateRequest.builder()
                .projectId(projectId)
                .projectName(projectName)
                .clientId(clientId)
                .clientName(clientName)
                .clientContactName("Jean Dupont")
                .clientContactEmail("jean.dupont@acme.fr")
                .clientContactPhone("+33123456789")
                .scheduleStartDate(LocalDate.now())
                .scheduleEndDate(LocalDate.now().plusDays(2))
                .scheduleColor("#FFAA00")
                .scheduleNotes("Initial planning")
                .isScheduled(true)
                .reminderEnabled(true)
                .createdBy("Scheduler")
                .build();
    }

    public static ScheduleUpdateRequest scheduleUpdateRequest() {
        return ScheduleUpdateRequest.builder()
                .scheduleNotes("Updated notes")
                .scheduleColor("#00AAFF")
                .build();
    }
}
