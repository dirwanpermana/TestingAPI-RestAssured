package cucumber.runners;

import org.testng.annotations.AfterSuite;
import cucumber.helper.GenerateReport;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
    features = "src/test/resources/features",
    glue = "cucumber.definitions",
    plugin = {
        "pretty",
        "json:target/cucumber.json",
        "html:target/cucumber-html-report"
    },
    monochrome = true
)
public class TestRunner extends AbstractTestNGCucumberTests {

    static {
        System.out.println(">>> TestRunner loaded");
    }

    @AfterSuite
    public void afterSuite() {
        System.out.println(">>> Generating report...");
        GenerateReport.generateReport();
        System.out.println(">>> Report generated!");
    }
}
