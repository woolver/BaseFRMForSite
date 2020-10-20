package ru.admi.helpers;

import org.apache.log4j.Logger;
import org.testng.Assert;

import javax.xml.soap.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static javax.xml.soap.SOAPConstants.SOAP_1_1_PROTOCOL;

/**
 * Класс позволяет создавать соап сообщения из xml файлов и отправлять их
 */
public class SOAPTool {

    private static final Logger log = Logger.getLogger(SOAPTool.class);
    private DBTool dbTool = new DBTool();
    private static int takenCompanies = 0;

    /**
     * Возвращает соап сообщение созданное из тега файла
     *
     * @param fileName   имя файла в папке ресурсов
     * @param soapAction SoapAction для метода сервиса
     * @return Построенное соап сообщение
     */
    public SOAPMessage buildXML(String fileName, String soapAction) {
        try {
            byte[] encoded = Files.readAllBytes(Paths.get(System.getProperty("resource.folder") + "\\" + fileName));
            String soapXml = new String(encoded, StandardCharsets.UTF_8);
            MessageFactory factory = MessageFactory.newInstance(SOAP_1_1_PROTOCOL);
            SOAPMessage message = factory.createMessage(new MimeHeaders(), new ByteArrayInputStream(encoded));
            MimeHeaders mimeHeader = message.getMimeHeaders();
            mimeHeader.setHeader("SOAPAction", "\"" + soapAction + "\"");
            message.saveChanges();
            return message;
        } catch (SOAPException e) {
            log.error(e);
        } catch (IOException e) {
            log.error(e);
        }
        return null;
    }

    /**
     * Базовый метод отправки соап сообщения
     *
     * @param message  соап сообщение
     * @param endpoint ендпоинт урл сервиса
     * @return возвращает соап ссобщение ответа
     * @throws SOAPException
     * @throws IOException
     */
    private SOAPMessage sentSOAP(SOAPMessage message, java.net.URL endpoint) throws SOAPException, IOException {
        SOAPMessage response = null;
        SSLTool.disableCertificateValidation();
        SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
        log.info("Протокол " + endpoint.getProtocol() + " адрес " + endpoint.toString() + "\n");
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        message.writeTo(bout);
        log.info("SOAP BEFORE SENDING" + bout.toString("UTF-8"));
        bout.reset();
        SOAPConnection connection = soapConnectionFactory.createConnection();

        try {
            response = connection.call(message, endpoint);
            response.writeTo(bout);
            log.info("SOAP RESPONSE:" + bout.toString("UTF-8"));
        } catch (SOAPException e) {
            log.error("Ошибка SOAP ", e);
            return null;
        } finally {
            connection.close();
        }
        bout.close();
        return response;
    }

    /**
     * Метод меняет местами месяц и день, форматирует строку содержащию дату в формате dd.mm.yyyy или mm.dd.yyyy,
     *
     * @param date дата в форматах dd.mm.yyyy, mm.dd.yyyy,
     */
    private String formatDateChangeDaysAndMonths(String date) {
        String formatedDate = date.substring(3, 6);
        formatedDate += date.substring(0, 2);
        formatedDate += date.substring(5, 10);
        return formatedDate;
    }

    /**
     * Берёт первый элемент подходящий по тегу и меняет его значение
     *
     * @param msg      Соап сообщение
     * @param tagName  Имя тега
     * @param newValue Новое значение
     */
    public void changeTagTextValue(SOAPMessage msg, String tagName, String newValue) {
        try {
            msg.getSOAPBody().getElementsByTagNameNS("*", tagName).item(0).setTextContent(newValue);
            msg.saveChanges();
        } catch (Exception e) {
            log.error("Ошибка при замене тега! Пробуем заполнить тег '" + tagName + "' значением '" + newValue + "' \nэксепшен:" + e);
            Assert.fail("Ошибка при замене тега! Пробуем заполнить тег '" + tagName + "' значением '" + newValue + "'", e);
        }
    }

    /**
     * Метод возвращает текстовое значение первого найденного тега с любым namespace и указанным именем
     *
     * @param msg     Соап сообщение в котором выполняется поиск
     * @param TagName имя тега для поиска
     * @return стринга текстовое значение тега либо ""
     * @throws SOAPException
     */
    private String getTagTextValue(SOAPMessage msg, String TagName) {

        String TagValue = "";
        try {
            TagValue = msg.getSOAPBody().getElementsByTagNameNS("*", TagName).item(0).getTextContent();
        } catch (Exception e) {
            log.error("Ошибка при попытке получить значение тега: '" + TagName + "' из соап сообщения\n", e);
            Assert.fail("Ошибка при попытке получить значение тега: '" + TagName + "' из соап сообщения\n", e);
        }
        return TagValue;
    }

}

