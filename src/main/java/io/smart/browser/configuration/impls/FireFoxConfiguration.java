package io.smart.browser.configuration.impls;

import io.smart.browser.configuration.Configuration;
import io.smart.browser.configuration.DefaultConfiguration;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.openqa.selenium.firefox.FirefoxDriverService;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;


@ToString
@SuperBuilder
@Getter
public class FireFoxConfiguration extends DefaultConfiguration implements Configuration {

    @NonNull
    public FirefoxOptions firefoxOptions;

    public FirefoxDriverService firefoxDriverService;

    public boolean noSandbox;

    public boolean disableGpu;

    public boolean disableExtensions;

    public boolean disableDevShmUsage;

    public FirefoxProfile firefoxProfile;
}
