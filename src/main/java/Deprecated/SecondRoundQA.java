package Deprecated;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class SecondRoundQA {

    String path;
    List<File> folders;

    public SecondRoundQA(String path) {
        this.path = path;
        this.folders = Arrays.asList(new File(path).listFiles());
    }

    public void produce() throws IOException, InvalidFormatException {

        for (File folder : this.folders) {


            File[] files = folder.listFiles();


            for (File f : files) {

                this.produce(f);

            }


        }


    }

    private void produce(File f) throws IOException, InvalidFormatException {
        System.out.println(f.getPath());
        XSSFWorkbook sheets = new XSSFWorkbook(f);
        XSSFSheet sheet = sheets.getSheetAt(0);

        StringBuilder sb = new StringBuilder();

        for (int i = 1; i < sheet.getLastRowNum(); i++) {
try {

    XSSFRow row = sheet.getRow(i);

    XSSFCell cell1 = row.getCell(1);

    XSSFCell cell2 = row.getCell(2);

    XSSFCell cell3 = row.getCell(3);

    XSSFCell cell4 = row.getCell(4);

    if (cell1 != null) {

        sb.append(cell1.getStringCellValue()).append("\t").append(cell2.getStringCellValue()).append("\r\n");

    }

    if (cell3 != null) {
        System.out.println(f.getName() + " " + i);
        sb.append(cell3.getStringCellValue()).append("\t").append(cell4.getStringCellValue()).append("\r\n");
    }
} catch (Exception e) {
    e.printStackTrace();
    continue;

}
        }


        String content = sb.toString();

        String newName = f.getPath().substring(0, f.getPath().lastIndexOf(".")) + ".txt";


        File newFile = new File(newName);

        FileOutputStream fos = new FileOutputStream(newFile);

        fos.write(content.getBytes());

    }

    public static void main(String[] args) throws IOException, InvalidFormatException {
        SecondRoundQA qaC = new SecondRoundQA("P:\\Projects\\1911\\207\\2-Production\\C-Engineering\\3-From_Engineering\\20200115");
        qaC.produce();
    }

}
