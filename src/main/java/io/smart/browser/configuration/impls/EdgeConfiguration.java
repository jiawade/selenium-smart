package io.smart.browser.configuration.impls;

import io.smart.browser.configuration.Configuration;
import io.smart.browser.configuration.DefaultConfiguration;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.openqa.selenium.edge.EdgeDriverService;
import org.openqa.selenium.edge.EdgeOptions;

import java.util.Map;

@ToString
@SuperBuilder
@Getter
public class EdgeConfiguration extends DefaultConfiguration implements Configuration {

    @NonNull
    public EdgeOptions edgeOptions;

    EdgeDriverService edgeDriverService;

    public Map<String, Object> experimentalOption;

    public String downloadDirectory;

    public boolean noSandbox;

    public boolean disableGpu;

    public boolean disableExtensions;

    public boolean disableDevShmUsage;

    public boolean enablePerformanceLog;
}
