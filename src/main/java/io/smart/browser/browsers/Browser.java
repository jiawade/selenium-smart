package io.smart.browser.browsers;

import io.smart.browser.configuration.Configuration;
import io.smart.enums.SystemType;
import io.smart.utils.tools.Helper;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;


@Slf4j
public abstract class Browser {
    protected SystemType platform = Helper.getPlatformType();


    public Browser() {
    }

    protected abstract WebDriver setUpBrowser(Boolean headless);

    protected abstract WebDriver setUpBrowser(Configuration conf);

}
