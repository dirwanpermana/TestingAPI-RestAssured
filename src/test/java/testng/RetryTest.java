package testng;

import org.testng.Assert;
import org.testng.annotations.Test;

import testng.retry_mecanism.RetrySample;

public class RetryTest {
    public static int count = 0;

    @Test(retryAnalyzer = RetrySample.class)
    public void flackyTest() {
        count++;
        System.out.println("Running test " + count);
        Assert.assertTrue(count > 2);
    }
}