package ru.practicum.main.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.main.model.dto.EndpointHit;

import java.util.Map;

@Service
public class EventClient extends BaseClient{

    @Autowired
    public EventClient(@Value("${stats.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build()
        );
    }

    public ResponseEntity<Object> saveEndpointHit(EndpointHit endpointHit) {
        return post(endpointHit);
    }

    public ResponseEntity<Object> getStatistic(Map<String, Object> parameters) {
        return get(parameters);
    }

}
