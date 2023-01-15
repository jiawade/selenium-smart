package io.smart.browser.configuration.impls;

import io.smart.browser.configuration.Configuration;
import io.smart.browser.configuration.DefaultConfiguration;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.openqa.selenium.edge.EdgeDriverService;
import org.openqa.selenium.edge.EdgeOptions;

@ToString
@SuperBuilder
@Getter
public class EdgeConfiguration extends DefaultConfiguration implements Configuration {

    @NonNull
    public EdgeOptions edgeOptions;

    EdgeDriverService edgeDriverService;
}
