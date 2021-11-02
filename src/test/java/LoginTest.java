import Base.BaseTest;
import Base.LoginPage;
import Base.WorklistsPage;
import Utils.AppConfig;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class LoginTest extends BaseTest {

    @Test
    public void validLogin(){
        LoginPage loginPage = new LoginPage();
        SoftAssert softAssert = new SoftAssert();
        WorklistsPage worklistsPage = loginPage.login(AppConfig.validPassword, AppConfig.validUsername);
        softAssert.assertTrue(worklistsPage.isUrlandCompanyLogoVisible());
        softAssert.assertTrue(worklistsPage.containsUrl(AppConfig.expectedUrlAfterLogin));
        softAssert.assertAll();
    }
}
