package ZimmerBiomet.SpineLabel.PostQa;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
//import java.util.Random;

public class RedundantRemover {

    String src;
    String tgt;

    XSSFWorkbook srcBook;
    XSSFWorkbook tgtBook;

    public static final String compiledFolder = "P:\\Projects\\1912\\56\\2-Production\\C-Engineering\\2-Build\\20200306_compile\\redundancyRemoved";


    public RedundantRemover(String src, String tgt) throws IOException, InvalidFormatException {
        this.src = src;
        this.tgt = tgt;

        this.srcBook = new XSSFWorkbook(new File(src).listFiles()[0]);
        this.tgtBook = new XSSFWorkbook(new File(tgt).listFiles()[0]);
    }

    public void produce() {

        XSSFSheet srcSheet = this.srcBook.getSheetAt(0);
        XSSFSheet tgtSheet = this.tgtBook.getSheetAt(0);

        for (int i = 1; i < srcSheet.getLastRowNum(); i++) {

            XSSFRow rowSrc = srcSheet.getRow(i);
            XSSFRow rowTgt = tgtSheet.getRow(i);
            for (int j = 5; j < rowSrc.getPhysicalNumberOfCells(); j++) {

                try {

                    XSSFCell cellRow = rowSrc.getCell(j);
                    XSSFCell cellTgt = rowTgt.getCell(j);
                    if (cellRow != null && cellRow.getStringCellValue().equalsIgnoreCase("X")) {
                        cellTgt.setCellValue("");
                    }

                } catch (NullPointerException e) {
                    e.printStackTrace();
                    continue;
                }

            }

        }

    }

    public void write() throws IOException {

        File file = new File(this.compiledFolder + "\\" + "compiled" + Math.random());

        this.tgtBook.write(new FileOutputStream(file));

    }

    public static void main(String[] args) throws IOException, InvalidFormatException {
        RedundantRemover producer = new RedundantRemover("P:\\Projects\\1912\\56\\2-Production\\C-Engineering\\2-Build\\20200306_compile\\master", "P:\\Projects\\1912\\56\\2-Production\\C-Engineering\\2-Build\\20200306_compile\\compiled");
        producer.produce();
        producer.write();
//        RedundantRemover producer2 = new RedundantRemover("P:\\Projects\\1912\\122\\2-Production\\C-Engineering\\2-Build\\20200129_redundancyRemover\\src2", "P:\\Projects\\1912\\122\\2-Production\\C-Engineering\\2-Build\\20200129_redundancyRemover\\preRedun2");
//
//        RedundantRemover producer3 = new RedundantRemover("P:\\Projects\\1912\\122\\2-Production\\C-Engineering\\2-Build\\20200129_redundancyRemover\\src3", "P:\\Projects\\1912\\122\\2-Production\\C-Engineering\\2-Build\\20200129_redundancyRemover\\preRedun3");
//
//        producer2.produce();
//        producer2.write();
//        producer3.produce();
//        producer3.write();

    }





}