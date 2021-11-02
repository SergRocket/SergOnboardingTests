package Base;

import Utils.AppConfig;
import org.openqa.selenium.By;

public class LoginPage extends BasePage {
    public By credentialsInput = new By.ById("loginForm");
    public By emailInput = new By.ById("userEmail");
    public By passwordInput = new By.ById("userPass");
    public By loginButton = new By.ById("se_userLogin");
    public By error = new By.ById("se_emailError");

    public  boolean  afterLoginPageisOpen(){
        waitForElementToBeVisible(credentialsInput);
        return isDysplayed(credentialsInput);
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
