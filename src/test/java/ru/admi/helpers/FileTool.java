package ru.admi.helpers;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Класс работы с файлом все методы работают только с локальными файлами
 */
public class FileTool {
    private String fPath;
    private static final Logger log = Logger.getLogger(FileTool.class);

    /**
     * Конструктор класса принимает значение из имени файла из папки ресурсов
     *
     * @param fileName
     */
    public FileTool(String fileName) {
        this.fPath = System.getProperty("user.dir") + "\\target\\test-classes\\" + fileName;
    }

    /**
     * Метод создаёт или дописывает в существующий файл содержимое массива строк, после каждого элемента массива вставляется конец строки
     *
     * @param lst - массив содержащий строки
     */
    public void writeCSV(List<String> lst) {
        File file = new File(fPath);
        try {
            Writer wr = new BufferedWriter(new FileWriter(file, true));
            for (int i = 0; i < lst.size(); i++) {
                wr.append(lst.get(i));
                ((BufferedWriter) wr).newLine();
            }
            wr.flush();
            wr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Метод читает построчно файл csv в массив List<String> и возвращает этот массив
     */
    public List<String> readCSV() {
        List<String> lst = new ArrayList<>();
        File file = new File(fPath);
        String curLine = null;
        try {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(file), "UTF8"));
            while ((curLine = in.readLine()) != null) {
                lst.add(curLine);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lst;
    }

    /**
     * Парсит xlsx и возвращает List содержащий строки из файла, столбцы в строке разделёны ";" между собой
     *
     * @return Возвращает лист со строками из файла либо null при ошибке
     */
    public List<String> readXLSX() {
        List<String> testDataList = new ArrayList<>();
        String curFileStr = "";
        Boolean flgIsFirstRow = true;
        try {
            Workbook workbook = new XSSFWorkbook(new FileInputStream(this.fPath));
            Sheet datatypeSheet = workbook.getSheetAt(0);
            Iterator<Row> iterator = datatypeSheet.iterator();
            DataFormatter objDefaultFormat = new DataFormatter();
            FormulaEvaluator objFormulaEvaluator = new XSSFFormulaEvaluator((XSSFWorkbook) workbook);
            while (iterator.hasNext()) {
                Row currentRow = iterator.next();
                //Iterator<Cell> cellIterator = currentRow.iterator();

                int lastColumn = currentRow.getLastCellNum();
                for (int cn = 0; cn < lastColumn; cn++) {
                    Cell currentCell = currentRow.getCell(cn, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    if (currentCell == null) {
                        curFileStr += ";";
                    } else {
                        objFormulaEvaluator.evaluate(currentCell);
                        String cellValueStr = objDefaultFormat.formatCellValue(currentCell, objFormulaEvaluator);
                        curFileStr = curFileStr + cellValueStr + ";";
                    }
                }
                if (!flgIsFirstRow) {
                    testDataList.add(curFileStr);
                }
                curFileStr = "";
                if (flgIsFirstRow) flgIsFirstRow = false;
            }
            return testDataList;

        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return null;
    }
}

