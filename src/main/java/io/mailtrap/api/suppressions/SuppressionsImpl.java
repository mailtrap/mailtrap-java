package io.mailtrap.api.suppressions;

import io.mailtrap.Constants;
import io.mailtrap.api.apiresource.ApiResource;
import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.http.RequestData;
import io.mailtrap.model.response.suppressions.SuppressionsResponse;

import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Optional;

import static io.mailtrap.http.RequestData.entry;

public class SuppressionsImpl extends ApiResource implements Suppressions {

  public SuppressionsImpl(MailtrapConfig config) {
    super(config);
    this.apiHost = Constants.GENERAL_HOST;
  }

  @Override
  public List<SuppressionsResponse> search(long accountId, String email) {
    var queryParams = RequestData.buildQueryParams(entry("email", Optional.ofNullable(email)));

    return
        httpClient.getList(
            String.format(apiHost + "/api/accounts/%d/suppressions", accountId),
            new RequestData(queryParams),
            SuppressionsResponse.class
        );
  }

  @Override
  public SuppressionsResponse deleteSuppression(long accountId, String suppressionId) {
    return
        httpClient.delete(
            String.format(apiHost + "/api/accounts/%d/suppressions/%s", accountId, URLEncoder.encode(suppressionId, Charset.defaultCharset())),
            new RequestData(),
            SuppressionsResponse.class
        );
  }
}
