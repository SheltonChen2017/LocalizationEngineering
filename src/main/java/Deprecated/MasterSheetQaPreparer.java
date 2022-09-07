package Deprecated;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class MasterSheetQaPreparer {

    int srcCol;
    int tgtStartCol;
    String unicodeSuffix;
    String folder;

    List<File> files;

    public MasterSheetQaPreparer(int srcCol, int tgtStartCol, String unicodeSuffix, String folder) {
        this.srcCol = srcCol;
        this.tgtStartCol = tgtStartCol;
        this.unicodeSuffix = unicodeSuffix;
        this.folder = folder;
        this.files = Arrays.asList(new File(this.folder).listFiles());
    }

    public void produce() throws IOException, InvalidFormatException {

        for (File f : this.files) {

            if (f.getName().endsWith(".xlsx")) {
                this.produce(f);
            }
        }


    }

    private void produce(File f) throws IOException, InvalidFormatException {

        XSSFWorkbook book = new XSSFWorkbook(f);

        XSSFSheet sheet = book.getSheetAt(0);


        this.generateUnicodeTest(sheet, f.getParent(), f.getName());
    }

    private void generateUnicodeTest(XSSFSheet sheet, String parent, String fileName) {
        StringBuilder sb = new StringBuilder();
        XSSFWorkbook sheets = new XSSFWorkbook();
        XSSFSheet qasheet = sheets.createSheet("QA");
        for (int j = this.tgtStartCol; j < this.tgtStartCol + 21; j++) {
            try {
                String lan = sheet.getRow(0).getCell(j).getStringCellValue();
//                lan = lan.substring(0, lan.lastIndexOf("(")).trim();
                System.out.println(lan);
                for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
//                    System.out.println(sheet.getPhysicalNumberOfRows());
                    XSSFRow row = sheet.getRow(i);
                    XSSFCell srcCell = row.getCell(this.srcCol);
                    XSSFCell tgtCell = row.getCell(j);
                    if (srcCell != null && tgtCell != null) {
                        String srcValue = srcCell.getStringCellValue();
                        String tgtValue = tgtCell.getStringCellValue();
//                        String tgtValue = tgtCell.getStringCellValue().replaceAll(",",".");
                        sb.append(srcValue).append("\t").append(tgtValue).append("\r\n");
                        qasheet.createRow(i).createCell(1).setCellValue(srcValue);
//                        qasheet.getRow(i).createCell(4).setCellValue(tgtValue.replaceAll("\\.", ","));
                        qasheet.getRow(i).createCell(4).setCellValue(tgtValue);
                    }
                }
                String text = sb.toString();
                System.out.println(text);
                File unicodeText = new File(parent + "\\" + lan + "_" + this.unicodeSuffix + "_" + fileName + ".txt");
                FileOutputStream fos = new FileOutputStream(unicodeText);
                fos.write(text.getBytes());
                File qaFolder = new File(parent + "\\" + "qaSheet");
                if (!qaFolder.exists()) {
                    qaFolder.mkdir();
                }
                File qaFile = new File(qaFolder.getPath() + "\\" + lan + "_" + fileName);
                FileOutputStream qaStream = new FileOutputStream(qaFile);
                sheets.write(qaStream);
                sb = new StringBuilder();
                sheets = new XSSFWorkbook();
                qasheet = sheets.createSheet("QA");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch(NullPointerException e){
                e.printStackTrace();
                continue;
            }
        }
    }

    public static void main(String[] args) throws IOException, InvalidFormatException {
        MasterSheetQaPreparer d1 = new MasterSheetQaPreparer(1, 2, "Description1", "C:\\Users\\trunk\\OneDrive\\桌面\\workaholic\\2006124\\qa");

        d1.produce();

    }
}
