import Base.BaseTest;
import Base.LoginPage;
import Utils.AppConfig;
import org.junit.Test;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

public class InvalidLoginTest extends BaseTest {
    @Test
    public void negativeLogInTest(){
        LoginPage loginPage = new LoginPage();
        SoftAssert softAssert = new SoftAssert();
        loginPage.login(AppConfig.invalidPassword, AppConfig.invalidUsername);
        String errorMessage = loginPage.checkErrorMessage();
        softAssert.assertTrue(errorMessage.contains(AppConfig.errorMessage));
        softAssert.assertAll();
    }
}
