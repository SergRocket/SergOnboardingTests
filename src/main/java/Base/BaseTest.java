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
import org.apache.commons.io.FileUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Listeners({BaseListener.class})
public class BaseTest {
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
    public static String TESTRAIL_PASSWORD = "a0.4zajeI2r8vcjTX3Of";
    public static String RAILS_ENGINE_URL = "https://sergtesterqa.testrail.io/";
    protected String testSuiteName;

    public static WebDriver getWebDriver(){
        return DRIVER_THREAD_LOCAL.get();
    }
    @Parameters({"PROJECT"})
    @BeforeSuite
    public void createTestRun(ITestContext context,@Optional("1")String PROJECT_ID) throws IOException, APIExeption{
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



    @BeforeClass
    public synchronized void beforeClass(ITestContext context){
        suiteName = context.getCurrentXmlTest().getSuite().getName();
        ExtentTest extentTest = ExtentReportManager.getiInstanceOfExtentReports(suiteName)
         .createTest(getClass().getName());
        parentTest.set(extentTest);
    }

    @Parameters({"browser"})
    @BeforeTest
    public void setBrowserAndEnv(@Optional("chrome")String browser) throws MalformedURLException {
        browserName = browser;
        //ChromeOptions options = new ChromeOptions();
        //driver = new RemoteWebDriver(new URL(AppConfig.HOST), options);
        driver = createDriver(browser);
        driver.get(AppConfig.startUrl);
        }

    @Parameters({"browser"})
    @BeforeMethod
    public void beforeMethodSetup(Method method,ITestContext context,@Optional("chrome")String browser){
        ExtentTest extentTest = parentTest.get().createNode(method.getName());
        test.set(extentTest);
        testName = method.getName();
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
        ExtentReportManager.getiInstanceOfExtentReports(suiteName).flush();
        Reporter.log("Start stopping tests");
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

    public void validLog(){
        driver.findElement(By.id("auth-username")).sendKeys(AppConfig.validUsername);
        driver.findElement(By.id("auth-password")).sendKeys(AppConfig.validPassword);
        driver.findElement(By.cssSelector("button[class*='btn-success authTop']")).click();
    }

    public void invalidLog(){
        driver.findElement(By.id("auth-username")).sendKeys(AppConfig.invalidUsername);
        driver.findElement(By.id("auth-password")).sendKeys(AppConfig.invalidPassword);
        driver.findElement(By.cssSelector("button[class*='btn-success authTop']")).click();
    }

    public void sendSearchQuery(){
        driver.findElement(By.id("artnum")).sendKeys(AppConfig.searchQuery);
        driver.findElement(By.cssSelector("button[onclick*='search_bubmit']")).click();
    }

    public void cleanSearchField(){
        String searchField = driver.findElement(By.id("artnum")).getText();
        if(searchField !=null){
            driver.findElement(By.id("artnum")).clear();
        }
    }

    public void logOut(){
        driver.findElement(By.xpath("//a[text()='Выход']")).click();
    }

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

    protected void takeScreenshots(String fileName, String methodName, String testName) {
        WebDriver augmentDriver = new Augmenter().augment(getWebDriver());
        File srcFile = ((TakesScreenshot) augmentDriver).getScreenshotAs(OutputType.FILE);
        String path = System.getProperty("user.dir") + File.separator + "test-outpit" + File.separator + "screenshots" +
                File.separator + getTodayDate() + File.separator + testSuiteName + File.separator + testName + File.separator
                + methodName + File.separator + getSystemTime() + " " + fileName + " .png";
        try {
            FileUtils.copyFile(srcFile, new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static String getTodayDate(){
        return (new SimpleDateFormat("yyyyMMdd").format(new Date()));
    }
    public String getSystemTime(){
        return (new SimpleDateFormat("HHmmssSSS").format(new Date()));
    }
    protected List<LogEntry> getBrowserLogs(){
        LogEntries logEntries = driver.manage().logs().get("browser");
        List<LogEntry> logEntries1 = logEntries.getAll();
        return logEntries1;
    }
}
