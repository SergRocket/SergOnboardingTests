package Utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ExtentReportManager {
    private static ExtentReports extentReports;

    public static ExtentReports getiInstanceOfExtentReports(String suiteName){
        if(extentReports==null)
            createInstanceOfReport(suiteName);
        return extentReports;
    }

    public static ExtentReports createInstanceOfReport(String suiteName){
        Date currentDate = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy_HHmmss");
        ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter
        (suiteName+ "-" +simpleDateFormat.format(currentDate)+ ".html");
        htmlReporter.config().setTestViewChartLocation(ChartLocation.TOP);
        htmlReporter.config().setChartVisibilityOnOpen(true);
        htmlReporter.config().setTheme(Theme.DARK);
        htmlReporter.config().setDocumentTitle("Smoke Tests");
        htmlReporter.config().setEncoding("utf-8");
        htmlReporter.config().setReportName("Smoke tests report");
        extentReports = new ExtentReports();
        extentReports.attachReporter(htmlReporter);
        return extentReports;
    }
}
