package io.smart.browser.factory.impls;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import io.smart.browser.configuration.impls.ChromeConfiguration;
import io.smart.browser.configuration.impls.EdgeConfiguration;
import io.smart.browser.configuration.impls.FireFoxConfiguration;
import io.smart.browser.configuration.impls.SafariConfiguration;
import io.smart.browser.factory.BrowserFactory;
import io.smart.enums.BrowserType;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

import java.io.File;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.logging.Level;

@Slf4j
public class SelenideBrowser implements BrowserFactory {
    private WebDriver driver;

    @Override
    public SelenideBrowser setUp(BrowserType browserType, Boolean headless) {
        if (browserType.equals(BrowserType.CHROME)) {
            this.instanceChrome(headless);
            return this;
        } else if (browserType.equals(BrowserType.FIREFOX)) {
            this.instanceFireFox(headless);
            return this;
        } else if (browserType.equals(BrowserType.EDGE)) {
            this.instanceEdge(headless);
            return this;
        } else if (browserType.equals(BrowserType.SAFARI)) {
            this.instanceSafari(headless);
            return this;
        } else {
            throw new IllegalArgumentException("no browser type found");
        }
    }

    @Override
    public SelenideBrowser setUp(BrowserType browserType, io.smart.browser.configuration.Configuration conf) {
        if (browserType.equals(BrowserType.CHROME)) {
            this.instanceChrome(conf);
            return this;
        } else if (browserType.equals(BrowserType.FIREFOX)) {
            this.instanceFireFox(conf);
            return this;
        } else if (browserType.equals(BrowserType.EDGE)) {
            this.instanceEdge(conf);
            return this;
        } else if (browserType.equals(BrowserType.SAFARI)) {
            this.instanceSafari(conf);
            return this;
        } else {
            throw new IllegalArgumentException("no browser type found");
        }
    }

    @Override
    public WebDriver getDriver() {
        return driver;
    }

    @Override
    public DevMode getDevTools() {
        return new DevMode(getDriver());
    }

    private synchronized ChromeDriver instanceChrome(boolean headless) {
        Configuration.browser = "chrome";
        if (headless) {
            Configuration.headless = true;
        }
        Selenide.open();
        driver = Selenide.webdriver().driver().getWebDriver();
        return (ChromeDriver) driver;
    }

    private synchronized ChromeDriver instanceChrome(io.smart.browser.configuration.Configuration configuration) {
        ChromeConfiguration conf = (ChromeConfiguration) configuration;
        ChromeOptions options = conf.getChromeOptions();
        if (!Objects.isNull(conf.getBinaryPath())) {
            Configuration.browserBinary = conf.getBinaryPath();
        }
        if (conf.enablePerformanceLog) {
            LoggingPreferences logPrefs = new LoggingPreferences();
            logPrefs.enable(LogType.PERFORMANCE, Level.ALL);
            options.setCapability("enableNetwork", true);
            options.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
            options.setCapability(EdgeOptions.LOGGING_PREFS, logPrefs);
        }
        if (conf.noSandbox) {
            options.addArguments("--no-sandbox");
        }
        if (conf.disableGpu) {
            options.addArguments("--disable-gpu");
        }
        if (conf.disableExtensions) {
            options.addArguments("--disable-extensions");
        }
        if (conf.disableDevShmUsage) {
            options.addArguments("--disable-dev-shm-usage");
        }
        if (!Objects.isNull(conf.getDownloadDirectory())) {
            Configuration.downloadsFolder = conf.getDownloadDirectory();
        }
        if (!Objects.isNull(conf.getDesiredCapabilities())) {
            options.merge(conf.getDesiredCapabilities());
        }
        if (!Objects.isNull(conf.getPageLoadStrategy())) {
            Configuration.pageLoadStrategy = conf.getPageLoadStrategy().toString();
        }
        Configuration.browserCapabilities = options;
        Configuration.browserSize = String.format("%sx%s", conf.getWidth(), conf.getHeight());
        Configuration.pageLoadTimeout = conf.getDuration().get(ChronoUnit.SECONDS) * 1000;
        if (!Objects.isNull(conf.getDriverPath())) {
            Configuration.driverManagerEnabled = false;
            if (new File(conf.getDriverPath()).exists()) {
                System.setProperty("webdriver.chrome.driver", conf.getDriverPath());
            } else {
                log.warn("chrome driver does not exist: {}", conf.getDriverPath());
            }
        }
        Configuration.browser = "chrome";
        Selenide.open();
        driver = Selenide.webdriver().driver().getWebDriver();
        if (conf.maximizeWindow) {
            driver.manage().window().maximize();
        }
        return (ChromeDriver) driver;
    }

    private synchronized FirefoxDriver instanceFireFox(boolean headless) {
        Configuration.browser = "firefox";
        if (headless) {
            Configuration.headless = true;
        }
        Selenide.open();
        driver = Selenide.webdriver().driver().getWebDriver();
        return (FirefoxDriver) driver;
    }

    private synchronized FirefoxDriver instanceFireFox(io.smart.browser.configuration.Configuration configuration) {
        FireFoxConfiguration conf = (FireFoxConfiguration) configuration;
        FirefoxOptions options = conf.getFirefoxOptions();
        if (!Objects.isNull(conf.getBinaryPath())) {
            Configuration.browserBinary = conf.getBinaryPath();
        }
        if (conf.noSandbox) {
            options.addArguments("--no-sandbox");
        }
        if (conf.disableGpu) {
            options.addArguments("--disable-gpu");
        }
        if (conf.disableExtensions) {
            options.addArguments("--disable-extensions");
        }
        if (conf.disableDevShmUsage) {
            options.addArguments("--disable-dev-shm-usage");
        }
        if (!Objects.isNull(conf.getDesiredCapabilities())) {
            options.merge(conf.getDesiredCapabilities());
        }
        if (!Objects.isNull(conf.getPageLoadStrategy())) {
            Configuration.pageLoadStrategy = conf.getPageLoadStrategy().toString();
        }
        Configuration.browserCapabilities = options;
        Configuration.browserSize = String.format("%sx%s", conf.getWidth(), conf.getHeight());
        Configuration.pageLoadTimeout = conf.getDuration().get(ChronoUnit.SECONDS) * 1000;
        if (!Objects.isNull(conf.getDriverPath())) {
            Configuration.driverManagerEnabled = false;
            if (new File(conf.getDriverPath()).exists()) {
                System.setProperty("webdriver.gecko.driver", conf.getDriverPath());
            } else {
                log.warn("gecko driver does not exist: {}", conf.getDriverPath());
            }
        }
        Configuration.browser = "firefox";
        Selenide.open();
        driver = Selenide.webdriver().driver().getWebDriver();
        if (conf.maximizeWindow) {
            driver.manage().window().maximize();
        }
        return (FirefoxDriver) driver;
    }

    private synchronized EdgeDriver instanceEdge(boolean headless) {
        Configuration.browser = "edge";
        if (headless) {
            Configuration.headless = true;
        }
        Selenide.open();
        driver = Selenide.webdriver().driver().getWebDriver();
        return (EdgeDriver) driver;
    }

    private synchronized EdgeDriver instanceEdge(io.smart.browser.configuration.Configuration configuration) {
        EdgeConfiguration conf = (EdgeConfiguration) configuration;
        EdgeOptions options = conf.getEdgeOptions();
        if (!Objects.isNull(conf.getBinaryPath())) {
            Configuration.browserBinary = conf.getBinaryPath();
        }
        if (conf.enablePerformanceLog) {
            LoggingPreferences logPrefs = new LoggingPreferences();
            logPrefs.enable(LogType.PERFORMANCE, Level.ALL);
            options.setCapability("enableNetwork", true);
            options.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
            options.setCapability(EdgeOptions.LOGGING_PREFS, logPrefs);
        }
        if (conf.noSandbox) {
            options.addArguments("--no-sandbox");
        }
        if (conf.disableGpu) {
            options.addArguments("--disable-gpu");
        }
        if (conf.disableExtensions) {
            options.addArguments("--disable-extensions");
        }
        if (conf.disableDevShmUsage) {
            options.addArguments("--disable-dev-shm-usage");
        }
        if (!Objects.isNull(conf.getDesiredCapabilities())) {
            options.merge(conf.getDesiredCapabilities());
        }
        if (!Objects.isNull(conf.getPageLoadStrategy())) {
            Configuration.pageLoadStrategy = conf.getPageLoadStrategy().toString();
        }
        Configuration.browserCapabilities = options;
        Configuration.browserSize = String.format("%sx%s", conf.getWidth(), conf.getHeight());
        Configuration.pageLoadTimeout = conf.getDuration().get(ChronoUnit.SECONDS) * 1000;
        if (!Objects.isNull(conf.getDriverPath())) {
            Configuration.driverManagerEnabled = false;
            if (new File(conf.getDriverPath()).exists()) {
                System.setProperty("webdriver.edge.driver", conf.getDriverPath());
            } else {
                log.warn("edge driver does not exist: {}", conf.getDriverPath());
            }
        }
        if (!Objects.isNull(conf.getDownloadDirectory())) {
            Configuration.downloadsFolder = conf.getDownloadDirectory();
        }

        if (!Objects.isNull(conf.getDesiredCapabilities())) {
            options.merge(conf.getDesiredCapabilities());
        }
        Configuration.browser = "edge";
        Selenide.open();
        driver = Selenide.webdriver().driver().getWebDriver();
        if (conf.maximizeWindow) {
            driver.manage().window().maximize();
        }
        return (EdgeDriver) driver;
    }

    private synchronized SafariDriver instanceSafari(boolean headless) {
        Configuration.browser = "safari";
        if (headless) {
            Configuration.headless = true;
        }
        Selenide.open();
        driver = Selenide.webdriver().driver().getWebDriver();
        return (SafariDriver) driver;
    }

    private synchronized SafariDriver instanceSafari(io.smart.browser.configuration.Configuration configuration) {
        SafariConfiguration conf = (SafariConfiguration) configuration;
        SafariOptions options = conf.getSafariOptions();
        if (!Objects.isNull(conf.getBinaryPath())) {
            Configuration.browserBinary = conf.getBinaryPath();
        }
        if (!Objects.isNull(conf.getDesiredCapabilities())) {
            options.merge(conf.getDesiredCapabilities());
        }
        if (!Objects.isNull(conf.getPageLoadStrategy())) {
            Configuration.pageLoadStrategy = conf.getPageLoadStrategy().toString();
        }
        Configuration.browserCapabilities = options;
        Configuration.browserSize = String.format("%sx%s", conf.getWidth(), conf.getHeight());
        Configuration.pageLoadTimeout = conf.getDuration().get(ChronoUnit.SECONDS) * 1000;
        Configuration.browser = "safari";
        Selenide.open();
        driver = Selenide.webdriver().driver().getWebDriver();
        if (conf.maximizeWindow) {
            driver.manage().window().maximize();
        }
        return (SafariDriver) driver;
    }
}
