package ru.admi.pages.basePages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.apache.log4j.Logger;
import org.testng.Assert;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.codeborne.selenide.Selenide.*;

//TODO дополнить необходимыми методами по кликам на страницы, расширить базовые методы в зависимости от потребностей в будущем
/**
 * Базовый класс страницы добавляет общие методы для всех страниц
 */
public class BasePage {
    //Хедер
    //Пиктограмма мкб
    public SelenideElement imgHeaderMkb = $x("//header//img[@class='header__logo-img']");
    //элементы меню хедера первая строка без саб элементов в пункте 'Ещё', не включает Местоположение и банкоматы
    public List<SelenideElement> lstHeaderMenu = $$x("//div[@class='header__top']/ul[@class='header__list header__list_sections'][1]/li[contains(@class,'header__list-item')]/a | //li[contains(@class,'header__list-item')]/span");
    //Элементы меню хедера вторая строка
    public List<SelenideElement> lstHeaderSubMenu = $$x("//div[@class='header__bottom']//ul[@class='header__list active']/li[contains(@class,'header__list-item')]/a");
    //Кнопка поиска
    public SelenideElement btnSearch = $x("//div[@class='header__bottom']//button[@class='header__search-button js-header-search-button']");
    //Список элементов меню раскрывающийся по кнопке Еще
    public List<SelenideElement> lstHeaderMenuMore = $$x("//header//li[@class='header__list-item  header__list-item_dropdown']//li[not(contains(@class, 'header__list-item header__list-item_show-in-tablet'))]/a");
    //Поле-линк выбранный город
    public SelenideElement fldCity=$x("//header//div[@class='header__city']/a");
    //Линк банкоматы
    public SelenideElement linkBankomats = $x("//header//div[@class='header__city']/../a[@class='header__link']");
    //Кнопка войти
    public SelenideElement btnEnter = $x("//header//div[@class='header__bottom-right']/button[contains(text(), 'Войти')]");
    //Список линков городов появляющийся при клике на fldCity
    public List<SelenideElement> lstCitysForSelect = $$x("//div[@class='cities__content']//li");

    //Футер
    //Пиктограмма поиска
    public SelenideElement footerSearchImg = $x("//footer//div[@class='footer__search']//img");
    //Поле поиска
    public SelenideElement footerSearhFld = $x("//footer//div[@class='footer__search']//input");
    //Кнопака выбора языка
    public SelenideElement footerBtnLang = $x("//footer//div[@class='lang footer__lang']/label");
    //Список значений языка
    public List<SelenideElement> lstlang = $$x("//footer//div[@class='lang footer__lang']//li/a");
    //Блок линков Частным лицам
    public List<SelenideElement> lstPersonMenuItems = $$x("//footer//div[@class='footer__body']//div[@class='footer__nav-col'][1]//li/a");
    //Блок линков Бинему
    public List<SelenideElement> lstBussinessMenuItems = $$x("//footer//div[@class='footer__body']//div[@class='footer__nav-col'][2]//li/a");
    //Блок линков О банке
    public List<SelenideElement> lstAboutBankMenuItems = $$x("//footer//div[@class='footer__body']//div[@class='footer__nav-col'][3]//li/a");
    //Блок контактов и соц линков
    public List<SelenideElement> lstContactAndSocial = $$x("//footer//div[@class='footer__body']//div[@class='footer__contacts']//li/a");
    //Блок линков сервисы
    public List<SelenideElement> lstServiceLinks = $$x("//footer//div[@class='footer__bottom']/div[contains(@class, 'footer__services')]//a");
    //Блок линков Приватность
    public List<SelenideElement> lstPrivaticy = $$x("//footer//div[@class='footer__bottom']/div[contains(@class, 'footer__privacy')]//a");
    //Поле с текстом копирайта
    public SelenideElement fldCopyrightingTextArea = $x("//footer//p[@Class='footer__copyright-text']");

    //логгер
    public Logger log = Logger.getLogger(this.getClass());// лог

    //TODO заменить return this на return new *Класс страницы* которую мы будем использовать
    @Step("Клик по ссылке 'Частным лицам' в хедере")
    public BasePage clickOnPersonalLink() {
        for(SelenideElement elem : lstHeaderMenu) {
            if (elem.getText().equalsIgnoreCase("Частным лицам")) {
                elem.click();
                return this;
            }
        }
        Assert.fail("Ошибка, ссылка 'Частным лицам' не найдена в списке ссылок хедера");
        return null;
    }

    //TODO заменить return this на return new *Класс страницы* которую мы будем использовать
    @Step("Клик по ссылке 'ещё>Финансовым организациям'")
    public BasePage clickOnFinOrgLink() {
        //наведение мышки на элемент Ещё
        lstHeaderMenu.get(lstHeaderMenu.size()-1).hover();

        for(SelenideElement elem : lstHeaderMenuMore) {
            if(elem.getText().equalsIgnoreCase("Финансовым организациям"))
            {
                elem.click();
                return this;
            }
        }
        Assert.fail("Ошибка, ссылка 'Ещё>Финансовым организациям' не найдена в списке ссылок хедера");
        return null;
    }


    /**
     * Метод для клика по уже раскрытому списку значений LOV
     *
     * @param LOV     локатор с списком значений
     * @param nameval отображаемое значеине в списке выбора
     */
    @Step("Выбор значения из уже раскрытого списка значений")
    public BasePage selectFromLOVbyName(List<SelenideElement> LOV, String nameval) {
        $$(LOV).find(Condition.text(nameval)).click();
        return this;
    }

    /**
     * @param btnDropDown Кнопка по которой раскрывается список
     * @param LOV         Массив с листом веб элементов
     * @param nameval     Текстовое отображаемое значение в списке выбора
     */
    @Step("Клик по кнопке раскрывающегося списка и выбор элемента по имени")
    public BasePage selectFromLOVbyName(SelenideElement btnDropDown, List<SelenideElement> LOV, String nameval) {
        btnDropDown.click();
        $$(LOV).find(Condition.text(nameval)).click();
        return this;
    }

    /**
     * Ожидание в секундах
     *
     * @param seconds длительность ожидания в секундах
     */
    protected void sleep(int seconds) {
        sleep(TimeUnit.SECONDS, seconds);
    }

    /**
     * Ожидание в заданных едицинах измерения
     *
     * @param measure Единицы измерения времени
     * @param time    Ожидание в единицах
     */
    protected void sleep(TimeUnit measure, int time) {
        log.debug("Ожидание - " + measure + " " + time);
        try {
            measure.sleep(time);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }
}
