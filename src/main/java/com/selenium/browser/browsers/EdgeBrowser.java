package com.selenium.browser.browsers;

import com.selenium.browser.configuration.Configuration;
import com.selenium.browser.configuration.impls.EdgeConfiguration;
import com.selenium.enums.SystemType;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

import java.io.File;
import java.time.Duration;
import java.util.Objects;

@Slf4j
public class EdgeBrowser extends Browser {

    public EdgeBrowser() {
    }

    @Override
    public EdgeDriver setUpBrowser(Boolean headless) {
        EdgeConfiguration conf = EdgeConfiguration.builder()
                .duration(Duration.ofSeconds(60))
                .width(1920)
                .hight(1080)
                .headless(false)
                .edgeOptions(new EdgeOptions())
                .build();
        EdgeOptions options = conf.getEdgeOptions();
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
    public EdgeDriver setUpBrowser(Configuration configuration) {
        EdgeConfiguration conf = (EdgeConfiguration) configuration;
        EdgeOptions options = conf.getEdgeOptions();
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

    private EdgeDriver getDriver(EdgeConfiguration conf, EdgeOptions options) {
        if (!Objects.isNull(conf.getDriverPath())) {
            if (new File(conf.getDriverPath()).exists()) {
                System.setProperty("webdriver.edge.driver", conf.getDriverPath());
            } else {
                log.warn("edge driver does not exist: {}", conf.getDriverPath());
            }
        } else {
            log.info("auto configure browser driver");
            WebDriverManager.edgedriver().setup();
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
        EdgeDriver driver;
        if (!Objects.isNull(conf.getEdgeDriverService())) {
            driver = new EdgeDriver(conf.getEdgeDriverService(), options);
        } else {
            driver = new EdgeDriver(options);
        }
        driver.manage().timeouts().pageLoadTimeout(conf.getDuration());
        driver.manage().window().setSize(new Dimension(conf.getWidth(), conf.getHight()));
        driver.manage().window().maximize();
        return driver;
    }
}
