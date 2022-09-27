package ru.practicum.main.client;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class BaseClient {

    protected final RestTemplate restTemplate;

    protected <T> ResponseEntity<Object> post(T body) {
        return makeAndSendRequest(HttpMethod.POST, "/hit", body, null);
    }

    protected <T> ResponseEntity<Object> get(@Nullable Map<String, Object> parameters) {
        return makeAndSendRequest(HttpMethod.GET, "/stats", null, parameters);
    }

    private <T> ResponseEntity<Object> makeAndSendRequest(HttpMethod method, String path, T body,
                                                          @Nullable Map<String, Object> parameters) {
        HttpEntity<T> httpEntity = new HttpEntity<>(body, defaultHeaders());

        ResponseEntity<Object> statisticServerResponse;
        try {
            if (parameters != null) {
                statisticServerResponse = restTemplate.exchange(path, method, httpEntity, Object.class, parameters);
            } else {
                statisticServerResponse = restTemplate.exchange(path, method, httpEntity, Object.class);
            }
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }

        return prepareGatewayResponse(statisticServerResponse);
    }

    private ResponseEntity<Object> prepareGatewayResponse(ResponseEntity<Object> statisticServerResponse) {
        if (statisticServerResponse.getStatusCode().is2xxSuccessful()) {
            return statisticServerResponse;
        }
        ResponseEntity.BodyBuilder bodyBuilder = ResponseEntity.status(statisticServerResponse.getStatusCode());
        if (statisticServerResponse.hasBody()) {
            bodyBuilder.body(statisticServerResponse.getBody());
        }
        return bodyBuilder.build();
    }

    private HttpHeaders defaultHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(List.of(MediaType.APPLICATION_JSON));
        return httpHeaders;
    }
}
