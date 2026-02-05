package brama.consultant_business_api.service.contact;

import brama.consultant_business_api.common.PagedResult;
import brama.consultant_business_api.domain.contact.dto.request.ContactCreateRequest;
import brama.consultant_business_api.domain.contact.dto.request.ContactReplyCreateRequest;
import brama.consultant_business_api.domain.contact.dto.request.ContactUpdateRequest;
import brama.consultant_business_api.domain.contact.dto.response.ContactReplyResponse;
import brama.consultant_business_api.domain.contact.dto.response.ContactResponse;
import brama.consultant_business_api.domain.contact.enums.ContactStatus;

import java.util.List;

public interface ContactService {
    ContactResponse create(ContactCreateRequest request);

    PagedResult<ContactResponse> search(String search,
                                        ContactStatus status,
                                        Boolean isRead,
                                        Integer page,
                                        Integer size);

    ContactResponse update(String id, ContactUpdateRequest request);

    void delete(String id);

    List<ContactReplyResponse> listReplies(String contactId);

    ContactReplyResponse addReply(String contactId, ContactReplyCreateRequest request);
}
