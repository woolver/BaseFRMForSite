package ru.admi.core;

import com.codeborne.selenide.Screenshots;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import io.qameta.allure.model.Status;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.reporters.ExitCodeListener;
import ru.admi.helpers.GeneratorTool;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.screenshot;

/**
 * Класс переделывает один метод onTestFailure
 * Приаттачивает в отчёт аллюра скриншот вызванный Filure
 * декарирует ошибки тест нг в случае если они связаны с появлением ошибок в отдельном попапе Siebel
 */
public class AllureDecorator extends ExitCodeListener {
    private final Logger log = LoggerFactory.getLogger(getClass());

    public static boolean captureSuccessfulTests;

    @Override
    public void onTestStart(ITestResult result) {
        super.onTestStart(result);
        String className = result.getMethod().getTestClass().getName();
        String methodName = result.getMethod().getMethodName();
        Screenshots.startContext(className, methodName);
    }

    /**
     * Вызывается при любой ошибке в тесте
     * аттачит скриншот ошибки в тест
     * декарирует ошибки тест нг в случае если они связаны с появлением ошибок в отдельном попапе Siebel
     * выходит из сибл чтобы независала сессия пользователя
     * @param result
     */
    @Override
    public void onTestFailure(ITestResult result) {
        Throwable throwable = result.getThrowable();
        String newMessage = "";
        String curMessage = throwable.getMessage();
        if ($$(By.cssSelector("div[aria-describedby='_sweview_popup']")).size() > 0) //TODO поменять локатор на локатор стандартного сообщения об ошибке на сайте
            if($(By.cssSelector("div[aria-describedby='_sweview_popup']")).getCssValue("display").equalsIgnoreCase("block")) {
                Allure.step("Определено появление окна с ошибкой", Status.FAILED);
                newMessage = "Определено появление окна с ошибкой:\n" + $(By.cssSelector("div[id='_sweview_popup']")).getText() + "\n\n";
                try {
                    FieldUtils.writeField(throwable, "detailMessage", newMessage + curMessage, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
        super.onTestFailure(result);
        try {
            screenShotForAllure();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        super.onTestSuccess(result);
        if (captureSuccessfulTests) {
            try {
                screenShotForAllure();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Метод делает скриншот и приаттачивает его в конце тела теста
     * @return
     * @throws IOException
     */
    @Attachment(value = "current screenshot", type = "image/png")
    public static byte[] screenShotForAllure() throws IOException {
        BufferedImage bImage = ImageIO.read(new File(screenshot("Error_"+GeneratorTool.randInt(10000000,90000000))));
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(bImage, "png", bos );
        byte[] screenshot = bos.toByteArray();
        return screenshot;
    }
}