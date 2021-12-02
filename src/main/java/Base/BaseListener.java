package Base;

import Utils.Reporter;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.Logs;
import org.openqa.selenium.remote.Augmenter;
import org.testng.IInvokedMethodListener;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static Base.BaseTest.getWebDriver;

public class BaseListener implements IInvokedMethodListener, ITestListener {

    @Override
    public void onTestFailure(ITestResult result) {
        Reporter.logFail("Test has failed");
        Reporter.logFail(getJSConsoleError());
    }

    public String getJSConsoleError() {
        try {
            String errorHeader = "Last console error is: ";
            WebDriver augmentedDriver = new Augmenter().augment(getWebDriver());
            Logs logs = augmentedDriver.manage().logs();
            LogEntries logEntries = logs.get(LogType.BROWSER);
            List<LogEntry> listOfLogs = logEntries.getAll();
            if (listOfLogs.size() > 0) {
                LogEntry lastEntry = listOfLogs.get(listOfLogs.size() - 1);
                if (lastEntry.getMessage().toLowerCase().contains("error")) {
                    errorHeader = errorHeader + lastEntry.getMessage();
                } else {
                    errorHeader = errorHeader + lastEntry.getMessage();
                }
                return errorHeader;
            }
            return "No errors were found";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "No errors were found";
    }

    protected void takeScreenshot(String methodName, String testName, ITestResult testResult) {
        WebDriver augmentDriver = new Augmenter().augment(getWebDriver());
        File srcFile = ((TakesScreenshot) augmentDriver).getScreenshotAs(OutputType.FILE);
        DateFormat dateFormat = new SimpleDateFormat("d:MM:yyyy");
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Kiev"));
        String currentDate = dateFormat.format(calendar.getTime());
        String timeIndentif = new SimpleDateFormat("d:MM:yyyy").format(calendar.getTime());
        String fileName = methodName + timeIndentif + ".png";
        String fileLocation = System.getProperty("user.dir") + File.separator + "target" + File.separator + "result"
                + File.separator + testName + File.separator + currentDate + File.separator;
        try {
            FileUtils.copyFile(srcFile, new File(fileLocation + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
