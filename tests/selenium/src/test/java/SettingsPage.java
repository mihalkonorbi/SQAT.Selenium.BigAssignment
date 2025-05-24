package src.test.java;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import src.test.java.BasePage;
import src.test.java.LoggedInPage;
import src.test.java.MainPage;

public class SettingsPage extends BasePage {
    
    public SettingsPage(WebDriver driver) {
        super(driver);
    }

    public void toggleHideMembersOnlyContentOption() {
        WebElement contentSettingsButton = waitAndReturnElement(By.xpath("//*[@id=\"app\"]/div/div[4]/div[1]/nav/div/ul/li[3]/button"));
        contentSettingsButton.click();
        WebElement hideMembersOnlyContentCheckBox = waitAndReturnElement(By.xpath("//*[@id=\"content\"]/div/div[2]/div[1]/div[2]/div"));
        hideMembersOnlyContentCheckBox.click();
    }

    public void toggleHideReportsOption() {
        WebElement contentSettingsButton = waitAndReturnElement(By.xpath("//*[@id=\"app\"]/div/div[4]/div[1]/nav/div/ul/li[3]/button"));
        contentSettingsButton.click();
        WebElement hideMembersOnlyContentCheckBox = waitAndReturnElement(By.xpath("//*[@id=\"content\"]/div/div[2]/div[2]/div[2]/div"));
        hideMembersOnlyContentCheckBox.click();
    }

    public void toggleHideShortContentOption() {
        WebElement contentSettingsButton = waitAndReturnElement(By.xpath("//*[@id=\"app\"]/div/div[4]/div[1]/nav/div/ul/li[3]/button"));
        contentSettingsButton.click();
        WebElement hideMembersOnlyContentCheckBox = waitAndReturnElement(By.xpath("//*[@id=\"content\"]/div/div[2]/div[3]/div[2]/div"));
        hideMembersOnlyContentCheckBox.click();
    }

    public LoggedInPage clickOnSave() {
        WebElement saveButton = waitAndReturnElement(By.xpath("//*[@id=\"app\"]/div/header/div/div[1]/button"));
        saveButton.click();
        return new LoggedInPage(driver);
    }
}
