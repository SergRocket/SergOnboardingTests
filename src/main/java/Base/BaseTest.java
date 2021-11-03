package Base;

import TestData.CurrentDateTime;
import TestData.TestRailApiSetup;
import Utils.APIExeption;
import Utils.ExtentReportManager;
import com.aventstack.extentreports.ExtentTest;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.openqa.selenium.WebDriver;
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
    private static final ThreadLocal<WebDriver> DRIVER_THREAD_LOCAL = new ThreadLocal<>();
    private static ThreadLocal<ExtentTest> parentTest = new ThreadLocal();
    private static ThreadLocal<ExtentTest> test = new ThreadLocal();
    public WebDriver driver;
    private String suiteName;
    private String testRailRunId;
    private static String ENV;
    public String testName;
    public String browserName;

    public static WebDriver getWebDriver(){
        return DRIVER_THREAD_LOCAL.get();
    }

    @Parameters({"testRailSuiteId", "environment"})
    @BeforeClass
    public void createTestRailRun(@Optional("0") String testRailCoreId, @Optional("0") String testRailSuiteId, String environment, ITestContext context) throws IOException, APIExeption {
        if(!testRailSuiteId.equals("0")){
            Map data = new HashMap();
            data.put("suiteId", testRailSuiteId);
            data.put("name", CurrentDateTime.getCurrentDate() + " " +
                    context.getCurrentXmlTest().getSuite().getName()+" (Automated) ");
            data.put("includeAll", true);
            JSONArray testRailRunsArr = (JSONArray)TestRailApiSetup.getInstance().sendGet("get_runs/1");
            testRailRunId = getNeededTestRunId(testRailRunsArr, environment);
            if(testRailRunId.equals("")){
                JSONObject testRun = (JSONObject)TestRailApiSetup.getInstance()
                        .sendPost("add_run/" + testRailCoreId, data);
            testRailRunId = String.valueOf(testRun.get("id"));
            }
        }
    }

    @DataProvider(name = "Test_data")
    public static Object[][] test_searchQueries(Method method){
        switch (method.getName()){
            case "searchForValidDifferent":
                return new Object[][]{{1, "ZAZ"}};
            case "searchForInValidDifferent":
                return new Object[][]{{1, "-="}};
        }
        return null;
    }



    @BeforeClass
    public synchronized void beforeClass(ITestContext context){
        suiteName = context.getCurrentXmlTest().getSuite().getName();
        ExtentTest extentTest = ExtentReportManager.getiInstanceOfExtentReports(suiteName)
        .createTest(getClass().getName());
        parentTest.set(extentTest);
    }


    @BeforeMethod
    public void beforeMethodSetup(Method method){
        ExtentTest extentTest = parentTest.get().createNode(method.getName());
        test.set(extentTest);
        testName = method.getName();
        Reporter.log("Method - " + testName + " - has started");
    }

    @Parameters({"browser", "env"})
    @BeforeTest
    public void setBrowserAndEnv(String browser, String env){
        browserName = browser;
        setENV(env);
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
        ExtentReportManager.getiInstanceOfExtentReports(suiteName).flush();
        Reporter.log("Test has stopped");
    }

    @AfterTest
    public void killDriver(){
        if(driver !=null){
            driver.close();
            driver.quit();
            DRIVER_THREAD_LOCAL.remove();
        }
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

    private String getNeededTestRunId(JSONArray allRuns, String env){
        String isCompleted;
        for(int i = allRuns.size()-1; i>=0; i--){
            JSONObject run = (JSONObject) allRuns.get(i);
            isCompleted = String.valueOf(run.get("is_completed"));
            if (String.valueOf(run.get("name")).contains(CurrentDateTime.getCurrentDate()) &&
                    String.valueOf(run.get("name")).contains(env) && isCompleted.equals("false")) {
                return String.valueOf(run.get("id"));
            }
        }
        return "";
        }

    @Parameters({"testRailCaseId", "testRailCaseId2", "testRailCaseId3", "testRailCaseId3", "testRailCaseId4", "testRailCaseId5"})
    @AfterMethod
    public void addResultForTestRail(@Optional("0") String testRailCaseId,@Optional("0") String testRailCaseId2,
                                     @Optional("0") String testRailCaseId3,@Optional("0") String testRailCaseId4,
                                     @Optional("0") String testRailCaseId5, ITestResult result){
        putResultsIntoTestRail(testRailCaseId, result);
        putResultsIntoTestRail(testRailCaseId2, result);
        putResultsIntoTestRail(testRailCaseId3, result);
        putResultsIntoTestRail(testRailCaseId4, result);
        putResultsIntoTestRail(testRailCaseId5, result);
    }

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
                TestRailApiSetup.getInstance().sendPost("add_result_for_case/" + testRailRunId + "/"
                + testCaseId, data);
                } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

}
