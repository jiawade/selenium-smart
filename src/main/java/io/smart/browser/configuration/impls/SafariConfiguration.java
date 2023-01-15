package io.smart.browser.configuration.impls;

import io.smart.browser.configuration.Configuration;
import io.smart.browser.configuration.DefaultConfiguration;
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
