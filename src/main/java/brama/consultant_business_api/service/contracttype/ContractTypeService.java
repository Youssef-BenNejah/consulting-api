package brama.consultant_business_api.service.contracttype;

import brama.consultant_business_api.domain.contracttype.dto.request.ContractTypeCreateRequest;
import brama.consultant_business_api.domain.contracttype.dto.request.ContractTypeUpdateRequest;
import brama.consultant_business_api.domain.contracttype.dto.response.ContractTypeResponse;

import java.util.List;

public interface ContractTypeService {
    List<ContractTypeResponse> list();

    ContractTypeResponse create(ContractTypeCreateRequest request);

    ContractTypeResponse getById(String id);

    ContractTypeResponse update(String id, ContractTypeUpdateRequest request);

    void delete(String id);

    void deleteAll();
}
