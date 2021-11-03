import Base.BaseTest;
import Base.LoginPage;
import Base.WorklistsPage;
import Utils.AppConfig;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;


public class SearchTest extends BaseTest {
    @Test(dataProvider ="Test_data")
    public void searchForValidDifferent(String lookingFor){
        LoginPage loginPage = new LoginPage();
        SoftAssert softAssert = new SoftAssert();
        WorklistsPage worklistsPage = loginPage.login(AppConfig.validPassword, AppConfig.validUsername);
        softAssert.assertTrue(worklistsPage.isUrlandCompanyLogoVisible());
        worklistsPage.sendSearchQuery(lookingFor);
        softAssert.assertTrue(worklistsPage.isResultsAreVisible());
        softAssert.assertAll();
    }

    @Test()
    public void searchForOInValidDifferent(){

    }

}