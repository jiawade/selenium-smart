import io.smart.browser.configuration.impls.ChromeConfiguration;
import io.smart.browser.factory.BrowserFactory;
import io.smart.browser.factory.impls.SelenideBrowser;
import io.smart.element.impls.ElementByXpath;
import io.smart.enums.BrowserType;
import io.smart.utils.tools.Helper;
import io.smart.utils.xpath.Xpath;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.v85.network.model.RequestWillBeSent;

import java.io.File;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class Examples {

    public static void main(String[] args) {
        BrowserFactory factory = new SelenideBrowser().setUp(BrowserType.CHROME, buildChromeConf());
        ElementByXpath browser = new ElementByXpath(factory.getDriver());
        browser.get("https://www.selenium.dev/");
        browser.click("text->Documentation");
        browser.clickText("Blog");
        browser.click(Xpath.attribute("data-toggle", "dropdown").build(), "1");
        browser.click(1800, 30);
        browser.input("@class->DocSearch-Input", "selenium-smart");
        Helper.sleep(3000);
        browser.getLogs().forEach(System.out::println);
        factory.getDevTools().register("Network.requestWillBeSent", RequestWillBeSent.class).getData().forEach((k, v)-> System.out.println(k+": "+v));
        browser.closeBrowser();
    }


    private static ChromeConfiguration buildChromeConf() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox", "--disable-extensions", "--disable-dev-shm-usage");
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
                .duration(Duration.ofSeconds(60))
                .experimentalOption(eOptions)
                .downloadDirectory(System.getProperty("user.dir") + File.separator + "target" + File.separator + "downloads")
                .maximizeWindow(true)
                .disableGpu(true)
                .disableExtensions(true)
                .disableDevShmUsage(true)
                .enablePerformanceLog(true)
                .pageLoadStrategy(PageLoadStrategy.NONE)
                .build();
    }

}
