package brama.consultant_business_api.service.client;

import brama.consultant_business_api.common.PagedResult;
import brama.consultant_business_api.domain.client.dto.request.ClientCreateRequest;
import brama.consultant_business_api.domain.client.dto.request.ClientUpdateRequest;
import brama.consultant_business_api.domain.client.dto.response.ClientResponse;

import java.util.List;

public interface ClientService {
    PagedResult<ClientResponse> search(String search,
                                       String industry,
                                       String country,
                                       List<String> tags,
                                       Integer page,
                                       Integer size,
                                       String sort);

    ClientResponse create(ClientCreateRequest request);

    ClientResponse getById(String id);

    ClientResponse update(String id, ClientUpdateRequest request);

    void delete(String id);
}
