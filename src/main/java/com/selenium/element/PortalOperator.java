package com.selenium.element;

import com.selenium.enums.Direction;
import com.selenium.enums.Position;
import lombok.NonNull;
import org.openqa.selenium.*;

import java.io.File;
import java.util.List;

public interface PortalOperator {

    void click(String text);

    void click(String text, String index);

    void click(String text, int secondTimeout);

    void click(WebElement element);

    void click(By by);

    void click(int width, int height);

    void click(int width, int height, String precondition);

    void click(int width, int height, String precondition, String expectation);

    void clickAny(String text);

    void clickContain(String text);

    void clickContain(String text, int secondTimeout);

    void jsClick(String xpath);

    void jsClick(WebElement element);

    String getText(String text);

    String getText(String text, int secondTimeout);

    String getText(WebElement element);

    List<String> getTexts(String text);

    List<String> getTexts(String text, int secondTimeOut);

    void clickXYBesidesText(int pixel, Direction direction, String xpath);

    void input(String xpath, String text);

    void input(WebElement element, String text);

    void input(String xpath, Keys keys);

    void uploadFile(String xpath, File file);

    void clearInputBox(String text);

    void clearInputBox(WebElement element);

    void pressKey(String text, Keys keys);

    WebElement findElement(String text);

    WebElement findElement(String text, String index);

    WebElement findElement(String text, int secondTimeout);

    WebElement findElement(String text, String index, int secondTimeout);

    WebElement findElementContains(String text, int secondTimeout);

    WebElement findElementContains(String text);

    List<WebElement> findElements(String text);

    List<WebElement> findElements(String text, int secondTimeout);

    void waitElementDisappear(String element);

    Object getElementCssValue(String text, String attribute);

    WebElement findEditableInputBox(String xpath);

    WebElement findClickableElement(String xpath);

    WebElement findClickableElement(String xpath, int millisTimeOut);

    List<WebElement> findClickableElements(String xpath);

    void hover(Position position);

    void hoverAnElement(WebElement element);

    void hoverAnElement(String element);

    void hoverAnElement(String element, String index);

    void hoverAnElement(int x, int y);

    void scrollToTop();

    void scrollToBottom();

    void scrollToLeft();

    void scrollToRight();

    void scrollToElement(String text);

    boolean isElementExist(String text);

    boolean isElementExist(String text, int secondTimeout);

    boolean isElementExist(String text, float millSecondTimeout);

    boolean isElementNotExist(String text);

    boolean isElementNotExist(String text, int secondTimeout);

    boolean isElementExistContain(String text);

    boolean isElementExistContain(String text, int secondTimeout);

    boolean isElementClickable(By by);

    boolean isElementClickable(By by, int timeOut);

    boolean isElementClickable(WebElement element);

    boolean isElementClickable(WebElement element, int millisTimeOut);

    boolean isElementClickable(String xpath);

    boolean isElementClickable(String xpath, int secondTimeOut);

    boolean isElementOperability(String text);

    boolean isElementOperability(String text, int secondTimeout);

    void waitElementOperability(String text);

    String getTagAllAttributes(String text);

    boolean isElementEnabled(String text);

    String getElementRGBAColor(String text);

    String getElementHexColor(String text);

    String getElementColorName(String text, String cssColor);

    Boolean isElementHidden(WebElement element);

    Boolean isElementHidden(String text);

    String text(@NonNull String text);

    void clickTextWithLastIndex(@NonNull String text);

    void clickText(@NonNull String text);

    void clickTextWithIndex(@NonNull String text, @NonNull String index);

    void clickPositionByBesidesText(int pixel, Direction direction, String xpath);
}
