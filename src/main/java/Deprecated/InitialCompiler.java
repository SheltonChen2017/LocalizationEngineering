package Deprecated;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InitialCompiler {

    String folder;

    public static String regexNum = "\\d*\\.?\\d+";
    public static String regexHolder = "\\[([\\d\\s]+)\\]";
    public static ArrayList box = new ArrayList();
    public static final String qaFolder = "";
    HashMap<String, XSSFWorkbook> srcs = new HashMap<String, XSSFWorkbook>();
    String srcFolder;
    HashMap<String, XSSFWorkbook> map = new HashMap<String, XSSFWorkbook>();

    public InitialCompiler(String folder, String srcFolder) throws IOException, InvalidFormatException {
        this.folder = folder;
        File[] files = new File(this.folder).listFiles();
        this.srcFolder = srcFolder;
        File[] srcFiles = new File(this.srcFolder).listFiles();
        for (File f : files) {
            this.map.put(f.getName(), new XSSFWorkbook(f));
        }

        for (File src : srcFiles) {
            this.srcs.put(src.getName(), new XSSFWorkbook(src));
        }

    }

    public void produce() throws IOException {


        Set<String> strings = this.map.keySet();

        for (String s : strings) {


            this.produce(s, this.map.get(s), this.srcs.get(s));
            System.out.println(s);

        }


    }

    private void produce(String s, XSSFWorkbook sheets, XSSFWorkbook srcSheets) throws IOException {

        XSSFSheet sheet = sheets.getSheetAt(0);

        XSSFSheet srcSheet = srcSheets.getSheetAt(0);

        for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
//System.out.println(i);
            XSSFCell cell1 = srcSheet.getRow(i).getCell(2);
            XSSFCell cell2 = sheet.getRow(i).getCell(3);


            String valueSrc = cell1.getStringCellValue();

            String valueTrans = cell2.getStringCellValue();

            Pattern p1 = Pattern.compile(this.regexNum);

            Pattern p2 = Pattern.compile(this.regexHolder);

            Matcher m1 = p1.matcher(valueSrc);
//System.out.println(m1.find());
            while (m1.find()) {
                this.box.add(m1.group(0));
            }

            Matcher m2 = p2.matcher(valueTrans);
//            System.out.println(valueTrans);
//System.out.println(m2.find());
            while (m2.find()) {
                String holder = m2.group(1).trim();
//                System.out.println(holder);
                int num = Integer.parseInt(holder);

                String holderIntact = m2.group(0);
                valueTrans = valueTrans.replace(holderIntact, this.box.get(num).toString());
//                System.out.println(valueTrans);
            }

            cell2.setCellValue(valueTrans);

            this.box = new ArrayList();

        }

        String newFileName = this.qaFolder+"\\"+ s + ".xlsx";
        File newFile = new File(newFileName);

        FileOutputStream fos = new FileOutputStream(newFile);

        sheets.write(fos);

    }

    public static void main(String[] args) throws IOException, InvalidFormatException {

//        new InitialCompiler("P:\\Projects\\1912\\90\\2-Production\\C-Engineering\\2-Build\\20191219_compilationTwst\\translations").produce();

    }
}
