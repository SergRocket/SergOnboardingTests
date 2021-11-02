package Base;

import Utils.AppConfig;
import org.openqa.selenium.By;

public class WorklistsPage extends BasePage {
    public By brandImage = new By.ById("headerLogo");
    public By accountMenu = new By.ById("topLoginLink");
    public By favorites = new By.ByCssSelector("#observed-counter>a[href*='favorites']");
    public By mainSearch = new By.ById("headerSearch");
    public By submitSearch = new By.ById("submit-searchmain");
    public By paginationLeftClick = new By.ByCssSelector("a[href*='page=2']");
    public By paginationRightClick = new By.ByCssSelector("span[data-cy*='prev']");
    public By currentPaginationPage = new By.ByCssSelector("span[class*='current']");
    public By logoutButton = new By.ById("login-box-logout");

    public boolean isUrlandCompanyLogoVisible() {

        return false;
    }

    public void logout() {
        clickElement(accountMenu);
        clickElement(logoutButton);
    }

    public void paginationClick(){
        findWebElement(paginationLeftClick).click();
        findWebElement(paginationRightClick).click();
    }

    public boolean isPaginationDysplayed(){
        if (findElements(currentPaginationPage).size() >0) {
            return true;
        } else
            return false;
    }

    public int isPaginationCounterUpdated(){
        findWebElement(paginationLeftClick).click();
        String text = getElementText(currentPaginationPage);
        Integer paginationValue = Integer.parseInt(text);
        return paginationValue;
    }

}
