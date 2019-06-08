package br.com.exmart.rtdpjlite.service.interceptor;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

public class MyClientHttpRequestInterceptor implements org.springframework.http.client.ClientHttpRequestInterceptor{

    @Override
    public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] body, ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {
        HttpHeaders headers = httpRequest.getHeaders();
        headers.add("X-Authorization", "XPTO1234");
        return clientHttpRequestExecution.execute(httpRequest, body);
    }
}
