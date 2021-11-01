package TestData;

import Utils.APIClient;

public class TestRailApiSetup {
    private static APIClient testRailApiClient;

    private static APIClient createInstance(){
        testRailApiClient = new APIClient("https://sergqaajun.testrail.io/");
        testRailApiClient.setUser("serg.lishko1988@gmail.com");
        testRailApiClient.setPassword("fg78N7RS");
        return testRailApiClient;
    }

    public static APIClient getInstance(){
        if(testRailApiClient == null)
            createInstance();
        return testRailApiClient;
    }
}
