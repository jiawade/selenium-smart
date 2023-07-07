package io.smart.browser.browsers;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.smart.browser.configuration.Configuration;
import io.smart.browser.configuration.impls.SafariConfiguration;
import io.smart.enums.SystemType;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

import java.io.File;
import java.time.Duration;
import java.util.Objects;

@Slf4j
public class SafariBrowser extends Browser {

    public SafariBrowser() {
    }

    @Override
    public SafariDriver setUpBrowser(Boolean headless) {
        SafariConfiguration conf = SafariConfiguration.builder()
                .duration(Duration.ofSeconds(60))
                .width(1920)
                .height(1080)
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
        log.info("auto configure browser driver");
        WebDriverManager.safaridriver().setup();
        if (!Objects.isNull(conf.getDesiredCapabilities())) {
            conf.getDesiredCapabilities().setCapability(ChromeOptions.CAPABILITY, options);
        }
        if (!Objects.isNull(conf.getPageLoadStrategy())) {
            options.setPageLoadStrategy(conf.getPageLoadStrategy());
        }
        SafariDriver driver;
        if (!Objects.isNull(conf.getSafariDriverService())) {
            driver = new SafariDriver(conf.getSafariDriverService(), options);
        } else {
            driver = new SafariDriver(options);
        }
        driver.manage().timeouts().pageLoadTimeout(conf.getDuration());
        driver.manage().window().setSize(new Dimension(conf.getWidth(), conf.getHeight()));
        if (conf.maximizeWindow) {
            driver.manage().window().maximize();
        }
        return driver;
    }
}
