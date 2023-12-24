
# selenium-smart #

selenium-smart is an encapsulation of selenium
based on Java8 and selenium(4.9.0)

## Installation

#### Maven
````xml
<dependency>
  <groupId>io.github.jiawade</groupId>
  <artifactId>selenium-smart</artifactId>
  <version>4.9.2</version>
</dependency>
````

#### Gradle
````gradle
compile 'io.github.jiawade:selenium-smart:4.9.2'
````

#### Included Dependencies
* selenium-java 4.9.0

##Xpath simple usage

###by text
````
text->"The name of the text to be operated on"
example: text->maven, text->cucumber
````

###by attribute
````
@key->value
example: @class->box, @data-type->recommend
or use the xpath builder: Xpath.attribute("data-toggle", "dropdown").build()
````

###by coordinate
````
The coordinate point where the element is located, 
the first parameter indicates the horizontal coordinate point, 
and the second parameter indicates the vertical coordinate point
example: (240,130);
````

## Usage Example

````java
public class Examples {

    public static void main(String[] args) {
        SeleniumBrowser factory = new SeleniumBrowser().setUp(BrowserType.CHROME, buildChromeConf());
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
                .height(1080)
                .duration(Duration.ofSeconds(60))
                .build();
    }
}
````

## Submitting Issues
For any issues or requests, please submit [here](https://github.com/jiawade/selenium-smart/issues)
