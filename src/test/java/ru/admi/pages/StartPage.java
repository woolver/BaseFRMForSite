package ru.admi.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.testng.Assert;
import ru.admi.pages.basePages.BasePage;
import java.util.List;
import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;


public class StartPage extends BasePage {
    private List<SelenideElement> lstSliders = $$x("//div[contains(@class, 'swiper-slide')][position()<6 and position() >1]//div[@class='main-slider__slide-content']");
    private SelenideElement btnNextSlide = $x("//div[@class='main-slider js-main-slider']//div[@aria-label='Previous slide']");
    private SelenideElement btnPrevSlide = $x("//div[@class='main-slider js-main-slider']//div[@aria-label='Next slide']");
    private List<SelenideElement> sliderPinsButtons = $$x("//span[contains(@class, 'swiper-pagination-bullet')]");
    private List<SelenideElement> productCards = $$x("//div[contains(@class, 'card featured-products__card')]");
    private List<SelenideElement> productCatigories = $$x("//div[@class='featured-products']//li[@class='tabs__item']");

    //TODO описать ещё три блока и методы работы с ними, а именно калькуляторы вклады кредиты и ипотека,
    //TODO узнать у разработчиков JSScript которым можно двигать слайдеры в калькулируемых полях
    //TODO описать блок с рекламой мобильного приложения

    @Step("Нажатие кнопки следующий слайд")
    public StartPage clickOnBtnNextSlide() {
        btnNextSlide.click();
        Selenide.sleep(500);
        return this;
    }

    @Step("Нажатие кнопки предыдущий слайд")
    public StartPage clickOnBtnPrevSlide() {
        btnPrevSlide.click();
        Selenide.sleep(500); //время анимации
        return this;
    }

    /**
     *
     * @param numberOfPin парамтер номер кнпоки слева на право принимает значения 1-4 включительно
     * @return
     */
    @Step("Нажатие на радиобаттон выбора слайда")
    public StartPage clickOnSliderPinButton(int numberOfPin) {
        sliderPinsButtons.get(numberOfPin-1).click();
        lstSliders.get(numberOfPin-1).waitUntil(Condition.visible,1000);
        return this;
    }

    @Step("Проверка текстового содержимого слайда под оперделённым номером (1-4)")
    public StartPage checkSliderText(int sliderNumber, String sliderCaption, String sliderTitle, String sliderDescription, String sliderLink, String sliderImageLink) {
         clickOnSliderPinButton(sliderNumber);
         Assert.assertEquals(lstSliders.get(sliderNumber-1).$x("./div").getText(), sliderCaption, "Ошибка при проверке названия слайда");
         Assert.assertEquals(lstSliders.get(sliderNumber-1).$x("./h2").getText(), sliderTitle, "Ошибка при проверке заголовка слайда");
         Assert.assertEquals(lstSliders.get(sliderNumber-1).$x("./p").getText(), sliderDescription, "Ошибка при проверке описания слайда");
         Assert.assertEquals(lstSliders.get(sliderNumber-1).$x("./a").getAttribute("href"), sliderLink, "Ошибка при проверке урла на кнопке подробнее слайда");
         Assert.assertEquals(lstSliders.get(sliderNumber-1).$x("..//img[@class='main-slider__image']").getAttribute("src"), sliderImageLink, "Ошибка при проверке урла картинки слайда");
         return this;
    }

    /**
     *
     * @param caption позаголовок категории продукта
     * @param titleText заголовок продукта
     * @param titleLink ссылка заголовка продукта
     * @param featureList список условий/фич по продукту
     * @param buttonText в случае отсутствия передавать null
     * @param buttonLink в случае отсутствия передавать null
     * @return
     */
    @Step("Проверка наличия карточки продукта с заданным текстом")
    public StartPage checkProductCard(String caption, String titleText, String titleLink, List<String> featureList, String imageLink, String buttonText, String buttonLink) {

        int numberCardInMass = searchProductCardByTitle(titleText);
        if(buttonText != null & buttonLink != null) {
                Assert.assertEquals(productCards.get(numberCardInMass).$x(".//a[2]").getText(),buttonText,"Ошибка проверки соответствия текста на кнопке под продуктом");
                Assert.assertEquals(productCards.get(numberCardInMass).$x(".//a[2]").getAttribute("href"),buttonLink,"Ошибка проверки соответствия линка на кнопке под продуктом");
            }
        Assert.assertEquals(productCards.get(numberCardInMass).$x(".//div[@class='featured-products__card-caption']").getText(),caption,"Ошибка проверки подзаголовока карточки продукта");
        Assert.assertEquals(productCards.get(numberCardInMass).$x(".//a[1]").getText(),titleText, "Ошибка проверки заголовка карточки продукта");
        Assert.assertEquals(productCards.get(numberCardInMass).$x(".//a[1]").getAttribute("href"), titleLink, "Ошибка проверки линка текста заголовка продукта");
        Assert.assertEquals(productCards.get(numberCardInMass).$x(".//img").getAttribute("src"), imageLink, "Ошибка проверки линка картинки продукта");
        checkProductFeatureList(numberCardInMass, featureList);
            return  this;
        }

    /**
     * Метод проверки в зависимости от наличия пунктов в фичах о продукте или простого текста
     * @param numberCardInMass номер карты продукта в массиве
     * @param pointForCheck массив значений для проверки
     */
    private void checkProductFeatureList(int numberCardInMass, List<String> pointForCheck) {
        if (productCards.get(numberCardInMass).$$x(".//li").size() > 0)
            Assert.assertEquals(productCards.get(numberCardInMass).$$x(".//li").texts(), pointForCheck, "Ошибка проверки списка фич/условий на продукте карты");
        else
        {
            Assert.assertEquals(productCards.get(numberCardInMass).$x(".//p").getText(), pointForCheck.get(0), "Ошибка проверки списка фич/условий на продукте карты");
        }
    }

    /**
     * вспомогательный метод поиска нужного номера картчоки продукта в массиве
     * @param title
     * @return
     */
    private int searchProductCardByTitle(String title) {
        for(int i = 0; i < productCards.size(); i++) {
            if(productCards.get(i).$x(".//a[1]").getText().equalsIgnoreCase(title)) return i;
        }
        Assert.fail("Не найдена карточка продукта с названием: " + title);
        return -1;
    }

    @Step("Клик по категории Рекомендованных продуктов")
    public StartPage clickOnCatigory(String catigoryName) {
        for(SelenideElement se : productCatigories)
            if(se.$x(".//span").getText().equalsIgnoreCase(catigoryName)) {
                se.click();
                return this;
            }
        Assert.fail("Ошибка при клике, отсутствует категория: " + catigoryName);
        return this;
    }
}
