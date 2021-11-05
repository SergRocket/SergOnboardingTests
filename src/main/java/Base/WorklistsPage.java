package Base;

import Utils.AppConfig;
import org.openqa.selenium.By;

import java.util.Random;

public class WorklistsPage extends BasePage {
    public By brandImage = new By.ByCssSelector("img[src*='media/logo']");
    public By afterSearchResults = new By.ByClassName("div[class*='part_box_wrapper']");
    public By mainSearch = new By.ById("artnum");
    public By submitSearch = new By.ByCssSelector("button[onclick*='search_bubmit']");
    public By paginationLeftClick = new By.ByCssSelector("a[href*='page=2']");
    public By paginationRightClick = new By.ByXPath("//a[text()='« Назад']");
    public By currentPaginationPage = new By.ByCssSelector("li[class*='number active']>a");
    public By logoutButton = new By.ByXPath("//a[text()='Выход']");
    public By navMenuBlocks = new By.ByCssSelector("div.top-menu > ul > li > div > div > a");
    public By carNames = new By.ByCssSelector("div[class*='model-name']");
    public By catParts = new By.ByCssSelector("div[class*='row parts_cats']");

    public boolean isCompanyLogoVisible() {
        if (findElements(brandImage).size() >0) {
            return true;
        } else
            return false;
    }

    public boolean isCarMakesAreVisible() {
        if (findElements(carNames).size() >0) {
            return true;
        } else
            return false;
    }

    public void logout() {
        findWebElement(logoutButton).click();
    }

    public void paginationClick(){
        findWebElement(paginationLeftClick).click();
    }

    public boolean isPaginationDysplayed(){
        if (findElements(currentPaginationPage).size() >0) {
            return true;
        } else
            return false;
    }

    public int isPaginationCounterUpdated(){
        String text = getElementText(currentPaginationPage);
        Integer paginationValue = Integer.parseInt(text);
        return paginationValue;
    }

    public void sendSearchQuery(String lookingFor){
        findWebElement(mainSearch).sendKeys(lookingFor);
        findWebElement(submitSearch).click();
    }

    public boolean isResultsAreVisible(){
        if(findElements(afterSearchResults).size() >0) {
            return true;
        } else
        return false;
    }

    public boolean isCategoriesAreVisible(){
        if(findElements(catParts).size() >0) {
            return true;
        } else
            return false;
    }

    public void selectingCategory(){
        int favQnt = findElements(navMenuBlocks).size() -1;
        int firstFavImg = 1;
        Random rn = new Random();
        int n = favQnt - firstFavImg - 1;
        int i = rn.nextInt()%n;
        int random = firstFavImg+i;
        findElements(navMenuBlocks).get(random).click();
    }

}
