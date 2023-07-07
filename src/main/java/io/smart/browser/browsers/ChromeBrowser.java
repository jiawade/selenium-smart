package io.smart.browser.browsers;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.smart.browser.configuration.Configuration;
import io.smart.browser.configuration.impls.ChromeConfiguration;
import io.smart.enums.SystemType;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;

import java.io.File;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;

@Slf4j
public class ChromeBrowser extends Browser {


    public ChromeBrowser() {
    }

    @Override
    public ChromeDriver setUpBrowser(Boolean headless) {
        ChromeConfiguration conf = ChromeConfiguration.builder()
                .duration(Duration.ofSeconds(60))
                .width(1920)
                .height(1080)
                .chromeOptions(new ChromeOptions())
                .build();
        ChromeOptions options = conf.getChromeOptions();
        log.debug("current system platform is: " + platform);
        if (platform.equals(SystemType.Linux)) {
            options.setHeadless(true);
        } else if (platform.equals(SystemType.Windows) || platform.equals(SystemType.Mac)) {
            if (headless) options.setHeadless(true);
        } else {
            throw new IllegalArgumentException("unsupported platform type: " + platform);
        }
        return getDriver(conf, options);
    }


    @Override
    public ChromeDriver setUpBrowser(Configuration configuration) {
        ChromeConfiguration conf = (ChromeConfiguration) configuration;
        ChromeOptions options = conf.getChromeOptions();
        log.debug("current system platform is: " + platform);
        if (platform.equals(SystemType.Linux)) {
            options.setHeadless(true);
        } else if (platform.equals(SystemType.Windows) || platform.equals(SystemType.Mac)) {
            if (conf.headless) options.setHeadless(true);
        } else {
            throw new IllegalArgumentException("unsupported platform type: " + platform);
        }
        return getDriver(conf, options);
    }

    private ChromeDriver getDriver(ChromeConfiguration conf, ChromeOptions options) {
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
        if (conf.enablePerformanceLog) {
            LoggingPreferences logPrefs = new LoggingPreferences();
            logPrefs.enable(LogType.PERFORMANCE, Level.ALL);
            options.setCapability("enableNetwork", true);
            options.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
            options.setCapability("goog:loggingPrefs", logPrefs);
        }
        if (!Objects.isNull(conf.getDriverPath())) {
            if (new File(conf.getDriverPath()).exists()) {
                System.setProperty("webdriver.chrome.driver", conf.getDriverPath());
            } else {
                log.warn("chrome driver does not exist: {}", conf.getDriverPath());
            }
        } else {
            log.info("auto configure browser driver...");
            WebDriverManager.chromedriver().setup();
        }
        if (!Objects.isNull(conf.getBinaryPath())) {
            if (new File(conf.getBinaryPath()).exists()) {
                options.setBinary(conf.getBinaryPath());
            } else {
                log.warn("browser binary does not exist: {}", conf.getBinaryPath());
            }
        }
        if (!Objects.isNull(conf.getDesiredCapabilities())) {
            conf.getDesiredCapabilities().setCapability(ChromeOptions.CAPABILITY, options);
        }
        if (!Objects.isNull(conf.getExperimentalOption())) {
            conf.getExperimentalOption().forEach(options::setExperimentalOption);
        }
        if (!Objects.isNull(conf.getDownloadDirectory())) {
            if (!Objects.isNull(conf.getExperimentalOption()) && conf.getExperimentalOption().containsKey("prefs")) {
                Map<String, Object> prefs = (Map) conf.getExperimentalOption().get("prefs");
                prefs.put("download.default_directory", conf.getDownloadDirectory());
                options.setExperimentalOption("prefs", conf.getExperimentalOption().get("prefs"));
            } else {
                Map<String, Object> prefs = new HashMap<>();
                prefs.put("download.default_directory", conf.getDownloadDirectory());
                options.setExperimentalOption("prefs", prefs);
            }
        }
        if (!Objects.isNull(conf.getPageLoadStrategy())) {
            options.setPageLoadStrategy(conf.getPageLoadStrategy());
        }
        if (!Objects.isNull(conf.getProxy())) {
            options.setProxy(conf.getProxy());
        }
        ChromeDriver driver;
        if (!Objects.isNull(conf.getChromeDriverService())) {
            driver = new ChromeDriver(conf.getChromeDriverService(), options);
        } else {
            driver = new ChromeDriver(options);
        }
        driver.manage().timeouts().pageLoadTimeout(conf.getDuration());
        driver.manage().window().setSize(new Dimension(conf.getWidth(), conf.getHeight()));
        if (conf.maximizeWindow) {
            driver.manage().window().maximize();
        }
        return driver;
    }


}
