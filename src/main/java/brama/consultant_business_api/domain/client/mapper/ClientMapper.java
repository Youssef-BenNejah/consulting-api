package brama.consultant_business_api.domain.client.mapper;

import brama.consultant_business_api.domain.client.dto.request.ClientCreateRequest;
import brama.consultant_business_api.domain.client.dto.request.ClientUpdateRequest;
import brama.consultant_business_api.domain.client.dto.response.ClientResponse;
import brama.consultant_business_api.domain.client.model.Client;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;

@Component
public class ClientMapper {
    public Client toEntity(final ClientCreateRequest request) {
        if (request == null) {
            return null;
        }
        return Client.builder()
                .name(request.getName())
                .industry(request.getIndustry())
                .country(request.getCountry())
                .primaryContact(request.getPrimaryContact())
                .email(request.getEmail())
                .phone(request.getPhone())
                .contractType(request.getContractType())
                .notes(request.getNotes())
                .tags(request.getTags() != null ? new ArrayList<>(request.getTags()) : new ArrayList<>())
                .createdAt(LocalDate.now())
                .build();
    }

    public void merge(final Client client, final ClientUpdateRequest request) {
        if (client == null || request == null) {
            return;
        }
        if (request.getName() != null) {
            client.setName(request.getName());
        }
        if (request.getIndustry() != null) {
            client.setIndustry(request.getIndustry());
        }
        if (request.getCountry() != null) {
            client.setCountry(request.getCountry());
        }
        if (request.getPrimaryContact() != null) {
            client.setPrimaryContact(request.getPrimaryContact());
        }
        if (request.getEmail() != null) {
            client.setEmail(request.getEmail());
        }
        if (request.getPhone() != null) {
            client.setPhone(request.getPhone());
        }
        if (request.getContractType() != null) {
            client.setContractType(request.getContractType());
        }
        if (request.getNotes() != null) {
            client.setNotes(request.getNotes());
        }
        if (request.getTags() != null) {
            client.setTags(new ArrayList<>(request.getTags()));
        }
    }

    public ClientResponse toResponse(final Client client) {
        if (client == null) {
            return null;
        }
        return ClientResponse.builder()
                .id(client.getId())
                .name(client.getName())
                .industry(client.getIndustry())
                .country(client.getCountry())
                .primaryContact(client.getPrimaryContact())
                .email(client.getEmail())
                .phone(client.getPhone())
                .contractType(client.getContractType())
                .notes(client.getNotes())
                .tags(client.getTags())
                .createdAt(client.getCreatedAt())
                .updatedAt(client.getLastModifiedDate())
                .build();
    }
}
