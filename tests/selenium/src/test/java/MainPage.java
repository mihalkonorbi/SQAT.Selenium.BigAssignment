package src.test.java;

import src.test.java.BasePage;
import src.test.java.LoginPage;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By.ById;

public class MainPage extends BasePage {
    private static final String BASE_URL = "https://odysee.com";
    
    public MainPage(WebDriver driver) {
        super(driver);
    }

    public void openBaseUrl() {
        this.driver.get(BASE_URL);
    }

    public WebElement getLoginButton() {
        return waitAndReturnElement(By.xpath("//*[@id=\"app\"]/div/header/div/div[3]/div[2]/a[1]"));
    }

    public LoginPage clickOnLoginButton() {
        WebElement loginButton = getLoginButton();
        loginButton.click();
        return new LoginPage(driver);
    }

    public void tryDeclineCookies() {
        try {
            WebElement rejectButton = waitAndReturnElement(By.id("onetrust-reject-all-handler"));
            rejectButton.click();
        } catch (Exception e) {
            
        }
    }
}
