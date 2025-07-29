package testrunner;

import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = {"src/test/java/api/features", "src/test/java/web/features"},
        glue = {"api.stepdefinitions", "web.stepdefinitions"},
        plugin = {
                "pretty",
                "json:build/reports/cucumber-report.json",
                "html:build/reports/cucumber-html-report"
        },
        monochrome = true,
        tags = "@api or @web"
)
public class TestRunner {}
