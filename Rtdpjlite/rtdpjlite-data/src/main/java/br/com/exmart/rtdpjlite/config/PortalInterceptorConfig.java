package br.com.exmart.rtdpjlite.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
@Component
public class PortalInterceptorConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Register guest interceptor with single path pattern
        registry.addInterceptor(new PortalInterceptor()).addPathPatterns("/portal/**").excludePathPatterns("/portal/usuario/login/**","/portal/link/**");
    }

    @Bean
    public InternalResourceViewResolver resolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".jsp");
        return resolver;
    }
    @Bean
    public PortalInterceptorConfig pagePopulationInterceptor() {
        return new PortalInterceptorConfig();
    }
}
