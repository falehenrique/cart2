package br.com.exmart.ui;

import br.com.exmart.ui.tools.ActionTools;
import com.vaadin.testbench.Parameters;
import com.vaadin.testbench.TestBenchTestCase;
import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.Assert.assertFalse;

/**
 * Base class for all our tests, allowing us to change the applicable driver,
 * test URL or other configurations in one place. For an example of setting up a
 * hub configuration, see {@link UsingHubITCase}.
 * 
 */
public class TestBase extends TestBenchTestCase {

    protected String ambiente = "prod"; //prod ou dev

    protected String baseUrl = (ambiente.equals("prod")?"http://2osasco.lumera.com.br":"http://localhost:8080/");

    //public static final String baseUrl = "http://192.168.1.34:8080/";
    public ActionTools actionTools;


    @Before
    public void setUp() throws Exception {
        actionTools = new ActionTools();
        // Create a new Selenium driver - it is automatically extended to work
        // with TestBench
        WebDriver driver = new ChromeDriver();
        setDriver(driver);

        Parameters.setMaxScreenshotRetries(2);

        // Open the test application URL with the ?restartApplication URL
        // parameter to ensure Vaadin provides us with a fresh UI instance.
        //getDriver().get(baseUrl + "?restartApplication");
        getDriver().get(baseUrl);

        // If you deploy using WTP in Eclipse, this will fail. You should
        // update baseUrl to point to where the app is deployed.
        String pageSource = getDriver().getPageSource();
        String errorMsg = "Application is not available at " + baseUrl + ". Server not started?";
        assertFalse(errorMsg, pageSource.contains("HTTP Status 404") ||
                pageSource.contains("can't establish a connection to the server"));
    }

    @After
    public void tearDown() throws Exception {
        // Calling quit() on the driver closes the test browser.
        // When called like this, the browser is immediately closed on _any_
        // error. If you wish to take a screenshot of the browser at the time
        // the error occurred, you'll need to add the ScreenshotOnFailureRule
        // to your test and remove this call to quit().
        getDriver().quit();
    }
}
