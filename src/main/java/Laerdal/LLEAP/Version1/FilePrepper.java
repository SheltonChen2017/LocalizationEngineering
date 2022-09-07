package Laerdal.LLEAP.Version1;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
//import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;

public class FilePrepper {


    String folderpath;
    File[] files;
    ArrayList<File> xbooks = new ArrayList<File>();

    void convert() throws IOException {


        for (File f : this.files) {

            XSSFWorkbook book = this.convert(f);

        }


    }

    void convertAndExtract() throws IOException, InvalidFormatException {

        this.convert();

        this.extract();

    }

    private void extract() throws IOException, InvalidFormatException {
        for (File file : this.xbooks) {
            this.extract0(file);
        }

    }

    private void extract0(File file) throws IOException, InvalidFormatException {
        XSSFWorkbook prepared = new XSSFWorkbook();

        XSSFWorkbook source = new XSSFWorkbook(file);

        XSSFSheet sourceSheet = source.getSheetAt(0);

        XSSFSheet preparedSheet = prepared.createSheet(sourceSheet.getSheetName());

        for (int i = 1; i < sourceSheet.getPhysicalNumberOfRows(); i++) {
            XSSFRow sourceRow = sourceSheet.getRow(i);

            XSSFRow preparedRow = preparedSheet.createRow(i);

            preparedRow.createCell(1).setCellValue(sourceRow.getCell(1).getStringCellValue());

        }

        String folderpathParent = file.getParent();

        String folderpath = new File(folderpathParent).getParent();

        boolean dir = new File(folderpath + "\\" + "prepped").isDirectory();

        if (dir == false) {
            new File(folderpath + "\\" + "prepped").mkdir();
        }

        String prefix = file.getName().substring(0, file.getName().lastIndexOf("."));

        File ff = new File(folderpath + "\\" + "prepped" + "\\" + prefix + ".xlsx");

        prepared.write(new FileOutputStream(ff));

//        POIXMLDocumentPart folderpath = prepared.getParent();


    }

    public FilePrepper(String folderpath) {
        this.folderpath = folderpath;
        this.files = new File(this.folderpath).listFiles();
    }


    XSSFWorkbook convert(File f) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(f));


        XSSFWorkbook newWorkbook = new XSSFWorkbook();

        Iterator<Sheet> iterator = workbook.sheetIterator();
        int i = 0;
//        int j = 0;
        while (iterator.hasNext()) {
            Sheet next = iterator.next();
            XSSFSheet newSheet = newWorkbook.createSheet(next.getSheetName());

            Iterator<Row> rIterator = next.rowIterator();


            while (rIterator.hasNext()) {


                Row row = rIterator.next();

                XSSFRow newRow = newSheet.createRow(i++);

                int cellNum = row.getPhysicalNumberOfCells();

                for (int m = 0; m < cellNum; m++) {

                    Cell cell = row.getCell(m);

                    if (cell != null) {
//                        System.out.println(cell.getCellType());
                        if (cell.getCellType() == CellType.NUMERIC) {
                            newRow.createCell(m).setCellValue(cell.getNumericCellValue());

                        }
                        if (cell.getCellType() == CellType.STRING) {
                            newRow.createCell(m).setCellValue(cell.getStringCellValue());
                        }

                    }
                }

            }
        }

        this.writeXlsx(newWorkbook, f);

        return newWorkbook;
    }

    private void writeXlsx(XSSFWorkbook newWorkbook, File f) throws IOException {
        String prefix = f.getName().substring(0, f.getName().lastIndexOf("."));
        String parent = f.getParent();

        String newfoldername = parent + "\\" + "xlsx";

        if (!new File(newfoldername).isDirectory()) {
            new File(newfoldername).mkdir();
        }

        String newname = newfoldername + "\\" + prefix + ".xlsx";

        File file = new File(newname);
        this.xbooks.add(file);
        newWorkbook.write(new FileOutputStream(file));

    }


    public static void main(String[] args) throws IOException, InvalidFormatException {
        System.out.println("Let me know the path of the folder where all the source files are stored");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String path = br.readLine();

        FilePrepper prepper = new FilePrepper(path);

        prepper.convertAndExtract();

    }

}
