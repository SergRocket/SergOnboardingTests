import Base.BaseTest;
import Base.LoginPage;
import Base.WorklistsPage;
import TestData.TestRailConfigAnnotation;
import Utils.AppConfig;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class InvalidLoginTest extends BaseTest {
    @TestRailConfigAnnotation(id="1")
    @Test(testName = "User can`t login with invalid credentials")
    public void negativeLogInTest() throws InterruptedException {
        LoginPage loginPage = new LoginPage();
        SoftAssert softAssert = new SoftAssert();
        WorklistsPage worklistsPage = new WorklistsPage();
        loginPage.login(AppConfig.invalidPassword, AppConfig.invalidUsername);
        softAssert.assertTrue(worklistsPage.isCarMakesAreVisible());
        softAssert.assertAll();
    }
}
