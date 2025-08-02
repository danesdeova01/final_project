package web.stepdefinitions;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chrome.ChromeDriver;
import web.pages.LoginPage;

public class LoginWebSteps {
    WebDriver driver;
    LoginPage loginPage;

    @Before
public void setUp() {
    WebDriverManager.chromedriver().setup();
    ChromeOptions options = new ChromeOptions();
    options.addArguments("--headless=old");             // gunakan mode headless lama
    options.addArguments("--no-sandbox");
    options.addArguments("--disable-dev-shm-usage");
    options.addArguments("--disable-gpu");
    options.addArguments("--window-size=1920,1080");
    options.addArguments("--remote-allow-origins=*");

    driver = new ChromeDriver(options);
    loginPage = new LoginPage(driver);
}


    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Given("User is on the login page")
    public void user_is_on_the_login_page() {
        Assert.assertTrue(driver.getCurrentUrl().contains("saucedemo"));
    }

    @When("User enters username {string} and password {string}")
    public void user_enters_username_and_password(String username, String password) {
        loginPage.enterUsername(username);
        loginPage.enterPassword(password);
    }

    @And("User clicks login")
    public void user_clicks_login() {
        loginPage.clickLogin();
    }

    @Then("User sees error message {string}")
    public void user_sees_error_message(String expectedMessage) {
        Assert.assertEquals(expectedMessage, loginPage.getErrorMessage());
    }
}
