import Base.BaseTest;
import Base.LoginPage;
import Base.WorklistsPage;
import TestData.TestRailConfigAnnotation;
import Utils.AppConfig;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;


public class SearchTest extends BaseTest {
    @TestRailConfigAnnotation(id="21")
    @Test(dataProvider ="Test_data",testName = "User can perform valid search query")
    public void searchForValidDifferent(String lookingFor) throws InterruptedException {
        LoginPage loginPage = new LoginPage();
        SoftAssert softAssert = new SoftAssert();
        WorklistsPage worklistsPage = loginPage.login(AppConfig.validPassword, AppConfig.validUsername);
        softAssert.assertTrue(worklistsPage.isCompanyLogoVisible());
        Thread.sleep(1500);
        worklistsPage.sendSearchQuery(lookingFor);
        softAssert.assertTrue(worklistsPage.isResultsAreVisible());
        softAssert.assertAll();
    }
    @TestRailConfigAnnotation(id="5")
    @Test(dataProvider ="Test_data",testName = "User can perform invalid search")
    public void searchForOInValidDifferent(String lookingFor) throws InterruptedException {
        LoginPage loginPage = new LoginPage();
        SoftAssert softAssert = new SoftAssert();
        WorklistsPage worklistsPage = loginPage.login(AppConfig.validPassword, AppConfig.validUsername);
        Thread.sleep(1900);
        softAssert.assertTrue(worklistsPage.isCompanyLogoVisible());
        Thread.sleep(1500);
        worklistsPage.sendSearchQuery(lookingFor);
        softAssert.assertTrue(worklistsPage.isErrorAreVisible());
        softAssert.assertEquals(worklistsPage.getErrorText(), "К сожалению, по запросу \"ZAZ\" ничего не найдено.\n" +
                "Свяжитесь, пожалуйста, с нашими специалистами по  выше телефонам.");
        softAssert.assertAll();
    }
}
