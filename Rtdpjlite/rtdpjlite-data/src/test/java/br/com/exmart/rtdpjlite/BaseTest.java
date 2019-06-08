package br.com.exmart.rtdpjlite;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.exmart.rtdpjlite.repository.NaturezaRepositoryTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = {Application.class})
@Ignore
public class BaseTest {
	protected static Logger logger= LoggerFactory.getLogger(BaseTest.class);
	
	protected Logger getLogger() {
		return logger;
	}
}
