package ZimmerBiomet.SpineLabel.SecondRoundQa;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class UnicodeGenerator {

    String folder;

    List<File> excelFiles;

    public UnicodeGenerator(String folder) {
        this.folder = folder;
        this.excelFiles = Arrays.asList(new File(folder).listFiles());
    }

    public void produce() throws IOException, InvalidFormatException {

        for (File f : this.excelFiles) {

            this.produce(f.getName(), new XSSFWorkbook(f));

        }

    }

    private void produce(String name, XSSFWorkbook sheets) throws IOException {

        XSSFSheet sheet = sheets.getSheetAt(0);

        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {

            try {

                XSSFRow row = sheet.getRow(i);

                String srcValue = row.getCell(0).getStringCellValue();
                String tgtValue = row.getCell(2).getStringCellValue();

                sb.append(srcValue + "\t" + tgtValue+ "\r\n");

            } catch (NullPointerException e) {

                e.printStackTrace();
                continue;
            }


        }

        this.write(name, sb);

    }

    private void write(String name, StringBuffer sb) throws IOException {

        String fileName = name.substring(0, name.lastIndexOf("."));

        File qaFolder = new File(this.folder + "\\" + "unicode");

        if (!qaFolder.isDirectory()) {
            qaFolder.mkdir();
        }

        File uni = new File(qaFolder + "\\" + fileName + ".txt");

        FileOutputStream fos = new FileOutputStream(uni);

        fos.write(sb.toString().getBytes());
    }

    public static void main(String[] args) throws IOException, InvalidFormatException {
        new UnicodeGenerator("P:\\Projects\\2002\\95\\2-Production\\C-Engineering\\3-From_Engineering\\20200228_postQaCompile\\frQa").produce();
    }

}
