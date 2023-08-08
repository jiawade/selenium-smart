package io.smart.driver;

import io.smart.utils.FileUtils;
import io.smart.utils.Pair;
import io.smart.utils.tools.Helper;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.events.WebDriverEventListener;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Slf4j
public class DriverOperation {
    public WebDriver driver;
    public Actions actions;
    public JavascriptExecutor js;
    public static final int secondTimeout = 30;
    public static final int interval = 100;
    private Set<String> logMessages = Collections.synchronizedSet(new HashSet<>());
    private Set<String> webSocketSentMessages = Collections.synchronizedSet(new HashSet<>());
    private Set<String> webSocketReceivedMessages = Collections.synchronizedSet(new HashSet<>());


    public DriverOperation(@NonNull WebDriver driver) {
        this.driver = driver;
        actions = new Actions(driver);
        js = (JavascriptExecutor) driver;
    }

    public DriverOperation(@NonNull WebDriver driver, @NonNull WebDriverEventListener listener) {
        this.driver = new EventFiringWebDriver(driver).register(listener);
        actions = new Actions(driver);
        js = (JavascriptExecutor) driver;
    }

    public void setWebDriver(@NonNull WebDriver driver) {
        this.driver = driver;
        actions = new Actions(driver);
        js = (JavascriptExecutor) driver;
    }

    public void setWebDriver(@NonNull WebDriver driver, @NonNull WebDriverEventListener listener) {
        this.driver = new EventFiringWebDriver(driver).register(listener);
        actions = new Actions(driver);
        js = (JavascriptExecutor) driver;
    }

    public void get(@NonNull String url) {
        log.info("input url: {}", url);
        driver.get(url);
    }

    public void closeBrowser() {
        log.info("quit browser");
        driver.quit();
    }

    public void closeCurrentBrowserTab() {
        log.info("close current tab");
        driver.close();
    }

    public void quit() {
        log.info("quit browser");
        driver.quit();
    }

    public String getPageSource() {
        return driver.getPageSource();
    }

    public String getWindowHandle() {
        return driver.getWindowHandle();
    }

    public Set<String> getWindowHandles() {
        return driver.getWindowHandles();
    }

    public String getTitle() {
        return driver.getTitle();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public void previousPage() {
        driver.navigate().back();
    }

    public void forwardPage() {
        driver.navigate().forward();
    }

    public void refreshBrowser() {
        driver.navigate().refresh();
    }

    public WebDriver.Options manage() {
        return driver.manage();
    }

    public WebDriver.TargetLocator switchTo() {
        return driver.switchTo();
    }

    public void createBrowserWindow() {
        driver.switchTo().newWindow(WindowType.WINDOW);
    }

    public void createBrowserTab() {
        driver.switchTo().newWindow(WindowType.TAB);
    }

    public void switchToFirstBrowserTab() {
        ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(0));
    }

    public void switchToLastBrowserTab() {
        ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(tabs.size() - 1));
    }

    public void switchToBrowserTab(int index) {
        log.info("switching to browser tab to: {}", index);
        final long finishAtMillis = System.currentTimeMillis() + (secondTimeout * 1000L);
        boolean wasInterrupted = false;
        boolean success = true;
        try {
            while (!switchBrowser(index)) {
                final long remainingMillis = finishAtMillis - System.currentTimeMillis();
                if (remainingMillis < 0) {
                    success = false;
                }
                try {
                    Thread.sleep(Math.min(100, remainingMillis));
                } catch (final InterruptedException ignore) {
                    wasInterrupted = true;
                } catch (final Exception ex) {
                    break;
                }
            }
        } finally {
            if (wasInterrupted) {
                Thread.currentThread().interrupt();
            }
        }
        if (!success) {
            ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
            driver.switchTo().window(tabs.get(index));
        }
    }

    private boolean switchBrowser(int index) {
        try {
            ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
            driver.switchTo().window(tabs.get(index));
            return true;
        } catch (IndexOutOfBoundsException e) {
            log.debug(e.toString(), e);
            return false;
        }
    }

    public boolean setWindowSize(int width, int height) {
        Dimension expectedSize = new Dimension(width, height);
        driver.manage().window().setSize(expectedSize);
        Dimension actualSize = driver.manage().window().getSize();
        return expectedSize.toString().equals(actualSize.toString());
    }

    public Pair<Integer, Integer> getElementSize(WebElement element) {
        return Pair.of(element.getSize().getWidth(), element.getSize().getHeight());
    }

    public Pair<Integer, Integer> getElementLocation(WebElement element) {
        return Pair.of(element.getLocation().getX(), element.getLocation().getY());
    }

    public Pair<Integer, Integer> getPageSize() {
        return Pair.of(manage().window().getSize().getWidth(), manage().window().getSize().getHeight());
    }

    public Object takeScreenshot(WebElement webElement, OutputType<?> outputType) {
        return webElement.getScreenshotAs(outputType);
    }

    public Object takeScreenshot(OutputType<?> outputType) {
        Object screenshot = null;
        if (driver instanceof ChromeDriver) {
            screenshot = ((ChromeDriver) driver).getScreenshotAs(outputType);
        } else if (driver instanceof FirefoxDriver) {
            screenshot = ((FirefoxDriver) driver).getScreenshotAs(outputType);
        } else if (driver instanceof EdgeDriver) {
            screenshot = ((EdgeDriver) driver).getScreenshotAs(outputType);
        }
        return screenshot;
    }

    public void setSessionItem(String key, String value) {
        if (driver instanceof ChromeDriver) {
            ((ChromeDriver) driver).getSessionStorage().setItem(key, value);
        } else if (driver instanceof FirefoxDriver) {
            ((FirefoxDriver) driver).getSessionStorage().setItem(key, value);
        } else if (driver instanceof EdgeDriver) {
            ((EdgeDriver) driver).getSessionStorage().setItem(key, value);
        }
    }

    public void setLocalItem(String key, String value) {
        if (driver instanceof ChromeDriver) {
            ((ChromeDriver) driver).getLocalStorage().setItem(key, value);
        } else if (driver instanceof FirefoxDriver) {
            ((FirefoxDriver) driver).getLocalStorage().setItem(key, value);
        } else if (driver instanceof EdgeDriver) {
            ((EdgeDriver) driver).getLocalStorage().setItem(key, value);
        }
    }

    public String getSessionItem(String key) {
        String value = null;
        ChromeDriver d = (ChromeDriver) driver;
        for (int i = 0; i <= 30; i++) {
            value = d.getSessionStorage().getItem(key);
            if (value != null) {
                break;
            } else {
                Helper.sleep(1000);
            }
        }
        return value;
    }

    public void savePageSource(File file) {
        log.info("saving current page soruce to: \"{}\"", file.getPath());
        String pageSource = this.getPageSource();
        FileUtils.writeStringToFile(file.getAbsolutePath(), pageSource);
    }

    public void savePageSourceToDesktop() {
        File desktopDirectory = FileSystemView.getFileSystemView().getHomeDirectory();
        File directory = new File(desktopDirectory.getAbsolutePath() + File.separator + this.getTitle() + ".html");
        savePageSource(directory);
    }

    public Set<String> getMessages() {
        getLogs().forEach(i -> logMessages.add(i.getMessage()));
        return this.logMessages;
    }

    public Set<String> getSentWebsocketMessages() {
        getMessages();
        this.logMessages.forEach(i -> {
            if (i.contains("Network.webSocketFrameSent")) {
                webSocketSentMessages.add(i);
            }
        });
        return this.webSocketSentMessages;
    }

    public Set<String> getReceivedWebsocketMessages() {
        getMessages();
        this.logMessages.forEach(i -> {
            if (i.contains("Network.webSocketFrameReceived")) {
                webSocketReceivedMessages.add(i);
            }
        });
        return this.webSocketReceivedMessages;
    }

    public LogEntries getLogs(String type) {
        Set<String> availableLogs = driver.manage().logs().getAvailableLogTypes();
        if (!availableLogs.contains(type)) {
            throw new IllegalArgumentException("unable to find performance log type, available log is: " + availableLogs);
        }
        return driver.manage().logs().get(type);
    }

    public LogEntries getLogs() {
        Set<String> availableLogs = driver.manage().logs().getAvailableLogTypes();
        if (!availableLogs.contains(LogType.PERFORMANCE)) {
            throw new IllegalArgumentException("unable to find performance log type, available log is: " + availableLogs);
        }
        return driver.manage().logs().get(LogType.PERFORMANCE);
    }

    public Map<String, Integer> getUrlAndStatus() {
        return new Logs().parseUrlAndCode(getLogs()).getUrlCode();
    }

    @Getter
    static class Logs {
        public Map<String, Integer> urlCode = new HashMap<>();
        private static final String urlReg = "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
        private static final String codeReg = "(\"status\":)(\\d+)";
        private final Pattern urlPattern = Pattern.compile(urlReg, Pattern.CASE_INSENSITIVE);
        private final Pattern codePattern = Pattern.compile(codeReg, Pattern.CASE_INSENSITIVE);

        public Logs parseUrlAndCode(LogEntries logEntries) {
            logEntries.forEach(i -> {
                if (i.getMessage().contains("status") && i.getMessage().contains("url")) {
                    Matcher urlMatcher = urlPattern.matcher(i.getMessage());
                    Matcher codeMatcher = codePattern.matcher(i.getMessage());
                    while (urlMatcher.find() && codeMatcher.find()) {
                        urlCode.put(i.getMessage().substring(urlMatcher.start(0), urlMatcher.end(0)), Integer.parseInt(codeMatcher.group(2)));
                    }
                }
            });
            return this;
        }

    }

}
