package Deprecated;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QaPreparerDepreacated {

    String srcFolder;
    String tgtFolder;

    File src;

    List<File> tgts;

    public QaPreparerDepreacated(String srcFolder, String tgtFolder) {
        this.srcFolder = srcFolder;
        this.tgtFolder = tgtFolder;

        this.tgts = Arrays.asList(new File(tgtFolder).listFiles());
        this.src = new File(srcFolder).listFiles()[0];

    }

    public void run() throws IOException, InvalidFormatException {

        this.swap(this.src, this.tgts);

    }

    private void swap(File src, List<File> tgtFolders) throws IOException, InvalidFormatException {

        for(File f: tgtFolders){
            this.swap0(src, this.extractLan(f.getName()), f.listFiles()[0]);
        }

    }



    private void swap0(File src, String lan, File tgt) throws IOException, InvalidFormatException {
        XSSFWorkbook tgtBook = new XSSFWorkbook(tgt);

        XSSFWorkbook srcBook = new XSSFWorkbook(src);

        XSSFSheet tgtSheet = tgtBook.getSheetAt(0);
        XSSFSheet srcSheet = srcBook.getSheetAt(0);

        XSSFRow lanRow = srcSheet.getRow(0);

        for(int i=2; i<23;i++){
            System.out.println(lanRow.getCell(i).getStringCellValue().equals(lan));
            if(lanRow.getCell(i).getStringCellValue().equals(lan)){
                
                
                this.copyColumn(srcSheet, tgtSheet,i);
                
            }
            
        }

        this.write(srcBook, src,lan);
        
        
    }

    private void write(XSSFWorkbook srcBook, File src, String lan) throws IOException {
        File file = new File(src.getParent() + "\\" + "test"+lan+".xlsx");

        srcBook.write(new FileOutputStream(file));
    }

    private void copyColumn(XSSFSheet srcSheet, XSSFSheet tgtSheet, int columnNum) {

        XSSFFont filterFont = srcSheet.getRow(53).getCell(2).getCellStyle().getFont();


        for(int i=1; i<tgtSheet.getPhysicalNumberOfRows();i++){

            XSSFRow tgtRow = tgtSheet.getRow(i);
            XSSFRow srcRow = srcSheet.getRow(i);
//            System.out.println(i);
            XSSFCell cell = tgtRow.getCell(1);
//            System.out.println(filterFont.equals(cell.getCellStyle().getFont()));
            if(filterFont.equals(cell.getCellStyle().getFont())&&srcRow.getCell(columnNum)!=null){

                XSSFCell srcRowCell = srcRow.getCell(columnNum);
                System.out.println(i+"dfgdgdgdfg"+columnNum);
                System.out.println("----------"+srcRowCell.getStringCellValue());
//                tgtRow.getCell(1);
                srcRowCell.setCellValue(tgtRow.getCell(1).getStringCellValue());
            }




        }
    }

    public String extractLan(String foldername) {

        Pattern p = Pattern.compile("-[a-zA-Z]+ ");

        Matcher matcher = p.matcher(foldername);

        if (matcher.find()) {
            System.out.println(matcher.group(0).trim().substring(1));
            return matcher.group(0).trim().substring(1);
        }

        return null;
    }

    public static void main(String[] args) throws IOException, InvalidFormatException {
        QaPreparerDepreacated preparer = new QaPreparerDepreacated("P:\\Projects\\1909\\181\\2-Production\\C-Engineering\\2-Build\\src", "P:\\Projects\\1909\\181\\2-Production\\A-Translation\\2-From_Editing\\New Files");
        preparer.run();
    }

}
