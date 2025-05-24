package src.test.java;

import src.test.java.BasePage;
import src.test.java.MainPage;
import src.test.java.SettingsPage;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class LoggedInPage extends BasePage {
    public LoggedInPage(WebDriver driver) {
        super(driver);
    }

    public MainPage logout() {
        openProfileMenu();
        WebElement logoutButton = waitAndReturnElement(By.xpath("//*[@id=\"basic-menu\"]/div[3]/ul/li/div"));
        logoutButton.click();
        return new MainPage(driver);
    }

    public SettingsPage goToSettingsPage() {
        openProfileMenu();
        WebElement settingsButton = waitAndReturnElement(By.xpath("//*[@id=\"basic-menu\"]/div[3]/ul/a[9]"));
        settingsButton.click();
        return new SettingsPage(driver);
    }

    public WebElement getProfileButton() {
        return waitAndReturnElement(By.xpath("//*[@id=\"basic-button\"]"));
    }

    public WebElement getProfileButtonNow() {
        return driver.findElement(By.xpath("//*[@id=\"basic-button\"]"));
    }

    public void openProfileMenu() {
        try {
            driver.findElement(By.xpath("//*[@id=\"basic-menu\"]/div[3]/ul"));
            // already open
        } catch (NoSuchElementException e) {
            WebElement profileButton = getProfileButton();
            profileButton.click();
        }
    }
}