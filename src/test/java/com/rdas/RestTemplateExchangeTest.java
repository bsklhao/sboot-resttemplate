package com.rdas;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.http.*;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.AsyncRequestCallback;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.ResponseExtractor;

import java.util.HashMap;
import java.util.Map;

/**
 * AsyncRestTemplate is Deprecated in Favour of org.springframework.web.reactive.function.client.WebClient
 */
@Slf4j
public class RestTemplateExchangeTest {

    @Test
    public void queryGoogle() throws Exception {
        AsyncRestTemplate asyncRestTemplate = new AsyncRestTemplate();
        String url = "http://google.com";
        HttpMethod method = HttpMethod.GET;
        Class<String> responseType = String.class;
        //create request entity using HttpHeaders
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        HttpEntity<String> requestEntity = new HttpEntity<String>("params", headers);

        ListenableFuture<ResponseEntity<String>> future = asyncRestTemplate.exchange(url, method, requestEntity, responseType);
        //waits for the result
        ResponseEntity<String> entity = future.get();

        log.info(entity.getBody());
    }

    @Test
    public void queryGoogleAsync() throws Exception {
        AsyncRestTemplate asyncRestTemplate = new AsyncRestTemplate();
        String url = "http://google.com";
        HttpMethod method = HttpMethod.GET;
        //create request entity using HttpHeaders
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        AsyncRequestCallback requestCallback = arg -> System.out.println(arg.getURI());

        ResponseExtractor<String> responseExtractor = arg -> arg.getStatusText();
        Map<String, String> urlVariable = new HashMap<>();
        urlVariable.put("q", "Concretepage");
        ListenableFuture<String> future = asyncRestTemplate.execute(url, method, requestCallback, responseExtractor, urlVariable);

        //waits for the result
        String result = future.get();
        log.info(result);

    }
}
