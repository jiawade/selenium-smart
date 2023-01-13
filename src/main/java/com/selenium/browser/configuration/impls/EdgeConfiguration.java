package com.selenium.browser.configuration.impls;

import com.selenium.browser.configuration.Configuration;
import com.selenium.browser.configuration.DefaultConfiguration;
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
