package com.selenium.element.impls;

import com.selenium.driver.DriverOperation;
import com.selenium.element.PortalOperator;
import com.selenium.enums.Direction;
import com.selenium.enums.Position;
import com.selenium.utils.tools.ColorUtils;
import com.selenium.utils.tools.Tools;
import com.selenium.utils.xpath.XpathBuilder;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;

import java.io.File;
import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Slf4j
public class ElementByXpath extends DriverOperation implements PortalOperator {
    private int currentXPoint = 0;
    private int currentYPoint = 0;
    private int nextXPoint = 0;
    private int nextYpoint = 0;

    public ElementByXpath(WebDriver driver) {
        super(driver);
    }

    @Override
    public void click(String text) {
        String value = XpathBuilder.xpathGenerator(text);
        executeClick(value);
    }

    @Override
    public void click(String text, String index) {
        String value = XpathBuilder.xpathGenerator(text, index);
        executeClick(value);
    }

    @Override
    public void click(String text, int secondTimeout) {
        String value = XpathBuilder.xpathGenerator(text);
        executeClick(value, secondTimeout);
    }

    @Override
    public void click(WebElement element) {
        log.info("clicking element: {}", getElementXPath(element));
        try {
            element.click();
        } catch (WebDriverException e) {
            log.warn(e.toString(), e);
            if (e instanceof ElementNotInteractableException) {
                jsClick(element);
            }
        }
    }

    @Override
    public void click(By by) {
        if (isElementClickable(by)) {
            try {
                driver.findElement(by).click();
            } catch (WebDriverException e) {
                log.warn(e.toString(), e);
                if (e instanceof ElementNotInteractableException) {
                    action(driver.findElement(by)).moveToElement(driver.findElement(by)).click().build().perform();
                }
            }
        } else {
            throw new WebDriverException("the element: (" + by.toString() + ") is not clickable");
        }
    }

    @Override
    public void click(int width, int height) {
        log.info("clicking coordinate: ({}, {})", nextXPoint, nextYpoint);
        action(width, height).moveByOffset(nextXPoint, nextYpoint).click().build().perform();
    }

    @Override
    public void click(int width, int height, String precondition) {
        log.info("clicking coordinate: ({}, {})", width, height);
        isElementExist(precondition);
        click(width, height);
    }

    @Override
    public void click(int width, int height, String precondition, String expectation) {
        log.info("clicking coordinate: ({}, {})", width, height);
        isElementExistContain(precondition);
        click(width, height);
        if (isElementExistContain(expectation)) {
            throw new WebDriverException("after clicking " + precondition + " could not show " + expectation);
        }
    }

    @Override
    public void clickAny(String text) {
        String value = XpathBuilder.xpathGenerator(text);
        log.info("clicking element: {}", value);
        if (isElementExist(value)) {
            Point location = findElement(value).getLocation();
            click(location.getX(), location.getY());
        } else {
            throw new WebDriverException("the element does not show on portal");
        }
    }

    @Override
    public void clickContain(String text) {
        String value = XpathBuilder.xpathContainsGenerator(text);
        executeClick(value);
    }

    @Override
    public void clickContain(String text, int secondTimeout) {
        String value = XpathBuilder.xpathContainsGenerator(text);
        executeClick(value, secondTimeout);
    }

    @Override
    public void jsClick(String xpath) {
        log.info("clicking element: {} by js", xpath);
        js.executeScript("arguments[0].click();", driver.findElement(By.xpath(XpathBuilder.xpathGenerator(xpath))));
    }

    @Override
    public void jsClick(WebElement element) {
        log.info("clicking element: {} by js", getElementXPath(element));
        js.executeScript("arguments[0].click();", element);
    }

    @Override
    public String getText(String text) {
        log.info("getting text by xpath: {}", text);
        String value = XpathBuilder.xpathGenerator(text);
        String temtext = "";
        for (int i = 0; i < secondTimeOut * 1000 / interval; i++) {
            try {
                String getText = driver.findElement(By.xpath(value)).getText();
                if (!"".equals(getText) && getText != null) {
                    temtext = getText;
                    break;
                } else {
                    Tools.sleep(interval);
                }
            } catch (WebDriverException ignore) {
            }
        }
        return temtext;
    }

    @Override
    public String getText(String text, int secondTimeout) {
        log.info("getting text by xpath: {}", text);
        String value = XpathBuilder.xpathGenerator(text);
        String temtext = "";
        for (int i = 0; i < secondTimeout * 1000 / interval; i++) {
            try {
                String getText = driver.findElement(By.xpath(value)).getText();
                if (!"".equals(getText) && getText != null) {
                    temtext = getText;
                    break;
                } else {
                    Tools.sleep(interval);
                }
            } catch (WebDriverException ignore) {
            }
        }
        return temtext;
    }

    @Override
    public String getText(WebElement element) {
        String text = null;
        for (int i = 0; i < secondTimeOut * 1000 / interval; i++) {
            try {
                Tools.sleep(interval);
                String getText = element.getText();
                if (getText != null) {
                    text = getText;
                    break;
                }
            } catch (WebDriverException ignore) {
            }
        }
        return text;
    }

    @Override
    public List<String> getTexts(String text) {
        List<WebElement> webElements = findElements(text);
        List<String> texts = new LinkedList<>();
        webElements.forEach(i -> {
            try {
                texts.add(i.getText());
            } catch (WebDriverException e) {
                log.debug(e.toString(), e);
            }
        });
        return texts;
    }

    @Override
    public List<String> getTexts(String text, int secondTimeOut) {
        List<WebElement> webElements = findElements(text, secondTimeOut);
        List<String> texts = new LinkedList<>();
        webElements.forEach(i -> {
            try {
                texts.add(i.getText());
            } catch (WebDriverException e) {
                log.debug(e.toString(), e);
            }
        });
        return texts;
    }

    @Override
    public void clickXYBesidesText(int pixel, Direction direction, String xpath) {
        List<WebElement> element = findElements(xpath);
        if (element.size() > 1) {
            throw new WebDriverException("found more then one element by xpath: " + xpath);
        }
        Dimension dimension = element.get(0).getSize();
        Actions action = new Actions(driver);
        action.moveToElement(element.get(0));
        switch (direction) {
            case UP:
                action.moveByOffset(0, (-dimension.getHeight() / 2) - pixel);
                break;
            case DOWN:
                action.moveByOffset(0, (dimension.getHeight() / 2) + pixel);
                break;
            case LEFT:
                action.moveByOffset((-dimension.getWidth() / 2) - pixel, 0);
                break;
            case RIGHT:
                action.moveByOffset((dimension.getWidth() / 2) + pixel, 0);
                break;
        }
        action.click().build().perform();
    }

    @Override
    public void input(String xpath, String text) {
        log.info("inputing: {}", text);
        String value = XpathBuilder.xpathGenerator(xpath);
        for (char i : text.toCharArray()) {
            findElement(value).sendKeys(String.valueOf(i));
        }
    }

    @Override
    public void input(WebElement element, String text) {
        log.info("inputing: {}", text);
        for (char i : text.toCharArray()) {
            element.sendKeys(String.valueOf(i));
        }
    }

    @Override
    public void input(String xpath, Keys keys) {
        log.info("pressing keyboard: {}", keys.toString());
        String value = XpathBuilder.xpathGenerator(xpath);
        findElement(value).sendKeys(keys);
    }

    @Override
    public void uploadFile(String xpath, File file) {
        log.info("uploading file: {}", file.getAbsolutePath());
        String value = XpathBuilder.xpathGenerator(xpath);
        findElement(value).sendKeys(file.getAbsolutePath());
    }

    @Override
    public void clearInputBox(String text) {
        findElement(text).sendKeys(Keys.CONTROL + "a");
        findElement(text).sendKeys(Keys.BACK_SPACE);
        findElement(text).sendKeys(Keys.ENTER);
        findElement(text).clear();
    }

    @Override
    public void clearInputBox(WebElement element) {
        element.sendKeys(Keys.CONTROL + "a");
        element.sendKeys(Keys.BACK_SPACE);
        element.sendKeys(Keys.ENTER);
        element.clear();
    }

    @Override
    public void pressKey(String text, Keys keys) {
        log.info("pressing keyboard: " + keys.getCodePoint());
        action(findElement(text)).moveByOffset(nextXPoint, nextYpoint).sendKeys(keys).build().perform();
    }

    @Override
    public WebElement findElement(String text) {
        String value = XpathBuilder.xpathGenerator(text);
        return executeFindElement(value);
    }

    @Override
    public WebElement findElement(String text, String index) {
        String value = XpathBuilder.xpathGenerator(text, index);
        return executeFindElement(value);
    }

    @Override
    public WebElement findElement(String text, int secondTimeout) {
        String value = XpathBuilder.xpathGenerator(text);
        return executeFindElement(value, secondTimeout);
    }

    @Override
    public WebElement findElement(String text, String index, int secondTimeout) {
        String value = XpathBuilder.xpathGenerator(text, index);
        return executeFindElement(value, secondTimeout);
    }

    @Override
    public WebElement findElementContains(String text, int secondTimeout) {
        String value = XpathBuilder.xpathContainsGenerator(text);
        return executeFindElement(value, secondTimeout);
    }

    @Override
    public WebElement findElementContains(String text) {
        String value = XpathBuilder.xpathContainsGenerator(text);
        return executeFindElement(value);
    }

    @Override
    public List<WebElement> findElements(String text) {
        String value = XpathBuilder.xpathGenerator(text);
        return executeFindElements(value);
    }

    @Override
    public List<WebElement> findElements(String text, int secondTimeout) {
        String value = XpathBuilder.xpathGenerator(text);
        return executeFindElements(value, secondTimeout);
    }

    @Override
    public void waitElementDisappear(String element) {
        String value = XpathBuilder.xpathGenerator(element);
        boolean ele;
        for (int i = 1; i <= secondTimeOut * 1000 / interval; i++) {
            ele = isElementClickable(value, 1);
            if (!ele) {
                break;
            }
            Tools.sleep(interval);
        }
    }

    @Override

    public Object getElementCssValue(String text, String attribute) {
        return findElement(text).getCssValue(attribute);
    }

    @Override
    public WebElement findEditableInputBox(String xpath) {
        WebElement ele = null;
        List<WebElement> allEle = findElements(xpath);
        for (WebElement i : allEle) {
            boolean flag = false;
            try {
                i.sendKeys("test");
                i.clear();
                flag = true;
            } catch (WebDriverException ignore) {
            }
            if (isElementClickable(i, 30) || flag) {
                ele = i;
                break;
            }
        }
        if (Objects.isNull(ele)) {
            throw new WebDriverException("could not found clickable webelement: " + xpath);
        }
        return ele;
    }

    @Override
    public WebElement findClickableElement(String xpath) {
        WebElement ele = null;
        List<WebElement> allEle = findElements(xpath);
        for (WebElement i : allEle) {
            if (isElementClickable(i, 30)) {
                ele = i;
                break;
            }
        }
        if (Objects.isNull(ele)) {
            throw new WebDriverException("could not found clickable webelement: " + xpath);
        }
        return ele;
    }

    @Override
    public WebElement findClickableElement(String xpath, int millisTimeOut) {
        WebElement ele = null;
        List<WebElement> allEle = findElements(xpath);
        for (WebElement i : allEle) {
            if (isElementClickable(i, millisTimeOut)) {
                ele = i;
                break;
            }
        }
        if (Objects.isNull(ele)) {
            throw new WebDriverException("could not found clickable webelement: " + xpath);

        }
        return ele;
    }

    @Override
    public List<WebElement> findClickableElements(String xpath) {
        List<WebElement> ele = new LinkedList<>();
        List<WebElement> allEle = findElements(xpath);
        for (WebElement i : allEle) {
            if (isElementClickable(i, 30)) {
                ele.add(i);
            }
        }
        return ele;
    }

    @Override
    public void hover(Position position) {
        log.info("hovering to: {}", position);
        Dimension location = driver.findElement(By.tagName("body")).getSize();
        int width = location.width;
        int height = location.height;
        switch (position) {
            case LETF_TOP:
                toInitialPoint();
                actions.moveByOffset(0, 0).build().perform();
                break;
            case LEFT_BUTTON:
                toInitialPoint();
                actions.moveByOffset(0, height).build().perform();
                break;
            case RIGHT_TOP:
                toInitialPoint();
                actions.moveByOffset(width, 0).build().perform();
                break;
            case RIGHT_BUTTON:
                toInitialPoint();
                actions.moveByOffset(width, height).build().perform();
                break;
            case LEFT_CENTER:
                toInitialPoint();
                actions.moveByOffset(0, height / 2).build().perform();
                break;
            case RIGHT_CENTER:
                toInitialPoint();
                actions.moveByOffset(width, height / 2).build().perform();
                break;
            case TOP_CENTER:
                toInitialPoint();
                actions.moveByOffset(width / 2, 0).build().perform();
                break;
            case BUTTON_CENTER:
                toInitialPoint();
                actions.moveByOffset(width / 2, height).build().perform();
                break;
        }
    }

    @Override
    public void hoverAnElement(WebElement element) {
        log.info("hovering element: {}", getElementXPath(element));
        action(element).moveByOffset(nextXPoint, nextYpoint).build().perform();
    }

    @Override
    public void hoverAnElement(String element) {
        log.info("hovering text: " + element);
        List<WebElement> elements = findElements(element);
        if (elements.size() == 1) {
            action(elements.get(0)).moveByOffset(nextXPoint, nextYpoint).build().perform();
        } else {
            for (WebElement i : elements) {
                try {
                    action(i).moveByOffset(nextXPoint, nextYpoint).build().perform();
                } catch (WebDriverException ignore) {
                    Tools.sleep(2000);
                }
            }
        }
    }


    @Override
    public void hoverAnElement(String element, String index) {
        log.info("hovering text: {}", element);
        isElementClickable(XpathBuilder.xpathGenerator(element, index));
        action(findElement(element, index)).moveByOffset(nextXPoint, nextYpoint).build().perform();
    }

    @Override
    public void hoverAnElement(int x, int y) {
        log.info("hovering to coordinate to: {}, {}", x, y);
        action(x, y).moveByOffset(nextXPoint, nextYpoint).build().perform();
    }

    @Override
    public void scrollToTop() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollTo(0,0)");
    }

    @Override
    public void scrollToBottom() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollTo(0,document.body.scrollHeight)");
    }

    @Override
    public void scrollToLeft() {
        js.executeScript("window.scroll(-500000, 0)");
    }

    @Override
    public void scrollToRight() {
        js.executeScript("window.scroll(500000, 0)");
    }

    @Override
    public void scrollToElement(String text) {
        js.executeScript("arguments[0].scrollIntoView(true);", findElement(text));
    }

    @Override
    public boolean isElementExist(String text) {
        boolean flag = false;
        String value = XpathBuilder.xpathGenerator(text);
        log.info("checking presence of the element: {}", value);
        for (int i = 0; i < secondTimeOut * 1000 / interval; i++) {
            try {
                WebElement ele = driver.findElement(By.xpath(value));
                highlightElement(ele);
                flag = true;
                break;
            } catch (WebDriverException e) {
                Tools.sleep(interval);
            }
        }
        return flag;
    }

    @Override
    public boolean isElementExist(String text, int secondTimeout) {
        boolean flag = false;
        String value = XpathBuilder.xpathGenerator(text);
        log.info("checking presence of the element: {}", value);
        for (int i = 0; i < secondTimeout * 1000 / interval; i++) {
            try {
                WebElement ele = driver.findElement(By.xpath(value));
                highlightElement(ele);
                flag = true;
                break;
            } catch (WebDriverException e) {
                Tools.sleep(interval);
            }
        }
        return flag;
    }

    @Override
    public boolean isElementExist(String text, float millSecondTimeout) {
        boolean flag = false;
        String value = XpathBuilder.xpathGenerator(text);
        log.info("checking presence of the element: {}", value);
        for (int i = 0; i < millSecondTimeout * 1000 / interval; i++) {
            try {
                WebElement ele = driver.findElement(By.xpath(value));
                highlightElement(ele);
                flag = true;
                break;
            } catch (WebDriverException e) {
                Tools.sleep(interval);
            }
        }
        return flag;
    }

    @Override
    public boolean isElementNotExist(String text) {
        boolean flag = false;
        String value = XpathBuilder.xpathGenerator(text);
        log.info("waiting element gone: {}", value);
        for (int i = 0; i < secondTimeOut * 1000 / interval; i++) {
            try {
                Tools.sleep(interval);
                WebElement ele = driver.findElement(By.xpath(value));
                highlightElement(ele);
            } catch (WebDriverException e) {
                if (e.toString() != null) {
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }

    @Override
    public boolean isElementNotExist(String text, int secondTimeout) {
        boolean flag = false;
        String value = XpathBuilder.xpathGenerator(text);
        log.info("waiting element gone: {}", value);
        for (int i = 0; i < secondTimeout * 1000 / interval; i++) {
            try {
                Tools.sleep(interval);
                WebElement ele = driver.findElement(By.xpath(value));
                highlightElement(ele);
            } catch (WebDriverException e) {
                if (e.toString() != null) {
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }

    @Override
    public boolean isElementExistContain(String text) {
        boolean flag;
        String value = XpathBuilder.xpathContainsGenerator(text);
        log.info("checking presence of the element: {}", value);
        try {
            executeFindElement(value);
            flag = true;
        } catch (WebDriverException e) {
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean isElementExistContain(String text, int secondTimeout) {
        boolean flag;
        String value = XpathBuilder.xpathContainsGenerator(text);
        log.info("checking presence of the element: {}", value);
        try {
            executeFindElement(value, secondTimeout);
            flag = true;
        } catch (WebDriverException e) {
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean isElementClickable(By by) {
        boolean flag;
        try {
            FluentWait<WebDriver> wait = new FluentWait<>(driver)
                    .withTimeout(Duration.ofSeconds(secondTimeOut))
                    .pollingEvery(Duration.ofMillis(interval))
                    .ignoring(WebDriverException.class);
            wait.until((ExpectedConditions.elementToBeClickable(by)));
            wait.until(ExpectedConditions.visibilityOf(driver.findElement(by)));
            flag = true;
        } catch (WebDriverException e) {
            log.debug(e.toString(), e);
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean isElementClickable(By by, int timeOut) {
        boolean flag;
        try {
            FluentWait<WebDriver> wait = new FluentWait<>(driver)
                    .withTimeout(Duration.ofSeconds(timeOut))
                    .pollingEvery(Duration.ofMillis(interval))
                    .ignoring(WebDriverException.class);
            wait.until((ExpectedConditions.elementToBeClickable(by)));
            wait.until(ExpectedConditions.visibilityOf(driver.findElement(by)));
            flag = true;
        } catch (WebDriverException e) {
            log.debug(e.toString(), e);
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean isElementClickable(WebElement element) {
        boolean flag;
        try {
            FluentWait<WebDriver> wait = new FluentWait<>(driver)
                    .withTimeout(Duration.ofSeconds(secondTimeOut))
                    .pollingEvery(Duration.ofMillis(interval))
                    .ignoring(WebDriverException.class);
            wait.until((ExpectedConditions.elementToBeClickable(element)));
            wait.until(ExpectedConditions.visibilityOf(element));
            flag = true;
        } catch (WebDriverException e) {
            log.debug(e.toString(), e);
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean isElementClickable(WebElement element, int millisTimeOut) {
        boolean flag;
        try {
            FluentWait<WebDriver> wait = new FluentWait<>(driver)
                    .withTimeout(Duration.ofMillis(millisTimeOut))
                    .pollingEvery(Duration.ofMillis(interval))
                    .ignoring(WebDriverException.class);
            wait.until((ExpectedConditions.elementToBeClickable(element)));
            wait.until(ExpectedConditions.visibilityOf(element));
            flag = true;
        } catch (WebDriverException e) {
            log.debug(e.toString(), e);
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean isElementClickable(String xpath) {
        boolean flag;
        try {
            FluentWait<WebDriver> wait = new FluentWait<>(driver)
                    .withTimeout(Duration.ofSeconds(secondTimeOut))
                    .pollingEvery(Duration.ofMillis(interval))
                    .ignoring(WebDriverException.class);
            wait.until((ExpectedConditions.elementToBeClickable(By.xpath(xpath))));
            flag = true;
        } catch (WebDriverException e) {
            log.debug(e.toString(), e);
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean isElementClickable(String xpath, int secondTimeOut) {
        boolean flag;
        try {
            FluentWait<WebDriver> wait = new FluentWait<>(driver)
                    .withTimeout(Duration.ofSeconds(secondTimeOut))
                    .pollingEvery(Duration.ofMillis(interval))
                    .ignoring(WebDriverException.class);
            wait.until((ExpectedConditions.elementToBeClickable(By.xpath(xpath))));
            flag = true;
        } catch (WebDriverException e) {
            log.debug(e.toString(), e);
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean isElementOperability(String text) {
        return findElement(text).isEnabled() && findElement(text).isDisplayed();
    }

    @Override
    public boolean isElementOperability(String text, int secondTimeout) {
        return findElement(text, secondTimeOut).isEnabled() && findElement(text, secondTimeOut).isDisplayed();
    }

    @Override
    public void waitElementOperability(String text) {
        for (int i = 0; i < secondTimeOut * 1000 / interval; i++) {
            if (isElementOperability(text)) {
                break;
            }
        }
    }

    @Override
    public String getTagAllAttributes(String text) {
        Object attr = js.executeScript("var items = {}; " +
                "for (index = 0; index < arguments[0].attributes.length; ++index) " +
                "{ items[arguments[0].attributes[index].name] = arguments[0].attributes[index].value }; " +
                "return items;", driver.findElement(By.xpath(XpathBuilder.xpathGenerator(text))));
        return attr.toString();
    }

    @Override
    public boolean isElementEnabled(String text) {
        return !getTagAllAttributes(text).contains("disable");
    }

    @Override
    public String getElementRGBAColor(String text) {
        return findElement(text).getCssValue("color");
    }

    @Override
    public String getElementHexColor(String text) {
        return Color.fromString(findElement(text).getCssValue("color")).asHex();

    }

    @Override
    public String getElementColorName(String text, String cssColor) {
        java.awt.Color colorObj = Color.fromString(findElementContains(text).getCssValue(cssColor)).getColor();
        return ColorUtils.getColorNameFromColor(colorObj);
    }

    @Override
    public Boolean isElementHidden(WebElement element) {
        return (Boolean) js.executeScript("return arguments[0].hidden;", element);
    }

    @Override
    public Boolean isElementHidden(String text) {
        return (Boolean) js.executeScript("return arguments[0].hidden;", findElement(text));
    }

    private void highlightElement(WebElement element) {
        try {
            js.executeScript("arguments[0].style.border='2.5px solid red'", element);
        } catch (Exception ignore) {
        }
    }

    private void highlightElement(List<WebElement> elements) {
        for (WebElement element : elements) {
            try {
                js.executeScript("arguments[0].style.border='2.5px solid red'", element);
            } catch (Exception ignore) {
            }
        }
    }

    private void unHighlightElement(WebElement element) {
        js.executeScript("arguments[0].removeAttribute('style','')", element);
    }

    private String getElementXPath(WebElement element) {
        return (String) js.executeScript(
                "getXPath=function(node)" + "{" + "if (node.id !== '')" + "{" +
                        "return '//' + node.tagName.toLowerCase() + '[@id=\"' + node.id + '\"]'" + "}" +
                        "if (node === document.body)" + "{" + "return node.tagName.toLowerCase()" + "}" +
                        "var nodeCount = 0;" + "var childNodes = node.parentNode.childNodes;" +
                        "for (var i=0; i<childNodes.length; i++)" + "{" + "var currentNode = childNodes[i];" +
                        "if (currentNode === node)" + "{" +
                        "return getXPath(node.parentNode) + '/' + node.tagName.toLowerCase() + '[' + (nodeCount+1) + ']'" + "}" +
                        "if (currentNode.nodeType === 1 && " +
                        "currentNode.tagName.toLowerCase() === node.tagName.toLowerCase())" + "{" + "nodeCount++" + "}" + "}" + "};" +
                        "return getXPath(arguments[0]);", element);
    }

    @Override
    public String text(@NonNull String text) {
        return "text->" + text.replace("\"", "");
    }

    @Override
    public void clickTextWithLastIndex(@NonNull String text) {
        List<WebElement> elements = findElements(text(text));
        click(text(text), String.valueOf(elements.size()));
    }

    @Override
    public void clickText(@NonNull String text) {
        click(text(text));
    }

    @Override
    public void clickTextWithIndex(@NonNull String text, @NonNull String index) {
        click(text(text), index);
    }

    @Override
    public void clickPositionByBesidesText(int pixel, Direction direction, String xpath) {
        switch (direction) {
            case UP:
                clickXYBesidesText(pixel, Direction.UP, xpath);
                break;
            case DOWN:
                clickXYBesidesText(pixel, Direction.DOWN, xpath);
                break;
            case RIGHT:
                clickXYBesidesText(pixel, Direction.RIGHT, xpath);
                break;
            case LEFT:
                clickXYBesidesText(pixel, Direction.LEFT, xpath);
                break;
            default:
                throw new IllegalArgumentException("unsupported direction type: " + direction);
        }
    }

    private void toInitialPoint() {
        Dimension location = driver.findElement(By.tagName("body")).getSize();
        int width = location.width;
        int height = location.height;
        actions.moveToElement(driver.findElement(By.tagName("body")), -width / 2, -height / 2).build().perform();
    }

    private void executeClick(String value) {
        log.info("clicking element: {}", value);
        if (isElementClickable(value)) {
            try {
                List<WebElement> elements = driver.findElements(By.xpath(value));
                highlightElement(elements);
                if (elements.size() == 1) {
                    elements.get(0).click();
                } else {
                    log.warn("the number of the element: {} are: {}", value, elements.size());
                    for (WebElement i : elements) {
                        if (isElementClickable(i, 1000)) {
                            i.click();
                        } else {
                            break;
                        }
                    }
                }
            } catch (WebDriverException e) {
                log.error(e.toString(), e);
                log.error("=====================================================================");
                log.error("xpath is: {}", getElementXPath(driver.findElements(By.xpath(value)).get(0)));
                log.error("=====================================================================");
                if (e instanceof StaleElementReferenceException || e instanceof ElementNotInteractableException) {
                    highlightElement(driver.findElement(By.xpath(value)));
                    jsClick(value);
                }
            }
        } else {
            List<WebElement> elements = new ArrayList<>(driver.findElements(By.xpath(value)));
            if (elements.size() == 1) {
                try {
                    elements.get(0).click();
                } catch (WebDriverException e) {
                    if (e instanceof StaleElementReferenceException || e instanceof ElementNotInteractableException) {
                        highlightElement(driver.findElement(By.xpath(value)));
                        jsClick(value);
                    } else {
                        throw new WebDriverException("the element: (" + value + ") is not clickable");
                    }
                }
            } else if (elements.size() > 1) {
                elements.remove(0);
                for (WebElement i : elements) {
                    try {
                        i.click();
                    } catch (WebDriverException e) {
                        if (e instanceof StaleElementReferenceException || e instanceof ElementNotInteractableException) {
                            highlightElement(driver.findElement(By.xpath(value)));
                            jsClick(value);
                        }
                    }
                }
            }
        }
    }

    private void executeClick(String value, int secondTimeout) {
        log.info("clicking element: {} with time out: {}", value, secondTimeout);
        if (isElementClickable(value, secondTimeout)) {
            try {
                highlightElement(driver.findElement(By.xpath(value)));
                driver.findElement(By.xpath(value)).click();
            } catch (WebDriverException e) {
                if (e instanceof ElementClickInterceptedException || e instanceof StaleElementReferenceException) {
                    highlightElement(driver.findElement(By.xpath(value)));
                    js.executeScript("arguments[0].click();", driver.findElement(By.xpath(value)));
                }
            }
        } else {
            try {
                driver.findElement(By.xpath(value)).click();
            } catch (WebDriverException e) {
                if (e instanceof ElementClickInterceptedException || e instanceof StaleElementReferenceException) {
                    highlightElement(driver.findElement(By.xpath(value)));
                    js.executeScript("arguments[0].click();", driver.findElement(By.xpath(value)));
                } else {
                    throw new WebDriverException("the element: (" + value + ") is not clickable");
                }
            }
        }
    }


    private WebElement executeFindElement(String value) {
        for (int i = 0; i < secondTimeOut * 1000 / interval; i++) {
            try {
                WebElement element = driver.findElement(By.xpath(value));
                highlightElement(element);
                break;
            } catch (WebDriverException e) {
                Tools.sleep(interval);
            }
        }
        return driver.findElement(By.xpath(value));
    }

    private WebElement executeFindElement(String value, int secondTimeOut) {
        for (int i = 0; i < secondTimeOut * 1000 / interval; i++) {
            try {
                WebElement element = driver.findElement(By.xpath(value));
                highlightElement(element);
                break;
            } catch (WebDriverException e) {
                Tools.sleep(interval);
            }
        }
        return driver.findElement(By.xpath(value));
    }

    private List<WebElement> executeFindElements(String value) {
        for (int i = 0; i < secondTimeOut * 1000 / interval; i++) {
            List<WebElement> elements = driver.findElements(By.xpath(value));
            highlightElement(elements);
            if (!elements.isEmpty()) {
                break;
            } else {
                Tools.sleep(interval);
            }
        }
        return driver.findElements(By.xpath(value));
    }

    private List<WebElement> executeFindElements(String value, int secondTimeout) {
        for (int i = 0; i < secondTimeout * 1000 / interval; i++) {
            List<WebElement> elements = driver.findElements(By.xpath(value));
            highlightElement(elements);
            if (!elements.isEmpty()) {
                break;
            } else {
                Tools.sleep(interval);
            }
        }
        return driver.findElements(By.xpath(value));
    }

    private Actions action(WebElement element) {
        nextXPoint = element.getLocation().getX() - currentXPoint;
        nextYpoint = element.getLocation().getY() - currentYPoint;
        currentXPoint = currentXPoint + nextXPoint;
        currentYPoint = currentYPoint + nextYpoint;
        return actions;
    }

    private Actions action(int X, int Y) {
        nextXPoint = X - currentXPoint;
        nextYpoint = Y - currentYPoint;
        currentXPoint = currentXPoint + nextXPoint;
        currentYPoint = currentYPoint + nextYpoint;
        return actions;
    }


}
