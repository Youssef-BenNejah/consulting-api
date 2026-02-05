package brama.consultant_business_api.service.schedule.impl;

import brama.consultant_business_api.common.PagedResult;
import brama.consultant_business_api.common.PaginationUtils;
import brama.consultant_business_api.common.QueryUtils;
import brama.consultant_business_api.domain.project.enums.ProjectStatus;
import brama.consultant_business_api.domain.project.model.Project;
import brama.consultant_business_api.domain.schedule.dto.request.ScheduleCreateRequest;
import brama.consultant_business_api.domain.schedule.dto.request.ScheduleUpdateRequest;
import brama.consultant_business_api.domain.schedule.dto.response.ScheduleResponse;
import brama.consultant_business_api.domain.schedule.mapper.ScheduleMapper;
import brama.consultant_business_api.domain.schedule.model.ProjectSchedule;
import brama.consultant_business_api.exception.BusinessException;
import brama.consultant_business_api.exception.EntityNotFoundException;
import brama.consultant_business_api.exception.ErrorCode;
import brama.consultant_business_api.repository.ProjectRepository;
import brama.consultant_business_api.repository.ScheduleRepository;
import brama.consultant_business_api.service.schedule.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {
    private final ScheduleRepository repository;
    private final ProjectRepository projectRepository;
    private final MongoTemplate mongoTemplate;
    private final ScheduleMapper mapper;

    @Override
    public PagedResult<ScheduleResponse> search(final String search,
                                                final String clientId,
                                                final String projectId,
                                                final ProjectStatus projectStatus,
                                                final LocalDate dateFrom,
                                                final LocalDate dateTo,
                                                final Integer page,
                                                final Integer size) {
        final Query query = buildQuery(search, clientId, projectId, projectStatus, dateFrom, dateTo);
        final long total = mongoTemplate.count(query, ProjectSchedule.class);
        final Pageable pageable = PaginationUtils.toPageable(page, size, null);
        query.with(pageable);
        final List<ScheduleResponse> items = mongoTemplate.find(query, ProjectSchedule.class).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
        return PagedResult.<ScheduleResponse>builder()
                .items(items)
                .total(total)
                .build();
    }

    @Override
    public ScheduleResponse create(final ScheduleCreateRequest request) {
        validateScheduleDates(request.getScheduleStartDate(), request.getScheduleEndDate());
        final ProjectSchedule schedule = mapper.toEntity(request);
        final ProjectSchedule saved = repository.save(schedule);
        return mapper.toResponse(saved);
    }

    @Override
    public ScheduleResponse getById(final String id) {
        final ProjectSchedule schedule = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Schedule not found: " + id));
        return mapper.toResponse(schedule);
    }

    @Override
    public ScheduleResponse update(final String id, final ScheduleUpdateRequest request) {
        final ProjectSchedule schedule = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Schedule not found: " + id));
        if (request.getScheduleStartDate() != null || request.getScheduleEndDate() != null) {
            final LocalDate start = request.getScheduleStartDate() != null ? request.getScheduleStartDate() : schedule.getScheduleStartDate();
            final LocalDate end = request.getScheduleEndDate() != null ? request.getScheduleEndDate() : schedule.getScheduleEndDate();
            validateScheduleDates(start, end);
        }
        mapper.merge(schedule, request);
        final ProjectSchedule saved = repository.save(schedule);
        return mapper.toResponse(saved);
    }

    @Override
    public void delete(final String id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Schedule not found: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    public byte[] exportCsv(final String search,
                            final String clientId,
                            final String projectId,
                            final ProjectStatus projectStatus,
                            final LocalDate dateFrom,
                            final LocalDate dateTo) {
        final Query query = buildQuery(search, clientId, projectId, projectStatus, dateFrom, dateTo);
        final List<ProjectSchedule> schedules = mongoTemplate.find(query, ProjectSchedule.class);
        final StringBuilder sb = new StringBuilder();
        sb.append("project_id,project_name,client_id,client_name,schedule_start_date,schedule_end_date,schedule_duration_days,schedule_color,is_scheduled,reminder_enabled\n");
        for (final ProjectSchedule schedule : schedules) {
            sb.append(escape(schedule.getProjectId())).append(",")
                    .append(escape(schedule.getProjectName())).append(",")
                    .append(escape(schedule.getClientId())).append(",")
                    .append(escape(schedule.getClientName())).append(",")
                    .append(schedule.getScheduleStartDate()).append(",")
                    .append(schedule.getScheduleEndDate()).append(",")
                    .append(schedule.getScheduleDurationDays()).append(",")
                    .append(escape(schedule.getScheduleColor())).append(",")
                    .append(schedule.isScheduled()).append(",")
                    .append(schedule.isReminderEnabled())
                    .append("\n");
        }
        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }

    private Query buildQuery(final String search,
                             final String clientId,
                             final String projectId,
                             final ProjectStatus projectStatus,
                             final LocalDate dateFrom,
                             final LocalDate dateTo) {
        final Query query = new Query();
        QueryUtils.addRegexOrCriteria(query, search, "projectName", "clientName", "scheduleNotes");
        QueryUtils.addIfNotBlank(query, "clientId", clientId);

        if (projectStatus != null) {
            final List<String> projectIds = projectRepository.findAll().stream()
                    .filter(project -> project.getStatus() == projectStatus)
                    .map(Project::getId)
                    .collect(Collectors.toList());
            if (projectIds.isEmpty()) {
                query.addCriteria(Criteria.where("projectId").in("__none__"));
            } else if (projectId != null && !projectId.isBlank()) {
                if (projectIds.contains(projectId)) {
                    query.addCriteria(Criteria.where("projectId").is(projectId));
                } else {
                    query.addCriteria(Criteria.where("projectId").in("__none__"));
                }
            } else {
                query.addCriteria(Criteria.where("projectId").in(projectIds));
            }
        } else {
            QueryUtils.addIfNotBlank(query, "projectId", projectId);
        }

        if (dateFrom != null) {
            query.addCriteria(Criteria.where("scheduleEndDate").gte(dateFrom));
        }
        if (dateTo != null) {
            query.addCriteria(Criteria.where("scheduleStartDate").lte(dateTo));
        }
        return query;
    }

    private void validateScheduleDates(final LocalDate start, final LocalDate end) {
        if (start != null && end != null && end.isBefore(start)) {
            throw new BusinessException(ErrorCode.INVALID_DATE_RANGE, "schedule_end_date before schedule_start_date");
        }
    }

    private String escape(final String value) {
        if (value == null) {
            return "";
        }
        final String escaped = value.replace("\"", "\"\"");
        if (escaped.contains(",") || escaped.contains("\"") || escaped.contains("\n")) {
            return "\"" + escaped + "\"";
        }
        return escaped;
    }
}
