package brama.consultant_business_api.service.projecttype.impl;

import brama.consultant_business_api.domain.projecttype.dto.request.ProjectTypeCreateRequest;
import brama.consultant_business_api.domain.projecttype.dto.request.ProjectTypeUpdateRequest;
import brama.consultant_business_api.domain.projecttype.dto.response.ProjectTypeResponse;
import brama.consultant_business_api.domain.projecttype.mapper.ProjectTypeMapper;
import brama.consultant_business_api.domain.projecttype.model.ProjectTypeConfig;
import brama.consultant_business_api.exception.EntityNotFoundException;
import brama.consultant_business_api.repository.ProjectTypeRepository;
import brama.consultant_business_api.service.projecttype.ProjectTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectTypeServiceImpl implements ProjectTypeService {
    private final ProjectTypeRepository repository;
    private final ProjectTypeMapper mapper;

    @Override
    public List<ProjectTypeResponse> list() {
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ProjectTypeResponse create(final ProjectTypeCreateRequest request) {
        final ProjectTypeConfig config = mapper.toEntity(request);
        final ProjectTypeConfig saved = repository.save(config);
        return mapper.toResponse(saved);
    }

    @Override
    public ProjectTypeResponse update(final String id, final ProjectTypeUpdateRequest request) {
        final ProjectTypeConfig config = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Project type not found: " + id));
        mapper.merge(config, request);
        final ProjectTypeConfig saved = repository.save(config);
        return mapper.toResponse(saved);
    }

    @Override
    public void delete(final String id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Project type not found: " + id);
        }
        repository.deleteById(id);
    }
}
