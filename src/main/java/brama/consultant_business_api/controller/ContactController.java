package brama.consultant_business_api.controller;

import brama.consultant_business_api.common.ApiResponse;
import brama.consultant_business_api.common.PageMeta;
import brama.consultant_business_api.common.PagedResult;
import brama.consultant_business_api.common.PaginationUtils;
import brama.consultant_business_api.domain.contact.dto.request.ContactCreateRequest;
import brama.consultant_business_api.domain.contact.dto.request.ContactReplyCreateRequest;
import brama.consultant_business_api.domain.contact.dto.request.ContactUpdateRequest;
import brama.consultant_business_api.domain.contact.dto.response.ContactReplyResponse;
import brama.consultant_business_api.domain.contact.dto.response.ContactResponse;
import brama.consultant_business_api.domain.contact.enums.ContactStatus;
import brama.consultant_business_api.service.contact.ContactService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/contacts")
@RequiredArgsConstructor
@Tag(name = "Contacts", description = "Contact inbox API")
public class ContactController {
    private final ContactService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ContactResponse> createContact(@Valid @RequestBody final ContactCreateRequest request) {
        return ApiResponse.ok(service.create(request));
    }

    @GetMapping
    public ApiResponse<List<ContactResponse>> listContacts(
            @RequestParam(required = false) final String search,
            @RequestParam(required = false) final ContactStatus status,
            @RequestParam(name = "is_read", required = false) final Boolean isRead,
            @RequestParam(required = false) final Integer page,
            @RequestParam(required = false) final Integer size) {
        final PagedResult<ContactResponse> result = service.search(search, status, isRead, page, size);
        final PageMeta meta = PageMeta.builder()
                .page(PaginationUtils.normalizePage(page))
                .size(PaginationUtils.normalizeSize(size))
                .total(result.getTotal())
                .build();
        return ApiResponse.of(result.getItems(), meta);
    }

    @PatchMapping("/{id}")
    public ApiResponse<ContactResponse> updateContact(@PathVariable final String id,
                                                      @Valid @RequestBody final ContactUpdateRequest request) {
        return ApiResponse.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteContact(@PathVariable final String id) {
        service.delete(id);
    }

    @GetMapping("/{id}/replies")
    public ApiResponse<List<ContactReplyResponse>> listReplies(@PathVariable final String id) {
        return ApiResponse.ok(service.listReplies(id));
    }

    @PostMapping("/{id}/replies")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ContactReplyResponse> addReply(@PathVariable final String id,
                                                      @Valid @RequestBody final ContactReplyCreateRequest request) {
        return ApiResponse.ok(service.addReply(id, request));
    }
}
