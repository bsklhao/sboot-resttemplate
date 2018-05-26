package com.rdas.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rdas.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class JsonUtility {
    @Autowired
    private ObjectMapper testOMapper;

    public List<Person> getPersons(String json) throws IOException {
        TypeReference<List<Person>> mapType = new TypeReference<List<Person>>() {};
        List<Person> persons = testOMapper.readValue(json, mapType);
        return persons;
    }

    public Person getPerson(String json) throws IOException {
        Person person = testOMapper.readValue(json, Person.class);
        return person;
    }

    public <T> String getString(T object) throws JsonProcessingException {
        return testOMapper.writeValueAsString(object);
    }
}
