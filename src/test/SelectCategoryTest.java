import Base.BaseTest;
import Base.LoginPage;
import Base.WorklistsPage;
import TestData.TestRailConfigAnnotation;
import Utils.AppConfig;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class SelectCategoryTest extends BaseTest {
    @TestRailConfigAnnotation(id="6")
    @Test(testName = "User can select from any category")
    public void selectingRandomCategory() throws InterruptedException {
        LoginPage loginPage = new  LoginPage();
        SoftAssert softAssert = new SoftAssert();
        WorklistsPage worklistsPage = loginPage.login(AppConfig.validPassword, AppConfig.validUsername);
        softAssert.assertTrue(loginPage.afterLoginPageisOpen());
        worklistsPage.selectingCategory();
        softAssert.assertTrue(worklistsPage.isCategoriesAreVisible());
        softAssert.assertAll();
    }
}
