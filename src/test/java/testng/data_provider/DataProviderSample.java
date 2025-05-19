package testng.data_provider;

import org.testng.annotations.DataProvider;

public class DataProviderSample {
    @DataProvider(name = "dataProvider")
    public Object[][] dataProviderMethod() {
        return new Object[][] {
            { 1234, "active" },
            { 4567, "inactive" },
            { 1255, "active" },
        };
    }
}
