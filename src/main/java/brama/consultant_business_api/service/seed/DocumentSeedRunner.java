package brama.consultant_business_api.service.seed;

import brama.consultant_business_api.domain.document.enums.DocumentCategoryKey;
import brama.consultant_business_api.domain.document.enums.DocumentEntityType;
import brama.consultant_business_api.domain.document.model.DocumentFile;
import brama.consultant_business_api.domain.project.model.Project;
import brama.consultant_business_api.repository.DocumentRepository;
import brama.consultant_business_api.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.seed.documents.enabled", havingValue = "true")
public class DocumentSeedRunner implements ApplicationRunner {
    private static final Logger logger = LoggerFactory.getLogger(DocumentSeedRunner.class);

    private final DocumentRepository documentRepository;
    private final ProjectRepository projectRepository;

    @Override
    public void run(final ApplicationArguments args) {
        final List<Project> projects = projectRepository.findAll();
        if (projects.isEmpty()) {
            logger.warn("No projects found. Skipping document seed.");
            return;
        }

        documentRepository.deleteAll();
        final List<DocumentFile> docs = new ArrayList<>();

        final Project p1 = projects.get(0);
        final Project p2 = projects.size() > 1 ? projects.get(1) : p1;
        final Project p3 = projects.size() > 2 ? projects.get(2) : p1;
        final Project p4 = projects.size() > 3 ? projects.get(3) : p1;

        docs.add(projectDoc(p1, "Master Service Agreement", DocumentCategoryKey.CONTRACT, "MSA_AtlasGroup.pdf", "PDF", "428 KB"));
        docs.add(projectDoc(p1, "Statement of Work", DocumentCategoryKey.SOW, "SOW_AtlasGroup_Phase1.pdf", "PDF", "312 KB"));
        docs.add(projectDoc(p2, "Requirements Spec", DocumentCategoryKey.REQUIREMENTS, "Requirements_PatientPortal.docx", "DOCX", "184 KB"));
        docs.add(projectDoc(p2, "UI Design Pack", DocumentCategoryKey.DESIGN, "Design_PatientPortal_Figma.zip", "ZIP", "22.4 MB"));
        docs.add(projectDoc(p3, "Test Report - Sprint 3", DocumentCategoryKey.TEST_REPORT, "TestReport_Sprint3.pdf", "PDF", "256 KB"));
        docs.add(projectDoc(p3, "Project Proposal", DocumentCategoryKey.PROPOSAL, "Proposal_RiskDashboard.pdf", "PDF", "198 KB"));
        docs.add(projectDoc(p4, "Invoice 2026-01", DocumentCategoryKey.INVOICE, "Invoice_2026_01.pdf", "PDF", "94 KB"));
        docs.add(projectDoc(p4, "Meeting Notes - Kickoff", DocumentCategoryKey.MEETING_NOTES, "MeetingNotes_Kickoff.docx", "DOCX", "64 KB"));

        // Client-level documents (entity type CLIENT)
        docs.add(clientDoc(p1, "Client Onboarding", DocumentCategoryKey.REQUIREMENTS, "Onboarding_Checklist.pdf", "PDF", "120 KB"));
        docs.add(clientDoc(p2, "Compliance Attestation", DocumentCategoryKey.CONTRACT, "Compliance_Attestation.pdf", "PDF", "88 KB"));

        // Generic documents (entity type DOCUMENT)
        docs.add(genericDoc("Company Branding Guide", DocumentCategoryKey.DESIGN, "Branding_Guide.pdf", "PDF", "2.1 MB"));
        docs.add(genericDoc("Consulting Methodology", DocumentCategoryKey.PROPOSAL, "Consulting_Methodology.pdf", "PDF", "540 KB"));

        documentRepository.saveAll(docs);
        logger.info("Document seed complete. inserted={}", docs.size());
    }

    private DocumentFile projectDoc(final Project project,
                                    final String name,
                                    final DocumentCategoryKey category,
                                    final String filename,
                                    final String fileType,
                                    final String size) {
        return DocumentFile.builder()
                .name(name)
                .category(category)
                .clientId(project.getClientId())
                .clientName(project.getClientName())
                .projectId(project.getProjectId())
                .projectName(project.getName())
                .entityType(DocumentEntityType.PROJECT)
                .entityId(project.getProjectId())
                .uploadedBy("system.seed")
                .uploadedAt(LocalDate.now().minusDays(3))
                .size(size)
                .fileType(fileType)
                .fileId(filename)
                .fileUrl(null)
                .storageProvider("seed")
                .resourceType("document")
                .build();
    }

    private DocumentFile clientDoc(final Project project,
                                   final String name,
                                   final DocumentCategoryKey category,
                                   final String filename,
                                   final String fileType,
                                   final String size) {
        return DocumentFile.builder()
                .name(name)
                .category(category)
                .clientId(project.getClientId())
                .clientName(project.getClientName())
                .entityType(DocumentEntityType.CLIENT)
                .entityId(project.getClientId())
                .uploadedBy("system.seed")
                .uploadedAt(LocalDate.now().minusDays(7))
                .size(size)
                .fileType(fileType)
                .fileId(filename)
                .fileUrl(null)
                .storageProvider("seed")
                .resourceType("document")
                .build();
    }

    private DocumentFile genericDoc(final String name,
                                    final DocumentCategoryKey category,
                                    final String filename,
                                    final String fileType,
                                    final String size) {
        return DocumentFile.builder()
                .name(name)
                .category(category)
                .entityType(DocumentEntityType.DOCUMENT)
                .entityId("general")
                .uploadedBy("system.seed")
                .uploadedAt(LocalDate.now().minusDays(14))
                .size(size)
                .fileType(fileType)
                .fileId(filename)
                .fileUrl(null)
                .storageProvider("seed")
                .resourceType("document")
                .build();
    }
}
