package tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import config.demowebshop.App;
import config.selenoid.Credentials;
import helpers.Attach;
import io.qameta.allure.selenide.AllureSelenide;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.remote.DesiredCapabilities;

import static com.codeborne.selenide.Selenide.closeWebDriver;
import static helpers.CustomAllureListener.withCustomTemplates;
import static java.lang.String.format;

public class TestBase {
    @BeforeAll
    static void setUp() {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide());

        RestAssured.baseURI = App.config.apiUrl();
        Configuration.baseUrl = App.config.webUrl();
        //RestAssured.filters(new AllureRestAssured());
        RestAssured.filters(withCustomTemplates());

        Configuration.browser = System.getProperty("browser", "chrome");
        Configuration.browserVersion = System.getProperty("browserVersion", "91");
        Configuration.browserSize = System.getProperty("browserSize", "1280x1080"); //960x748 1024x768

        String remoteUrl = System.getProperty("remoteUrl"); //Получаем значение из параметров Jenkins
        String user = Credentials.config.user();
        String password = Credentials.config.password();
        Configuration.remote = format("https://%s:%s@%s", user, password, remoteUrl);


        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("enableVNC", true);
        capabilities.setCapability("enableVideo", true);
        Configuration.browserCapabilities = capabilities;
    }


    @AfterEach
    void addAttachments() {
        Attach.screenshotAs("Last screenshot");
        Attach.pageSourceText();
        Attach.PageSourceHtml();
        Attach.browserConsoleLogs();
        Attach.addVideo();
        closeWebDriver();
    }


}
