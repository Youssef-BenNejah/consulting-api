package brama.consultant_business_api.service.issue;

import brama.consultant_business_api.common.PagedResult;
import brama.consultant_business_api.domain.issue.dto.request.IssueCreateRequest;
import brama.consultant_business_api.domain.issue.dto.request.IssueUpdateRequest;
import brama.consultant_business_api.domain.issue.dto.response.IssueResponse;
import brama.consultant_business_api.domain.issue.enums.IssueSeverity;
import brama.consultant_business_api.domain.issue.enums.IssueStatus;

public interface IssueService {
    PagedResult<IssueResponse> search(String projectId,
                                      IssueStatus status,
                                      IssueSeverity severity,
                                      Integer page,
                                      Integer size);

    IssueResponse create(IssueCreateRequest request);

    IssueResponse update(String id, IssueUpdateRequest request);

    void delete(String id);
}
