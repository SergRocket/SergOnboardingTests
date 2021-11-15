package PageObjects;

import Base.BasePage;
import PageObjects.MainPage;
import org.openqa.selenium.By;

public class LoginPage extends BasePage {
    public By credentialsInput = new By.ById("block-notlogged");
    public By emailInput = new By.ById("auth-username");
    public By passwordInput = new By.ById("auth-password");
    public By loginButton = new By.ByCssSelector("button[class*='btn-success authTop']");
    public By error = new By.ById("se_emailError");
    public By credentialsInputAfterLog = new By.ById("block-loggedin");
    public By logoutButton = new By.ByXPath("//a[text()='Выход']");

    public  boolean  afterLoginPageisOpen() throws InterruptedException {
        Thread.sleep(1500);
        waitForElementToBeVisible(credentialsInputAfterLog);
        return isDysplayed(credentialsInputAfterLog);
    }

    public MainPage login(String password, String username) throws InterruptedException {
        Thread.sleep(1500);
        findWebElement(emailInput).sendKeys(username);
        findWebElement(passwordInput).sendKeys(password);
        findWebElement(loginButton).click();
        return new MainPage();
    }

    public String checkErrorMessage(){
        waitForElementToBeVisible(error);
        return findWebElement(error).getText();
    }

}
