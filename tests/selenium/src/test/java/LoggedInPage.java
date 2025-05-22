package src.test.java;

import src.test.java.BasePage;
import src.test.java.MainPage;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class LoggedInPage extends BasePage {
    public LoggedInPage(WebDriver driver) {
        super(driver);
    }

    public MainPage logout() {
        WebElement profileButton = getProfileButton();
        profileButton.click();
        WebElement logoutButton = waitAndReturnElement(By.xpath("//*[@id=\"basic-menu\"]/div[3]/ul/li/div/div"));
        logoutButton.click();
        return new MainPage(driver);
    }

    public WebElement getProfileButton() {
        return waitAndReturnElement(By.xpath("//*[@id=\"basic-button\"]"));
    }
}