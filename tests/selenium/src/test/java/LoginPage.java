package src.test.java;

import src.test.java.BasePage;
import src.test.java.LoggedInPage;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class LoginPage extends BasePage {
    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public LoggedInPage login(String email, String password) {
        WebElement emailTextField = waitAndReturnElement(By.xpath("//*[@id=\"username\"]"));
        emailTextField.sendKeys(email);
        WebElement loginButton = waitAndReturnElement(By.xpath("//*[@id=\"main-content\"]/section/div/div/section/div[1]/div[2]/div/form/div/button[1]"));
        loginButton.click();
        WebElement passwordTextField = waitAndReturnElement(By.xpath("//*[@id=\"password\"]"));
        passwordTextField.sendKeys(password);
        WebElement continueButton = waitAndReturnElement(By.xpath("//*[@id=\"main-content\"]/section/div/div/section/div/div[2]/form/div[2]/button[1]"));
        continueButton.click();
        return new LoggedInPage(driver);
    }
}