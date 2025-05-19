package testng.retry_mecanism;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetrySample implements IRetryAnalyzer {
    private int retryCount = 0;
    private static final int maxRetryCount = 5;

    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < maxRetryCount) {
            retryCount++;
            System.out.println("Retrying test " + result.getName() + " for the " + retryCount + " time.");
            return true;
        }
        return false;
    }
    
}
