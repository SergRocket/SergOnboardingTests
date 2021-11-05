package Base;

import Utils.AppConfig;
import Utils.Reporter;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class BasePage {
private WebDriver driver;
private WebDriverWait webDriverWait;
private WebDriverWait webDriverFastWait;
private FluentWait<WebDriver> fluentWait;
private static final long TIMEOUT_AFTER_30SEC = 30;
private static final long TIMEOUT_AFTER_1SEC = 1;
private static final int retryAttempts = 10;
private static final long IMPLICID_WAIT = 20;

public BasePage() {
    driver = BaseTest.getWebDriver();
    webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
    webDriverFastWait = new WebDriverWait(driver, Duration.ofSeconds(1));
    fluentWait = new FluentWait<>(driver).withTimeout(Duration.ofSeconds(5)).pollingEvery(Duration.ofMillis(500))
            .ignoring(NoSuchElementException.class);
}

    public WebElement findWebElement(By element){ return driver.findElement(element);}

    public List<WebElement> findElements(By element){return driver.findElements(element);}

    public void clickElement(By element){
        findWebElement(element).click();
    }


    public void clickElementInList(By elements, int elementIndex){
    Reporter.log("Clicking element by index "+ elementIndex + " from list of elements");
    driver.findElements(elements).get(elementIndex -1).click();
    }

    public String getElementText(By element){
        Reporter.log("Extracting element text ");
        String text = findWebElement(element).getText();
        Reporter.log("Element text is - " + text);
        return text;
    }

    public String getElementAttrValue(By element, String attribute){
       Reporter.log("Extracting set value from element");
       String givenValue = findWebElement(element).getAttribute(attribute);
       Reporter.log("The " + attribute + " from " + element + " is " + givenValue);
       return givenValue;
    }

    public void refreshPage(){
      Reporter.log("Refreshing the page");
      driver.navigate().refresh();
    }

    public void clickWithJS(By element){
      Reporter.log("Clicking on element with JS executor");
      JavascriptExecutor executor = (JavascriptExecutor)driver;
      executor.executeScript("arguments[0].click();", element);
    }

    public void hoverOverElement(By element){
      Reporter.log("Hovering the mouse over element");
      Actions actions = new Actions(driver);
      actions.moveToElement(driver.findElement(element)).build().perform();
    }

    public void clearTextField(By element){
      Reporter.log("Clearing the text from element");
      findWebElement(element).clear();
    }

    public boolean checkTheQuantOfElements(By element, int expectedQant){
      Reporter.log("Checking that elements qnt is more or equal to " + expectedQant);
      if(findElements(element).size() <= expectedQant){
        return false;
     } else {
        return true;
     }
    }

    public String getUrl(){
      return driver.getCurrentUrl();
    }

    public String getChangedUrl(String regex, String replacement){
      Reporter.log("Changing the currentURL with "+ replacement);
      String changedURl = driver.getCurrentUrl().replaceFirst(regex, replacement);
      return changedURl;
    }

    public List<String> getTextFromListOfElements(By element){
      Reporter.log("Getting text from elements");
      List<String> stringList = new ArrayList<>();
      List<WebElement> elements = findElements(element);
      for (WebElement elemt : elements){
          stringList.add(elemt.getText());
      }
      return stringList;
    }

    public boolean isDysplayed(By element){
        if (findElements(element).size() >0) {
            return true;
        } else
            return false;
    }

    public boolean containsUrl (String str){
        boolean currentUrl = driver.getCurrentUrl().contains(str);
        return currentUrl;
    }

    public void scrollWithJSToElement(WebElement element){
    Reporter.log("Scrolling to element with JS");
    JavascriptExecutor executor = (JavascriptExecutor)driver;
    executor.executeScript("arguments[0].scrollIntoView(true);", element);
    }

    public void waitForElementToBeVisible(By element){
      webDriverWait.until(ExpectedConditions.visibilityOf(findWebElement(element)));
    }

    public void clickelementWithJS(By element){
    Reporter.log("Clicking an element with JS executor");
    JavascriptExecutor executor =(JavascriptExecutor)driver;
    executor.executeScript("arguments[0].click();", element);
    }


}
