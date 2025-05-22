package src.test.java;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.*;

import src.test.java.CredentialsProvider;
import src.test.java.LoggedInPage;
import src.test.java.MainPage;

import java.net.URL;

import static org.junit.Assert.assertNotNull;

import java.net.MalformedURLException;
import org.junit.*;

public class SeleniumTaskTest {
    private RemoteWebDriver driver;
    private WebDriverWait wait;

    @Before
    public void setup() throws MalformedURLException {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        this.driver = new RemoteWebDriver(new URL("http://selenium:4444/wd/hub"), options);
        this.driver.manage().window().maximize();
    }

    @Test
    public void openMainPage() {
        MainPage mainPage = new MainPage(driver);
        mainPage.openBaseUrl();
        mainPage.tryDeclineCookies();
        System.out.println(mainPage.getBodyText());
        Assert.assertTrue(mainPage.getBodyText().toLowerCase().contains("home"));
        WebElement darkModeButton = mainPage.waitAndReturnElement(By.xpath("//*[@id=\"app\"]/div/header/div/div[3]/div[1]/button"));
        Assert.assertTrue(darkModeButton.getAttribute("aria-label").equals("Dark"));
        darkModeButton.click();
        WebElement lightModeButton = mainPage.waitAndReturnElement(By.xpath("//*[@id=\"app\"]/div/header/div/div[3]/div[1]/button"));
        Assert.assertTrue(darkModeButton.getAttribute("aria-label").equals("Light"));
    }

    @Test
    public void loginAndLogout() {
        MainPage mainPage = new MainPage(driver);
        mainPage.openBaseUrl();
        mainPage.tryDeclineCookies();
        LoginPage loginPage = mainPage.clickOnLoginButton();
        LoggedInPage loggedInPage = loginPage.login(CredentialsProvider.getUsername(), CredentialsProvider.getPassword());
        mainPage = loggedInPage.logout();
        WebElement loginButton = mainPage.getLoginButton();
        Assert.assertNotNull(loginButton);
        Assert.assertEquals("log in", loginButton.getText().toLowerCase());
    }

    @After
    public void close() {
        if (this.driver != null) {
            this.driver.quit();
        }
    }
}

