package brama.consultant_business_api.service.contracttype.impl;

import brama.consultant_business_api.domain.contracttype.dto.request.ContractTypeCreateRequest;
import brama.consultant_business_api.domain.contracttype.dto.request.ContractTypeUpdateRequest;
import brama.consultant_business_api.domain.contracttype.dto.response.ContractTypeResponse;
import brama.consultant_business_api.domain.contracttype.mapper.ContractTypeMapper;
import brama.consultant_business_api.domain.contracttype.model.ContractTypeConfig;
import brama.consultant_business_api.exception.EntityNotFoundException;
import brama.consultant_business_api.repository.ContractTypeRepository;
import brama.consultant_business_api.service.contracttype.ContractTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContractTypeServiceImpl implements ContractTypeService {
    private final ContractTypeRepository repository;
    private final ContractTypeMapper mapper;

    @Override
    public List<ContractTypeResponse> list() {
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ContractTypeResponse create(final ContractTypeCreateRequest request) {
        final ContractTypeConfig config = mapper.toEntity(request);
        final ContractTypeConfig saved = repository.save(config);
        return mapper.toResponse(saved);
    }

    @Override
    public ContractTypeResponse getById(final String id) {
        final ContractTypeConfig config = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contract type not found: " + id));
        return mapper.toResponse(config);
    }

    @Override
    public ContractTypeResponse update(final String id, final ContractTypeUpdateRequest request) {
        final ContractTypeConfig config = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contract type not found: " + id));
        mapper.merge(config, request);
        final ContractTypeConfig saved = repository.save(config);
        return mapper.toResponse(saved);
    }

    @Override
    public void delete(final String id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Contract type not found: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }
}
