package com.selenium.browser.configuration;

import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.time.Duration;

@Getter
@SuperBuilder
public abstract class DefaultConfiguration {
    @NonNull
    public int width;

    @NonNull
    public int hight;

    @NonNull
    public Duration duration;

    @NonNull
    public boolean headless;

    public String binaryPath;

    public String driverPath;

    public DesiredCapabilities desiredCapabilities;
}
