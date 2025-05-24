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
    public void openAndTestMainPage() {
        MainPage mainPage = createNewMainPage();
        Assert.assertEquals("Odysee", mainPage.getPageTitle());
        Assert.assertTrue(mainPage.getBodyText().toLowerCase().contains("home"));
        WebElement darkModeButton = mainPage.waitAndReturnElement(By.xpath("//*[@id=\"app\"]/div/header/div/div[3]/div[1]/button"));
        Assert.assertTrue(darkModeButton.getAttribute("aria-label").equals("Dark"));
        darkModeButton.click();
        WebElement lightModeButton = mainPage.waitAndReturnElement(By.xpath("//*[@id=\"app\"]/div/header/div/div[3]/div[1]/button"));
        Assert.assertTrue(darkModeButton.getAttribute("aria-label").equals("Light"));
    }

    @Test
    public void loginAndLogout() {
        MainPage mainPage = createNewMainPage();
        LoggedInPage loggedInPage = loginNormally(mainPage);
        loggedInPage.openProfileMenu();
        WebElement userEmailSpan = loggedInPage.waitAndReturnElement(By.xpath("//*[@id=\"basic-menu\"]/div[3]/ul/li/div/span"));
        Assert.assertEquals(CredentialsProvider.getUsername(), userEmailSpan.getText());
        mainPage = loggedInPage.logout();
        WebElement loginButton = mainPage.getLoginButton();
        Assert.assertNotNull(loginButton);
        Assert.assertEquals("log in", loginButton.getText().toLowerCase());
    }

    @Test
    public void logoutThenLoginWithCookieManipulation() {
        //log in normally
        MainPage mainPage = createNewMainPage();
        LoggedInPage loggedInPage = loginNormally(mainPage);
        
        // log out by cookies
        Cookie authCookie = loggedInPage.getCookie("auth_token");
        loggedInPage.deleteCookie(authCookie);
        loggedInPage.reloadCurrentPage();
        WebElement loginButton = mainPage.getLoginButton();
        Assert.assertNotNull(loginButton);
        try {
            loggedInPage.getProfileButtonNow();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NoSuchElementException);
        }
        Assert.assertEquals("log in", loginButton.getText().toLowerCase());

        // log back in with cookies
        mainPage.addCookie(authCookie);
        mainPage.reloadCurrentPage();
        loggedInPage.openProfileMenu();
        WebElement userEmailSpan = loggedInPage.waitAndReturnElement(By.xpath("//*[@id=\"basic-menu\"]/div[3]/ul/li/div/span"));
        Assert.assertEquals(CredentialsProvider.getUsername(), userEmailSpan.getText());

        // log out normally
        loggedInPage.logout();
    }

    @Test
    public void changeSettings() {
        MainPage mainPage = createNewMainPage();
        LoggedInPage loggedInPage = loginNormally(mainPage);

        SettingsPage settingsPage = loggedInPage.goToSettingsPage();
        settingsPage.toggleHideMembersOnlyContentOption();
        settingsPage.toggleHideReportsOption();
        settingsPage.toggleHideShortContentOption();
        loggedInPage = settingsPage.clickOnSave();
        
        Assert.assertEquals("https://odysee.com/", loggedInPage.getCurrentUrl());
    }

    private MainPage createNewMainPage() {
        MainPage mainPage = new MainPage(driver);
        mainPage.openBaseUrl();
        mainPage.tryDeclineCookies();
        return mainPage;
    }

    private LoggedInPage loginNormally(MainPage mainPage) {
        LoginPage loginPage = mainPage.clickOnLoginButton();
        return loginPage.login(CredentialsProvider.getUsername(), CredentialsProvider.getPassword());
    }

    @After
    public void close() {
        if (this.driver != null) {
            this.driver.quit();
        }
    }
}

