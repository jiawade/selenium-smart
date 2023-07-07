package io.smart.browser.configuration;

import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.time.Duration;

@Getter
@SuperBuilder
public abstract class DefaultConfiguration {
    @NonNull
    public int width;

    @NonNull
    public int height;

    @NonNull
    public Duration duration;

    @NonNull
    public boolean headless;

    public String binaryPath;

    public String driverPath;

    public DesiredCapabilities desiredCapabilities;

    public boolean maximizeWindow;

    public PageLoadStrategy pageLoadStrategy;

    public Proxy proxy;

}
