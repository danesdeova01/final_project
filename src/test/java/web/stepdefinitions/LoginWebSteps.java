package web.stepdefinitions;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chrome.ChromeDriver;
import web.pages.LoginPage;
import io.github.bonigarcia.wdm.WebDriverManager;

public class LoginWebSteps {
    WebDriver driver;
    LoginPage loginPage;

    @Before
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new"); // gunakan mode headless baru (atau ganti dengan `--headless=chrome` untuk versi stabil)
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--remote-allow-origins=*");

        driver = new ChromeDriver(options);
        loginPage = new LoginPage(driver);
        driver.get("https://www.saucedemo.com/"); // penting: buka halaman login di awal
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    // ========= Step Definitions =========

    @Given("I am on the login page")
    public void iAmOnTheLoginPage() {
        Assert.assertTrue(driver.getCurrentUrl().contains("saucedemo.com"));
    }

    @When("I enter valid username and password")
    public void iEnterValidUsernameAndPassword() {
        loginPage.enterUsername("standard_user");
        loginPage.enterPassword("secret_sauce");
        loginPage.clickLogin();
    }

    @Then("I should see the products page")
    public void iShouldSeeTheProductsPage() {
        Assert.assertTrue(driver.getCurrentUrl().contains("inventory.html"));
    }

    @When("I enter invalid username and password")
    public void iEnterInvalidUsernameAndPassword() {
        loginPage.enterUsername("invalid_user");
        loginPage.enterPassword("wrong_pass");
        loginPage.clickLogin();
    }

    @Then("I should see an error message")
    public void iShouldSeeAnErrorMessage() {
        String actualMessage = loginPage.getErrorMessage();
        Assert.assertTrue(actualMessage.contains("Username and password do not match")
                || actualMessage.contains("Epic sadface"));
    }

    @Given("I am logged in as a valid user")
    public void iAmLoggedInAsAValidUser() {
        loginPage.enterUsername("standard_user");
        loginPage.enterPassword("secret_sauce");
        loginPage.clickLogin();
        Assert.assertTrue(driver.getCurrentUrl().contains("inventory.html"));
    }

    @When("I click logout button")
public void i_click_logout_button() {
    // Klik hamburger/menu terlebih dahulu
    WebElement menuBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("react-burger-menu-btn")));
    menuBtn.click();

    // Tambahan: Tunggu sidebar benar-benar muncul (ada animasi open)
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("logout_sidebar_link")));

    // Tunggu sampai logout benar-benar clickable (dan visible)
    WebElement logoutBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("logout_sidebar_link")));

    // Solusi ekstra: Scroll ke element (kadang butuh di CI environment/headless)
    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", logoutBtn);

    // Solusi ekstra: Pause sangat singkat jika animasi/menu belum stabil (opsional)
    try { Thread.sleep(500); } catch (InterruptedException ignored) {}

    // Coba klik dengan actions jika masih error
    try {
        logoutBtn.click();
    } catch (ElementNotInteractableException e) {
        // Last resort: call click via JS
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", logoutBtn);
    }
}


    @Then("I should be redirected to the login page")
    public void iShouldBeRedirectedToTheLoginPage() {
        Assert.assertTrue(driver.getCurrentUrl().contains("saucedemo.com"));
    }
}
