package ru.admi.helpers;

import org.apache.log4j.Logger;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;

/**
 * Класс содержит методы для работы с базой данных. выполнеиние запросов, простоение sql из файла *.sql.
 */
public class DBTool {

    private static final String JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver";
    private static String schemaDB = ""; //TODO заполнить схемой бд
    private static String userDB = ""; //TODO заполнить имем юзера бд
    private static String passDB = ""; //TODO заполнить паролем юзера БД
    private static String hostDBPreprod = ""; //TODO заполнить хостом БД препрода
    private static String hostDBTest = ""; //TODO заполнить хостом БДтеста
    private static String sidDB = ""; //TODO заполнить sid-ом БД
    private static String hostDB;
    private static final Logger log = Logger.getLogger(DBTool.class);


    private Connection connection = null;

    /**
     * Базовый метод для выполнения запросов в БД
     *
     * @param baseURL   текущий урл
     * @param sqlString sql стринга запроса
     * @return Возвращает объект RS (коннешн не закрывает! Закрывайте в методе сами!
     */
    private ResultSet queryDB(String baseURL, String sqlString) {
        ResultSet rs = null;
        try {
            if (baseURL.contains("test3")) hostDB = hostDBTest;
            else
                hostDB = hostDBPreprod;
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            return null;
        }
        try {
            if (connection != null) connection.close();
            connection = DriverManager.getConnection(
                    "jdbc:oracle:thin:@" + hostDB + ":1521:" + sidDB, userDB, passDB);
        } catch (SQLException e) {
            log.error("Ошибка подключения к БД ", e);
            return null;
        }
        try {
            connection.setSchema(schemaDB);
            Statement stm = connection.createStatement();
            rs = stm.executeQuery(sqlString);
        } catch (SQLException e) {
            log.error("Ошибка выполнения запроса", e);
        }
        return rs;
    }

    /**
     * Метод выполняет апдейт и коммит при успешном выполнении запроса
     *
     * @param baseURL   текущий урл
     * @param sqlString sql стринга содержащая код апдейта
     */
    private void updateDB(String baseURL, String sqlString) {
        try {
            if (baseURL.contains("test3")) hostDB = hostDBTest;
            else
                hostDB = hostDBPreprod;
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            log.error("Ошибка Драйвера", e);
            return;
        }
        try {
            if (connection != null) connection.close();
            connection = DriverManager.getConnection(
                    "jdbc:oracle:thin:@" + hostDB + ":1521:" + sidDB, userDB, passDB);
        } catch (SQLException e) {
            log.error("Ошибка подключения к БД ", e);
            return;
        }
        try {
            connection.setAutoCommit(false);
            connection.setSchema(schemaDB);
            Statement stm = connection.createStatement();
            stm.executeUpdate(sqlString);
            connection.commit();
            connection.close();
        } catch (SQLException e) {
            try {
                connection.rollback();
                connection.close();
            } catch (SQLException e1) {
                log.error("Ошибка rollback ", e1);
            }
            log.error("Ошибка апдейта ", e);
        }
    }

    /**
     * Создаёт объект документ из строки, парсит XML
     *
     * @param xmlString строка с xml документом
     * @return распарсенный документ
     */
    private org.w3c.dom.Document docGenerator(String xmlString) {
        DocumentBuilder builder = null;
        org.w3c.dom.Document doc = null;
        DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
        try {
            builder = f.newDocumentBuilder();
        } catch (ParserConfigurationException e1) {
            e1.printStackTrace();
        }
        f.setValidating(false);

        try {
            doc = builder.parse(new InputSource(new ByteArrayInputStream(xmlString.getBytes("UTF-16"))));
        } catch (SAXException e) {
            //e.printStackTrace();
            log.error(e.getMessage());
        } catch (IOException e) {
            //e.printStackTrace();
            log.error(e.getMessage());
        }
        return doc;
    }

    /**
     * Метод парсит файл *.sql и возвращает sql стрингу содержащую данные из файла
     *
     * @param fileName имя файла в папке ресурсов
     * @return Стринга содержащая распарсенный sql запрос из файла
     */
    private String loadSQLfromFile(String fileName) {
        byte[] encoded = new byte[0];
        try {
            encoded = Files.readAllBytes(Paths.get(System.getProperty("ResourceFolder") + "\\" + fileName));
        } catch (IOException e) {
            log.error("Ошибка чтения SQL" + e.getMessage());
        }
        String returnStr = new String(encoded, StandardCharsets.UTF_8);
        return returnStr;
    }
}

