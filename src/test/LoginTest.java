import Base.BaseTest;
import Base.LoginPage;
import Base.WorklistsPage;
import TestData.TestRailConfigAnnotation;
import Utils.AppConfig;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class LoginTest extends BaseTest {
    @TestRailConfigAnnotation(id="3")
    @Test(testName = "User can login with valid credentials")
    public void validLogin() throws InterruptedException {
        LoginPage loginPage = new LoginPage();
        SoftAssert softAssert = new SoftAssert();
        WorklistsPage worklistsPage = loginPage.login(AppConfig.validPassword, AppConfig.validUsername);
        softAssert.assertTrue(worklistsPage.isCompanyLogoVisible());
        softAssert.assertTrue(worklistsPage.containsUrl(AppConfig.expectedUrlAfterLogin));
        softAssert.assertAll();
    }
}
