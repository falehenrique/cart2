package br.com.exmart.rtdpjlite.config;

import br.com.exmart.rtdpjlite.service.interceptor.MyClientHttpRequestInterceptor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Configuration
public class RestConfiguration {
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {

        RestTemplate retorno = builder.build();
        retorno.setInterceptors(Collections.singletonList(new MyClientHttpRequestInterceptor()));
//        retorno.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
        return  retorno;
    }
}
