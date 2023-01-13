package com.selenium.browser.browsers;

import com.selenium.browser.configuration.Configuration;
import com.selenium.browser.configuration.impls.EdgeConfiguration;
import com.selenium.browser.configuration.impls.SafariConfiguration;
import com.selenium.enums.SystemType;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

import java.io.File;
import java.time.Duration;
import java.util.Objects;

@Slf4j
public class SafariBrowser extends Browser{

    public SafariBrowser() {
    }

    @Override
    public SafariDriver setUpBrowser(Boolean headless) {
        SafariConfiguration conf = SafariConfiguration.builder()
                .duration(Duration.ofSeconds(60))
                .width(1920)
                .hight(1080)
                .headless(false)
                .safariOptions(new SafariOptions())
                .build();
        SafariOptions options = conf.getSafariOptions();
        log.debug("current system platform is: " + platform);
        if (platform.equals(SystemType.Linux)) {
            throw new IllegalArgumentException("unsupported platform type: " + platform);
        }
        return getDriver(conf, options);
    }

    @Override
    public SafariDriver setUpBrowser(Configuration configuration) {
        SafariConfiguration conf = (SafariConfiguration) configuration;
        SafariOptions options = conf.getSafariOptions();
        log.debug("current system platform is: " + platform);
        if (platform.equals(SystemType.Linux)) {
            throw new IllegalArgumentException("unsupported platform type: " + platform);
        }
        return getDriver(conf, options);
    }

    private SafariDriver getDriver(SafariConfiguration conf, SafariOptions options) {
        if (!Objects.isNull(conf.getDriverPath())) {
            if (new File(conf.getDriverPath()).exists()) {
                System.setProperty("webdriver.edge.driver", conf.getDriverPath());
            } else {
                log.warn("edge driver does not exist: {}", conf.getDriverPath());
            }
        } else {
            log.info("auto configure browser driver");
            WebDriverManager.safaridriver().setup();
        }
        if (!Objects.isNull(conf.getDesiredCapabilities())) {
            conf.getDesiredCapabilities().setCapability(ChromeOptions.CAPABILITY, options);
        }
        SafariDriver driver;
        if (!Objects.isNull(conf.getSafariDriverService())) {
            driver = new SafariDriver(conf.getSafariDriverService(), options);
        } else {
            driver = new SafariDriver(options);
        }
        driver.manage().timeouts().pageLoadTimeout(conf.getDuration());
        driver.manage().window().setSize(new Dimension(conf.getWidth(), conf.getHight()));
        driver.manage().window().maximize();
        return driver;
    }
}
