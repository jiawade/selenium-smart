import io.smart.browser.configuration.impls.ChromeConfiguration;
import io.smart.browser.factory.impls.SeleniumBrowser;
import io.smart.element.impls.ElementByXpath;
import io.smart.enums.BrowserType;
import io.smart.enums.Direction;
import io.smart.utils.xpath.Xpath;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SmartUnitTest {
    private ElementByXpath browser;
    private static final String downloadDirectory = System.getProperty("user.dir") + File.separator + "target" + File.separator + "downloads";

    @BeforeClass
    public void setUp() {
        SeleniumBrowser driver = new SeleniumBrowser().setUp(BrowserType.CHROME, buildChromeConf());
        browser = new ElementByXpath(driver.getDriver());
        browser.get("https://www.selenium.dev/");
    }

    @AfterClass
    public void tearDown() {
        if (!Objects.isNull(browser))
            browser.closeBrowser();
    }


    @Test
    public void gotoDocumentationPage() {
        browser.click("text->Documentation");
        Assert.assertEquals(browser.getCurrentUrl(), "https://www.selenium.dev/documentation/");
    }

    @Test
    public void gotoBlogPage() {
        browser.click("text->Documentation");
        browser.clickText("Blog");
        Assert.assertTrue(browser.isElementDisplay("text->Selenium Blog"));
    }

    @Test
    public void gotoHistoryPage() {
        //click about dropdown icon
//        browser.click(Xpath.attribute("data-toggle", "dropdown").build(), "1");
//        browser.clickText("History");
//        Assert.assertTrue(browser.isElementDisplay("text->Selenium History"));
    }

    @Test
    public void searchTest() {
        browser.click(1800, 30, "search box on top right page");
        Assert.assertTrue(browser.isElementDisplay("text->No recent searches"), "unable to open search box");
        browser.input("@class->DocSearch-Input", "selenium");
        Assert.assertTrue(browser.isElementDisplay("text->Documentation"));
    }

    @Test
    public void testUrlCode() {
        Map<String, Integer> urlCode = browser.getUrlAndStatus();
        Assert.assertFalse(urlCode.isEmpty());
    }

    @Test
    public void testClickCondition() {
        WebElement webElement = browser.findElement("text->Documentation");
        browser.clickByCondition(webElement, Direction.DOWN,()->browser.isElementDisplay("text->selenium"));
    }


    private ChromeConfiguration buildChromeConf() {
        ChromeOptions options = new ChromeOptions();
        Map<String, Object> eOptions = new HashMap<>();
        Map<String, Object> infoBar = new HashMap<>();
        infoBar.put("profile.password_manager_enabled", false);
        infoBar.put("credentials_enable_service", false);
        eOptions.put("excludeSwitches", new String[]{"enable-automation"});
        eOptions.put("prefs", infoBar);
        return ChromeConfiguration.builder()
                .chromeOptions(options)
                .width(1920)
                .height(1080)
                .experimentalOption(eOptions)
                .downloadDirectory(downloadDirectory)
                .duration(Duration.ofSeconds(60))
                .maximizeWindow(true)
                .headless(false)
                .noSandbox(true)
                .disableGpu(true)
                .disableDevShmUsage(true)
                .enablePerformanceLog(true)
                .build();
    }


}
