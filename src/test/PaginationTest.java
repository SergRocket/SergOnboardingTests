import Base.BaseTest;
import PageObjects.LoginPage;
import PageObjects.WorklistsPage;
import TestData.TestRailConfigAnnotation;
import Utils.AppConfig;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class PaginationTest extends BaseTest {
    @TestRailConfigAnnotation(id="4")
    @Test(testName = "User can switch between pages after results are shown")
    public void paginationTest() throws InterruptedException {
        LoginPage loginPage = new  LoginPage();
        SoftAssert softAssert= new SoftAssert();
        loginPage.login(AppConfig.validPassword, AppConfig.validUsername);
        WorklistsPage worklistsPage = new  WorklistsPage();
        worklistsPage.sendSearchQuery(AppConfig.searchQuery);
        worklistsPage.scrollToPagination();
        worklistsPage.paginationClick();
        softAssert.assertTrue(worklistsPage.isPaginationDysplayed());
        softAssert.assertEquals(worklistsPage.isPaginationCounterUpdated(),2);
        softAssert.assertAll();
    }
}
