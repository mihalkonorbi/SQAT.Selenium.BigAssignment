package src.test.java;

import org.junit.*;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.openqa.selenium.support.ui.ExpectedConditions;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.NoSuchElementException;


public class BasePage {
    public static final String BASE_URL = "https://odysee.com";
    protected WebDriver driver;
    protected WebDriverWait wait;
    
    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, 10);
    }

    public void get(String url) {
        driver.get(url);
    }
    
    protected WebElement waitAndReturnElement(By locator) {
        this.wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        return this.driver.findElement(locator);
    } 

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
    
    public void reloadCurrentPage() {
        driver.navigate().refresh();
    }

    public String getBodyText() {
        WebElement bodyElement = this.waitAndReturnElement(By.tagName("body"));
        return bodyElement.getText();
    }
   
    public String getPageTitle() {
        return driver.getTitle();
    }

    public Cookie getCookie(String cookieName) {
        return driver.manage().getCookieNamed(cookieName);
    }

    public void deleteCookie(String cookieName) {
        driver.manage().deleteCookieNamed(cookieName);;
    }

    public void deleteCookie(Cookie cookie) {
        driver.manage().deleteCookie(cookie);;
    }

    public void deleteAllCookies() {
        driver.manage().deleteAllCookies();
    }

    public void addCookie(Cookie cookie) {
        driver.manage().addCookie(cookie);
    }

    public void addCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        driver.manage().addCookie(cookie);
    }

    protected void sleepForCheck(long time) {
        try {
            Thread.sleep(time);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}
