package ru.admi.tests;

import io.qameta.allure.TmsLink;
import org.testng.annotations.Test;
import ru.admi.core.BaseTest;
import ru.admi.pages.*;

import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Selenide.open;

/**
 * Тест - пример, разделил глобавльно на три теста тест по хедеру по боди и по футеру
 * разделение - просто пример, реальные тесты писать в таком формате не надо
 * просто тест и последовательность действий в нём. При написании тестов в дальнейшем придерживаться логики:
 * Наменование тестовых классов: Test_*смысл теста на англ языке*_*номер из тест рейл* прим. Test_CheckClientData_12345 в ситуациях если один тест содержит несколько тестов из тест рейл перечислять их в названии прим: Test_CheckClientData_12345_2345
 * количество тестовых методов = кол-ву тест кейсов из Test Rail реализованных в текущем тесте = кол-ву тестовых методов с аннотацией @Test
 */

public class Test_Check_StartPage extends BaseTest {
    StartPage startPage = new StartPage();



    @Test(description = "Тест проверки хедера стартовой страницы")
    @TmsLink("") //TODO сюда вписать номер кейса из тестрейл номер теста должен быть без буквы "C" только цифры
    public void testCheckHeader() {
        open(url);
        startPage.clickOnPersonalLink()
                 .clickOnFinOrgLink();
        //TODO пара проверок для футера для примера
    }

    @Test(description = "Тест проверки тела стартовой страницы")
    @TmsLink("") //TODO сюда вписать номер кейса из тестрейл номер теста должен быть без буквы "C" только цифры
    public void testCheckBody() {
        open(url);
        //проверка кнопок влево вправо на слайдере слайдов
        startPage.clickOnBtnNextSlide()
                 .clickOnBtnNextSlide()
                 .clickOnBtnPrevSlide()
                 .clickOnBtnPrevSlide()
                 .clickOnBtnPrevSlide();
        //проверка текстов и линков каждого слайда
        startPage.checkSliderText(1,"Акция","Нам по пути","Поездки в метро в 2 раза дешевле при оплате картой Mastercard", url+"#",url+"assets/images/home/main-slider/slide1.png")
                 .checkSliderText(2,"Акция","Нам по пути","Поездки в метро в 2 раза дешевле при оплате картой Mastercard", url+"#",url+"assets/images/home/main-slider/slide1.png")
                 .checkSliderText(3,"Акция","Нам по пути","Поездки в метро в 2 раза дешевле при оплате картой Mastercard", url+"#",url+"assets/images/home/main-slider/slide1.png")
                 .checkSliderText(4,"Акция","Нам по пути","Поездки в метро в 2 раза дешевле при оплате картой Mastercard", url+"#",url+"assets/images/home/main-slider/slide1.png");
        //проверка карточек продуктов
        startPage.checkProductCard("Кредитная карта", "Можно больше", url + "#", new ArrayList<>(List.of("Бесплатные переводы до 100 000 \u20BD и бесплатное снятие наличных")), url+"assets/images/home/featured-products/product1.png","Узнать подробнее",url+"#")
                 .checkProductCard("Дебетовая карта", "Москарта Black", url + "#", new ArrayList<>(List.of("123 дня льготного периода", "до 800 000 кредитного лимита")), url+"assets/images/home/featured-products/product2.png",null,null)
                 .checkProductCard("Дебетовая карта", "Потребительский кредит", url + "#", new ArrayList<>(List.of("Сумма кредита до 3 млн рублей", "Срок кредита до 15 лет")), url+"assets/images/home/featured-products/product3.png",null,null)
                 .checkProductCard("Пенсионная карта", "Карта «Мудрость»", url + "#", new ArrayList<>(List.of("10% баллами в аптеках", "5,5% доход на остаток по карте")), url+"assets/images/home/featured-products/product4.png",null,null)
                 .checkProductCard("Акция", "Платежное кольцо в подарок!", url + "#", new ArrayList<>(List.of("Акция действует c 10.06.2019 до 20.03.2020")), url+"assets/images/home/featured-products/product5.png",null,null)
                 .clickOnCatigory("Пенсионерам")
                 .checkProductCard("Кредитная карта", "Можно больше", url + "#", new ArrayList<>(List.of("Бесплатные переводы до 100 000 \u20BD и бесплатное снятие наличных")), url+"assets/images/home/featured-products/product1.png","Узнать подробнее",url+"#")
                 .checkProductCard("Пенсионная карта", "Карта «Мудрость»", url + "#", new ArrayList<>(List.of("10% баллами в аптеках", "5,5% доход на остаток по карте")), url+"assets/images/home/featured-products/product4.png",null,null)
                 .clickOnCatigory("Студентам")
                 .checkProductCard("Дебетовая карта", "Москарта Black", url + "#", new ArrayList<>(List.of("123 дня льготного периода", "до 800 000 кредитного лимита")), url+"assets/images/home/featured-products/product2.png","Узнать подробнее",url+"#")
                 .checkProductCard("Дебетовая карта", "Потребительский кредит", url + "#", new ArrayList<>(List.of("Сумма кредита до 3 млн рублей", "Срок кредита до 15 лет")), url+"assets/images/home/featured-products/product3.png",null,null)
                 .clickOnCatigory("Молодой семье")
                 .checkProductCard("Акция", "Платежное кольцо в подарок!", url + "#", new ArrayList<>(List.of("Акция действует c 10.06.2019 до 20.03.2020")), url+"assets/images/home/featured-products/product5.png","Узнать подробнее",url+"#");

        //TODO для примера и понимания как реализовывать проверки
        //TODO Дописать остальные проверки что будут, тесты по калькуляторам я бы оформир отдельными тестами в отдельных классах
    }

    @Test(description = "Тест проверки футера стартовой страницы")
    @TmsLink("") //TODO сюда вписать номер кейса из тестрейл номер теста должен быть без буквы "C" только цифры
    public void testCheckFooter() {
        //TODO тут написать проверки что будут по футеру
    }


}
