package com.selenium.browser.configuration.impls;

import com.selenium.browser.configuration.Configuration;
import com.selenium.browser.configuration.DefaultConfiguration;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.openqa.selenium.safari.SafariDriverService;
import org.openqa.selenium.safari.SafariOptions;

@ToString
@SuperBuilder
@Getter
public class SafariConfiguration  extends DefaultConfiguration implements Configuration {
    @NonNull
    public SafariOptions safariOptions;

    SafariDriverService safariDriverService;
}
