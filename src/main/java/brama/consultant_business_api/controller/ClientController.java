package brama.consultant_business_api.controller;

import brama.consultant_business_api.common.ApiResponse;
import brama.consultant_business_api.common.PageMeta;
import brama.consultant_business_api.common.PagedResult;
import brama.consultant_business_api.common.PaginationUtils;
import brama.consultant_business_api.domain.client.dto.request.ClientCreateRequest;
import brama.consultant_business_api.domain.client.dto.request.ClientUpdateRequest;
import brama.consultant_business_api.domain.client.dto.response.ClientResponse;
import brama.consultant_business_api.service.client.ClientService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clients")
@RequiredArgsConstructor
@Tag(name = "Clients", description = "Client API")
public class ClientController {
    private final ClientService service;

    @GetMapping
    public ApiResponse<List<ClientResponse>> listClients(
            @RequestParam(required = false) final String search,
            @RequestParam(required = false) final String industry,
            @RequestParam(required = false) final String country,
            @RequestParam(required = false) final List<String> tags,
            @RequestParam(required = false) final Integer page,
            @RequestParam(required = false) final Integer size,
            @RequestParam(required = false) final String sort) {
        final PagedResult<ClientResponse> result = service.search(search, industry, country, tags, page, size, sort);
        final PageMeta meta = PageMeta.builder()
                .page(PaginationUtils.normalizePage(page))
                .size(PaginationUtils.normalizeSize(size))
                .total(result.getTotal())
                .build();
        return ApiResponse.of(result.getItems(), meta);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ClientResponse> createClient(@Valid @RequestBody final ClientCreateRequest request) {
        return ApiResponse.ok(service.create(request));
    }

    @GetMapping("/{id}")
    public ApiResponse<ClientResponse> getClient(@PathVariable final String id) {
        return ApiResponse.ok(service.getById(id));
    }

    @PatchMapping("/{id}")
    public ApiResponse<ClientResponse> updateClient(@PathVariable final String id,
                                                    @Valid @RequestBody final ClientUpdateRequest request) {
        return ApiResponse.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteClient(@PathVariable final String id) {
        service.delete(id);
    }
}
