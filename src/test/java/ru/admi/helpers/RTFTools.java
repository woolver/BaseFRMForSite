package ru.admi.helpers;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.rtf.RTFEditorKit;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс для раболты с rtf файлами содержит так же методы работы с файлом по http
 */
public class RTFTools {

    /**
     * Метод принимает строку содержащую полный путь к rtf - файлу, парсит его и выдаёт стрингу с содержимым. Включая перносы строки '\n'
     * @param directUrlToFile Прямой путь к файлу "http://abc.com/text.rtf"
     * @return Строка содержащая текст файла
     * @throws IOException
     * @throws BadLocationException
     */
    public static String getRTFAsStringFromURL(String directUrlToFile) throws IOException, BadLocationException {
        //URL url = new URL("directUrlToFile");
        InputStream in = null;
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet httpget = new HttpGet(directUrlToFile);
        HttpResponse response = httpClient.execute(httpget);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            long len = entity.getContentLength();
            in = entity.getContent();
        }
        BufferedReader bfr = new BufferedReader(new InputStreamReader(in));
        RTFEditorKit kit = new RTFEditorKit();
        Document doc = kit.createDefaultDocument();
        kit.read(bfr,doc,0);
        String plainText = "";
        plainText = doc.getText(0, doc.getLength());
        //String plainText = new String(doc.getText(0, doc.getLength()).getBytes(StandardCharsets.ISO_8859_1), "Windows-1251");
        bfr.close();
        in.close();
        return plainText;
    }


    /**
     * Метод принимает строку содержащую полный путь к rtf - файлу, парсит его и выдаёт стрингу с содержимым. Включая перносы строки '\n'
     * @param directPathToFile Прямой путь к файлу "C:\\text.rtf"
     * @return Строка содержащая текст файла
     * @throws IOException
     * @throws BadLocationException
     */
    public static String getRTFAsString(String directPathToFile) throws IOException, BadLocationException {
        File rtfFiler = new File(directPathToFile);
        return getRTFAsString(rtfFiler);
    }

    /**
     * Метод принимает rtf файл, парсит его и выдаёт стрингу с содержимым. Включая перносы строки '\n'
     * @param fileName файл
     * @return Строка содержащая текст файла
     * @throws IOException
     * @throws BadLocationException
     */
    public static String getRTFAsString(File fileName) throws IOException, BadLocationException {
        FileInputStream stream = new FileInputStream(fileName);
        BufferedReader bfr = new BufferedReader(new InputStreamReader(stream));
        RTFEditorKit kit = new RTFEditorKit();
        Document doc = kit.createDefaultDocument();
        kit.read(bfr,doc,0);
        String plainText = "";
        plainText = doc.getText(0, doc.getLength());
        //String plainText = new String(doc.getText(0, doc.getLength()).getBytes(StandardCharsets.ISO_8859_1), "Windows-1251");
        bfr.close();
        stream.close();
        return plainText;
    }

    /**
     * Метод принимает строку содержащую полный путь к rtf - файлу, парсит его и выдаёт массив List<String> с содержимым. Включая пустые строки.
     * @param directPathToFile Прямой путь к файлу "C:\\text.rtf"
     * @return Массив строк содержащий текст файла построчно
     * @throws IOException
     * @throws BadLocationException
     */
    public static List<String> getRTFAsList(String directPathToFile) throws IOException, BadLocationException {
        File rtfFiler = new File(directPathToFile);
        return getRTFAsList(rtfFiler);
    }

    /**
     * Метод принимает rtf файл, парсит его и выдаёт массив List<String> с содержимым. Включая пустые строки.
     * @param fileName файл
     * @return Массив строк содержащий текст файла построчно
     * @throws IOException
     * @throws BadLocationException
     */
    public static List<String> getRTFAsList(File fileName) throws IOException, BadLocationException {
        FileInputStream stream = new FileInputStream(fileName);
        BufferedReader bfr = new BufferedReader(new InputStreamReader(stream));
        RTFEditorKit kit = new RTFEditorKit();
        Document doc = kit.createDefaultDocument();
        kit.read(bfr,doc,0);
        String plainText = "";
        plainText = doc.getText(0, doc.getLength());
        //plainText = new String(doc.getText(0, doc.getLength()).getBytes(StandardCharsets.US_ASCII), "Windows-1251");
        bfr.close();
        stream.close();
        StringReader reader = new StringReader(plainText);
        bfr = new BufferedReader(reader);
        String curLine;
        List<String> lst = new ArrayList<>();
        while ((curLine = bfr.readLine()) != null) {
            lst.add(curLine);
        }
        bfr.close();
        reader.close();
        return lst;
    }
}
