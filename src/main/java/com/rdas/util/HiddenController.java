package com.rdas.util;

import com.rdas.model.Person;
import com.rdas.model.PersonType;
import com.rdas.service.RestTemplateClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * This controller is to be used for testing
 */
@RestController
public class HiddenController {

    @Autowired
    private RestTemplateClient restTemplateClient;

    /**
     * curl -i  http://localhost:8080/rs/person/id/1/type/MALE
     */
    @GetMapping("/person/id/{id}/type/{type}")
    public Optional<Person> getPersonsBy(@PathVariable("id") Integer id, @PathVariable("type") PersonType type) {
        return restTemplateClient.getPersonBy(id, type);
    }

    /**
     * curl -i  http://localhost:8080/rs/persons
     */
    @GetMapping("/persons")
    public List<Person> getPersons() throws IOException {
        return restTemplateClient.getPersons();
    }

    /**
     * curl -i  http://localhost:8080/rs/personsByType?type=MALE
     */
    @GetMapping("/personsByType")
    public List<Person> getPersonsByType(@RequestParam("type") PersonType type) {
        return restTemplateClient.getPersonsByType(type);
    }
}
