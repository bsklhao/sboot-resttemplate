package com.rdas.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rdas.model.Person;
import com.rdas.model.PersonType;
import com.rdas.util.JsonUtility;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {RestTemplateClientTest.LocalConfig.class})
@RestClientTest(RestTemplateClient.class)
public class RestTemplateClientTest {

    // TODO : This is done, so that we only create whats needed here in this test.
    //@ComponentScan({"com.rdas.service", "com.rdas.util"})
    @Configuration
    public static class LocalConfig {
        @Bean
        public JsonUtility jsonUtility() {
            return new JsonUtility();
        }

        @Autowired
        private JsonUtility utility;
        @Value("${user.service.url}")
        private String url;
        @Autowired
        private RestTemplateBuilder templateBuilder;

        @Bean
        public RestTemplateClient restTemplateClient() {
            return new RestTemplateClient(utility, url, templateBuilder);
        }
    }

    @Autowired
    private RestTemplateClient client;

    @Autowired
    private MockRestServiceServer server;

    @Autowired
    private ObjectMapper testOMapper;

    private List<Person> peoples;

    @Before
    public void setUp() throws Exception {
        peoples = new ArrayList<>();

    }

    @Test
    public void smokeTests() {
        assertThat(client).isNotNull();
        assertThat(server).isNotNull();
        assertThat(testOMapper).isNotNull();
    }

    @Test
    public void whenCallingPersona_thenClientGetsMockedPersonsCorrectly()  throws Exception {
        peoples.clear();

        peoples.add(Person.builder().type(PersonType.MALE).age(21).name("Raja").id(14).build());
        peoples.add(Person.builder().id(12).name("Rana").age(50).type(PersonType.MALE).build());
        peoples.add(Person.builder().id(19).name("Jennifer").age(49).type(PersonType.FEMALE).build());
        String detailsString = testOMapper.writeValueAsString(peoples);

        server.expect(MockRestRequestMatchers.requestTo("http://localhost:8088/persons"))
                .andRespond(withSuccess(detailsString, MediaType.APPLICATION_JSON));


        List<Person> persons = client.getPersons();
        System.out.println(persons);
        assertThat(persons.size()).isEqualTo(3);
    }

    @Test
    public void whenCallingGetUsersByType_ClientReturnsCorrectly()  throws Exception {
        peoples.clear();

        peoples.add(Person.builder().type(PersonType.MALE).age(21).name("Raja").id(14).build());
        peoples.add(Person.builder().id(12).name("Rana").age(50).type(PersonType.MALE).build());
        String detailsString = testOMapper.writeValueAsString(peoples);

        server.expect(MockRestRequestMatchers.requestTo("http://localhost:8088/personsByType?type=MALE"))
                .andRespond(withSuccess(detailsString, MediaType.APPLICATION_JSON));


        List<Person> persons = this.client.getPersonsByType(PersonType.MALE);
        assertThat(persons.size()).isEqualTo(2);
    }
}