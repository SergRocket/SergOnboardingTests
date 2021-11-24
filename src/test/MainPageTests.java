import Base.BaseTest;
import PageObjects.LoginPage;
import PageObjects.MainPage;
import TestData.TestRailConfigAnnotation;
import Utils.AppConfig;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.lang.reflect.Method;

public class MainPageTests extends BaseTest {
    @TestRailConfigAnnotation(id="1")
    @Test(testName = "LoginTest")
    public void validLogin() {
        SoftAssert softAssert = new SoftAssert();
        MainPage mainPage = new MainPage();
        validLog();
        softAssert.assertTrue(mainPage.isCompanyLogoVisible());
        softAssert.assertTrue(mainPage.containsUrl(AppConfig.expectedUrlAfterLogin));
        softAssert.assertAll();
        System.out.print("Hello");          
    }

    @TestRailConfigAnnotation(id="4")
    @Test(testName = "UserPaginationTest")
    public void paginationTest() {
        SoftAssert softAssert= new SoftAssert();
        MainPage mainPage = new MainPage();
        sendSearchQuery();
        mainPage.scrollToPagination();
        mainPage.paginationClick();
        softAssert.assertTrue(mainPage.isPaginationDysplayed());
        softAssert.assertEquals(mainPage.isPaginationCounterUpdated(),2);
        softAssert.assertAll();
    }

    @TestRailConfigAnnotation(id="5")
    @Test(dataProvider ="Test_data",testName = "UsersearchValidTest")
    public void searchForValidDifferent(String lookingFor) {
        SoftAssert softAssert = new SoftAssert();
        MainPage mainPage = new MainPage();
        softAssert.assertTrue(mainPage.isCompanyLogoVisible());
        mainPage.scrollToSearch();
        cleanSearchField();
        mainPage.sendSearchQuery(lookingFor);
        softAssert.assertTrue(mainPage.isResultsAreVisible());
        softAssert.assertAll();
    }
    @TestRailConfigAnnotation(id="6")
    @Test(dataProvider ="Test_data",testName = "UserInvalidSearchTest")
    public void searchForOInValidDifferent(String lookingFor) {
        SoftAssert softAssert = new SoftAssert();
        MainPage mainPage = new MainPage();
        softAssert.assertTrue(mainPage.isCompanyLogoVisible());
        cleanSearchField();
        mainPage.sendSearchQuery(lookingFor);
        softAssert.assertTrue(mainPage.isErrorAreVisible());
        softAssert.assertEquals(mainPage.getErrorText(), "К сожалению, по запросу \"ZAZ\" ничего не найдено.\n" +
                "Свяжитесь, пожалуйста, с нашими специалистами по указанным выше телефонам.");
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

    @TestRailConfigAnnotation(id="3")
    @Test(testName = "UserSelectTest")
    public void selectingRandomCategory() throws InterruptedException {
        LoginPage loginPage = new  LoginPage();
        SoftAssert softAssert = new SoftAssert();
        MainPage mainPage = new MainPage();
        softAssert.assertTrue(loginPage.afterLoginPageisOpen());
        mainPage.scrollToSearch();
        cleanSearchField();
        mainPage.selectingCategory();
        softAssert.assertTrue(mainPage.isCategoriesAreVisible());
        softAssert.assertAll();
    }

    @TestRailConfigAnnotation(id="2")
    @Test(testName = "LogoutTest")
    public void loginOut() {
        MainPage mainPage = new MainPage();
        SoftAssert softAssert = new SoftAssert();
        mainPage.scrollToSearch();
        logOut();
        softAssert.assertTrue(mainPage.isCarMakesAreVisible());
        softAssert.assertAll();
    }

    @TestRailConfigAnnotation(id="7")
    @Test(testName = "UserNegativeLogintest")
    public void negativeLogInTest() {
        SoftAssert softAssert = new SoftAssert();
        MainPage mainPage = new MainPage();
        invalidLog();
        softAssert.assertTrue(mainPage.isCarMakesAreVisible());
        softAssert.assertAll();
    }
}
