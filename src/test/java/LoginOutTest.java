import Base.BaseTest;
import Base.LoginPage;
import Base.WorklistsPage;
import Utils.AppConfig;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class LoginOutTest extends BaseTest {
    @Test
    public void loginOut(){
        LoginPage loginPage = new  LoginPage();
        SoftAssert softAssert = new SoftAssert();
        WorklistsPage worklistsPage = loginPage.login(AppConfig.validPassword, AppConfig.validUsername);
        worklistsPage.logout();
        softAssert.assertTrue(loginPage.afterLoginPageisOpen());
        softAssert.assertAll();
    }
}
