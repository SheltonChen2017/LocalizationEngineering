package ZimmerBiomet.SpineLabel.PreQa;


import ZimmerBiomet.SpineLabel.cleanup.Util;
//import com.sun.org.apache.xpath.internal.operations.String;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
//import org.apache.poi.ss.formula.functions.Index;
import org.apache.poi.xssf.usermodel.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WSFilesQaGenerator {
    // regular expression used to locate measurements in the source language
    public static String regexNum = "(\\d*)(\\.?)(\\d+)";
    //regular expression used to find tags in the target language.
    public static String regexHolder = "\\[([\\d\\s]+)\\]";
    public static ArrayList box = new ArrayList();
    // regular expression used to convert "." to "," for numerical information that is followed by a measurement or multiply sign
    public static String numericalDotConverterInstance1 = "(\\d+\\s*?)(\\.)(\\s*?\\d+\\s*?)([Mx×Xm*])";
    // regular expression used to convert "." to "," for numerical data that is preceded by a multiply sign
    public static String numericalDotConverterInstance2 = "([xX*×mM]\\s*?\\d+\\s*?)(\\.)(\\s*?\\d+)";
    // regular expression used to convert the \d,\d M[^M] back into \d.\d
    public static String numericalDotConverterInstance3 = "(\\d+?)(,)(\\d+?\\s*?)([Mm][^Mm])";
    //Error Log String Buffer
    public static StringBuffer errorLog = new StringBuffer();

    Pattern p1 = Pattern.compile(this.regexNum);

    Pattern p2 = Pattern.compile(this.regexHolder);

    Pattern instance1Pattern = Pattern.compile(this.numericalDotConverterInstance1);

    Pattern instance2Pattern = Pattern.compile(this.numericalDotConverterInstance2);

    Pattern instance3Pattern = Pattern.compile(this.numericalDotConverterInstance3);

    List<File> transFolders;

    String srcFolder;
    String WSFolder;
    String qaPath;

    List<File> srcFiles;

    public WSFilesQaGenerator(String srcFolder, String WSFolder, String qaPath) {
        this.srcFolder = srcFolder;
        this.WSFolder = WSFolder;
        this.qaPath = qaPath;
        this.srcFiles = Arrays.asList(new File(this.srcFolder).listFiles());

        this.transFolders = Arrays.asList(new File(this.WSFolder).listFiles());

    }


    public void produce() throws IOException, InvalidFormatException {

        for (File f : this.srcFiles) {

            this.produce(f, this.transFolders);

        }

    }

    private void produce(File srcFile, List<File> transFolders) throws IOException, InvalidFormatException {

        for (File folder : transFolders) {

            File[] translations = folder.listFiles();

            for (File translation : translations) {

                if (translation.getName().contains(srcFile.getName())) {

                    this.makeQaFiles(srcFile, translation);

                }

            }
        }

    }

    private void makeQaFiles(File srcFile, File translation) throws IOException, InvalidFormatException {

        XSSFWorkbook srcBook = new XSSFWorkbook(srcFile);

        XSSFWorkbook transBook = new XSSFWorkbook(translation);


        XSSFSheet srcSheet = srcBook.getSheetAt(0);

        XSSFSheet transSheet = transBook.getSheetAt(0);

        ArrayList<Integer> colNums = Util.findRelevantColumns(srcSheet);

        String s = this.copyTexts(srcSheet, transSheet, colNums);

        String lan = Util.parseLanguage(translation.getPath());

        String fileName = srcFile.getName().substring(0, srcFile.getName().lastIndexOf("."));

        this.writeUnicode(lan, fileName, this.qaPath, s);

        this.writeExcel(lan, fileName, this.qaPath, s);

    }

    private void writeExcel(String lan, String name, String qaPath, String s) throws IOException {
System.out.println("the line at issue is "+s);
        String[] lines = s.split("\r\n");

        XSSFWorkbook sheets = new XSSFWorkbook();

        XSSFSheet sheet = sheets.createSheet();
//        sheet.protectSheet("mEdIaLoCaTe");
        int i = 0;
//        XSSFCellStyle lockStyle = sheets.createCellStyle();
//        lockStyle.setLocked(false);
        for (String line : lines) {
//System.out.println(line+"''''''");

            String[] combo = line.split("\t");
            XSSFRow row = sheet.createRow(i);
            XSSFCell cellSource = row.createCell(0);
            cellSource.setCellValue(combo[0]);
//            cellSource.setCellStyle(lockStyle);
            XSSFCell cellTrans = row.createCell(2);
            System.out.println(combo[0]);
            String translationRaw = combo[1].trim();

            String translationChanged = this.convertDotToCommaForNumericalData(translationRaw);

            cellTrans.setCellValue(translationChanged);

//            cellTrans.setCellStyle(lockStyle);

            i++;


        }

        sheet.autoSizeColumn(2);
        sheet.autoSizeColumn(0);

        File folderForExcel = new File(qaPath + "\\" + "qaSheet");

        if (!folderForExcel.isDirectory()) {
            folderForExcel.mkdir();
        }

        File qaFile = new File(folderForExcel + "\\" + lan + "_" + name + ".xlsx");

        FileOutputStream fos = new FileOutputStream(qaFile);

        sheets.write(fos);

    }

    private String convertDotToCommaForNumericalData(String translationRaw) {

        String translationR = translationRaw.replaceAll("\\p{Z}", " ");

        Matcher instance1Matcher = this.instance1Pattern.matcher(translationR);

        while (instance1Matcher.find()) {

            String rawIntact = instance1Matcher.group(0);

            translationR = translationR.replace(rawIntact, instance1Matcher.group(1) + "," + instance1Matcher.group(3) + instance1Matcher.group(4));

//            System.out.println(translationR);

        }

//        String translationFinal = translationChanged;

        Matcher instance2Mathcer = this.instance2Pattern.matcher(translationR);

        while (instance2Mathcer.find()) {
            String rawIntact = instance2Mathcer.group(0);
            translationR = translationR.replace(rawIntact, instance2Mathcer.group(1) + "," + instance2Mathcer.group(3));
//            System.out.println(translationRaw);
        }

        Matcher instance3Matcher = this.instance3Pattern.matcher(translationR);

        while (instance3Matcher.find()) {
            String rawIntact = instance3Matcher.group(0);
            translationR = translationR.replace(rawIntact, instance3Matcher.group(1) + "." + instance3Matcher.group(3) + instance3Matcher.group(4));
        }


        this.instance1Pattern = Pattern.compile(this.numericalDotConverterInstance1);
        this.instance2Pattern = Pattern.compile(this.numericalDotConverterInstance2);
        this.instance3Pattern = Pattern.compile(this.numericalDotConverterInstance3);

        return translationR;
    }

    private void writeUnicode(String lan, String fileName, String qaPath, String s) throws IOException {

        File folderForUnicode = new File(qaPath + "\\" + "unicode");

        if (!folderForUnicode.isDirectory()) {
            folderForUnicode.mkdir();
        }

        File qaFile = new File(folderForUnicode + "\\" + lan + "_" + fileName + ".txt");

        FileOutputStream fos = new FileOutputStream(qaFile);

        fos.write(s.getBytes());

    }

    private String copyTexts(XSSFSheet srcSheet, XSSFSheet transSheet, ArrayList<Integer> colNums) {

        StringBuilder sb = new StringBuilder();
System.out.println(colNums.size());
        for (int i = 1; i < srcSheet.getPhysicalNumberOfRows(); i++) {

            for (Integer colNum : colNums) {


                XSSFCell srcCell = srcSheet.getRow(i).getCell(colNum);
                try {
                    System.out.println(srcCell.getStringCellValue());
                } catch (NullPointerException e) {
//                    e.printStackTrace();
                    System.out.println(colNum);
                    continue;
                }
                XSSFCell transCell = transSheet.getRow(i).getCell(colNum);


                if (srcCell != null && transCell != null && srcCell.getStringCellValue().trim() != "" && transCell.getStringCellValue().trim() != "") {


                    String srcValue = srcCell.getStringCellValue();
                    String transValue = transCell.getStringCellValue();

                    String transValuePlus = this.interpret(srcValue, transValue);

//                    byte[] byteSrc = srcValue.getBytes();
//
//                    byte[] tgtBytes = transValuePlus.getBytes();
//
//                    String convertedTgt = new String(tgtBytes, Charset.forName("UTF-8"));
//
//                    String convertedSrc = new String(byteSrc, Charset.forName("UTF-8"));

                    sb.append(srcValue).append("\t").append(transValuePlus).append("\r\n");

                }


            }

        }

        return sb.toString();


    }

    private String interpret(String srcCellValue, String transCellValue) {

//        this.holderChecker(srcCellValue, transCellValue);

        Matcher m1 = this.p1.matcher(srcCellValue);

        while (m1.find()) {
            this.box.add(m1.group(0));
        }
        Matcher m2 = this.p2.matcher(transCellValue);
        while (m2.find()) {
            String holder = m2.group(1).trim();
//                System.out.println(holder);
            int num = Integer.parseInt(holder);

            String holderIntact = m2.group(0);
            try {
                transCellValue = transCellValue.replace(holderIntact, this.box.get(num).toString());
//                System.out.println("TransValue is "+ transCellValue);
                Util.replaceDotToPeriodForNumbers(transCellValue);
//                System.out.println("TransValue is "+ transCellValue);
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
                System.out.println("TransValue is " + transCellValue);
                System.out.println("Src Value is " + srcCellValue);
                continue;
            }
        }


        this.box = new ArrayList();

        return transCellValue;

    }


    public static void main(String[] args) throws IOException, InvalidFormatException {
        WSFilesQaGenerator generator = new WSFilesQaGenerator("C:\\Users\\trunk\\OneDrive\\desktop\\DEMO\\Project2_non-typical-excel-demo2-automation&tokenization\\forQa\\master",
                "C:\\Users\\trunk\\OneDrive\\desktop\\DEMO\\Project2_non-typical-excel-demo2-automation&tokenization\\fromCat",
                "C:\\Users\\trunk\\OneDrive\\desktop\\DEMO\\Project2_non-typical-excel-demo2-automation&tokenization\\forQa");

        generator.produce();

    }

}
