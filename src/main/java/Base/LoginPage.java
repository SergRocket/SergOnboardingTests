package Base;

import Utils.AppConfig;
import org.openqa.selenium.By;

public class LoginPage extends BasePage {
    public By credentialsInput = new By.ById("block-notlogged");
    public By emailInput = new By.ById("auth-username");
    public By passwordInput = new By.ById("auth-password");
    public By loginButton = new By.ByCssSelector("button[class*='btn-success authTop']");
    public By error = new By.ById("se_emailError");
    public By credentialsInputAfterLog = new By.ById("block-loggedin");

    public  boolean  afterLoginPageisOpen(){
        waitForElementToBeVisible(credentialsInputAfterLog);
        return isDysplayed(credentialsInputAfterLog);
    }

    public WorklistsPage login(String password, String username){
        findWebElement(emailInput).sendKeys(username);
        findWebElement(passwordInput).sendKeys(password);
        findWebElement(loginButton).click();
        return new WorklistsPage();
    }

    public String checkErrorMessage(){
        waitForElementToBeVisible(error);
        return findWebElement(error).getText();
    }

}
