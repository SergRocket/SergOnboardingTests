import Base.BaseTest;
import Base.LoginPage;
import Base.WorklistsPage;
import Utils.AppConfig;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class AddingToFavTest extends BaseTest {
    @Test
    public void selectingRandomCategory(){
        LoginPage loginPage = new  LoginPage();
        SoftAssert softAssert = new SoftAssert();
        WorklistsPage worklistsPage = loginPage.login(AppConfig.validPassword, AppConfig.validUsername);
        softAssert.assertTrue(loginPage.afterLoginPageisOpen());
        worklistsPage.selectingCategory();
        softAssert.assertTrue(worklistsPage.isCategoriesAreVisible());
        softAssert.assertAll();
    }
}
