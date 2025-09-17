package io.mailtrap.testutils;

import lombok.Data;
import lombok.NonNull;

import java.util.Collections;
import java.util.Map;

@Data
public class DataMock {

    @NonNull
    private final String url;

    @NonNull
    private final String methodName;

    private final String requestFile;
    private final String responseFile;

    @NonNull
    private final Map<String, ?> queryParams;

    public static DataMock build(final String url, final String methodName, final String requestFile, final String responseFile) {
        return new DataMock(
                url,
                methodName,
                requestFile,
                responseFile,
                Collections.emptyMap()
        );
    }

    public static DataMock build(final String url, final String methodName, final String requestFile, final String responseFile, final Map<String, ?> queryParams) {
        return new DataMock(
                url,
                methodName,
                requestFile,
                responseFile,
                queryParams
        );
    }
}
