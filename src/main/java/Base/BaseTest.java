package Base;

import TestData.CurrentDateTime;
import TestData.TestRailApiSetup;
import TestData.TestRailConfigAnnotation;
import Utils.APIClient;
import Utils.APIExeption;
import Utils.AppConfig;
import Utils.ExtentReportManager;
import com.aventstack.extentreports.ExtentTest;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Listeners({BaseListener.class})
public class BaseTest {
    private static final String PROJECT_ID = "1";
    private static final ThreadLocal<WebDriver> DRIVER_THREAD_LOCAL = new ThreadLocal<>();
    private static ThreadLocal<ExtentTest> parentTest = new ThreadLocal();
    private static ThreadLocal<ExtentTest> test = new ThreadLocal();
    public WebDriver driver;
    private String suiteName;
    private static String ENV;
    public String testName;
    public String browserName;
    protected APIClient client;
    protected HashMap data;
    public static String TESTRAIL_USERNAME = "serg.lishko1988@gmail.com";
    public static String TESTRAIL_PASSWORD = "fg78N7RS";
    public static String RAILS_ENGINE_URL = "https://sergqaajun.testrail.io/";

    public static WebDriver getWebDriver(){
        return DRIVER_THREAD_LOCAL.get();
    }
    @Parameters({"browser"})
    @BeforeSuite
    public void createTestRun(ITestContext context, @Optional("chrome") String browser) throws IOException, APIExeption{
        client = new APIClient(RAILS_ENGINE_URL);
        client.setUser(TESTRAIL_USERNAME);
        client.setPassword(TESTRAIL_PASSWORD);
        data = new HashMap();
        data.put("include_all", true);
        data.put("name", "Test Run " + System.currentTimeMillis());
        JSONObject jsonObject = null;
        jsonObject = (JSONObject) client.sendPost("add_run/" + PROJECT_ID, data);
        Long suite_Id = (Long) jsonObject.get("id");
        context.setAttribute("suiteId", suite_Id);

    }

    /*@Parameters({"testRailCoreId","testRailSuiteId"})
    @BeforeClass
    public void createTestRailRun(@Optional("0") String testRailCoreId, @Optional("0") String testRailSuiteId, ITestContext context) throws IOException, APIExeption {
        if(!testRailSuiteId.equals("0")){
            Map data = new HashMap();
            data.put("suiteId", testRailSuiteId);
            data.put("name", CurrentDateTime.getCurrentDate() + " " +
                    context.getCurrentXmlTest().getSuite().getName()+" (Automated) ");
            data.put("includeAll", true);
            JSONArray testRailRunsArr = (JSONArray)TestRailApiSetup.getInstance().sendGet("get_runs/1");
            testRailRunId = getNeededTestRunId(testRailRunsArr);
            if(testRailRunId.equals("")){
                JSONObject testRun = (JSONObject)TestRailApiSetup.getInstance()
                        .sendPost("add_run/" + testRailCoreId, data);
            testRailRunId = String.valueOf(testRun.get("id"));
            }
        }
    }*/


    @BeforeClass
    public synchronized void beforeClass(ITestContext context){
        suiteName = context.getCurrentXmlTest().getSuite().getName();
        ExtentTest extentTest = ExtentReportManager.getiInstanceOfExtentReports(suiteName)
         .createTest(getClass().getName());
        parentTest.set(extentTest);
    }

    @Parameters({"browser"})
    @BeforeTest
    public void setBrowserAndEnv(@Optional("chrome") String browser){
        /*browserName = browser;
        driver = createDriver(browser);*/
    }
    @Parameters({"browser"})
    @BeforeMethod
    public void beforeMethodSetup(Method method,ITestContext context,@Optional("chrome") String browser){
        ExtentTest extentTest = parentTest.get().createNode(method.getName());
        test.set(extentTest);
        testName = method.getName();
        browserName = browser;
        driver = createDriver(browser);
        driver.get(AppConfig.startUrl);
        Reporter.log("Method - " + testName + " - has started");
        if(method.isAnnotationPresent(TestRailConfigAnnotation.class)){
            TestRailConfigAnnotation configAnnotation = method.getAnnotation(TestRailConfigAnnotation.class);
            context.setAttribute("caseId", configAnnotation.id());
        }
    }

    @AfterMethod
    public void afterTest(ITestResult result, ITestContext context) throws IOException, APIExeption {
        Map data = new HashMap();
        client = new APIClient(RAILS_ENGINE_URL);
        client.setUser(TESTRAIL_USERNAME);
        client.setPassword(TESTRAIL_PASSWORD);
        if (result.isSuccess()) {
            data.put("status_id", 1);
        } else {
            data.put("status_id", 5);
            data.put("comment", result.getThrowable().toString());
        }
        String caseId = (String) context.getAttribute("caseId");
        Long suiteId = (Long) context.getAttribute("suiteId");
        client.sendPost("add_result_for_case/" + suiteId + "/" + caseId, data);
    }

    @AfterSuite
    public void tearDownTestRailAndDriver(ITestContext context) throws IOException, APIExeption {
        Long suiteId = (Long) context.getAttribute("suiteId");
        client.sendPost("close_run/" + suiteId + "/", data);
    }

    @AfterMethod
    public void afterMethodSetup(ITestResult results){
        if(results.getStatus() == ITestResult.FAILURE){
            test.get().fail(results.getThrowable());
            Utils.Reporter.logFail("Test has Failed");
        } else if (results.getStatus() == ITestResult.SKIP)
            test.get().skip(results.getThrowable());
        else
            test.get().pass("Test has passed");
        //ExtentReportManager.getiInstanceOfExtentReports(suiteName).flush();
        Reporter.log("Test has stopped");
    }

    @AfterMethod
    public void killDriver(){
        if(driver !=null){
            driver.close();
            driver.quit();
            DRIVER_THREAD_LOCAL.remove();
        }
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

    private static void setENV(String envnForTests){
        ENV = envnForTests;
    }

    public static ThreadLocal<ExtentTest> getTest() {
        return test;
    }

    private static void setEnvironmentForTests(String environmentForTests) {
        ENV = environmentForTests;
    }

    public static String getENV(){return ENV;}

    private String getNeededTestRunId(JSONArray allRuns){
        String isCompleted;
        for(int i = allRuns.size()-1; i>=0; i--){
            JSONObject run = (JSONObject) allRuns.get(i);
            isCompleted = String.valueOf(run.get("is_completed"));
            if (String.valueOf(run.get("name")).contains(CurrentDateTime.getCurrentDate())&& isCompleted.equals("false")) {
                return String.valueOf(run.get("id"));
            }
        }
        return "";
        }

        public WebDriver createDriver(String browserName){
            switch (browserName){
                case "chrome":
                    WebDriverManager.chromedriver().setup();
                    DRIVER_THREAD_LOCAL.set(new ChromeDriver());
                    break;
                case "opera":
                    WebDriverManager.operadriver().setup();
                    DRIVER_THREAD_LOCAL.set(new OperaDriver());
                    break;
                default:
                    System.out.print("Failed to setup browser");
            }
            return DRIVER_THREAD_LOCAL.get();
        }


    /*@Parameters({"testRailCaseId", "testRailCaseId2", "testRailCaseId3", "testRailCaseId4", "testRailCaseId5"})
    @AfterMethod
    public void addResultForTestRail(@Optional("0") String testRailCaseId,@Optional("0") String testRailCaseId2,
                                     @Optional("0") String testRailCaseId3,@Optional("0") String testRailCaseId4, @Optional("0") String testRailCaseId5,
                                     ITestResult result){
        putResultsIntoTestRail(testRailCaseId, result);
        putResultsIntoTestRail(testRailCaseId2, result);
        putResultsIntoTestRail(testRailCaseId3, result);
        putResultsIntoTestRail(testRailCaseId4, result);
        putResultsIntoTestRail(testRailCaseId5, result);
    }*/

    public void putResultsIntoTestRail(String testCaseId, ITestResult result){
        if(!testCaseId.equals("0")){
            Map data = new HashMap();
            if(result.getStatus() == ITestResult.FAILURE){
                data.put("status_id", 5);
                data.put("comment", "Fail");
            } else if (result.getStatus() == ITestResult.SKIP){
                data.put("status_id", 5);
                data.put("comment", "Fail");
            } else {
                data.put("status_id", 1);
                data.put("comment", "Pass");
            }
            try {
                TestRailApiSetup.getInstance().sendPost("add_result_for_case/" + /*testRailRunId*/  "/"
                + testCaseId, data);
                } catch (IOException | APIExeption e){
                e.printStackTrace();
            }
        }
    }



}
