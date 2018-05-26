package com.rdas.service;

import com.rdas.model.Person;
import com.rdas.model.PersonType;
import com.rdas.util.JsonUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.io.IOException;
import java.util.*;

@Slf4j
@Service
public class RestTemplateClient {
    private JsonUtility jsonUtility;
    private String baseUrl;
    private final RestTemplate restTemplate;

    @Autowired
    public RestTemplateClient(JsonUtility utility,
                              @Value("${user.service.url}") String url,
                              RestTemplateBuilder templateBuilder) {
        jsonUtility = utility;
        baseUrl = url;
        restTemplate = templateBuilder.build();
        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(url));

    }

    public List<Person> getPersons() throws IOException {
        return restTemplate.getForObject("/persons", List.class);
    }

    public List<Person> getPersonsByType(PersonType type) {
        Map<String, Object> params = new HashMap<>();
        params.put("type", type);

        ResponseEntity<Person[]> response = restTemplate.getForEntity("/persons", Person[].class, params);
        log.info(Arrays.asList(response.getBody()).toString());
        return restTemplate.getForObject("/persons", List.class);
    }
//
//    public Optional<Person> getPersonsById(Integer id) {
//        return null;
//    }
//
//    public boolean addPerson(Person person) throws IOException {
//        return false;
//    }
}
