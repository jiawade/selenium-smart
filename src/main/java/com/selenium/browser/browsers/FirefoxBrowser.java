package com.selenium.browser.browsers;

import com.selenium.browser.configuration.Configuration;
import com.selenium.browser.configuration.impls.FireFoxConfiguration;
import com.selenium.enums.SystemType;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.io.File;
import java.time.Duration;
import java.util.Objects;

@Slf4j
public class FirefoxBrowser extends Browser {

    public FirefoxBrowser() {
    }

    @Override
    public FirefoxDriver setUpBrowser(Boolean headless) {
        FireFoxConfiguration conf = FireFoxConfiguration.builder()
                .duration(Duration.ofSeconds(60))
                .width(1920)
                .hight(1080)
                .headless(false)
                .firefoxOptions(new FirefoxOptions())
                .build();
        FirefoxOptions options = conf.getFirefoxOptions();
        log.debug("current system platform is: " + platform);
        if (platform.equals(SystemType.Linux)) {
            options.setHeadless(true);
        } else if (platform.equals(SystemType.Windows) || platform.equals(SystemType.Mac)) {
            if (headless) {
                options.setHeadless(true);
            }
        } else {
            throw new IllegalArgumentException("unsupported platform type: " + platform);
        }
        return getDriver(conf, options);
    }


    @Override
    public FirefoxDriver setUpBrowser(Configuration configuration) {
        FireFoxConfiguration conf = (FireFoxConfiguration) configuration;
        FirefoxOptions options = conf.getFirefoxOptions();
        log.debug("current system platform is: " + platform);
        if (platform.equals(SystemType.Linux)) {
            options.setHeadless(true);
        } else if (platform.equals(SystemType.Windows) || platform.equals(SystemType.Mac)) {
            if (conf.headless) {
                options.setHeadless(true);
            }
        } else {
            throw new IllegalArgumentException("unsupported platform type: " + platform);
        }
        return getDriver(conf, options);
    }

    private FirefoxDriver getDriver(FireFoxConfiguration conf, FirefoxOptions options) {
        if (!Objects.isNull(conf.getDriverPath())) {
            if (new File(conf.getDriverPath()).exists()) {
                System.setProperty("webdriver.gecko.driver", conf.getDriverPath());
            }else {
                log.warn("gecko driver does not exist: {}", conf.getDriverPath());
            }
        } else {
            log.info("auto configure browser driver");
            WebDriverManager.firefoxdriver().setup();
        }
        if (!Objects.isNull(conf.getBinaryPath())) {
            if (new File(conf.getBinaryPath()).exists()) {
                options.setBinary(conf.getBinaryPath());
            } else {
                log.warn("browser binary does not exist: {}", conf.getBinaryPath());
            }
        }
        if (!Objects.isNull(conf.getDesiredCapabilities())){
            conf.getDesiredCapabilities().setCapability(ChromeOptions.CAPABILITY, options);
        }
        FirefoxDriver driver;
        if (!Objects.isNull(conf.getFirefoxDriverService())){
            driver= new FirefoxDriver(conf.getFirefoxDriverService(),options);
        }else {
            driver=new FirefoxDriver(options);
        }
        driver.manage().timeouts().pageLoadTimeout(conf.getDuration());
        driver.manage().window().setSize(new Dimension(conf.getWidth(), conf.getHight()));
        driver.manage().window().maximize();
        return driver;
    }

}
