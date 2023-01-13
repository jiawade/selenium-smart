package com.selenium.browser.browsers;

import com.selenium.browser.configuration.impls.ChromeConfiguration;
import com.selenium.browser.configuration.Configuration;
import com.selenium.enums.SystemType;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;


import java.io.File;
import java.time.Duration;
import java.util.*;

@Slf4j
public class ChromeBrowser extends Browser {


    public ChromeBrowser() {
    }

    @Override
    public ChromeDriver setUpBrowser(Boolean headless) {
        ChromeConfiguration conf = ChromeConfiguration.builder()
                .duration(Duration.ofSeconds(60))
                .width(1920)
                .hight(1080)
                .headless(false)
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
        ChromeDriver driver;
        if (!Objects.isNull(conf.getChromeDriverService())) {
            driver = new ChromeDriver(conf.getChromeDriverService(), options);
        } else {
            driver = new ChromeDriver(options);
        }
        driver.manage().timeouts().pageLoadTimeout(conf.getDuration());
        driver.manage().window().setSize(new Dimension(conf.getWidth(), conf.getHight()));
        driver.manage().window().maximize();
        return driver;
    }
}
