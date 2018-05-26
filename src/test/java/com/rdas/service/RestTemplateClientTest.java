package com.rdas.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rdas.model.Person;
import com.rdas.model.PersonType;
import com.rdas.util.JsonUtility;
import org.junit.Before;
import org.junit.Ignore;
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
        peoples.add(Person.builder().type(PersonType.MALE).age(21).name("Raja").id(24).build());
        peoples.add(Person.builder().id(1).name("Rana").age(50).type(PersonType.MALE).build());
        peoples.add(Person.builder().id(1).name("Jennifer").age(49).type(PersonType.FEMALE).build());
        String detailsString = testOMapper.writeValueAsString(peoples);

        this.server.expect(MockRestRequestMatchers.requestTo("http://localhost:8080/persons"))
                .andRespond(withSuccess(detailsString, MediaType.APPLICATION_JSON));
    }

    @Test
    public void smokeTests() {
        assertThat(client).isNotNull();
        assertThat(server).isNotNull();
        assertThat(testOMapper).isNotNull();
    }


    @Test
    public void whenCallingGetUserDetails_thenClientMakesCorrectCall()  throws Exception {
        List<Person> persons = this.client.getPersons();
        System.out.println(persons);
        assertThat(persons.size()).isEqualTo(3);
//        assertThat(details.getName()).isEqualTo("John Smith");
    }


//    @Test
//    public void whenCallingGetUserDetails()  throws Exception {
//        List<Person> persons = this.client.getPersonsByType(PersonType.MALE);
//        System.out.println(persons);
//    }
}