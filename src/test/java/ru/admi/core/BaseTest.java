package ru.admi.core;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.apache.log4j.Logger;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.net.MalformedURLException;
import java.net.URI;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static com.codeborne.selenide.WebDriverRunner.setWebDriver;

@Listeners({ AllureDecorator.class})
public class BaseTest {
    public Logger log = Logger.getLogger(this.getClass());// лог
    public String url;

    @BeforeClass
    public void addAllureLogger() throws MalformedURLException {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide().screenshots(true).savePageSource(false));
        ConfigurationLoader.loadProperty();
        url = ConfigurationLoader.getProperty("selenide.baseUrl");
        DesiredCapabilities dc = null;
        switch (System.getProperty("selenide.browser"))
        {
            case "chrome" : {
                dc = DesiredCapabilities.chrome();
                break;
            }

            case "firefox" : {
                dc = DesiredCapabilities.firefox();
                break;
            }
        }
        dc.setCapability("acceptInsecureCerts", true);
        dc.setBrowserName(Configuration.browser);
        dc.setVersion(Configuration.browserVersion);
        dc.setCapability("enableVNC", true);
        dc.setCapability("enableVideo", false);
        dc.setCapability("screenResolution", "1960x1280x24");
        dc.setCapability(CapabilityType.TAKES_SCREENSHOT, true);
        //dc.setCapability("videoName", "selenoid_recording.mp4");
        //dc.setCapability("videoScreenSize", "1960x1280");
        dc.setPlatform(Platform.LINUX);
        dc.setJavascriptEnabled(true);
        RemoteWebDriver selenoidDriver = new RemoteWebDriver(URI.create(ConfigurationLoader.getProperty("selenide.remote")).toURL(), dc);
        setWebDriver(selenoidDriver);
        getWebDriver().manage().window().setSize(new Dimension(1920, 1080));
    }

    @BeforeTest
    public void prepareParameters() {
        log.info("Старт тестов в классе: " + this.getClass().getName() + "\n");
    }

    /**
     * Использовать при дебаге или запуске вручную
     * @param url урл куда перейти
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * возвращает текущий установленный урл
     * @return
     */
    public String getUrl() {
        return url;
    }

    @AfterTest
    public void afterEachTestDo() {
        log.info("Завершили тесты в классе: " + this.getClass().getName() + "\n");
        closeWebDriver();
    }

    @AfterMethod
    public void getResult(ITestResult result) {
        log.info("Завершили тестовый метод: " + result.getMethod().getMethodName() + " результат (1/2)=(успех/ошибка): " + result.getStatus());
    }


}
