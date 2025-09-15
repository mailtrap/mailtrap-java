package io.mailtrap.api.suppressions;

import io.mailtrap.Constants;
import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.factory.MailtrapClientFactory;
import io.mailtrap.model.response.suppressions.SendingStream;
import io.mailtrap.model.response.suppressions.SuppressionType;
import io.mailtrap.model.response.suppressions.SuppressionsResponse;
import io.mailtrap.testutils.BaseTest;
import io.mailtrap.testutils.DataMock;
import io.mailtrap.testutils.TestHttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SuppressionsImplTest extends BaseTest {

  private Suppressions api;

  @BeforeEach
  public void init() {
    final TestHttpClient httpClient = new TestHttpClient(List.of(
        DataMock.build(
            Constants.GENERAL_HOST + "/api/accounts/" + accountId + "/suppressions",
            "GET", null, "api/suppressions/searchSuppressions.json",
            Map.of("email", email)
        ),
        DataMock.build(
            Constants.GENERAL_HOST + "/api/accounts/" + accountId + "/suppressions/" + suppressionIdEncoded,
            "DELETE", null, "api/suppressions/deleteSuppression.json"
        )
    ));

    final MailtrapConfig testConfig = new MailtrapConfig.Builder()
        .httpClient(httpClient)
        .token("dummy_token")
        .build();

    api = MailtrapClientFactory.createMailtrapClient(testConfig).sendingApi().suppressions();
  }

  @Test
  void test_search() {
    final List<SuppressionsResponse> searchResponse = api.search(accountId, email);

    assertEquals(1, searchResponse.size());
    assertEquals(suppressionId, searchResponse.get(0).getId());
    assertEquals(email, searchResponse.get(0).getEmail());
    assertEquals(SendingStream.BULK, searchResponse.get(0).getSendingStream());
    assertEquals(SuppressionType.SPAM_COMPLAINT, searchResponse.get(0).getType());
  }

  @Test
  void test_deleteSuppression() {
    final SuppressionsResponse deleted = api.deleteSuppression(accountId, suppressionId);

    assertNotNull(deleted);
    assertEquals(suppressionId, deleted.getId());
    assertEquals(email, deleted.getEmail());
    assertEquals(SendingStream.BULK, deleted.getSendingStream());
    assertEquals(SuppressionType.SPAM_COMPLAINT, deleted.getType());
  }
}
