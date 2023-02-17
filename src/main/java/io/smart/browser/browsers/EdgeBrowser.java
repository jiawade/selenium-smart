package io.smart.browser.browsers;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.smart.browser.configuration.Configuration;
import io.smart.browser.configuration.impls.EdgeConfiguration;
import io.smart.enums.SystemType;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

import java.io.File;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
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
        if (!Objects.isNull(conf.getExperimentalOption())) {
            conf.getExperimentalOption().forEach(options::setExperimentalOption);
        }
        if (!Objects.isNull(conf.getExperimentalOption()) && conf.getExperimentalOption().containsKey("prefs")) {
            if (conf.getExperimentalOption().containsKey("prefs")) {
                Map<String, Object> prefs = (Map) conf.getExperimentalOption().get("prefs");
                prefs.put("download.default_directory", conf.getDownloadDirectory());
                options.setExperimentalOption("prefs", conf.getExperimentalOption().get("prefs"));
            } else {
                Map<String, Object> prefs = new HashMap<>();
                prefs.put("download.default_directory", conf.getDownloadDirectory());
                options.setExperimentalOption("prefs", prefs);
            }
        }
        EdgeDriver driver;
        if (!Objects.isNull(conf.getEdgeDriverService())) {
            driver = new EdgeDriver(conf.getEdgeDriverService(), options);
        } else {
            driver = new EdgeDriver(options);
        }
        driver.manage().timeouts().pageLoadTimeout(conf.getDuration());
        driver.manage().window().setSize(new Dimension(conf.getWidth(), conf.getHight()));
        if (conf.maximizeWindow) {
            driver.manage().window().maximize();
        }
        return driver;
    }
}
