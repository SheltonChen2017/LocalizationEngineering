package Deprecated;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class QA {

    public static void main(String[] args) throws IOException, InvalidFormatException {

//        new XSSFWorkbook(new File())

       File f= new File("P:\\Projects\\1910\\229\\2-Production\\C-Engineering\\2-Build\\20191118").listFiles()[0];
        XSSFWorkbook workbook = new XSSFWorkbook(f);

        XSSFSheet sheet = workbook.getSheet("Part List");


//        XSSFRow lanRow = sheet.getRow(0);


        System.out.println(f.getPath());

        for(int j=1; j<=21;j++){

            StringBuilder sb = new StringBuilder();

            for(int i=1; i<=520; i++){

                XSSFRow row = sheet.getRow(i);
try {
    XSSFCell cell = row.getCell(j);

    String transValue = cell.getStringCellValue();

    String srcValue = row.getCell(0).getStringCellValue();

    sb.append(srcValue).append("\t").append(transValue).append("\r\n");
} catch (Exception e) {
    e.printStackTrace();
    continue;
}
            }

            File fileT = new File(f.getParent() + "\\" + sheet.getRow(0).getCell(j).getStringCellValue() + ".txt");

            FileOutputStream fos = new FileOutputStream(fileT);

            fos.write(sb.toString().getBytes());

        }


    }

}
