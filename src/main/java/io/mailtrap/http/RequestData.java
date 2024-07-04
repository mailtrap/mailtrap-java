package io.mailtrap.http;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Data class representing additional request data, such as query parameters and headers.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestData {

    /**
     * The map of query parameters for the request.
     * The keys represent parameter names, and the values represent optional parameter values.
     */
    private Map<String, ? extends Optional<?>> queryParams = new HashMap<>();

    /**
     * The map of headers for the request.
     * The keys represent header names, and the values represent header values.
     */
    private Map<String, ?> headers = new HashMap<>();

    /**
     * Builds a map of query parameters from the specified key-value pairs.
     *
     * @param entries The key-value pairs representing the query parameters.
     * @return The map of query parameters.
     */
    @SafeVarargs
    public static <T extends Optional<?>> Map<String, T> buildQueryParams(Map.Entry<String, T>... entries) {
        Map<String, T> params = new HashMap<>();
        for (Map.Entry<String, T> entry : entries) {
            params.put(entry.getKey(), entry.getValue());
        }
        return params;
    }
}
