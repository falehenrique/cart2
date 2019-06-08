package br.com.exmart.app;

import br.com.exmart.rtdpjlite.repository.LoteRepository;
import br.com.exmart.rtdpjlite.service.LoteService;
import br.com.exmart.rtdpjlite.service.ProtocoloService;
import br.com.exmart.util.BeanLocator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.vaadin.spring.events.annotation.EnableEventBus;

import java.io.IOException;

@SpringBootApplication//(scanBasePackageClasses = { MyUI.class, Application.class, BeanLocator.class, ServiceFacade.class, NaturezaService.class})
@EnableAutoConfiguration
@ComponentScan(basePackages = {"br.com.exmart"})
@EntityScan(basePackages = {"br.com.exmart"})
@EnableJpaRepositories(basePackages="br.com.exmart")
@EnableEventBus
@EnableAsync
//@EnableCaching
public class Application extends SpringBootServletInitializer {

	public static final String APP_URL = "/";
	public static final String LOGIN_URL = "/login.html";
	public static final String LOGOUT_URL = "/login.html?logout";
	public static final String LOGIN_FAILURE_URL = "/login.html?error";
	public static final String LOGIN_PROCESSING_URL = "/login";

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
//		try {
//			BeanLocator.find(LoteService.class).gerarArquivoDonload(
//					BeanLocator.find(LoteRepository.class).findAll().get(0)
//			);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

	}
//$2a$10$GNg4TK8fE.Jk/ksF2IBaH.1LGpWer7qmuDUiru8rec44QvSu7ypj.
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Application.class);
	}
}
