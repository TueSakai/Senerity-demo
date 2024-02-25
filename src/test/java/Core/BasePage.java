package Core;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BasePage {
    public WebDriver driver;
    private long longTimeout = GlobalConstains.LONG_TIME;
    private long shortTimeout = GlobalConstains.SHORT_TIME;
    public void openPageURL(WebDriver driver, String pageURL){
        driver.get(pageURL);
    }

    public String getPageTitle(WebDriver driver) {
        return driver.getTitle();
    }

    public String getPageSourceCode(WebDriver driver) {
        return driver.getPageSource();
    }

    public void backToPage(WebDriver driver) {
        driver.navigate().back();
    }

    public void forwardToPage(WebDriver driver) {
        driver.navigate().forward();
    }

    public void refreshCurrentPage(WebDriver driver) {
        driver.navigate().refresh();
    }

    public Alert waitForAlertPresence(WebDriver driver) {
        WebDriverWait explicitWait = new WebDriverWait(driver, Duration.ofSeconds(longTimeout));
        return explicitWait.until(ExpectedConditions.alertIsPresent());
    }

    public void acceptAlert(WebDriver driver) {
        waitForAlertPresence(driver).accept();
    }

    public void cancelAlert(WebDriver driver) {
        waitForAlertPresence(driver).dismiss();
    }

    public String getTextAlert(WebDriver driver) {
        return waitForAlertPresence(driver).getText();
    }

    public void sendkeyToAlert(WebDriver driver, String textValue) {
        waitForAlertPresence(driver).sendKeys(textValue);
    }

    public void switchToWindowByID(WebDriver driver, String parentID) {
        Set<String> allWindows = driver.getWindowHandles();
        for (String window : allWindows) {
            if (!window.equals(parentID)) {
                driver.switchTo().window(window);
                break;
            }
        }
    }

    public void switchToWindowByTitle(WebDriver driver, String title) {
        Set<String> allWindows = driver.getWindowHandles();
        for (String window : allWindows) {
            driver.switchTo().window(window);
            String currentWinTitle = driver.getTitle();
            if (currentWinTitle.equals(title)) {
                break;
            }
        }
    }

    public void closeAllWindowWithoutParent(WebDriver driver, String parentID) {
        Set<String> allWindows = driver.getWindowHandles();
        for (String id : allWindows) {
            if (!id.equals(parentID)) {
                driver.switchTo().window(id);
                driver.close();
            }
        }
        driver.switchTo().window(parentID);
    }


    public By getByLocator(String locatorType){
        By by = null;
        if (locatorType.startsWith("id=")){
            by = By.id(locatorType.substring(3));
        } else if (locatorType.startsWith("css=")){
            by = By.cssSelector(locatorType.substring(4));
        } else if (locatorType.startsWith("name=")){
            by = By.name(locatorType.substring(5));
        } else if (locatorType.startsWith("class=")){
            by = By.className(locatorType.substring(6));
        } else if (locatorType.startsWith("xpath=")){
            by = By.xpath(locatorType.substring(6));
        } else {
            throw new RuntimeException("Locator type is not supported");
        }
        return by;
    }

    // Nếu truyền vào locator Type = xpath thì đúng, còn lại sai
    public String getDynamicXpath(String locatorType, String... dynamicValues){
        if(locatorType.startsWith("xpath=")) {
            locatorType = String.format(locatorType, (Object[]) dynamicValues);
        }
        return locatorType;
    }

    public WebElement getWebElement(WebDriver driver, String locatorType ) {
        return driver.findElement(getByLocator(locatorType));
    }

    public WebElement getWebElement(WebDriver driver, String locatorType, String... dynamicValues ) {
        return driver.findElement(getByLocator(getDynamicXpath(locatorType, dynamicValues)));
    }

    public List<WebElement> getListWebElement(WebDriver driver, String locatorType) {
        return driver.findElements(getByLocator(locatorType));
    }

    public List<WebElement> getListWebElement(WebDriver driver, String locatorType, String... dynamicValues) {
        return driver.findElements(getByLocator(getDynamicXpath(locatorType, dynamicValues)));
    }

    public void clickOnElement(WebDriver driver, String locatorType) {
        getWebElement(driver, locatorType).click();
    }

    public void clickOnElement(WebDriver driver, String locatorType, String... dynamicValues) {
        getWebElement(driver, getDynamicXpath(locatorType, dynamicValues)).click();
    }

    public void sendkeyToElement(WebDriver driver, String locatorType, String textValue) {
        WebElement element = getWebElement(driver, locatorType);
        element.clear();
        element.sendKeys(textValue);
    }

    public void sendkeyToElement(WebDriver driver, String locatorType, String textValue, String... dynamicValues) {
        WebElement element = getWebElement(driver, getDynamicXpath(locatorType, dynamicValues));
        element.clear();
        element.sendKeys(textValue);
    }

    public void selectItemInDefaultDropdown(WebDriver driver, String locatorType, String textItem) {
        Select select = new Select(getWebElement(driver, locatorType));
        select.selectByValue(textItem);
    }

    public void selectItemInDefaultDropdown(WebDriver driver, String locatorType, String textItem, String... dynamicValues) {
        Select select = new Select(getWebElement(driver, getDynamicXpath(locatorType, dynamicValues)));
        select.selectByVisibleText(textItem);
    }

    public String getSelectItemDefaultDropdown(WebDriver driver, String locatorType) {
        Select select = new Select(getWebElement(driver, locatorType));
        return select.getFirstSelectedOption().getText();
    }

    public String getSelectItemDefaultDropdown(WebDriver driver, String locatorType, String... dynamicValues) {
        Select select = new Select(getWebElement(driver, getDynamicXpath(locatorType, dynamicValues)));
        return select.getFirstSelectedOption().getText();
    }

    public boolean isDropdownMultiple(WebDriver driver, String locatorType) {
        Select select = new Select(getWebElement(driver, locatorType));
        return select.isMultiple();
    }

    public boolean isDropdownMultiple(WebDriver driver, String locatorType, String... dynamicValues) {
        Select select = new Select(getWebElement(driver, getDynamicXpath(locatorType, dynamicValues)));
        return select.isMultiple();
    }

    public void selectItemInDropdown(WebDriver driver, String parentXpath, String childXpath, String expectedText) {
        getWebElement(driver, parentXpath).click();
        sleepInSecond(1);
        WebDriverWait explicitWait = new WebDriverWait(driver, Duration.ofSeconds(longTimeout));
        List<WebElement> allItems = explicitWait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(getByLocator(childXpath)));
        for (WebElement item : allItems) {
            if (item.getText().trim().equals(expectedText)) {
                if (item.isDisplayed()) {
                    item.click();
                } else {
                    JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
                    jsExecutor.executeScript("arguments[0].scrollIntoView(true);", item);
                    item.click();
                }
                break;
            }
        }
    }

    public String getElementAttribute(WebDriver driver, String attributeName) {
        return getWebElement(driver, attributeName).getAttribute(attributeName);
    }

    public String getElementAttribute(WebDriver driver, String locatorType, String atrributeName, String...dynamicValue) {
        return getWebElement(driver, getDynamicXpath(locatorType, dynamicValue)).getAttribute(atrributeName);
    }

    public String getElementText(WebDriver driver, String locatorType) {
        return getWebElement(driver, locatorType).getText();
    }

    public String getElementText(WebDriver driver, String locatorType, String... dynamicValues) {
        return getWebElement(driver, getDynamicXpath(locatorType, dynamicValues)).getText();
    }

    public String getElementCssValue(WebDriver driver, String locatorType, String propertyName) {
        return getWebElement(driver, locatorType).getCssValue(propertyName);
    }

    public String getHexaColorFromRGBA(String rgbaValue) {
        return Color.fromString(rgbaValue).asHex();
    }

    public int getElementSize(WebDriver driver, String locatorType) {
        return getListWebElement(driver, locatorType).size();
    }

    public int getElementSize(WebDriver driver, String locatorType, String... dynamicValues) {
        return getListWebElement(driver, getDynamicXpath(locatorType, dynamicValues)).size();
    }

    public void checkToDefaultCheckboxOrRadio(WebDriver driver, String locatorType) {
        WebElement element = getWebElement(driver, locatorType);
        if (!element.isSelected()) {
            element.click();
        }
    }

    public void checkToDefaultCheckboxOrRadio(WebDriver driver, String locatorType, String... dynamicValues) {
        WebElement element = getWebElement(driver, getDynamicXpath(locatorType, dynamicValues));
        if (!element.isSelected()){
            element.click();
        }
    }

    public void uncheckToDefaultCheckbox(WebDriver driver, String locatorType) {
        WebElement element = getWebElement(driver, locatorType);
        if (element.isSelected()) {
            element.click();
        }
    }

    public void uncheckToDefaultCheckbox(WebDriver driver, String locatorType, String... dynamicValues) {
        WebElement element = getWebElement(driver, getDynamicXpath(locatorType, dynamicValues));
        if (element.isSelected()) {
            element.click();
        }
    }

    public boolean isElementDisplayed(WebDriver driver, String locatorType) {
        try {
            return getWebElement(driver, locatorType).isDisplayed();
        } catch (NoSuchElementException e){
            return false;
        }
    }

    public void overrideGlobalTimeout(WebDriver driver, long timeOut){
        driver.manage().timeouts().implicitlyWait(timeOut, TimeUnit.SECONDS);
    }

    public boolean isElementUndisplay(WebDriver driver, String locatorType){
        overrideGlobalTimeout(driver,shortTimeout);
        List<WebElement> elements = getListWebElement(driver,locatorType);
        overrideGlobalTimeout(driver,longTimeout);

        if(elements.size()==0){
            return true;
        } else if(elements.size()>0 && !elements.get(0).isDisplayed()){
            return true;
        } else {
            return false;
        }
    }

    public boolean isElementUndisplay(WebDriver driver, String locatorType, String... dynamicValues){
        System.out.println("Start time =" + new Date().toString());

        overrideGlobalTimeout(driver,5);
        List<WebElement> elements = getListWebElement(driver,getDynamicXpath(locatorType, dynamicValues));
        overrideGlobalTimeout(driver,30);

        if(elements.size()==0){
            System.out.println("Element not in DOM");
            System.out.println("Start time =" + new Date().toString());
            return true;
        } else if(elements.size()>0 && !elements.get(0).isDisplayed()){
            System.out.println("Element in DOM but not visible/displayed");
            System.out.println("Start time =" + new Date().toString());
            return true;
        } else {
            System.out.println("Element in DOM and visible");
            return false;
        }
    }

    public boolean isElementUndisplayed(WebDriver driver, String locatorType) {
        boolean status = true;
        if(getWebElement(driver, locatorType).isDisplayed()){
            status = false;
        }
        return status;
    }

    public boolean isElementDisplayed(WebDriver driver, String locatorType, String... dynamicValues) {
        return getWebElement(driver, getDynamicXpath(locatorType, dynamicValues)).isDisplayed();
    }

    public boolean isElementEnabled(WebDriver driver, String locatorType) {
        return getWebElement(driver, locatorType).isEnabled();
    }
    public boolean isElementEnabled(WebDriver driver, String locatorType, String... dynamicValues) {
        return getWebElement(driver, getDynamicXpath(locatorType, dynamicValues)).isEnabled();
    }

    public boolean isElementSelected(WebDriver driver, String locatorType) {
        return getWebElement(driver, locatorType).isSelected();
    }

    public boolean isElementSelected(WebDriver driver, String locatorType, String... dynamicValues) {
        return getWebElement(driver, getDynamicXpath(locatorType, dynamicValues)).isSelected();
    }

    public void switchToFrameIframe(WebDriver driver, String locatorType) {
        driver.switchTo().frame(getWebElement(driver, locatorType));
    }

    public void switchToFrameIframe(WebDriver driver, String locatorType, String... dynamicValues) {
        driver.switchTo().frame(getWebElement(driver, getDynamicXpath(locatorType, dynamicValues)));
    }

    public void switchToDefaultContent(WebDriver driver) {
        driver.switchTo().defaultContent();
    }

    public void hoverMouseToElement(WebDriver driver, String locatorType) {
        Actions action = new Actions(driver);
        action.moveToElement(getWebElement(driver, locatorType)).perform();
    }

    public void hoverMouseToElement(WebDriver driver, String locatorType, String... dynamicValues) {
        Actions action = new Actions(driver);
        action.moveToElement(getWebElement(driver, getDynamicXpath(locatorType, dynamicValues))).perform();
    }

    public void pressKeyToElement(WebDriver driver, String locatorType, Keys key) {
        Actions action = new Actions(driver);
        action.sendKeys(getWebElement(driver, locatorType), key).perform();
    }

    public void pressKeyToElement(WebDriver driver, String locatorType,Keys key, String... dynamicValues) {
        Actions action = new Actions(driver);
        action.sendKeys(getWebElement(driver, getDynamicXpath(locatorType, dynamicValues)), key).perform();
    }

    public void scrollToBottomPage(WebDriver driver) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        jsExecutor.executeScript("window.scrollBy(0,document.body.scrollHeight)");
    }

    public void highlightElement(WebDriver driver, String locatorType) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        WebElement element = getWebElement(driver, locatorType);
        String originalStyle = element.getAttribute("style");
        jsExecutor.executeScript("arguments[0].setAttribute(arguments[1], arguments[2])", element, "style", "border: 2px solid red; border-style: dashed;");
        sleepInSecond(1);
        jsExecutor.executeScript("arguments[0].setAttribute(arguments[1], arguments[2])", element, "style", originalStyle);
    }

    public void highlightElement(WebDriver driver, String locatorType, String... dynamicValues) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        WebElement element = getWebElement(driver, getDynamicXpath(locatorType, dynamicValues));
        String originalStyle = element.getAttribute("style");
        jsExecutor.executeScript("arguments[0].setAttribute(arguments[1], arguments[2])", element, "style", "border: 2px solid red; border-style: dashed;");
        sleepInSecond(1);
        jsExecutor.executeScript("arguments[0].setAttribute(arguments[1], arguments[2])", element, "style", originalStyle);
    }

    public void clickOnElementByJS(WebDriver driver, String locatorType) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        jsExecutor.executeScript("arguments[0].click();", getWebElement(driver, locatorType));
    }

    public void clickToElementByJS(WebDriver driver, String locatorType,String... dynamicValues) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        jsExecutor.executeScript("arguments[0].click();", getWebElement(driver, getDynamicXpath(locatorType, dynamicValues)));
    }

    public void scrollToElement(WebDriver driver, String locatorType) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        jsExecutor.executeScript("arguments[0].scrollIntoView(true);", getWebElement(driver, locatorType));
    }

    public void scrollToElement(WebDriver driver, String locatorType, String... dynamicValues) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        jsExecutor.executeScript("arguments[0].scrollIntoView(true);", getWebElement(driver, getDynamicXpath(locatorType, dynamicValues)));
    }

    public void removeAttributeInDOM(WebDriver driver, String locatorType, String attributeRemove) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        jsExecutor.executeScript("arguments[0].removeAttribute('" + attributeRemove + "');", getWebElement(driver, locatorType));
    }

    public void removeAttributeInDOM(WebDriver driver, String locatorType, String attributeRemove, String... dynamicValues) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        jsExecutor.executeScript("arguments[0].removeAttribute('" + attributeRemove + "');", getWebElement(driver, getDynamicXpath(locatorType, dynamicValues)));
    }

    public boolean areJQueryAndJSLoadedSuccess(WebDriver driver) {
        WebDriverWait explicitWait = new WebDriverWait(driver, Duration.ofSeconds(longTimeout));
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        ExpectedCondition<Boolean> jQueryLoad = new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    return ((Long) jsExecutor.executeScript("return jQuery.active") == 0);
                } catch (Exception e) {
                    return true;
                }
            }
        };
        ExpectedCondition<Boolean> jsLoad = new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return jsExecutor.executeScript("return document.readyState").toString().equals("complete");
            }
        };
        return explicitWait.until(jQueryLoad) && explicitWait.until(jsLoad);
    }

    public String getElementValidationMessage(WebDriver driver, String locatorType) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        return (String) jsExecutor.executeScript("return arguments[0].validationMessage;", getWebElement(driver, locatorType));
    }

    public String getElementValidationMessage(WebDriver driver, String locatorType, String... dynamicValues) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        return (String) jsExecutor.executeScript("return arguments[0].validationMessage;", getWebElement(driver, getDynamicXpath(locatorType, dynamicValues)));
    }

    public boolean isImageLoaded(WebDriver driver, String locatorType) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        boolean status = (boolean) jsExecutor.executeScript("return arguments[0].complete && typeof arguments[0].naturalWidth != \"undefined\" && arguments[0].naturalWidth > 0", getWebElement(driver, locatorType));
        if (status) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isImageLoaded(WebDriver driver, String locatorType, String... dynamicValues) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        boolean status = (boolean) jsExecutor.executeScript("return arguments[0].complete && typeof arguments[0].naturalWidth != \"undefined\" && arguments[0].naturalWidth > 0", getWebElement(driver, getDynamicXpath(locatorType, dynamicValues)));
        if (status) {
            return true;
        } else {
            return false;
        }
    }

    public void waitForElementVisible(WebDriver driver, String locatorType) {
        WebDriverWait explicitWait = new WebDriverWait(driver, Duration.ofSeconds(longTimeout));
        explicitWait.until(ExpectedConditions.visibilityOfElementLocated(getByLocator(locatorType)));
    }

    public void waitForElementVisible(WebDriver driver, String locatorType, String... dynamicValues) {
        WebDriverWait explicitWait = new WebDriverWait(driver, Duration.ofSeconds(longTimeout));
        explicitWait.until(ExpectedConditions.visibilityOfElementLocated(getByLocator(getDynamicXpath(locatorType, dynamicValues))));
    }

    public void waitForAllElementVisible(WebDriver driver, String locatorType) {
        WebDriverWait explicitWait = new WebDriverWait(driver, Duration.ofSeconds(longTimeout));
        explicitWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(getByLocator(locatorType)));
    }

    public void waitForAllElementVisible(WebDriver driver, String locatorType, String... dynamicValues) {
        WebDriverWait explicitWait = new WebDriverWait(driver, Duration.ofSeconds(longTimeout));
        explicitWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(getByLocator(getDynamicXpath(locatorType, dynamicValues))));
    }

    public void waitForElementInvisible(WebDriver driver, String locatorType) {
        WebDriverWait explicitWait = new WebDriverWait(driver, Duration.ofSeconds(longTimeout));
        explicitWait.until(ExpectedConditions.invisibilityOfElementLocated(getByLocator(locatorType)));
    }

    public void waitForElementInvisible(WebDriver driver, String locatorType, String... dynamicValues) {
        WebDriverWait explicitWait = new WebDriverWait(driver, Duration.ofSeconds(longTimeout));
        explicitWait.until(ExpectedConditions.invisibilityOfElementLocated(getByLocator(getDynamicXpath(locatorType, dynamicValues))));
    }

    public void waitForAllElementInvisible(WebDriver driver, String locatorType) {
        WebDriverWait explicitWait = new WebDriverWait(driver, Duration.ofSeconds(longTimeout));
        explicitWait.until(ExpectedConditions.invisibilityOfAllElements(getListWebElement(driver, locatorType)));
    }

    public void waitForAllElementInvisible(WebDriver driver, String locatorType, String... dynamicValues) {
        WebDriverWait explicitWait = new WebDriverWait(driver, Duration.ofSeconds(longTimeout));
        explicitWait.until(ExpectedConditions.invisibilityOfAllElements(getListWebElement(driver, getDynamicXpath(locatorType, dynamicValues))));
    }

    public void waitForElementUndisplay(WebDriver driver, String locatorType) {
        WebDriverWait explicitWait = new WebDriverWait(driver, Duration.ofSeconds(shortTimeout));
        overrideGlobalTimeout(driver, shortTimeout);
        explicitWait.until(ExpectedConditions.invisibilityOfAllElements(getListWebElement(driver, locatorType)));
        overrideGlobalTimeout(driver, longTimeout);
    }

    public void waitForElementClickable(WebDriver driver, String locatorType) {
        WebDriverWait explicitWait = new WebDriverWait(driver, Duration.ofSeconds(longTimeout));
        explicitWait.until(ExpectedConditions.elementToBeClickable(getByLocator(locatorType)));
    }

    public void waitForElementClickable(WebDriver driver, String locatorType, String... dynamicValues) {
        WebDriverWait explicitWait = new WebDriverWait(driver, Duration.ofSeconds(longTimeout));
        explicitWait.until(ExpectedConditions.elementToBeClickable(getByLocator(getDynamicXpath(locatorType, dynamicValues))));
    }
    public static final String UPLOAD_FILE="xpath=//input[@type='file']";
    public void uploadMultipleFile(WebDriver driver, String... fileNames){
        String filePath = GlobalConstains.UPLOAD_FILE;
        String fullFileName = "";
        for (String file:fileNames){
            fullFileName = fullFileName + filePath + file;
        }
        fullFileName = fullFileName.trim();
        getWebElement(driver, UPLOAD_FILE).sendKeys(fullFileName);
    }

    public void sleepInSecond(long timeoutInSecond) {
        try {
            Thread.sleep(timeoutInSecond * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


//    // Tối ưu lần 1 ở bài học Switch Page
//    public UserCustomerInfoPageObject openCustomerPage(WebDriver driver) {
//        waitForElementClickable(driver, BasePageUI.CUSTOMER_LINK);
//        clickToElement(driver, BasePageUI.CUSTOMER_LINK);
//        return PageGeneratorManager.getUserCustomerPage(driver);
//    }
//    public UserAddressesPageObject openAddressesPage(WebDriver driver){
//        waitForElementClickable(driver, BasePageUI.ADDRESSES_LINK);
//        clickToElement(driver, BasePageUI.ADDRESSES_LINK);
//        return PageGeneratorManager.getUserAddressesPage(driver);
//    }
//    public UserOrderPageObject openOrderPage(WebDriver driver){
//        waitForElementClickable(driver, BasePageUI.ORDER_LINK);
//        clickToElement(driver, BasePageUI.ORDER_LINK);
//        return PageGeneratorManager.getUserOrderPage(driver);
//    }
//    public UserDownloadableProductPageObject openDownloadableProductPage(WebDriver driver){
//        waitForElementClickable(driver, BasePageUI.DOWNLOADABLE_PRODUCT_LINK);
//        clickToElement(driver, BasePageUI.DOWNLOADABLE_PRODUCT_LINK);
//        return PageGeneratorManager.getUserDownloadableProductPage(driver);
//    }
//    public UserBackInStockSubscriptionsPageObject openBackInStockSubscriptions(WebDriver driver){
//        waitForElementClickable(driver, BasePageUI.BACK_IN_STOCK_SUBSCRIPTIONS_LINK);
//        clickToElement(driver, BasePageUI.BACK_IN_STOCK_SUBSCRIPTIONS_LINK);
//        return PageGeneratorManager.getuserBackInStockSubscriptions(driver);
//    }
//    public UserRewardPointsPageObject openRewardPointPage(WebDriver driver){
//        waitForElementClickable(driver, BasePageUI.REWARD_POINT_LINK);
//        clickToElement(driver, BasePageUI.REWARD_POINT_LINK);
//        return PageGeneratorManager.getUserRewardPointsPage(driver);
//    }
//    public UserMyProductReviewPageObject openMyProductReviewPage(WebDriver driver){
//        waitForElementClickable(driver, BasePageUI.MY_PRODUCT_REVIEWS_LINK);
//        clickToElement(driver, BasePageUI.MY_PRODUCT_REVIEWS_LINK);
//        return PageGeneratorManager.getUserMyProductReviewPage(driver);
//    }
//
//    // Tối ưu lần 2 ở bài học Dynamic Locator
//    public BasePage openPageMyAcountPageByName(WebDriver driver, String pageName){
//        waitForElementClickable(driver, BasePageUI.DYNAMIC_PAGE_AT_MY_ACCOUT_AREA, pageName);
//        clickToElement(driver, BasePageUI.DYNAMIC_PAGE_AT_MY_ACCOUT_AREA, pageName);
//        switch (pageName){
//            case "Customer info":
//                return PageGeneratorManager.getUserCustomerPage(driver);
//            case "Addresses":
//                return PageGeneratorManager.getUserAddressesPage(driver);
//            case "Orders":
//                return PageGeneratorManager.getUserOrderPage(driver);
////            case "Downloadable product":
////                return PageGeneratorManager.getUserDownloadableProductPage(driver);
////            case "Back in stock subscriptions":
////                return PageGeneratorManager.getuserBackInStockSubscriptions(driver);
//            case "Reward points":
//                return PageGeneratorManager.getUserRewardPointsPage(driver);
//            case "My product reviews":
//                return PageGeneratorManager.getUserMyProductReviewPage(driver);
//
//            default:
//                throw new RuntimeException(" Invalid page name at My Account area.");
//        }
//    }
//
//    // Tối ưu lần 1 ở bài Switch Role
//    public UserHomePageObject clickTOLogoutLinkAtUser(WebDriver driver){
//        waitForElementClickable(driver,BasePageUI.LOGOUT_LINK_USER);
//        clickToElement(driver,BasePageUI.LOGOUT_LINK_USER);
//        return PageGeneratorManager.getUserHomePage(driver);
//    }
//    public AdminLoginPageObject clickToLogoutLinkAtAdmin(WebDriver driver){
//        waitForElementClickable(driver,BasePageUI.LOGOUT_LINK_ADMIN);
//        clickToElement(driver,BasePageUI.LOGOUT_LINK_ADMIN);
//        return PageGeneratorManager.getAdminLoginPage(driver);
//
//    }
//
//    public Set<Cookie> getAllCookies(WebDriver driver){
//        return  driver.manage().getCookies();
//    }
//
//    public void setAllCookies(WebDriver driver, Set<Cookie> allCookies){
//        for (Cookie cookie: allCookies){
//            driver.manage().addCookie(cookie);
//        }
//    }
//
//    public void enterToTextboxID(WebDriver driver, String textboxID, String value) {
//        waitForElementVisible(driver, BasePageUI.DYNAMIC_TEXTBOX_BY_ID,textboxID);
//        sendkeyToElement(driver,BasePageUI.DYNAMIC_TEXTBOX_BY_ID,value,textboxID);
//    }
//
//    public void clickToLinkByName(WebDriver driver, String linkText) {
//        waitForElementVisible(driver, BasePageUI.DYNAMIC_LINK_BY_TEXT,linkText);
//        clickToElement(driver,BasePageUI.DYNAMIC_LINK_BY_TEXT,linkText);
//    }

}
