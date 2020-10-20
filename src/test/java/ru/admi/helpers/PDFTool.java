package ru.admi.helpers;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;
import io.qameta.allure.Step;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;
import org.testng.Assert;

import javax.swing.text.BadLocationException;
import java.io.*;

/**
 * Класс для работы с pdf файлами содержит так же методы работы с файлом по http
 */
public class PDFTool {
    public Logger log = Logger.getLogger(this.getClass());// лог
    /**
     * Метод принимает pdf файл, парсит указанную страницу и выдаёт стрингу с содержимым.
     *
     * @param fileName   файл
     * @param pageNumber номер страницы
     * @return Строка содержащая текст файла
     * @throws IOException
     */
    @Step("Чтение файла и возврат в строке указанной страницы")
    public String readPdfAndReturnString(File fileName, int pageNumber) throws IOException {

        FileInputStream stream = new FileInputStream(fileName);
        PdfReader reader = new PdfReader(stream);

        TextExtractionStrategy strategy = new SimpleTextExtractionStrategy();
        String text = PdfTextExtractor.getTextFromPage(reader, pageNumber, strategy);

        reader.close();
        return text;
    }

    /**
     * Метод принимает строку содержащую полный путь к pdf - файлу, парсит его и выдаёт стрингу с содержимым. Включая перносы строки '\n'
     * @param directUrlToFile Прямой путь к файлу "http://abc.com/text.rtf"
     * @return Строка содержащая текст файла
     * @throws IOException
     * @throws BadLocationException
     */
    public String readPdfAndReturnStringFromURL(String directUrlToFile, int pageNumber) {
        InputStream in = null;
        try {
            SSLTool.disableCertificateValidation();
            log.info("File NAME: " + directUrlToFile);
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpGet httpget = new HttpGet(directUrlToFile);
            HttpResponse response = httpClient.execute(httpget);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                long len = entity.getContentLength();
                in = entity.getContent();
            }
            PdfReader reader = new PdfReader(in);
            TextExtractionStrategy strategy = new SimpleTextExtractionStrategy();
            String text = PdfTextExtractor.getTextFromPage(reader, pageNumber, strategy);
            reader.close();
            log.info("Содержимое файла:\n" + text + "\n");
            return text;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            Assert.fail("Ошибка скачивания файла через http", e);
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail("Ошибка ввода вывода при чтении файла через http", e);
        }
        return null;
    }
}
