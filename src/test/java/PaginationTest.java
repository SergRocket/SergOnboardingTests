import Base.BaseTest;
import Base.LoginPage;
import Base.WorklistsPage;
import Utils.AppConfig;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class PaginationTest extends BaseTest {
    @Test
    public void paginationTest(){
        LoginPage loginPage = new  LoginPage();
        SoftAssert softAssert= new SoftAssert();
        loginPage.login(AppConfig.validPassword, AppConfig.validUsername);
        WorklistsPage worklistsPage = new  WorklistsPage();
        worklistsPage.sendSearchQuery(AppConfig.searchQuery);
        worklistsPage.paginationClick();
        softAssert.assertTrue(worklistsPage.isPaginationDysplayed());
        softAssert.assertEquals(worklistsPage.isPaginationCounterUpdated(),2);
        softAssert.assertAll();
    }
}
