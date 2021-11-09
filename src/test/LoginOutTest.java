import Base.BaseTest;
import Base.LoginPage;
import Base.WorklistsPage;
import TestData.TestRailConfigAnnotation;
import Utils.AppConfig;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class LoginOutTest extends BaseTest {
    @TestRailConfigAnnotation(id="2")
    @Test(testName = "User can logout after he/she was logged in")
    public void loginOut() throws InterruptedException {
        LoginPage loginPage = new  LoginPage();
        SoftAssert softAssert = new SoftAssert();
        WorklistsPage worklistsPage = loginPage.login(AppConfig.validPassword, AppConfig.validUsername);
        softAssert.assertTrue(loginPage.afterLoginPageisOpen());
        Thread.sleep(2000);
        worklistsPage.logout();
        Thread.sleep(1500);
        softAssert.assertTrue(worklistsPage.isCarMakesAreVisible());
        softAssert.assertAll();
    }
}
