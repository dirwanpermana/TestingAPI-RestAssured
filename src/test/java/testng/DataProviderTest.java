package testng;

import org.testng.annotations.Test;
import testng.data_provider.DataProviderSample;

public class DataProviderTest {
    @Test(dataProvider = "dataProvider", dataProviderClass = DataProviderSample.class)
    public void testMethod(int productId, String status) {
        System.out.println("Test method executed " + productId + " with status " + status);
    }
}
