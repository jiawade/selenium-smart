package io.smart.browser.configuration.impls;

import io.smart.browser.configuration.Configuration;
import io.smart.browser.configuration.DefaultConfiguration;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.Map;

@ToString
@SuperBuilder
@Getter
public class ChromeConfiguration extends DefaultConfiguration implements Configuration {

    @NonNull
    public ChromeOptions chromeOptions;

    public ChromeDriverService chromeDriverService;

    public Map<String, Object> experimentalOption;

    public String downloadDirectory;

    public boolean noSandbox;

    public boolean disableGpu;

    public boolean disableExtensions;

    public boolean disableDevShmUsage;

}
