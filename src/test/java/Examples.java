import com.google.common.collect.Lists;
import io.smart.browser.configuration.impls.ChromeConfiguration;
import io.smart.browser.factory.impls.SeleniumBrowserFactory;
import io.smart.element.impls.ElementByXpath;
import io.smart.enums.BrowserType;
import io.smart.utils.tools.Tools;
import io.smart.utils.xpath.Xpath;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;
import java.util.Map;
import java.util.stream.Collectors;

public class Examples {

    public static void main(String[] args) {
        SeleniumBrowserFactory factory = new SeleniumBrowserFactory().setUp(BrowserType.CHROME, buildChromeConf());
        ElementByXpath browser = new ElementByXpath(factory.getDriver());
        browser.get("https://www.selenium.dev/");
        browser.click("text->Documentation");
        browser.clickText("Blog");
        browser.click(Xpath.attribute("data-toggle", "dropdown").build(),"1");
        browser.click(1800,30);
        browser.input("@class->DocSearch-Input", "selenium-smart");
        Tools.sleep(3000);
        browser.closeBrowser();
    }


    private static ChromeConfiguration buildChromeConf() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox", "--disable-extensions", "--disable-dev-shm-usage");
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        Map<String, Boolean> infoBar = Lists.newArrayList("profile.password_manager_enabled", "credentials_enable_service").stream().collect(Collectors.toMap(i -> i, i -> false));
        options.setExperimentalOption("prefs", infoBar);
        return ChromeConfiguration.builder()
                .chromeOptions(options)
                .width(1920)
                .hight(1080)
                .duration(Duration.ofSeconds(60))
                .build();
    }

}
