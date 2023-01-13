package com.selenium.browser.configuration.impls;

import com.selenium.browser.configuration.Configuration;
import com.selenium.browser.configuration.DefaultConfiguration;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.openqa.selenium.firefox.FirefoxDriverService;
import org.openqa.selenium.firefox.FirefoxOptions;


@ToString
@SuperBuilder
@Getter
public class FireFoxConfiguration extends DefaultConfiguration implements Configuration {

    @NonNull
    public FirefoxOptions firefoxOptions;

    public FirefoxDriverService firefoxDriverService;
}
