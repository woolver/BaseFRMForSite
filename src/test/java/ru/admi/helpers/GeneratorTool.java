package ru.admi.helpers;

import org.apache.log4j.Logger;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;

/**
 * Класс содержит методы генерации различных рандомных и не очень данных
 */

public class GeneratorTool {

    static char[] alphabetCir = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ".toLowerCase().toCharArray();
    static char[] alphabetLat = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toLowerCase().toCharArray();
    static char[] alphabetSymb = "|;".toLowerCase().toCharArray();
    static char[] alphabetNum = "0123456789".toLowerCase().toCharArray();
    private static LocalDate dtNow = LocalDate.now();
    private static LocalDate BirthDate = LocalDate.of((dtNow.getYear() - 19), randInt(1, 12), randInt(1, 28));
    private static final Logger log = Logger.getLogger(GeneratorTool.class);
    private static Random random = new Random();

    /***
     *
     * @param start Стартовое число для генерации
     * @param end конечное число для генерации
     * @return Возвращает сгенерированное число
     */
    public static int randInt(int start, int end) {
        return start + (int) Math.round(Math.random() * (end - start));
    }


    /****
     *
     * @param max мин значение генерируемой строки
     * @param min макс значение генерируемой строки
     * @param type 0- генерация кирилицы, 1 - генерация латинцы, 2 - генерация спец символов, 3 - генерация чисел
     * @return Возвращает рандомносгенерённую строку указанной длинны и типа
     */
    public static String randString(int max, int min, int type) {
        char[] rev = null;
        String ReturnString = "";
        int lengthName = randInt(min, max);
        switch (type) {
            case 0:
                rev = alphabetCir;

                for (int i = 0; i < lengthName; i++)
                    ReturnString = ReturnString + rev[randInt(0, 32)];
                break;
            case 1:
                rev = alphabetLat;
                for (int i = 0; i < lengthName; i++)
                    ReturnString = ReturnString + rev[randInt(0, 25)];
                break;
            case 2:
                rev = alphabetSymb;
                for (int i = 0; i < lengthName; i++)
                    ReturnString = ReturnString + rev[randInt(0, 1)];
                break;
            case 3:
                rev = alphabetNum;
                for (int i = 0; i < lengthName; i++)
                    ReturnString = ReturnString + rev[randInt(0, 9)];
                break;
            default:
                log.error("Введён неверный тип для генерации");
                break;
        }
        return ReturnString;
    }

    /**
     * Метод возвращает дату рождения для клиента в формате dd.mm.yyyy
     *
     * @param maxBirthDate Максимальный возраст для генерации возраста клиента
     * @return стринга с датой
     */
    public static String getRandBirthDate(int maxBirthDate) {
        LocalDate dt;
        dt = LocalDate.of(randInt(dtNow.getYear() - maxBirthDate, dtNow.getYear() - 19), randInt(1, 12), randInt(1, 28));
        BirthDate = dt;

        return addZeroBeforeNum(dt.getDayOfMonth()) + "." + addZeroBeforeNum(dt.getMonthValue()) + "." + dt.getYear();
    }

    /**
     * метод возвращает актуальную дату выдачи для паспорта в зависимости от возраста по умолчанию или возраста который был запрошен ранее
     * в методе getRandomBirthDate
     *
     * @return Стринга с датой в формате dd.mm.yyyy
     */
    public static String getPassIssueDate() {
        LocalDate dt = LocalDate.now();

        if ((dtNow.getYear() - BirthDate.getYear() >= 14) && (dtNow.getYear() - BirthDate.getYear() < 20))
            dt = LocalDate.of(randInt(BirthDate.getYear() + 15, dtNow.getYear()), dtNow.getMonthValue(), dtNow.getDayOfMonth());
        else if ((dtNow.getYear() - BirthDate.getYear() >= 20) && (dtNow.getYear() - BirthDate.getYear() < 45))
            dt = LocalDate.of(randInt(BirthDate.getYear() + 21, dtNow.getYear()), dtNow.getMonthValue(), dtNow.getDayOfMonth());
        else if (dtNow.getYear() - BirthDate.getYear() >= 45)
            dt = LocalDate.of(randInt(BirthDate.getYear() + 46, dtNow.getYear()), dtNow.getMonthValue(), dtNow.getDayOfMonth());

        if (dt.isAfter(dtNow)) {
            dtNow = dtNow.minusDays(1);
            return addZeroBeforeNum(dtNow.getDayOfMonth()) + "." + addZeroBeforeNum(dtNow.getMonthValue()) + "." + dtNow.getYear();
        } else
            return addZeroBeforeNum(dt.getDayOfMonth()) + "." + addZeroBeforeNum(dt.getMonthValue()) + "." + dt.getYear();
    }

    /**
     * Возвращает случайную дату между первой и второй
     *
     * @param date1 первая дата
     * @param date2 втораяв дата
     * @return стринга с датой в формате dd.mm.yyyy
     */
    public static String randDateBetween(LocalDate date1, LocalDate date2) {
        LocalDate dt;
        if (date1.isAfter(date2)) {
            dt = LocalDate.of(randInt(date2.getYear(), date1.getYear()), randInt(date2.getMonthValue(), date1.getMonthValue()), randInt(date2.getDayOfMonth(), date1.getDayOfMonth()));
        } else {
            dt = LocalDate.of(randInt(date1.getYear(), date2.getYear()), randInt(date1.getMonthValue(), date2.getMonthValue()), randInt(date1.getDayOfMonth(), date2.getDayOfMonth()));
        }
        return addZeroBeforeNum(dt.getDayOfMonth()) + "." + addZeroBeforeNum(dt.getMonthValue()) + "." + dt.getYear();
    }

    /***
     *
     * @return Возвращает текущую дату в формате dd.mm.yyyy с ведущими нулями
     * пример:
     * "01.02.2010"
     * "11.12.2011"
     */
    public static String getNowDateAsString() {
        LocalDate date = LocalDate.now();
        return addZeroBeforeNum(date.getDayOfMonth()) + "." + addZeroBeforeNum(date.getMonthValue()) + "." + date.getYear();
    }

    /***
     *
     * @return Возвращает текущую дату в формате dd.mm.yyyy с ведущими нулями
     * пример:
     * "01.02.2010"
     * "11.12.2011"
     */
    public static String getNowFullDateAsString() {
        LocalDateTime localDate = LocalDateTime.now();

        return addZeroBeforeNum(localDate.getDayOfMonth()) + "." +
                addZeroBeforeNum(localDate.getMonthValue()) + "." +
                localDate.getYear() + " " +
                addZeroBeforeNum(localDate.getHour()) + ":" +
                addZeroBeforeNum(localDate.getMinute());
    }

    /***
     *
     * @return Возвращает текущую дату в формате mm/dd/yyyy с ведущими нулями
     * пример:
     * "12/23/2010"
     */
    public static String getNowDateAsStringSOAPFormat() {
        LocalDate date = LocalDate.now();
        return addZeroBeforeNum(date.getMonthValue()) + "/" + addZeroBeforeNum(date.getDayOfMonth()) + "/" + date.getYear();
    }

    /***
     *
     * @return Возвращает текущую дату в формате mm/dd/yyyy HH24:MI:SS с ведущими нулями
     * пример:
     * "12/23/2010 14:20:59"
     */
    public static String getNowDateTimeAsStringSOAPFormat() {
        LocalDateTime date = LocalDateTime.now();
        return addZeroBeforeNum(date.getMonthValue()) + "/"
                + addZeroBeforeNum(date.getDayOfMonth()) + "/"
                + date.getYear()
                + " " +
                addZeroBeforeNum(date.getHour()) + ":" +
                addZeroBeforeNum(date.getMinute()) + ":" +
                addZeroBeforeNum(date.getSecond());
    }

    /***
     *
     * @return Возвращает текущую дату + время в минутах в формате mm/dd/yyyy HH24:MI:SS
     * пример:
     * "12/23/2010 14:20:59"
     */
    public static String getNowDateTimePlusMinutesAsStringSOAPFormat(long plusMinutes) {
        LocalDateTime date = LocalDateTime.now();
        date.plusMinutes(plusMinutes);
        return addZeroBeforeNum(date.getMonthValue()) + "/"
                + addZeroBeforeNum(date.getDayOfMonth()) + "/"
                + date.getYear()
                + " " +
                addZeroBeforeNum(date.getHour()) + ":" +
                addZeroBeforeNum(date.getMinute()) + ":" +
                addZeroBeforeNum(date.getSecond());
    }

    /***
     *
     * @return Возвращает текущую дату + в формате mm/dd/yyyy HH24:MI:SS  ведущими нулями
     * пример:
     * "12/23/2010 14:20:59"
     */
    public static String getNowDateTimeMinusMinutesAsStringSOAPFormat(long munisMinutes) {
        LocalDateTime date = LocalDateTime.now();
        date.minusMinutes(munisMinutes);
        return addZeroBeforeNum(date.getMonthValue()) + "/"
                + addZeroBeforeNum(date.getDayOfMonth()) + "/"
                + date.getYear()
                + " " +
                addZeroBeforeNum(date.getHour()) + ":" +
                addZeroBeforeNum(date.getMinute()) + ":" +
                addZeroBeforeNum(date.getSecond());
    }

    /***
     *
     * @return Возвращает текущую дату + какое-то количество дней в формате dd.mm.yyyy с ведущими нулями
     * пример:
     * "01.02.2010"
     * "11.12.2011"
     */
    public static String getNowDatePlusDaysAsString(int countDays) {
        LocalDate date = LocalDate.now().plusDays(countDays);
        return addZeroBeforeNum(date.getDayOfMonth()) +
                "." + addZeroBeforeNum(date.getMonthValue()) +
                "." + date.getYear();
    }

    /**
     * генерирует номер кода депортамента для паспорта РФ
     *
     * @return стринга в формате %%%-%%%, где % - цифры 0-9
     */
    public static String getPassportDepCode() {
        return ("" + randInt(0, 9) + randInt(0, 9) + randInt(0, 9) + "-" + randInt(0, 9) + randInt(0, 9) + randInt(0, 9));
    }

    /****
     *
     * @param num int число
     * @return Возвращает строку содержащую число из параметра @num если число меньше 10 то оно возвращается в формате "0num"
     */

    public static String addZeroBeforeNum(int num) {
        if (num < 10)
            return "0" + num;
        else
            return "" + num;
    }


    /* для будущих вариантов генерации фио
    public static String getRndFname()
    {
        int row= randInt(1,100);
        XLSXworker xls =  new XLSXworker("FIOs.xlsx");
//        return xls.readStringCell(1, 1);
        return xls.readStringCell(row, 1);
    }

    public static String getRndLname()
    {
        int row= randInt(1,100);
        XLSXworker xls =  new XLSXworker("FIOs.xlsx");
        return xls.readStringCell(row, 2);
    }

    public static String getRndMname()
    {
        int row= randInt(1,100);
        XLSXworker xls =  new XLSXworker("FIOs.xlsx");
        return xls.readStringCell(row, 3);
    }
    */

    /**
     * генерирует валидный ИНН длинной в 10 символов
     *
     * @return возвращать его в виде строки
     */

    public static String getINNul() {
        int[] inn;
        String INN = "";
        inn = new int[10];
        for (int i = 0; i < 9; i++) {
            inn[i] = random.nextInt(9);
            INN = INN + inn[i];
        }
        inn[9] = ((2 * inn[0] + 4 * inn[1] + 10 * inn[2] + 3 * inn[3] + 5 * inn[4] + 9 * inn[5] + 4 * inn[6] + 6 * inn[7] + 8 * inn[8]) % 11) % 10;
        INN = INN + inn[9];
        return INN;
    }

    /**
     * генерирует валидный ИНН длинной в 12 символов
     *
     * @return возвращать его в виде строки
     */

    public static String getINNfl() {
        int[] inn;
        String ipINN = "";
        inn = new int[12];
        for (int i = 0; i < 10; i++) {
            inn[i] = random.nextInt(9);
            ipINN = ipINN + inn[i];
        }
        inn[10] = ((7 * inn[0] + 2 * inn[1] + 4 * inn[2] + 10 * inn[3] + 3 * inn[4] + 5 * inn[5] + 9 * inn[6] + 4 * inn[7] + 6 * inn[8] + 8 * inn[9]) % 11) % 10;
        inn[11] = ((3 * inn[0] + 7 * inn[1] + 2 * inn[2] + 4 * inn[3] + 10 * inn[4] + 3 * inn[5] + 5 * inn[6] + 9 * inn[7] + 4 * inn[8] + 6 * inn[9] + 8 * inn[10]) % 11) % 10;
        ipINN = ipINN + inn[10] + inn[11];
        return ipINN;
    }

    /**
     * генерирует валидный ОГРН
     *
     * @return возвращать его в виде строки
     */

    public static String getOGRN() {
        String OGRN = "";
        int[] ogrn = new int[13];
        for (int i = 0; i < 12; i++) {
            ogrn[i] = random.nextInt(9);
            OGRN = OGRN + ogrn[i];
        }
        long g = Long.parseLong(OGRN) % 11;
        if (g == 10)
            g = 0;

        OGRN = OGRN + g;
        return OGRN;
    }

    /**
     * генерирует валидный КПП для РФ
     *
     * @return возвращать его в виде строки
     */

    public static String getKPPrus(String inn) {
        String KPP = "";

        int[] kpp = new int[9];
        KPP = KPP + inn.substring(0, 4);

        kpp[4] = 0;
        KPP = KPP + kpp[4];
        kpp[5] = 1;
        KPP = KPP + kpp[5];

        for (int i = 6; i < 9; i++) {
            kpp[i] = random.nextInt(9);
            KPP = KPP + kpp[i];
        }
        return KPP;
    }

    /**
     * генерирует валидный ОКПО длинной в 8 символов
     *
     * @return возвращать его в виде строки
     */

    public static String getOKPO() {
        String OKPO = "";
        int[] okpo = new int[8];

        for (int i = 0; i < 7; i++) {
            okpo[i] = random.nextInt(9);
            OKPO = OKPO + okpo[i];
        }
        okpo[7] = ((okpo[0] + 2 * okpo[1] + 3 * okpo[2] + 4 * okpo[3] + 5 * okpo[4] + 6 * okpo[5] + 7 * okpo[6]) % 11);

        if (okpo[7] == 10) {
            okpo[7] = ((3 * okpo[0] + 4 * okpo[1] + 5 * okpo[2] + 6 * okpo[3] + 7 * okpo[4] + 8 * okpo[5] + 9 * okpo[6]) % 11);
        }
        if (okpo[7] == 10) {
            okpo[7] = 0;
        }

        OKPO = OKPO + okpo[7];

        return OKPO;
    }

}
