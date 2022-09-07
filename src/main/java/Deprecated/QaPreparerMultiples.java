package Deprecated;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QaPreparerMultiples {

    String masterFolder;
    String transFolder;
    String qaFolder;

    List<File> srcs;
    List<File> translations = new ArrayList<File>();

    public QaPreparerMultiples(String masterFolder, String transFolder, String qaFolder) {
        this.masterFolder = masterFolder;
        this.transFolder = transFolder;
        this.qaFolder = qaFolder;
        this.srcs = Arrays.asList(new File(this.masterFolder).listFiles());
        this.translations = this.extractFiles(transFolder);
    }

    private List<File> extractFiles(String transFolder) {

        File[] files = new File(transFolder).listFiles();

        for (File f : files) {

            if (!f.isDirectory()&&f.getName().endsWith(".xlsx")) {
                this.translations.add(f);
            } else {
                this.extractFiles(f.getPath());
            }

        }

        return this.translations;

    }

    public String parseLanguage(String foldername) {

        Pattern p = Pattern.compile("-[a-zA-Z]+ ");

        Matcher matcher = p.matcher(foldername);

        if (matcher.find()) {
            return matcher.group(0).trim().substring(1);
        }

        return null;
    }

    public void produce() throws IOException, InvalidFormatException {

        for (File f : this.srcs) {

            this.produce(f, this.translations);


        }


    }

    private void produce(File src, List<File> translations) throws IOException, InvalidFormatException {
        for (File translation : translations) {

            if (src.getName().equals(translation.getName())) {
                this.copyColumn(src, translation);
            }

        }
    }

    private void copyColumn(File src, File translation) throws IOException, InvalidFormatException {

        XSSFWorkbook srcBook = new XSSFWorkbook(src);
        XSSFWorkbook transBook = new XSSFWorkbook(translation);

        XSSFWorkbook newBook = new XSSFWorkbook();
        XSSFSheet newSheet = newBook.createSheet("QA");
        XSSFSheet sheetSrc = srcBook.getSheetAt(0);
        XSSFSheet sheetTrans = transBook.getSheetAt(0);


        String lan = this.parseLanguage(translation.getParent());

        for (int i = 1; i < sheetSrc.getPhysicalNumberOfRows(); i++) {

            XSSFRow rowSrc = sheetSrc.getRow(i);

            XSSFCell src1Col = rowSrc.getCell(1);
            XSSFCell src2Col = rowSrc.getCell(2);

            XSSFRow rowTrans = sheetTrans.getRow(i);

            XSSFCell trans1Col = rowTrans.getCell(1);

            XSSFCell trans2Col = rowTrans.getCell(2);

            XSSFRow newRow = newSheet.createRow(i);

            if (src1Col != null && trans1Col != null) {

                newRow.createCell(1).setCellValue(src1Col.getStringCellValue());
                newRow.createCell(2).setCellValue(trans1Col.getStringCellValue());

            }

            if (src2Col != null && trans2Col != null) {
                newRow.createCell(3).setCellValue(src2Col.getStringCellValue());
                newRow.createCell(4).setCellValue(trans2Col.getStringCellValue());
            }

            XSSFRow firstRow = sheetTrans.getRow(0);

            for(int m=3; m<firstRow.getPhysicalNumberOfCells();m++){

                if(firstRow.getCell(m).getStringCellValue().equalsIgnoreCase(lan)){


                    XSSFCell cell = rowSrc.getCell(m);

                    if(cell==null){
                        continue;
                    } else {

                        newRow.createCell(8).setCellValue(cell.getStringCellValue());

                    }

                }

            }


        }

        File newFile = new File(this.qaFolder + "\\" + lan + "_" + translation.getName());

        newBook.write(new FileOutputStream(newFile));

    }

    public static void main(String[] args) throws IOException, InvalidFormatException {

        new QaPreparerMultiples("P:\\Projects\\1912\\90\\2-Production\\C-Engineering\\2-Build\\20200108_scriptFixedFirstRerun_prepare\\src","P:\\Projects\\1912\\90\\2-Production\\C-Engineering\\2-Build\\20200108_scriptFixedFirstRerun_prepare\\fromWS","P:\\Projects\\1912\\90\\2-Production\\C-Engineering\\2-Build\\20200108_scriptFixedFirstRerun_prepare\\qa").produce();
    }

}
