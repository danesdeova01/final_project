package web.stepdefinitions;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;
import web.pages.LoginPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.Assert.*;
import java.time.Duration;

public class LoginWebSteps {
    private WebDriver driver;
    private LoginPage loginPage;
    private WebDriverWait wait;

    @Before
    public void setup() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--incognito");
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        loginPage = new LoginPage(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @After
    public void teardown() {
        if(driver != null) driver.quit();
    }

    @Given("I am on the login page")
    public void i_am_on_the_login_page() {
        driver.get("https://www.saucedemo.com/");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
    }

    @When("I enter valid username and password")
    public void i_enter_valid_username_and_password() {
        wait.until(ExpectedConditions.visibilityOf(loginPage.getUsernameField()));
        loginPage.enterUsername("standard_user");
        loginPage.enterPassword("secret_sauce");
        loginPage.clickLogin();
    }

    @Then("I should see the products page")
    public void i_should_see_the_products_page() {
        wait.until(ExpectedConditions.titleContains("Swag Labs"));
        assertTrue(driver.getTitle().contains("Swag Labs"));
        assertTrue(driver.getCurrentUrl().contains("inventory.html"));
    }

    @When("I enter invalid username and password")
    public void i_enter_invalid_username_and_password() {
        wait.until(ExpectedConditions.visibilityOf(loginPage.getUsernameField()));
        loginPage.enterUsername("wrong_user");
        loginPage.enterPassword("wrong_password");
        loginPage.clickLogin();
    }

    @Then("I should see an error message")
    public void i_should_see_an_error_message() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h3[data-test='error']")));
        String errorText = loginPage.getErrorMessage();
        assertTrue(errorText.toLowerCase().contains("username and password do not match") ||
                errorText.toLowerCase().contains("error"));
    }

    @Given("I am logged in as a valid user")
    public void i_am_logged_in_as_a_valid_user() {
        i_am_on_the_login_page();
        i_enter_valid_username_and_password();
        i_should_see_the_products_page();
    }

    @When("I click logout button")
    public void i_click_logout_button() {
        WebElement menuBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("react-burger-menu-btn")));
        menuBtn.click();

        WebElement logoutBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("logout_sidebar_link")));
        logoutBtn.click();
    }

    @Then("I should be redirected to the login page")
    public void i_should_be_redirected_to_the_login_page() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user-name")));

        assertTrue(driver.getCurrentUrl().contains("saucedemo.com"));
    }
}
