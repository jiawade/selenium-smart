package com.selenium.browser.configuration.impls;

import com.selenium.browser.configuration.Configuration;
import com.selenium.browser.configuration.DefaultConfiguration;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;

@ToString
@SuperBuilder
@Getter
public class ChromeConfiguration extends DefaultConfiguration implements Configuration {

    @NonNull
    public ChromeOptions chromeOptions;

    public ChromeDriverService chromeDriverService;

}
