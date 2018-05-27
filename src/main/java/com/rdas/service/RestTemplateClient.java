package com.rdas.service;

import com.rdas.model.Person;
import com.rdas.model.PersonType;
import com.rdas.util.JsonUtility;
import com.rdas.util.RequestResponseLoggingInterceptor;
import jdk.nashorn.internal.runtime.regexp.joni.encoding.ObjPtr;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.*;

/**
 * http://www.baeldung.com/spring-requestmapping
 * https://www.journaldev.com/3358/spring-requestmapping-requestparam-pathvariable-example
 * https://www.oodlestechnologies.com/blogs/Learn-To-Make-REST-calls-With-RestTemplate-In-Spring-Boot
 * https://howtodoinjava.com/spring/spring-restful/spring-restful-client-resttemplate-example/
 *
 */
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
        //restTemplate.setInterceptors(Collections.singletonList(new RequestResponseLoggingInterceptor()));

    }

    public List<Person> getPersons() throws IOException {
        List forObject = restTemplate.getForObject("/persons", List.class);
        return forObject;
    }

    public List<Person> getPersonsByType(PersonType type) {
        Map<String, Object> params = new HashMap<>();
        params.put("type", type);

        ResponseEntity<Person[]> response = restTemplate.getForEntity("/persons", Person[].class, params);
        log.info(Arrays.asList(response.getBody()).toString());
        return restTemplate.getForObject("/persons", List.class);
    }

    public Optional<Person> getPersonBy(Integer id, PersonType personType) {
        /*
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromUriString("/person")
                .queryParam("id", id)
                .queryParam("type", personType);

        Person forObject = restTemplate.getForObject(builder.toUriString(), Person.class);
        */
        String uri = "http://localhost:8088/person/id/{id}/type/{type}";

        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("type", personType);


        //ResponseEntity<Person> response = restTemplate.getForEntity(uri, Person.class, params);
        //System.out.println(Arrays.asList(response.getBody()));

        Person forObject = restTemplate.getForObject(uri, Person.class, params);

        return Optional.ofNullable(forObject);
    }

    public Optional<Person> getPersonsById(Integer id) {
        return null;
    }

    public boolean addPerson(Person person) throws IOException {
        return false;
    }
}
