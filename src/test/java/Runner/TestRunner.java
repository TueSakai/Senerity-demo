package Runner;

import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;


@CucumberOptions(
        glue = "StepDefinitions",
        features = "src/test/resources/features/cucumber.feature"
)
@RunWith(CucumberWithSerenity.class)
public class TestRunner {
}
