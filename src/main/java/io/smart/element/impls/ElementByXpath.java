package io.smart.element.impls;

import io.smart.driver.DriverOperation;
import io.smart.element.PortalOperator;
import io.smart.enums.Direction;
import io.smart.enums.Position;
import io.smart.function.Operator;
import io.smart.utils.tools.ColorUtils;
import io.smart.utils.tools.Tools;
import io.smart.utils.xpath.XpathBuilder;
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


    public void click(String text) {
        String value = XpathBuilder.xpathGenerator(text);
        executeClick(value);
    }


    public void click(String text, Operator before, Operator after) {
        String value = XpathBuilder.xpathGenerator(text);
        executeClick(value, before, after);
    }


    public void click(String text, String index) {
        String value = XpathBuilder.xpathGenerator(text, index);
        executeClick(value);
    }


    public void click(String text, int secondTimeout) {
        String value = XpathBuilder.xpathGenerator(text);
        executeClick(value, secondTimeout);
    }


    public void click(WebElement element) {
        log.info("clicking element: {}", element);
        try {
            element.click();
        } catch (WebDriverException e) {
            log.warn(e.toString(), e);
            if (e instanceof ElementNotInteractableException) {
                jsClick(element);
            }
        }
    }


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


    public void click(int width, int height) {
        log.info("clicking coordinate: ({}, {})", width, height);
        action(width, height).moveByOffset(nextXPoint, nextYpoint).click().build().perform();
    }

    public void click(int width, int height, String description) {
        log.info("clicking element: \"{}\" by coordinate: ({}, {})", description, width, height);
        action(width, height).moveByOffset(nextXPoint, nextYpoint).click().build().perform();
    }


    public void click(int width, int height, Operator before, Operator after) {
        log.info("clicking coordinate: ({}, {})", width, height);
        before.apply();
        click(width, height);
        after.apply();
    }

    public void rightClick() {
        log.info("right click current point");
        actions.contextClick().build().perform();
    }

    public void rightClick(int width, int height) {
        log.info("right click coordinate: ({}, {})", width, height);
        action(width, height).moveByOffset(nextXPoint, nextYpoint).contextClick().build().perform();
    }

    public void rightClick(int width, int height, String description) {
        log.info("right click element: \"{}\" by coordinate: ({}, {})", description, width, height);
        action(width, height).moveByOffset(nextXPoint, nextYpoint).contextClick().build().perform();
    }

    public void rightClick(String text) {
        String value = XpathBuilder.xpathGenerator(text);
        Point location = findElement(value).getLocation();
        log.info("right click element: {}", value);
        action(location.getX(), location.getY()).moveToElement(findElement(value)).contextClick().build().perform();
    }

    public void rightClick(WebElement element) {
        int xPoint = element.getLocation().getX();
        int yPoint = element.getLocation().getY();
        log.info("right click coordinate: ({}, {})", xPoint, yPoint);
        action(xPoint, yPoint).moveToElement(element).contextClick().build().perform();
    }

    public void clickContain(String text) {
        String value = XpathBuilder.xpathContainsGenerator(text);
        executeClick(value);
    }


    public void clickContain(String text, int secondTimeout) {
        String value = XpathBuilder.xpathContainsGenerator(text);
        executeClick(value, secondTimeout);
    }


    public void jsClick(String xpath) {
        log.info("clicking element: {} by js", xpath);
        js.executeScript("arguments[0].click();", driver.findElement(By.xpath(XpathBuilder.xpathGenerator(xpath))));
    }


    public void jsClick(WebElement element) {
        log.info("clicking element: {} by js", getElementXPath(element));
        js.executeScript("arguments[0].click();", element);
    }

    public void forceClick(String text) {
        String value = XpathBuilder.xpathGenerator(text);
        log.info("clicking element: {}", value);
        Point location = findElement(value).getLocation();
        click(location.getX(), location.getY());
    }

    public void forceClick(WebElement element) {
        log.info("clicking element: {}", element);
        Point location = element.getLocation();
        click(location.getX(), location.getY());
    }

    public String getText(String text) {
        log.info("getting text by xpath: {}", text);
        isElementExist(text);
        String value = XpathBuilder.xpathGenerator(text);
        return driver.findElement(By.xpath(value)).getText();
    }


    public String getText(String text, int secondTimeout) {
        log.info("getting text by xpath: {}", text);
        isElementExist(text, secondTimeout);
        String value = XpathBuilder.xpathGenerator(text);
        return driver.findElement(By.xpath(value)).getText();
    }


    public String getText(WebElement element) {
        return element.getText();
    }


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

    public List<String> getTexts(List<WebElement> elements) {
        List<String> texts = new LinkedList<>();
        elements.forEach(i -> {
            try {
                texts.add(i.getText());
            } catch (WebDriverException e) {
                log.debug(e.toString(), e);
            }
        });
        return texts;
    }


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


    public void input(String xpath, String text) {
        log.info("inputing: {}", text);
        String value = XpathBuilder.xpathGenerator(xpath);
        for (char i : text.toCharArray()) {
            findElement(value).sendKeys(String.valueOf(i));
        }
    }


    public void input(WebElement element, String text) {
        log.info("inputing: {}", text);
        for (char i : text.toCharArray()) {
            element.sendKeys(String.valueOf(i));
        }
    }


    public void input(String xpath, Keys keys) {
        log.info("pressing keyboard: {}", keys.toString());
        String value = XpathBuilder.xpathGenerator(xpath);
        findElement(value).sendKeys(keys);
    }


    public void uploadFile(String xpath, File file) {
        log.info("uploading file: {}", file.getAbsolutePath());
        String value = XpathBuilder.xpathGenerator(xpath);
        findElement(value).sendKeys(file.getAbsolutePath());
    }


    public void clearInputBox(String text) {
        findElement(text).sendKeys(Keys.CONTROL + "a");
        findElement(text).sendKeys(Keys.BACK_SPACE);
        findElement(text).sendKeys(Keys.ENTER);
        findElement(text).clear();
    }


    public void clearInputBox(WebElement element) {
        element.sendKeys(Keys.CONTROL + "a");
        element.sendKeys(Keys.BACK_SPACE);
        element.sendKeys(Keys.ENTER);
        element.clear();
    }


    public void pressKey(String text, Keys keys) {
        log.info("pressing keyboard: " + keys.getCodePoint());
        action(findElement(text)).moveByOffset(nextXPoint, nextYpoint).sendKeys(keys).build().perform();
    }


    public WebElement findElement(String text) {
        String value = XpathBuilder.xpathGenerator(text);
        return executeFindElement(value);
    }


    public WebElement findElement(String text, String index) {
        String value = XpathBuilder.xpathGenerator(text, index);
        return executeFindElement(value);
    }


    public WebElement findElement(String text, int secondTimeout) {
        String value = XpathBuilder.xpathGenerator(text);
        return executeFindElement(value, secondTimeout);
    }


    public WebElement findElement(String text, String index, int secondTimeout) {
        String value = XpathBuilder.xpathGenerator(text, index);
        return executeFindElement(value, secondTimeout);
    }


    public WebElement findElementContains(String text, int secondTimeout) {
        String value = XpathBuilder.xpathContainsGenerator(text);
        return executeFindElement(value, secondTimeout);
    }


    public WebElement findElementContains(String text) {
        String value = XpathBuilder.xpathContainsGenerator(text);
        return executeFindElement(value);
    }


    public List<WebElement> findElements(String text) {
        String value = XpathBuilder.xpathGenerator(text);
        return executeFindElements(value);
    }


    public List<WebElement> findElements(String text, String index, int secondTimeout) {
        String value = XpathBuilder.xpathGenerator(text, index);
        return executeFindElements(value, secondTimeout);
    }


    public List<WebElement> findElements(String text, int secondTimeout) {
        String value = XpathBuilder.xpathGenerator(text);
        return executeFindElements(value, secondTimeout);
    }


    public void waitElementDisappear(String element) {
        String value = XpathBuilder.xpathGenerator(element);
        boolean ele;
        for (int i = 1; i <= secondTimeout * 1000 / interval; i++) {
            ele = isElementClickable(value, 1);
            if (!ele) {
                break;
            }
            Tools.sleep(interval);
        }
    }


    public Object getElementCssValue(String text, String attribute) {
        return findElement(text).getCssValue(attribute);
    }


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


    public List<WebElement> findClickableElements(String xpath) {
        List<WebElement> clickableElements = new LinkedList<>();
        List<WebElement> elements = findElements(xpath);
        for (WebElement i : elements) {
            if (isElementClickable(i, 30)) {
                clickableElements.add(i);
            }
        }
        return clickableElements;
    }

    public void dragElementTo(String source, String target) {
        WebElement elementSource = findElement(source);
        WebElement elementTarget = findElement(target);
        log.info("dragging element: {} to element: {}", source, target);
        action(elementSource).dragAndDrop(elementSource, elementTarget).build().perform();
        log.info("drag done");
    }

    public void dragElementTo(String source, int targetX, int targetY) {
        WebElement elementSource = findElement(source);
        log.info("dragging element: {} to : ({},{})", source, targetX, targetY);
        action(elementSource).dragAndDropBy(elementSource, targetX, targetY).build().perform();
        log.info("drag done");
    }

    public void dragElementTo(WebElement source, WebElement target) {
        log.info("dragging element: {} to element: {}", source, target);
        action(source).dragAndDrop(source, target).build().perform();
        log.info("drag done");
    }

    public void dragElementTo(WebElement source, int targetX, int targetY) {
        log.info("dragging element: {} to : ({},{})", source, targetX, targetY);
        action(source).dragAndDropBy(source, targetX, targetY).build().perform();
        log.info("drag done");
    }

    public void hoverAnElement(WebElement element) {
        log.info("hovering element: {} by ({},{})", getElementXPath(element), nextXPoint, nextYpoint);
        action(element).moveToElement(element).build().perform();
    }

    public void hoverAnElement(String element) {
        log.info("hovering text: " + element);
        List<WebElement> elements = findElements(element);
        if (elements.size() == 1) {
            action(elements.get(0)).moveToElement(elements.get(0)).build().perform();
        } else {
            for (WebElement i : elements) {
                try {
                    action(i).moveToElement(i).build().perform();
                } catch (WebDriverException ignore) {
                    Tools.sleep(2000);
                }
            }
        }
    }


    public void hoverAnElement(String element, String index) {
        log.info("hovering text: {}", element);
        isElementClickable(XpathBuilder.xpathGenerator(element, index));
        WebElement e = findElement(element, index);
        action(e).moveToElement(e).build().perform();
    }


    public void hoverAnElement(int x, int y) {
        log.info("hovering to coordinate to: {}, {}", x, y);
        action(x, y).moveByOffset(x, y).build().perform();
    }

    public void hoverAnElement(Position position) {
        log.info("hovering to: {}", position);
        Dimension location = driver.findElement(By.tagName("body")).getSize();
        int width = location.width;
        int height = location.height;
        switch (position) {
            case LETF_TOP:
                action(0, 0).moveByOffset(nextXPoint, nextYpoint).build().perform();
                break;
            case LEFT_BOTTOM:
                action(0, height).moveByOffset(nextXPoint, nextYpoint).build().perform();
                break;
            case RIGHT_TOP:
                action(width, 0).moveByOffset(nextXPoint, nextYpoint).build().perform();
                break;
            case RIGHT_BOTTOM:
                action(width, height).moveByOffset(nextXPoint, nextYpoint).build().perform();
                break;
            case LEFT_CENTER:
                action(0, height / 2).moveByOffset(nextXPoint, nextYpoint).build().perform();
                break;
            case RIGHT_CENTER:
                action(width, height / 2).moveByOffset(nextXPoint, nextYpoint).build().perform();
                break;
            case TOP_CENTER:
                action(width / 2, 0).moveByOffset(nextXPoint, nextYpoint).build().perform();
                break;
            case BUTTON_CENTER:
                action(width / 2, height).moveByOffset(nextXPoint, nextYpoint).build().perform();
                break;
            case CENTER:
                action(width / 2, height / 2).moveByOffset(nextXPoint, nextYpoint).build().perform();
                break;
        }
    }


    public void scrollToTop() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollTo(0,0)");
    }


    public void scrollToBottom() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollTo(0,document.body.scrollHeight)");
    }


    public void scrollToLeft() {
        js.executeScript("window.scroll(-500000, 0)");
    }


    public void scrollToRight() {
        js.executeScript("window.scroll(500000, 0)");
    }


    public void scrollToElement(String text) {
        js.executeScript("arguments[0].scrollIntoView(true);", findElement(text));
    }


    public boolean isElementExist(String text) {
        Duration duration = driver.manage().timeouts().getImplicitWaitTimeout();
        String value = XpathBuilder.xpathGenerator(text);
        log.info("waiting for element: \"{}\" to appear within {} seconds", value, secondTimeout);
        boolean wasInterrupted = false;
        final long finishAtMillis = System.currentTimeMillis() + (secondTimeout * 1000L);
        try {
            while (!elementLoaded(value)) {
                final long remainingMillis = finishAtMillis - System.currentTimeMillis();
                if (remainingMillis < 0) {
                    return false;
                }
                try {
                    Thread.sleep(Math.min(interval, remainingMillis));
                } catch (final InterruptedException ignore) {
                    wasInterrupted = true;
                } catch (final Exception ex) {
                    break;
                }
            }
        } finally {
            driver.manage().timeouts().implicitlyWait(duration);
            if (wasInterrupted) {
                Thread.currentThread().interrupt();
            }
        }
        return true;
    }


    public boolean isElementExist(String text, int secondTimeout) {
        Duration duration = driver.manage().timeouts().getImplicitWaitTimeout();
        String value = XpathBuilder.xpathGenerator(text);
        log.info("waiting for element: \"{}\" to appear within {} seconds", value, secondTimeout);
        boolean wasInterrupted = false;
        final long finishAtMillis = System.currentTimeMillis() + (secondTimeout * 1000L);
        try {
            while (!elementLoaded(value)) {
                final long remainingMillis = finishAtMillis - System.currentTimeMillis();
                if (remainingMillis < 0) {
                    return false;
                }
                try {
                    Thread.sleep(Math.min(interval, remainingMillis));
                } catch (final InterruptedException ignore) {
                    wasInterrupted = true;
                } catch (final Exception ex) {
                    break;
                }
            }
        } finally {
            driver.manage().timeouts().implicitlyWait(duration);
            if (wasInterrupted) {
                Thread.currentThread().interrupt();
            }
        }
        return true;
    }

    public boolean isElementNotExist(String text) {
        Duration duration = driver.manage().timeouts().getImplicitWaitTimeout();
        String value = XpathBuilder.xpathGenerator(text);
        log.info("waiting for element: \"{}\" to disappear within {} seconds", value, secondTimeout);
        boolean wasInterrupted = false;
        final long finishAtMillis = System.currentTimeMillis() + (secondTimeout * 1000L);
        try {
            while (elementLoaded(value)) {
                final long remainingMillis = finishAtMillis - System.currentTimeMillis();
                if (remainingMillis < 0) {
                    return false;
                }
                try {
                    Thread.sleep(Math.min(interval, remainingMillis));
                } catch (final InterruptedException ignore) {
                    wasInterrupted = true;
                } catch (final Exception ex) {
                    break;
                }
            }
        } finally {
            driver.manage().timeouts().implicitlyWait(duration);
            if (wasInterrupted) {
                Thread.currentThread().interrupt();
            }
        }
        return true;
    }


    public boolean isElementNotExist(String text, int secondTimeout) {
        Duration duration = driver.manage().timeouts().getImplicitWaitTimeout();
        String value = XpathBuilder.xpathGenerator(text);
        log.info("waiting for element: \"{}\" to disappear within {} seconds", value, secondTimeout);
        boolean wasInterrupted = false;
        final long finishAtMillis = System.currentTimeMillis() + (secondTimeout * 1000L);
        try {
            while (elementLoaded(value)) {
                final long remainingMillis = finishAtMillis - System.currentTimeMillis();
                if (remainingMillis < 0) {
                    return false;
                }
                try {
                    Thread.sleep(Math.min(interval, remainingMillis));
                } catch (final InterruptedException ignore) {
                    wasInterrupted = true;
                } catch (final Exception ex) {
                    break;
                }
            }
        } finally {
            driver.manage().timeouts().implicitlyWait(duration);
            if (wasInterrupted) {
                Thread.currentThread().interrupt();
            }
        }
        return true;
    }


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


    public boolean isElementClickable(By by) {
        boolean flag;
        try {
            FluentWait<WebDriver> wait = new FluentWait<>(driver)
                    .withTimeout(Duration.ofSeconds(secondTimeout))
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


    public boolean isElementClickable(WebElement element) {
        boolean flag;
        try {
            FluentWait<WebDriver> wait = new FluentWait<>(driver)
                    .withTimeout(Duration.ofSeconds(secondTimeout))
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


    public boolean isElementClickable(String xpath) {
        boolean flag;
        try {
            FluentWait<WebDriver> wait = new FluentWait<>(driver)
                    .withTimeout(Duration.ofSeconds(secondTimeout))
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


    public boolean isElementOperability(String text) {
        return findElement(text).isEnabled() && findElement(text).isDisplayed();
    }


    public boolean isElementOperability(String text, int secondTimeout) {
        return findElement(text, DriverOperation.secondTimeout).isEnabled() && findElement(text, DriverOperation.secondTimeout).isDisplayed();
    }


    public void waitElementOperability(String text) {
        for (int i = 0; i < secondTimeout * 1000 / interval; i++) {
            if (isElementOperability(text)) {
                break;
            }
        }
    }


    public String getTagAllAttributes(String text) {
        Object attr = js.executeScript("var items = {}; " +
                "for (index = 0; index < arguments[0].attributes.length; ++index) " +
                "{ items[arguments[0].attributes[index].name] = arguments[0].attributes[index].value }; " +
                "return items;", driver.findElement(By.xpath(XpathBuilder.xpathGenerator(text))));
        return attr.toString();
    }


    public boolean isElementEnabled(String text) {
        return !getTagAllAttributes(text).contains("disable");
    }


    public String getElementRGBAColor(String text) {
        return findElement(text).getCssValue("color");
    }


    public String getElementHexColor(String text) {
        return Color.fromString(findElement(text).getCssValue("color")).asHex();

    }


    public String getElementColorName(String text, String cssColor) {
        java.awt.Color colorObj = Color.fromString(findElementContains(text).getCssValue(cssColor)).getColor();
        return ColorUtils.getColorNameFromColor(colorObj);
    }


    public Boolean isElementHidden(WebElement element) {
        return (Boolean) js.executeScript("return arguments[0].hidden;", element);
    }


    public Boolean isElementHidden(String text) {
        return (Boolean) js.executeScript("return arguments[0].hidden;", findElement(text));
    }


    public String text(String text) {
        return "text->" + text.replace("\"", "");
    }


    public void clickTextWithLastIndex(String text) {
        List<WebElement> elements = findElements(text(text));
        click(text(text), String.valueOf(elements.size()));
    }


    public void clickText(String text) {
        click(text(text));
    }


    public void clickTextWithIndex(String text, String index) {
        click(text(text), index);
    }


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


    public int countElements(String value) {
        String xpath = XpathBuilder.xpathGenerator(value);
        if (isElementExist(value)) {
            List<WebElement> elements = driver.findElements(By.xpath(xpath));
            return elements.size();
        } else {
            throw new NoSuchContextException("unable to find the element: " + xpath);
        }
    }


    public int countElements(String value, int seconds) {
        String xpath = XpathBuilder.xpathGenerator(value);
        if (isElementExist(value, seconds)) {
            List<WebElement> elements = driver.findElements(By.xpath(xpath));
            return elements.size();
        } else {
            throw new NoSuchContextException("unable to find the element: " + xpath);
        }
    }

    public WebElement getTableLastElement(String rowName) {
        return new Table().getLastColByRowText(rowName);
    }

    public WebElement getTableLastElementWithSuffix(String rowName, String suffix) {
        return new Table().getLastColByRowText(rowName, suffix);
    }

    public WebElement getTableElementByindex(String rowName, String index) {
        return new Table().getLastColByRowTextWithIndex(rowName, index);
    }

    public WebElement getTableElementByRowColum(String rowName, String columName) {
        return new Table().getByRowColum(rowName, columName);
    }

    public WebElement getTableElementByRowColum(String rowName, String columName, String targetElementSuffix) {
        return new Table().getByRowColum(rowName, columName, targetElementSuffix);
    }

    public List<WebElement> getElementsByColumnName(String columnName) {
        return new Table().getByColum(columnName);
    }

    public List<WebElement> getElementsByColumnName(String columnName, int offset) {
        return new Table().getByColum(columnName, String.valueOf(offset));
    }

    class Table {
        String lastXpathModle = "//*[text()=\"%s\"]/ancestor::tr/td[%s]";
        String rowColumModel = "//*[text()=\"%s\"]/ancestor::tr/td[count(//table//thead//*[text()=\"%s\"]/ancestor::th//preceding-sibling::*)]";
        String specificColumText = "//table//td[count((//table//*[text()=\"%s\"])[last()]/ancestor-or-self::th/preceding-sibling::*) +%s]";

        WebElement getLastColByRowText(String text) {
            return findElement(String.format(lastXpathModle, text, "last()"));
        }

        WebElement getLastColByRowText(String text, String targetTextSuffix) {
            return findElement(String.format(lastXpathModle + targetTextSuffix, text, "last()"));
        }

        WebElement getLastColByRowTextWithIndex(String text, String index) {
            return findElement(String.format(lastXpathModle, text, index));
        }

        WebElement getByRowColum(String row, String column) {
            return findElement(String.format(rowColumModel, row, column));
        }

        WebElement getByRowColum(String row, String column, String targetElementSuffix) {
            return findElement(String.format(rowColumModel + targetElementSuffix, row, column));
        }

        List<WebElement> getByColum(String column) {
            return findElements(String.format(specificColumText, column, 1));
        }

        List<WebElement> getByColum(String column, String index) {
            return findElements(String.format(specificColumText, column, index));
        }
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

    private boolean elementLoaded(String text) {
        try {
            WebElement element = driver.findElement(By.xpath(text));
            highlightElement(element);
            return true;
        } catch (WebDriverException e) {
            return false;
        }
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

    private void executeClick(String value, Operator before, Operator after) {
        log.info("clicking element: {}", value);
        if (isElementClickable(value)) {
            try {
                List<WebElement> elements = driver.findElements(By.xpath(value));
                highlightElement(elements);
                if (elements.size() == 1) {
                    before.apply();
                    elements.get(0).click();
                    after.apply();
                } else {
                    log.warn("the number of the element: {} are: {}", value, elements.size());
                    for (WebElement i : elements) {
                        if (isElementClickable(i, secondTimeout)) {
                            before.apply();
                            i.click();
                            after.apply();
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
                    before.apply();
                    jsClick(value);
                    after.apply();
                }
            }
        } else {
            List<WebElement> elements = new ArrayList<>(driver.findElements(By.xpath(value)));
            if (elements.size() == 1) {
                try {
                    before.apply();
                    elements.get(0).click();
                    after.apply();
                } catch (WebDriverException e) {
                    if (e instanceof StaleElementReferenceException || e instanceof ElementNotInteractableException) {
                        highlightElement(driver.findElement(By.xpath(value)));
                        before.apply();
                        jsClick(value);
                        after.apply();
                    } else {
                        throw new WebDriverException("the element: (" + value + ") is not clickable");
                    }
                }
            } else if (elements.size() > 1) {
                elements.remove(0);
                for (WebElement i : elements) {
                    try {
                        before.apply();
                        i.click();
                        after.apply();
                    } catch (WebDriverException e) {
                        if (e instanceof StaleElementReferenceException || e instanceof ElementNotInteractableException) {
                            highlightElement(driver.findElement(By.xpath(value)));
                            before.apply();
                            jsClick(value);
                            after.apply();
                        }
                    }
                }
            }
        }
    }

    private WebElement executeFindElement(String value) {
        for (int i = 0; i < secondTimeout * 1000 / interval; i++) {
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
        if ((currentXPoint + currentYPoint) == 0) {
            nextXPoint = X;
            nextYpoint = Y;
        } else {
            nextXPoint = X - currentXPoint;
            nextYpoint = Y - currentYPoint;
        }
        currentXPoint = currentXPoint + nextXPoint;
        currentYPoint = currentYPoint + nextYpoint;
        return actions;
    }


}
