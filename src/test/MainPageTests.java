import Base.BaseTest;
import Base.LoginPage;
import Base.WorklistsPage;
import TestData.TestRailConfigAnnotation;
import Utils.AppConfig;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.lang.reflect.Method;

public class MainPageTests extends BaseTest {
    @TestRailConfigAnnotation(id="3")
    @Test(testName = "User can login with valid credentials")
    public void validLogin() {
        SoftAssert softAssert = new SoftAssert();
        WorklistsPage worklistsPage = new WorklistsPage();
        validLog();
        softAssert.assertTrue(worklistsPage.isCompanyLogoVisible());
        softAssert.assertTrue(worklistsPage.containsUrl(AppConfig.expectedUrlAfterLogin));
        softAssert.assertAll();
    }

    @TestRailConfigAnnotation(id="4")
    @Test(testName = "User can switch between pages after results are shown")
    public void paginationTest() {
        SoftAssert softAssert= new SoftAssert();
        WorklistsPage worklistsPage = new  WorklistsPage();
        sendSearchQuery();
        worklistsPage.scrollToPagination();
        worklistsPage.paginationClick();
        softAssert.assertTrue(worklistsPage.isPaginationDysplayed());
        softAssert.assertEquals(worklistsPage.isPaginationCounterUpdated(),2);
        softAssert.assertAll();
    }

    @TestRailConfigAnnotation(id="21")
    @Test(dataProvider ="Test_data",testName = "User can perform valid search query")
    public void searchForValidDifferent(String lookingFor) throws InterruptedException {
        SoftAssert softAssert = new SoftAssert();
        WorklistsPage worklistsPage = new WorklistsPage();
        softAssert.assertTrue(worklistsPage.isCompanyLogoVisible());
        Thread.sleep(1500);
        worklistsPage.scrollToSearch();
        cleanSearchField();
        worklistsPage.sendSearchQuery(lookingFor);
        softAssert.assertTrue(worklistsPage.isResultsAreVisible());
        softAssert.assertAll();
    }
    @TestRailConfigAnnotation(id="5")
    @Test(dataProvider ="Test_data",testName = "User can perform invalid search")
    public void searchForOInValidDifferent(String lookingFor) throws InterruptedException {
        SoftAssert softAssert = new SoftAssert();
        WorklistsPage worklistsPage = new WorklistsPage();
        Thread.sleep(1900);
        softAssert.assertTrue(worklistsPage.isCompanyLogoVisible());
        Thread.sleep(1500);
        cleanSearchField();
        worklistsPage.sendSearchQuery(lookingFor);
        softAssert.assertTrue(worklistsPage.isErrorAreVisible());
        softAssert.assertEquals(worklistsPage.getErrorText(), "К сожалению, по запросу \"ZAZ\" ничего не найдено.\n" +
                "Свяжитесь, пожалуйста, с нашими специалистами по  выше телефонам.");
        softAssert.assertAll();
    }

    @DataProvider(name = "Test_data")
    public static Object[][] test_searchQueries(Method method){
        switch (method.getName()){
            case "searchForOInValidDifferent":
                return new Object[][]{{"ZAZ"}};
            case "searchForValidDifferent":
                return new Object[][]{{"090"}};
        }
        return null;
    }

    @TestRailConfigAnnotation(id="6")
    @Test(testName = "User can select from any category")
    public void selectingRandomCategory() throws InterruptedException {
        LoginPage loginPage = new  LoginPage();
        SoftAssert softAssert = new SoftAssert();
        WorklistsPage worklistsPage = new WorklistsPage();
        softAssert.assertTrue(loginPage.afterLoginPageisOpen());
        worklistsPage.scrollToSearch();
        cleanSearchField();
        worklistsPage.selectingCategory();
        softAssert.assertTrue(worklistsPage.isCategoriesAreVisible());
        softAssert.assertAll();
    }

    @TestRailConfigAnnotation(id="2")
    @Test(testName = "User can logout after he/she was logged in")
    public void loginOut() throws InterruptedException {
        WorklistsPage worklistsPage = new WorklistsPage();
        SoftAssert softAssert = new SoftAssert();
        Thread.sleep(2000);
        worklistsPage.scrollToSearch();
        logOut();
        Thread.sleep(1500);
        softAssert.assertTrue(worklistsPage.isCarMakesAreVisible());
        softAssert.assertAll();
    }

    @TestRailConfigAnnotation(id="1")
    @Test(testName = "User can`t login with invalid credentials")
    public void negativeLogInTest() {
        SoftAssert softAssert = new SoftAssert();
        WorklistsPage worklistsPage = new WorklistsPage();
        invalidLog();
        softAssert.assertTrue(worklistsPage.isCarMakesAreVisible());
        softAssert.assertAll();
    }
}
