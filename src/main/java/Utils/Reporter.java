package Utils;


import Base.BaseTest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Reporter {
    private static final Logger LOGGER = LogManager.getLogger(Reporter.class.getName());
    public static void log(String str) {
        printMsgOut(str);
    }
    private static void printMsgOut(String message) {
        BaseTest.getTest().get().info(message);
        LOGGER.info(message);
    }

    public static void logFail(String str){
        LOGGER.error(str);
    }
}
