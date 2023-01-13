package com.selenium.browser.browsers;

import com.selenium.browser.configuration.Configuration;
import com.selenium.enums.SystemType;
import com.selenium.utils.tools.Tools;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;


@Slf4j
public abstract class Browser {
    protected SystemType platform = Tools.getPlatformType();


    public Browser() {
    }

    protected abstract WebDriver setUpBrowser(Boolean headless);

    protected abstract WebDriver setUpBrowser(Configuration conf);

}
