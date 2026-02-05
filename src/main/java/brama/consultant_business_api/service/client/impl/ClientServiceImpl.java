package brama.consultant_business_api.service.client.impl;

import brama.consultant_business_api.common.PagedResult;
import brama.consultant_business_api.common.PaginationUtils;
import brama.consultant_business_api.common.QueryUtils;
import brama.consultant_business_api.common.SortUtils;
import brama.consultant_business_api.domain.client.dto.request.ClientCreateRequest;
import brama.consultant_business_api.domain.client.dto.request.ClientUpdateRequest;
import brama.consultant_business_api.domain.client.dto.response.ClientResponse;
import brama.consultant_business_api.domain.client.mapper.ClientMapper;
import brama.consultant_business_api.domain.client.model.Client;
import brama.consultant_business_api.exception.EntityNotFoundException;
import brama.consultant_business_api.repository.ClientRepository;
import brama.consultant_business_api.service.client.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {
    private final ClientRepository repository;
    private final MongoTemplate mongoTemplate;
    private final ClientMapper mapper;

    @Override
    public PagedResult<ClientResponse> search(final String search,
                                              final String industry,
                                              final String country,
                                              final List<String> tags,
                                              final Integer page,
                                              final Integer size,
                                              final String sort) {
        final Query query = new Query();
        QueryUtils.addRegexOrCriteria(query, search,
                "name", "industry", "country", "primaryContact", "email", "phone", "notes");
        QueryUtils.addIfNotBlank(query, "industry", industry);
        QueryUtils.addIfNotBlank(query, "country", country);
        QueryUtils.addIfNotEmpty(query, "tags", tags);

        final long total = mongoTemplate.count(query, Client.class);
        final Pageable pageable = PaginationUtils.toPageable(page, size, SortUtils.parseSort(sort));
        query.with(pageable);
        final List<ClientResponse> items = mongoTemplate.find(query, Client.class).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
        return PagedResult.<ClientResponse>builder()
                .items(items)
                .total(total)
                .build();
    }

    @Override
    public ClientResponse create(final ClientCreateRequest request) {
        final Client client = mapper.toEntity(request);
        final Client saved = repository.save(client);
        return mapper.toResponse(saved);
    }

    @Override
    public ClientResponse getById(final String id) {
        final Client client = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Client not found: " + id));
        return mapper.toResponse(client);
    }

    @Override
    public ClientResponse update(final String id, final ClientUpdateRequest request) {
        final Client client = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Client not found: " + id));
        mapper.merge(client, request);
        final Client saved = repository.save(client);
        return mapper.toResponse(saved);
    }

    @Override
    public void delete(final String id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Client not found: " + id);
        }
        repository.deleteById(id);
    }
}
