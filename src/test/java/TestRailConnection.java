import Base.BaseTest;
import TestData.TestRailConfigAnnotation;
import Utils.APIClient;
import Utils.APIExeption;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class TestRailConnection extends BaseTest {
    public void setMethod1NameForTRail(ITestContext context, Method method) throws NoSuchMethodException{
        Method methodInvalidLogin = InvalidLoginTest.class.getMethod(method.getName());
        Method methodLoginOut = LoginOutTest.class.getMethod(method.getName());
        Method methodValidLogin = LoginTest.class.getMethod(method.getName());
        Method methodPagination = PaginationTest.class.getMethod(method.getName());
        Method methodSearch = SearchTest.class.getMethod(method.getName());
        Method methodSelectCat = SelectCategoryTest.class.getMethod(method.getName());
        if(methodInvalidLogin.isAnnotationPresent(TestRailConfigAnnotation.class)){
            TestRailConfigAnnotation testRConf = methodInvalidLogin.getAnnotation(TestRailConfigAnnotation.class);
            context.setAttribute("caseId", testRConf.id());
        }
        if(methodLoginOut.isAnnotationPresent(TestRailConfigAnnotation.class)){
            TestRailConfigAnnotation testRConf = methodLoginOut.getAnnotation(TestRailConfigAnnotation.class);
            context.setAttribute("caseId", testRConf.id());
        }
        if(methodValidLogin.isAnnotationPresent(TestRailConfigAnnotation.class)){
            TestRailConfigAnnotation testRConf = methodValidLogin.getAnnotation(TestRailConfigAnnotation.class);
            context.setAttribute("caseId", testRConf.id());
        }
        if(methodPagination.isAnnotationPresent(TestRailConfigAnnotation.class)){
            TestRailConfigAnnotation testRConf = methodPagination.getAnnotation(TestRailConfigAnnotation.class);
            context.setAttribute("caseId", testRConf.id());
        }
        if(methodSearch.isAnnotationPresent(TestRailConfigAnnotation.class)){
            TestRailConfigAnnotation testRConf = methodSearch.getAnnotation(TestRailConfigAnnotation.class);
            context.setAttribute("caseId", testRConf.id());
        }
        if(methodSelectCat.isAnnotationPresent(TestRailConfigAnnotation.class)){
            TestRailConfigAnnotation testRConf = methodSelectCat.getAnnotation(TestRailConfigAnnotation.class);
            context.setAttribute("caseId", testRConf.id());
        }
    }


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


    public void tearDownTestRailAndDriver(ITestContext context) throws IOException, APIExeption {
        Long suiteId = (Long) context.getAttribute("suiteId");
        client.sendPost("close_run/" + suiteId + "/", data);
    }


}
