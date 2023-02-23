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
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        log.info("clicking element: \"{}\"", getFindBy(element));
        try {
            element.click();
        } catch (WebDriverException e) {
            if (e instanceof ElementNotInteractableException) {
                jsClick(element);
            }
        }
    }

    public void click(By by) {
        if (isElementClickableOfMillis(by, secondTimeout * 1000)) {
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

    public void click(int xPoint, int yPoint) {
        log.info("clicking coordinate: ({}, {})", xPoint, yPoint);
        action(xPoint, yPoint).moveByOffset(nextXPoint, nextYpoint).click().build().perform();
    }

    public void click(int xPoint, int yPoint, String description) {
        log.info("clicking element: \"{}\" by coordinate: ({}, {})", description, xPoint, yPoint);
        action(xPoint, yPoint).moveByOffset(nextXPoint, nextYpoint).click().build().perform();
    }

    public void click(int xPoint, int yPoint, Operator before, Operator after) {
        log.info("clicking coordinate: ({}, {})", xPoint, yPoint);
        before.apply();
        click(xPoint, yPoint);
        after.apply();
    }

    public void rightClick() {
        log.info("right click current point");
        actions.contextClick().build().perform();
    }

    public void rightClick(int xPoint, int yPoint) {
        log.info("right click coordinate: ({}, {})", xPoint, yPoint);
        action(xPoint, yPoint).moveByOffset(nextXPoint, nextYpoint).contextClick().build().perform();
    }

    public void rightClick(int xPoint, int yPoint, String description) {
        log.info("right click element: \"{}\" by coordinate: ({}, {})", description, xPoint, yPoint);
        action(xPoint, yPoint).moveByOffset(nextXPoint, nextYpoint).moveByOffset(0, -9).contextClick().build().perform();
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
        log.info("clicking element: \"{}\" by javascript", xpath);
        js.executeScript("arguments[0].click();", driver.findElement(By.xpath(XpathBuilder.xpathGenerator(xpath))));
    }

    public void jsClick(WebElement element) {
        log.warn("clicking element\"{}\" by javascript", getFindBy(element));
        js.executeScript("arguments[0].click();", element);
    }

    public void forceClick(String text) {
        String value = XpathBuilder.xpathGenerator(text);
        log.info("clicking element: \"{}\"", value);
        Point location = findElement(value).getLocation();
        click(location.getX(), location.getY());
    }

    public void forceClick(WebElement element) {
        log.info("clicking element: \"{}\"", getFindBy(element));
        Point location = element.getLocation();
        click(location.getX(), location.getY());
    }

    public String getText(String text) {
        log.info("getting text by xpath: \"{}\"", text);
        isElementExist(text);
        String value = XpathBuilder.xpathGenerator(text);
        return driver.findElement(By.xpath(value)).getText();
    }

    public String getText(String text, int secondTimeout) {
        log.info("getting text by xpath: \"{}\"", text);
        isElementExist(text, secondTimeout);
        String value = XpathBuilder.xpathGenerator(text);
        return driver.findElement(By.xpath(value)).getText();
    }

    public String getText(WebElement element) {
        log.info("clicking element: \"{}\"", getFindBy(element));
        return element.getText();
    }

    public List<String> getTexts(String text) {
        List<WebElement> webElements = findElements(text);
        List<String> texts = new LinkedList<>();
        webElements.forEach(i -> {
            try {
                texts.add(i.getText());
            } catch (WebDriverException e) {
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
        log.info("inputing text: \"{}\"", text);
        WebElement element = findElement(xpath);
        if (!isElementClickableOfMillis(element, 1000)) {
            element = findClickableElement(xpath);
        }
        for (char i : text.toCharArray()) {
            element.sendKeys(String.valueOf(i));
        }
    }

    public void input(WebElement element, String text) {
        log.info("inputing text: \"{}\" into element: \"{}\"", text, getFindBy(element));
        for (char i : text.toCharArray()) {
            element.sendKeys(String.valueOf(i));
        }
    }

    public void input(String xpath, Keys keys) {
        log.info("inputing text: \"{}\" into element: \"{}\"", keys.toString(), xpath);
        findElement(xpath).sendKeys(keys);
    }

    public void input(String text, int xPoint, int yPoint) {
        log.info("inputing text: \"{}\" into element coordinate: \"({}, {})\"", text, xPoint, yPoint);
        action(xPoint, yPoint).moveByOffset(nextXPoint, nextYpoint).sendKeys(text).build().perform();
    }

    public void uploadFile(String xpath, File file) {
        log.info("uploading file: \"{}\"", file.getAbsolutePath());
        String value = XpathBuilder.xpathGenerator(xpath);
        findElement(value).sendKeys(file.getAbsolutePath());
    }

    public void clearInputBox(String text) {
        log.info("clear element: \"{}\" input box", XpathBuilder.xpathGenerator(text));
        WebElement clickableElement = findClickableElement(text);
        clickableElement.sendKeys(Keys.CONTROL + "a");
        clickableElement.sendKeys(Keys.BACK_SPACE);
        clickableElement.clear();
    }

    public void clearInputBox(int xPoint, int yPoint) {
        log.info("clear input box by coordinate : \"({}, {})\" ", xPoint, yPoint);
        action(xPoint, yPoint).moveByOffset(nextXPoint, nextYpoint).doubleClick().sendKeys("").build().perform();
    }

    public void clearInputBox(WebElement element) {
        log.info("clear element: \"{}\" input box", getFindBy(element));
        element.sendKeys(Keys.CONTROL + "a");
        element.sendKeys(Keys.BACK_SPACE);
        element.clear();
    }

    public void pressKey(String text, Keys keys) {
        log.info("pressing keyboard: " + keys.getCodePoint());
        action(findElement(text)).moveByOffset(nextXPoint, nextYpoint).sendKeys(keys).build().perform();
    }

    public WebElement findElement(String text) {
        String value = XpathBuilder.xpathGenerator(text);
        return executeFindElements(value, secondTimeout)
                .orElseThrow(() -> new NoSuchElementException("unable to find the element: \"" + value + "\" within " + secondTimeout + " seconds"))
                .get(0);
    }

    public WebElement findElement(String text, String index) {
        String value = XpathBuilder.xpathGenerator(text, index);
        return executeFindElements(value, secondTimeout)
                .orElseThrow(() -> new NoSuchElementException("unable to find the element: \"" + value + "\" within " + secondTimeout + " seconds"))
                .get(0);
    }

    public WebElement findElement(String text, int secondTimeout) {
        String value = XpathBuilder.xpathGenerator(text);
        return executeFindElements(value, secondTimeout)
                .orElseThrow(() -> new NoSuchElementException("unable to find the element: \"" + value + "\" within " + secondTimeout + " seconds"))
                .get(0);
    }

    public WebElement findElement(String text, String index, int secondTimeout) {
        String value = XpathBuilder.xpathGenerator(text, index);
        return executeFindElements(value, secondTimeout)
                .orElseThrow(() -> new NoSuchElementException("unable to find the element: \"" + value + "\" within " + secondTimeout + " seconds"))
                .get(0);
    }

    public WebElement findElementContains(String text) {
        String value = XpathBuilder.xpathContainsGenerator(text);
        return executeFindElements(value, secondTimeout)
                .orElseThrow(() -> new NoSuchElementException("unable to find the element: \"" + value + "\" within " + secondTimeout + " seconds"))
                .get(0);
    }

    public WebElement findElementContains(String text, int secondTimeout) {
        String value = XpathBuilder.xpathContainsGenerator(text);
        return executeFindElements(value, secondTimeout)
                .orElseThrow(() -> new NoSuchElementException("unable to find the element: \"" + value + "\" within " + secondTimeout + " seconds"))
                .get(0);
    }

    public WebElement findElementContains(String text, String index, int secondTimeout) {
        String value = XpathBuilder.xpathContainsGenerator(text, index);
        return executeFindElements(value, secondTimeout)
                .orElseThrow(() -> new NoSuchElementException("unable to find the element: \"" + value + "\" within " + secondTimeout + " seconds"))
                .get(0);
    }

    public List<WebElement> findElements(String text) {
        String value = XpathBuilder.xpathGenerator(text);
        return executeFindElements(value, secondTimeout)
                .orElseThrow(() -> new NoSuchElementException("unable to find the element: \"" + value + "\" within " + secondTimeout + " seconds"));
    }

    public List<WebElement> findElements(String text, String index, int secondTimeout) {
        String value = XpathBuilder.xpathGenerator(text, index);
        return executeFindElements(value, secondTimeout)
                .orElseThrow(() -> new NoSuchElementException("unable to find the element: \"" + value + "\" within " + secondTimeout + " seconds"));
    }

    public List<WebElement> findElements(String text, int secondTimeout) {
        String value = XpathBuilder.xpathGenerator(text);
        return executeFindElements(value, secondTimeout)
                .orElseThrow(() -> new NoSuchElementException("unable to find the element: \"" + value + "\" within " + secondTimeout + " seconds"))
                ;
    }

    public Object getElementCssValue(String text, String attribute) {
        return findElement(text).getCssValue(attribute);
    }

    public WebElement findClickableElement(String text) {
        List<WebElement> foundedElements = findClickableElements(text);
        if (foundedElements.size() == 0) {
            throw new WebDriverException("could not find web element by: \"" + XpathBuilder.xpathGenerator(text) + "\"");
        }
        return foundedElements.get(0);
    }

    public List<WebElement> findClickableElements(String text) {
        List<WebElement> foundedElements = findElements(text);
        if (foundedElements.size() == 0) {
            throw new WebDriverException("could not find web element by: \"" + XpathBuilder.xpathGenerator(text) + "\"");
        }
        List<WebElement> clickableElements = foundedElements
                .parallelStream()
                .filter(i -> isElementClickableOfMillis(i, 1000))
                .collect(Collectors.toList());
        if (!clickableElements.isEmpty()) {
            foundedElements.forEach(i -> highlightElement(clickableElements));
        }
        return clickableElements;
    }

    public List<WebElement> findClickableElements(List<WebElement> elements) {
        log.info("searching for clickable elements");
        if (elements.isEmpty()) {
            throw new WebDriverException("elements list is empty");
        }
        List<WebElement> clickableElements = elements
                .parallelStream()
                .filter(i -> isElementClickableOfMillis(i, 1000))
                .collect(Collectors.toList());
        if (!clickableElements.isEmpty()) {
            clickableElements.forEach(i -> highlightElement(clickableElements));
        }
        return clickableElements;
    }

    public Optional<WebElement> waitElementDisplay(String text) {
        return waitElementDisplay(text, secondTimeout);
    }

    public Optional<WebElement> waitElementDisplay(String text, int secondTimeout) {
        String xpath = XpathBuilder.xpathGenerator(text);
        log.info("waiting element: \"{}\" display within {} seconds", xpath, secondTimeout);
        boolean exist = isElementExist(xpath);
        if (!exist) {
            throw new NoSuchElementException("unable to find the element: \"" + xpath + "\"");
        }
        WebElement element = findElement(xpath);
        if (isElementDisplay(element, secondTimeout)) {
            return Optional.of(element);
        }
        return Optional.empty();
    }

    public Optional<WebElement> waitElementDisplay(WebElement element) {
        log.info("waiting element: \"{}\" display within {} seconds", getFindBy(element), secondTimeout);
        FluentWait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(secondTimeout))
                .pollingEvery(Duration.ofMillis(interval))
                .ignoring(WebDriverException.class);
        return Optional.of(wait.until(ExpectedConditions.visibilityOf(element)));
    }

    public Optional<WebElement> waitElementDisplay(WebElement element, int secondTimeout) {
        log.info("waiting element: \"{}\" display within {} seconds", getFindBy(element), secondTimeout);
        FluentWait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(secondTimeout))
                .pollingEvery(Duration.ofMillis(interval))
                .ignoring(WebDriverException.class);
        return Optional.of(wait.until(ExpectedConditions.visibilityOf(element)));
    }


    public boolean waitElementDisappear(String text) {
        return waitElementDisappear(text, secondTimeout);
    }

    public boolean waitElementDisappear(String text, int secondTimeout) {
        String xpath = XpathBuilder.xpathGenerator(text);
        log.info("waiting element: \"{}\" disappear within {} seconds", xpath, secondTimeout);
        try {
            WebElement element = findElement(xpath, 1);
            if (!element.isDisplayed()) {
                return true;
            }
            FluentWait<WebDriver> wait = new FluentWait<>(driver)
                    .withTimeout(Duration.ofSeconds(secondTimeout))
                    .pollingEvery(Duration.ofMillis(interval))
                    .ignoring(WebDriverException.class);
            return wait.until(ExpectedConditions.invisibilityOf(element));
        } catch (WebDriverException e) {
            return true;
        }
    }

    public boolean waitElementDisappear(WebElement element) {
        return waitElementDisappear(element, secondTimeout);
    }

    public boolean waitElementDisappear(WebElement element, int secondTimeout) {
        log.info("waiting element: \"{}\" disappear within {} seconds", getFindBy(element), secondTimeout);
        FluentWait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(secondTimeout))
                .pollingEvery(Duration.ofMillis(interval))
                .ignoring(WebDriverException.class);
        return wait.until(ExpectedConditions.invisibilityOf(element));
    }

    public void dragElementTo(String source, String target) {
        WebElement elementSource = findElement(source);
        WebElement elementTarget = findElement(target);
        log.info("dragging element: \"{}\" to element: \"{}\"", getFindBy(elementSource), getFindBy(elementTarget));
        action(elementSource).dragAndDrop(elementSource, elementTarget).build().perform();
        log.info("drag done");
    }

    public void dragElementTo(String source, int targetX, int targetY) {
        WebElement elementSource = findElement(source);
        log.info("dragging element: \"{}\" to : ({},{})", getFindBy(elementSource), targetX, targetY);
        action(elementSource).dragAndDropBy(elementSource, targetX, targetY).build().perform();
        log.info("drag done");
    }

    public void dragElementTo(WebElement source, WebElement target) {
        log.info("dragging element: \"{}\" to element: \"{}\"", source, target);
        action(source).dragAndDrop(source, target).build().perform();
        log.info("drag done");
    }

    public void dragElementTo(WebElement source, int targetX, int targetY) {
        log.info("dragging element: \"{}\" to : ({},{})", source, targetX, targetY);
        action(source).dragAndDropBy(source, targetX, targetY).build().perform();
        log.info("drag done");
    }

    public void hoverAnElement(WebElement element) {
        log.info("hovering element: \"{}\" by ({},{})", getFindBy(element), nextXPoint, nextYpoint);
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
        log.info("hovering text: \"{}\"", element);
        WebElement e = findElement(element, index);
        action(e).moveToElement(e).build().perform();
    }

    public void hoverAnElement(int x, int y) {
        log.info("hovering to coordinate to: ({}, {})", x, y);
        action(x, y).moveByOffset(x, y).build().perform();
    }

    public void hoverAnElement(Position position) {
        log.info("hovering to: \"{}\"", position);
        Dimension location = driver.findElement(By.tagName("body")).getSize();
        int xPoint = location.getWidth();
        int yPoint = location.getHeight();
        switch (position) {
            case LETF_TOP:
                action(0, 0).moveByOffset(nextXPoint, nextYpoint).build().perform();
                break;
            case LEFT_BOTTOM:
                action(0, yPoint).moveByOffset(nextXPoint, nextYpoint).build().perform();
                break;
            case RIGHT_TOP:
                action(xPoint, 0).moveByOffset(nextXPoint, nextYpoint).build().perform();
                break;
            case RIGHT_BOTTOM:
                action(xPoint, yPoint).moveByOffset(nextXPoint, nextYpoint).build().perform();
                break;
            case LEFT_CENTER:
                action(0, yPoint / 2).moveByOffset(nextXPoint, nextYpoint).build().perform();
                break;
            case RIGHT_CENTER:
                action(xPoint, yPoint / 2).moveByOffset(nextXPoint, nextYpoint).build().perform();
                break;
            case TOP_CENTER:
                action(xPoint / 2, 0).moveByOffset(nextXPoint, nextYpoint).build().perform();
                break;
            case BUTTON_CENTER:
                action(xPoint / 2, yPoint).moveByOffset(nextXPoint, nextYpoint).build().perform();
                break;
            case CENTER:
                action(xPoint / 2, yPoint / 2).moveByOffset(nextXPoint, nextYpoint).build().perform();
                break;
        }
    }

    public void scrollToTop() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollTo(0,0)");
    }

    public void scrollToBottom() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollTo(0,document.body.scrollyPoint)");
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
        return isElementDisplay(text, secondTimeout);
    }

    public boolean isElementExist(String text, int secondTimeout) {
        String xpath = XpathBuilder.xpathGenerator(text);
        log.info("checking presence of the element: \"{}\" within {} seconds", xpath, secondTimeout);
        boolean flag;
        try {
            FluentWait<WebDriver> wait = new FluentWait<>(driver)
                    .withTimeout(Duration.ofSeconds(secondTimeout))
                    .pollingEvery(Duration.ofMillis(interval))
                    .ignoring(WebDriverException.class);
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
            flag = true;
        } catch (WebDriverException e) {
            flag = false;
        }
        return flag;
    }

    public boolean isElementExistContain(String text) {
        return isElementExistContain(text, secondTimeout);
    }

    public boolean isElementExistContain(String text, int secondTimeout) {
        String xpath = XpathBuilder.xpathContainsGenerator(text);
        log.info("checking presence of the element: \"{}\" within {} seconds", xpath, secondTimeout);
        boolean flag;
        try {
            FluentWait<WebDriver> wait = new FluentWait<>(driver)
                    .withTimeout(Duration.ofSeconds(secondTimeout))
                    .pollingEvery(Duration.ofMillis(interval))
                    .ignoring(WebDriverException.class);
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
            flag = true;
        } catch (WebDriverException e) {
            flag = false;
        }
        return flag;
    }

    public Boolean isElementDisplay(String text) {
        String xpath = XpathBuilder.xpathGenerator(text);
        log.info("checking element: \"{}\" is displayed within {} seconds", xpath, secondTimeout);
        return isElementDisplay(xpath, secondTimeout);
    }

    public Boolean isElementDisplay(String text, int secondTimeout) {
        String xpath = XpathBuilder.xpathGenerator(text);
        log.info("checking element: \"{}\" is displayed within {} seconds", xpath, secondTimeout);
        boolean exist = isElementExist(xpath, secondTimeout);
        if (!exist) {
            return false;
        }
        return isElementDisplay(findElement(xpath, 1), secondTimeout);
    }

    public Boolean isElementDisplay(WebElement element) {
        log.info("checking element: \"{}\" is displayed within {} seconds", getFindBy(element), secondTimeout);
        return isElementDisplay(element, secondTimeout);
    }

    public Boolean isElementDisplay(WebElement element, int secondTimeout) {
        log.info("checking element: \"{}\" is displayed within {} seconds", getFindBy(element), secondTimeout);
        boolean flag;
        try {
            FluentWait<WebDriver> wait = new FluentWait<>(driver)
                    .withTimeout(Duration.ofSeconds(secondTimeout))
                    .pollingEvery(Duration.ofMillis(interval))
                    .ignoring(WebDriverException.class);
            wait.until(ExpectedConditions.visibilityOf(element));
            flag = true;
        } catch (WebDriverException e) {
            flag = false;
        }
        return flag;
    }

    public boolean isElementClickableOfMillis(By by, long millisTimeout) {
        boolean flag;
        try {
            FluentWait<WebDriver> wait = new FluentWait<>(driver)
                    .withTimeout(Duration.ofMillis(millisTimeout))
                    .pollingEvery(Duration.ofMillis(interval))
                    .ignoring(WebDriverException.class);
            wait.until((ExpectedConditions.elementToBeClickable(by)));
            wait.until(ExpectedConditions.visibilityOf(driver.findElement(by)));
            flag = true;
        } catch (WebDriverException e) {
            flag = false;
        }
        return flag;
    }

    public boolean isElementClickableOfMillis(String xpath, long millisTimeout) {
        boolean flag;
        try {
            FluentWait<WebDriver> wait = new FluentWait<>(driver)
                    .withTimeout(Duration.ofMillis(millisTimeout))
                    .pollingEvery(Duration.ofMillis(1))
                    .ignoring(WebDriverException.class);
            wait.until((ExpectedConditions.elementToBeClickable(By.xpath(xpath))));
            flag = true;
        } catch (WebDriverException e) {
            flag = false;
        }
        return flag;
    }

    public boolean isElementClickableOfMillis(WebElement element, long millisTimeout) {
        boolean flag;
        try {
            FluentWait<WebDriver> wait = new FluentWait<>(driver)
                    .withTimeout(Duration.ofMillis(millisTimeout))
                    .pollingEvery(Duration.ofMillis(1))
                    .ignoring(WebDriverException.class);
            wait.until((ExpectedConditions.elementToBeClickable(element)));
            flag = true;
        } catch (WebDriverException e) {
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

    public String getTagAllAttributes(String text) {
        Object attr = js.executeScript("var items = {}; " +
                "for (index = 0; index < arguments[0].attributes.length; ++index) " +
                "{ items[arguments[0].attributes[index].name] = arguments[0].attributes[index].value }; " +
                "return items;", driver.findElement(By.xpath(text)));
        return attr.toString();
    }

    public boolean isElementEnabled(String text) {
        String xpath = XpathBuilder.xpathGenerator(text);
        return !getTagAllAttributes(xpath).contains("disable");
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
            throw new NoSuchElementException("unable to find the element: " + xpath);
        }
    }

    public int countElements(String value, int seconds) {
        String xpath = XpathBuilder.xpathGenerator(value);
        if (isElementExist(value, seconds)) {
            List<WebElement> elements = driver.findElements(By.xpath(xpath));
            return elements.size();
        } else {
            throw new NoSuchElementException("unable to find the element: " + xpath);
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

    public List<WebElement> getTableElementsByColumnName(String columnName) {
        return new Table().getByColum(columnName);
    }

    public List<WebElement> getTableElementsByColumnName(String columnName, int offset) {
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

    private String text(String text) {
        return "text->" + text.replace("\"", "");
    }

    private String getAbsoluteXPath(WebElement element) {
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

    private String getFindBy(WebElement element) {
        return element.toString().split("->")[1];
    }

    private void toInitialPoint() {
        Dimension location = driver.findElement(By.tagName("body")).getSize();
        int xPoint = location.getWidth();
        int yPoint = location.getHeight();
        actions.moveToElement(driver.findElement(By.tagName("body")), -xPoint / 2, -yPoint / 2).build().perform();
    }

    private void executeClick(String xpath) {
        log.info("clicking element: \"{}\"", xpath);
        List<WebElement> allElements = findElements(xpath);
        if (allElements.size() == 0) {
            throw new WebDriverException("could not find web element by: \"" + xpath + "\"");
        }
        if (isElementClickableOfMillis(allElements.get(0), 1000)) {
            highlightElement(allElements.get(0));
            allElements.get(0).click();
        } else {
            List<WebElement> clickableElements = findClickableElements(allElements);
            if (!clickableElements.isEmpty()) {
                highlightElement(allElements.get(0));
                clickableElements.get(0).click();
            } else {
                log.warn("all elements are not clickable, the number of the element: \"{}\" is: {}, will click on it one by one", xpath, allElements.size());
                highlightElement(allElements);
                allElements.forEach(this::jsClick);
            }
        }
    }

    private void executeClick(String xpath, int secondTimeout) {
        log.info("clicking element: \"{}\"", xpath);
        List<WebElement> allElements = findElements(xpath, secondTimeout);
        if (allElements.size() == 0) {
            throw new WebDriverException("could not find web element by: \"" + xpath + "\"");
        }
        if (isElementClickableOfMillis(allElements.get(0), secondTimeout * 1000)) {
            highlightElement(allElements.get(0));
            allElements.get(0).click();
        } else {
            List<WebElement> clickableElements = findClickableElements(allElements);
            if (!clickableElements.isEmpty()) {
                highlightElement(allElements.get(0));
                clickableElements.get(0).click();
            } else {
                log.warn("all elements are not clickable, the number of the element: \"{}\" is: {}, will click on it one by one", xpath, allElements.size());
                highlightElement(allElements);
                allElements.forEach(this::jsClick);
            }
        }
    }

    private void executeClick(String xpath, Operator before, Operator after) {
        log.info("clicking element: \"{}\"", xpath);
        List<WebElement> allElements = findElements(xpath);
        if (allElements.size() == 0) {
            throw new WebDriverException("could not find web element by: \"" + xpath + "\"");
        }
        if (isElementClickableOfMillis(allElements.get(0), 1000)) {
            highlightElement(allElements.get(0));
            before.apply();
            allElements.get(0).click();
            after.apply();
        } else {
            List<WebElement> clickableElements = findClickableElements(allElements);
            if (!clickableElements.isEmpty()) {
                highlightElement(allElements.get(0));
                before.apply();
                clickableElements.get(0).click();
                after.apply();
            } else {
                log.warn("all elements are not clickable, the number of the element: \"{}\" is: {}, will click on it one by one", xpath, allElements.size());
                highlightElement(allElements);
                allElements.forEach(i -> {
                    before.apply();
                    jsClick(i);
                    after.apply();
                });
            }
        }
    }

    private Optional<List<WebElement>> executeFindElements(String xpath, int secondTimeout) {
        FluentWait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(secondTimeout))
                .pollingEvery(Duration.ofMillis(interval))
                .ignoring(WebDriverException.class);
        try {
            return Optional.of(wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(xpath))));
        } catch (WebDriverException e) {
            log.debug(e.toString(), e);
            return Optional.empty();
        }
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
