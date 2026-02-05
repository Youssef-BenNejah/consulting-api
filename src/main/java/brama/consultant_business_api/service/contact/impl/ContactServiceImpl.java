package brama.consultant_business_api.service.contact.impl;

import brama.consultant_business_api.common.PagedResult;
import brama.consultant_business_api.common.PaginationUtils;
import brama.consultant_business_api.common.QueryUtils;
import brama.consultant_business_api.domain.contact.dto.request.ContactCreateRequest;
import brama.consultant_business_api.domain.contact.dto.request.ContactReplyCreateRequest;
import brama.consultant_business_api.domain.contact.dto.request.ContactUpdateRequest;
import brama.consultant_business_api.domain.contact.dto.response.ContactReplyResponse;
import brama.consultant_business_api.domain.contact.dto.response.ContactResponse;
import brama.consultant_business_api.domain.contact.enums.ContactStatus;
import brama.consultant_business_api.domain.contact.mapper.ContactMapper;
import brama.consultant_business_api.domain.contact.mapper.ContactReplyMapper;
import brama.consultant_business_api.domain.contact.model.Contact;
import brama.consultant_business_api.domain.contact.model.ContactReply;
import brama.consultant_business_api.exception.EntityNotFoundException;
import brama.consultant_business_api.repository.ContactReplyRepository;
import brama.consultant_business_api.repository.ContactRepository;
import brama.consultant_business_api.service.contact.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {
    private final ContactRepository contactRepository;
    private final ContactReplyRepository replyRepository;
    private final MongoTemplate mongoTemplate;
    private final ContactMapper contactMapper;
    private final ContactReplyMapper replyMapper;

    @Override
    public ContactResponse create(final ContactCreateRequest request) {
        final Contact contact = contactMapper.toEntity(request);
        final Contact saved = contactRepository.save(contact);
        return contactMapper.toResponse(saved);
    }

    @Override
    public PagedResult<ContactResponse> search(final String search,
                                               final ContactStatus status,
                                               final Boolean isRead,
                                               final Integer page,
                                               final Integer size) {
        final Query query = new Query();
        QueryUtils.addRegexOrCriteria(query, search, "name", "email", "company", "service", "message");
        QueryUtils.addIfNotNull(query, "status", status);
        if (isRead != null) {
            query.addCriteria(org.springframework.data.mongodb.core.query.Criteria.where("isRead").is(isRead));
        }
        final long total = mongoTemplate.count(query, Contact.class);
        final Pageable pageable = PaginationUtils.toPageable(page, size, null);
        query.with(pageable);
        final List<ContactResponse> items = mongoTemplate.find(query, Contact.class).stream()
                .map(contactMapper::toResponse)
                .collect(Collectors.toList());
        return PagedResult.<ContactResponse>builder()
                .items(items)
                .total(total)
                .build();
    }

    @Override
    public ContactResponse getById(final String id) {
        final Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contact not found: " + id));
        return contactMapper.toResponse(contact);
    }

    @Override
    public ContactResponse update(final String id, final ContactUpdateRequest request) {
        final Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contact not found: " + id));
        contactMapper.merge(contact, request);
        final Contact saved = contactRepository.save(contact);
        return contactMapper.toResponse(saved);
    }

    @Override
    public void delete(final String id) {
        if (!contactRepository.existsById(id)) {
            throw new EntityNotFoundException("Contact not found: " + id);
        }
        contactRepository.deleteById(id);
        replyRepository.findByContactId(id).forEach(reply -> replyRepository.deleteById(reply.getId()));
    }

    @Override
    public List<ContactReplyResponse> listReplies(final String contactId) {
        return replyRepository.findByContactId(contactId).stream()
                .map(replyMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ContactReplyResponse addReply(final String contactId, final ContactReplyCreateRequest request) {
        final Contact contact = contactRepository.findById(contactId)
                .orElseThrow(() -> new EntityNotFoundException("Contact not found: " + contactId));
        final ContactReply reply = replyMapper.toEntity(contactId, request);
        final ContactReply savedReply = replyRepository.save(reply);

        if (contact.getStatus() != ContactStatus.CLOSED) {
            contact.setStatus(ContactStatus.REPLIED);
        }
        contact.setRead(true);
        contact.setUpdatedAt(Instant.now());
        contactRepository.save(contact);

        return replyMapper.toResponse(savedReply);
    }
}
