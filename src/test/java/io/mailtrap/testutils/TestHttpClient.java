package io.mailtrap.testutils;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import io.mailtrap.Mapper;
import io.mailtrap.exception.http.HttpException;
import io.mailtrap.http.CustomHttpClient;
import io.mailtrap.http.RequestData;
import io.mailtrap.model.AbstractModel;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TestHttpClient implements CustomHttpClient {

    private final Map<String, List<DataMock>> mocks;

    public TestHttpClient(final List<DataMock> mocks) {
        this.mocks = mocks.stream().collect(
                Collectors.groupingBy(
                        mock -> getRequestIdentifier(mock.getUrl(), mock.getMethodName())
                )
        );
    }

    @Override
    public <T> T get(final String url, final RequestData requestData, final Class<T> responseType) throws HttpException {
        return this.request(url, "GET", null, requestData, responseType);
    }

    @Override
    public <T> List<T> getList(final String url, final RequestData requestData, final Class<T> responseClassType) throws HttpException {
        final JavaType responseType = TypeFactory.defaultInstance().constructCollectionType(List.class, responseClassType);
        return this.request(url, "GET", null, requestData, responseType);
    }

    @Override
    public <T> T delete(final String url, final RequestData requestData, final Class<T> responseType) throws HttpException {
        return this.request(url, "DELETE", null, requestData, responseType);
    }

    @Override
    public <T> T head(final String url, final RequestData requestData, final Class<T> responseType) throws HttpException {
        return this.request(url, "HEAD", null, requestData, responseType);
    }

    @Override
    public <T, V extends AbstractModel> T post(final String url, final V data, final RequestData requestData, final Class<T> responseType) throws HttpException {
        return this.request(url, "POST", data, requestData, responseType);
    }

    @Override
    public <T, V extends AbstractModel> T put(final String url, final V data, final RequestData requestData, final Class<T> responseType) throws HttpException {
        return this.request(url, "PUT", data, requestData, responseType);
    }

    @Override
    public <T, V extends AbstractModel> T patch(final String url, final V data, final RequestData requestData, final Class<T> responseType) throws HttpException {
        return this.request(url, "PATCH", data, requestData, responseType);
    }

    private <T, V extends AbstractModel> T request(String url, String methodName, V requestBody, RequestData requestData, Class<T> responseType) throws HttpException {
        return requestInternal(url, methodName, requestBody, requestData, responseType, null);
    }

    private <T, V extends AbstractModel> T request(final String url, final String methodName, final V requestBody, final RequestData requestData, final JavaType responseType) throws HttpException {
        return requestInternal(url, methodName, requestBody, requestData, null, responseType);
    }

    private <T, V extends AbstractModel> T requestInternal(final String url, final String methodName, final V requestBody, final RequestData requestData, final Class<T> responseClassType, final JavaType responseJavaType) throws HttpException {
        try {
            final String requestIdentifier = this.getRequestIdentifier(url, methodName);

            if (!this.mocks.containsKey(requestIdentifier)) {
                throw new AssertionError("No mock data for request: " + requestIdentifier);
            }

            final List<DataMock> dataMocks = this.mocks.get(requestIdentifier);

            for (int i = 0; i < dataMocks.size(); i++) {
                final var dataMock = dataMocks.get(i);

                final Map<String, Object> urlParams = requestData.getQueryParams().entrySet().stream()
                        .filter(entry -> entry.getValue().isPresent())
                        .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().get()));
                if (!urlParams.equals(dataMock.getQueryParams()) && i == dataMocks.size() - 1) {
                    throw new AssertionError("No match for url query parameters : " + requestIdentifier);
                } else if (!urlParams.equals(dataMock.getQueryParams()) && i < dataMocks.size() - 1) {
                    continue;
                }

                // request
                if (requestBody != null) {
                    if (StringUtils.isEmpty(dataMock.getRequestFile())) {
                        throw new AssertionError("No mock request body provided: " + requestIdentifier);
                    }

                    final InputStream requestInputStream = this.getClass().getClassLoader().getResourceAsStream(dataMock.getRequestFile());

                    if (requestInputStream == null) {
                        throw new AssertionError(String.format("Failed to load request mock payload %s for request %s", dataMock.getRequestFile(), requestIdentifier));
                    }

                    final String requestPayloadMock = new BufferedReader(new InputStreamReader(requestInputStream)).lines().collect(Collectors.joining("\n"));

                    final boolean sameRequests = Mapper.get().readTree(requestPayloadMock).equals(Mapper.get().readTree(requestBody.toJson()));

                    if (!sameRequests && i == dataMocks.size() - 1) {
                        throw new AssertionError("No match for request payload " + requestIdentifier);
                    } else if (!sameRequests && i < dataMocks.size() - 1) {
                        continue;
                    }

                    if (!sameRequests) {
                        throw new AssertionError("No match for request payload: " + requestIdentifier);
                    }
                }

                // handle response
                // not interested in response at all
                if (Void.class.equals(responseClassType)) {
                    return null;
                }
                if (StringUtils.isEmpty(dataMock.getResponseFile())) {
                    throw new AssertionError("No mock response body provided: " + requestIdentifier);
                }

                final InputStream responseInputStream = this.getClass().getClassLoader().getResourceAsStream(dataMock.getResponseFile());
                if (responseInputStream == null) {
                    throw new AssertionError("Failed to load response mock payload " + dataMock.getResponseFile() + " for request" + requestIdentifier);
                }
                final String responsePayloadMock = new BufferedReader(new InputStreamReader(responseInputStream)).lines().collect(Collectors.joining("\n"));

                if (responseClassType != null) {
                    if (String.class.equals(responseClassType)) {
                        return responseClassType.cast(responsePayloadMock);
                    }
                    return Mapper.get().readValue(responsePayloadMock, responseClassType);
                } else if (responseJavaType != null) {
                    return Mapper.get().readValue(responsePayloadMock, responseJavaType);
                } else {
                    throw new IllegalArgumentException("Both responseType and typeReference are null");
                }
            }
        } catch (final Exception e) {
            throw new AssertionError("Failed to execute mocked request", e);
        }
        return null;
    }

    private String getRequestIdentifier(String url, String methodName) {
        return methodName + "_" + url;
    }

}
