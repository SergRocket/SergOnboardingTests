package Base;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {
private int retryCounter = 0;
private static final int maxRetries = 2;

    @Override
    public boolean retry(ITestResult iTestResult) {
        if(retryCounter < maxRetries){
            retryCounter++;
            return true;
        }
        return false;
    }
}
