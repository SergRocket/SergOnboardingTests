import Base.BaseTest;
import Base.LoginPage;
import Base.WorklistsPage;
import Utils.AppConfig;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class InvalidLoginTest extends BaseTest {
    @Test
    public void negativeLogInTest(){
        LoginPage loginPage = new LoginPage();
        SoftAssert softAssert = new SoftAssert();
        WorklistsPage worklistsPage = new WorklistsPage();
        loginPage.login(AppConfig.invalidPassword, AppConfig.invalidUsername);
        softAssert.assertTrue(worklistsPage.isCarMakesAreVisible());
        softAssert.assertAll();
    }
}
