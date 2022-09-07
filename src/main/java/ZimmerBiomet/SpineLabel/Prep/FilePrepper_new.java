package ZimmerBiomet.SpineLabel.Prep;

import ZimmerBiomet.SpineLabel.cleanup.Util;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.*;

import java.io.File;
//import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
//import java.util.ArrayList;
import java.util.ArrayList;
//import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//
//
public class FilePrepper_new {

    String folder;

    List<File> files = new ArrayList<>();
    public static final String regex = "\\d*\\.?\\d+";

    public FilePrepper_new(String folder) {
        this.folder = folder;
        this.files = this.groupFiles(new File(folder));
    }


    public List<File> groupFiles(File ff) {


        File[] files = ff.listFiles();

        for (File f : files) {

            if (!f.isDirectory()) {

                this.files.add(f);

            } else {
                this.groupFiles(f);
            }

        }

        return this.files;

    }


    public void produce() throws IOException, InvalidFormatException {

        for (File f : this.files) {

            XSSFWorkbook book = new XSSFWorkbook(f);

            this.produce(book, f);

        }


    }

    private void produce(XSSFWorkbook book, File f) throws IOException {
        XSSFWorkbook newBook = placeHolderOnSheet(book);


        this.writeFile(f, newBook);


    }

    private XSSFWorkbook placeHolderOnSheet(XSSFWorkbook book) {
        XSSFWorkbook newBook = new XSSFWorkbook();

        for (int n = 0; n < book.getNumberOfSheets(); n++) {

            XSSFSheet newSheet = newBook.createSheet();

            XSSFSheet sheet = book.getSheetAt(n);

            changeSheets(newSheet, sheet);

        }


        return newBook;
    }

    private void changeSheets(XSSFSheet newSheet, XSSFSheet sheet) {

        int n = Util.findMaxNumOfRows(sheet);
        ArrayList<Integer> cols = Util.findRelevantColumns(sheet);
//System.out.println(cols.get(0));
        System.out.println(n);
        for (int rowNum = 1; rowNum <= n; rowNum++) {


            XSSFRow row = sheet.getRow(rowNum);

            for (int colNum : cols) {
                try {
                    XSSFCell cell = row.getCell(colNum);
                    this.doChangeSheets(newSheet, rowNum, cell, colNum);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    continue;
                }
            }


        }
    }

    private void doChangeSheets(XSSFSheet newSheet, int rowNum, XSSFCell cell, int colNum) {

        XSSFRow newRow;

        if (newSheet.getRow(rowNum) == null) {
            newRow = newSheet.createRow(rowNum);
        } else {
            newRow = newSheet.getRow(rowNum);
        }
        XSSFCell newCell = newRow.createCell(colNum);
        String text = cell.getStringCellValue();

        String s = this.replaceNumberWithPlaceHolder(text);
        newCell.setCellValue(s);

    }



    private String replaceNumberWithPlaceHolder(String text) {


        Pattern p = Pattern.compile(this.regex);
        int n = 0;
        StringBuffer sb = new StringBuffer();

        Matcher matcher = p.matcher(text);

        while (matcher.find()) {

            matcher.appendReplacement(sb, "[" + n + "]");

            n++;

        }

        matcher.appendTail(sb);


        return sb.toString();

    }


    private void writeFile(File f, XSSFWorkbook book) throws IOException {

        String folder = f.getParent();

        File nfile = new File(folder + "\\" + f.getName() + "_prepped.xlsx");

        FileOutputStream fos = new FileOutputStream(nfile);

        book.write(fos);

    }

    public static void main(String[] args) throws IOException, InvalidFormatException {
        new FilePrepper_new("C:\\Users\\trunk\\OneDrive\\desktop\\DEMO\\Project2_non-typical-excel-demo2-automation&tokenization\\source").produce();
    }
}

