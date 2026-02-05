package brama.consultant_business_api.integration;

import brama.consultant_business_api.domain.contracttype.dto.request.ContractTypeCreateRequest;
import brama.consultant_business_api.integration.support.BaseIntegrationTest;
import brama.consultant_business_api.integration.support.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

class ContractTypeApiIT extends BaseIntegrationTest {

    @Test
    void contractTypeCrudFlow() {
        ContractTypeCreateRequest request = TestDataFactory.contractTypeCreateRequest();
        ResponseEntity<String> created = post("/api/v1/contract-types", request);
        assertThat(created.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        String typeId = idFrom(created);

        ResponseEntity<String> list = get("/api/v1/contract-types");
        assertThat(list.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(data(list).size()).isGreaterThanOrEqualTo(1);

        ResponseEntity<String> deleted = delete("/api/v1/contract-types/" + typeId);
        assertThat(deleted.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
