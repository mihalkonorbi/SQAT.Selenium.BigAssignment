package src.test.java;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.*;

import src.test.java.BasePage;
import src.test.java.CredentialsProvider;
import src.test.java.LoggedInPage;
import src.test.java.MainPage;
import src.test.java.StaticPageEntity;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.stream.Stream;

import static org.junit.Assert.assertNotNull;

import java.net.MalformedURLException;
import org.junit.*;

public class SeleniumTaskTest {
    private RemoteWebDriver driver;
    private WebDriverWait wait;
    private List<StaticPageEntity> staticPages = Arrays.asList(
        new StaticPageEntity("/$/explore",  
            By.xpath("//*[@id=\"main-content\"]/h1/label"),
            By.xpath("//*[@id=\"main-content\"]/div/div/div/div/div[1]/div/div[1]"),
            By.xpath("//*[@id=\"main-content\"]/div/div/div/div/div[1]/div/div[3]/div/div"),
            By.xpath("//*[@id=\"navigation-button\"]")),
        new StaticPageEntity("/$/settings",
            By.xpath("//*[@id=\"language_select\"]"),
            By.xpath("//*[@id=\"main-content\"]/section/div/div/div[2]/div[2]/div"),
            By.xpath("//*[@id=\"homepage_select\"]"),
            By.xpath("//*[@id=\"theme_select\"]"),
            By.xpath("//*[@id=\"main-content\"]/section/div/div/div[4]/div[2]/fieldset-section[2]/div"),
            By.xpath("//*[@id=\"app\"]/div/header/div/div[1]/button"),
            By.xpath("//*[@id=\"app\"]/div/div[4]/div[1]/nav/div"))
    );

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

    @Test
    public void testStaticPages() {
        BasePage basePage = new BasePage(driver);
        staticPages.forEach(staticPage -> {
            basePage.get(BasePage.BASE_URL + staticPage.getUrlPath());
            staticPage.getByList().forEach(by -> {
                WebElement webElement = basePage.waitAndReturnElement(by);
                Assert.assertNotNull(webElement);
            });
        });
    }

    @Test
    public void historyTest() {
        BasePage basePage = new BasePage(driver);
        basePage.get(BasePage.BASE_URL);
        Stack<String> urls = new Stack<>();
        Stream.of("/$/explore", "/$/featured", "/$/popculture", "/$/artists", "/$/education", "/$/lifestyle",
        "/$/spooky", "/$/gaming", "/$/tech", "/$/comedy", "/$/music", "/$/sports", "/$/universe", "/$/finance",
        "/$/spirituality", "/$/news").forEach(urlPath -> {
            String url = BasePage.BASE_URL + urlPath;
            basePage.get(url);
            urls.push(url);
        });
        basePage.get(BasePage.BASE_URL);
        while (!urls.isEmpty()) {
            basePage.goBack();
            Assert.assertEquals(urls.pop(), basePage.getCurrentUrl());
        }
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

