package ADP;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.*;

import java.io.*;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ADPLogConverter {

    String XTMLogPath;
    String folderpath;

    XSSFWorkbook XTMBook;

    XSSFWorkbook MTDBook;


    public final static ResourceBundle bundle = ResourceBundle.getBundle("XTMLogLang");

    public ADPLogConverter(String XTMLogPath, String folderpath) throws IOException, InvalidFormatException {
        this.XTMLogPath = XTMLogPath;
        this.folderpath = folderpath;

        File log =new File(this.XTMLogPath).listFiles()[0];

        this.XTMBook = new XSSFWorkbook(log);
        this.MTDBook = new XSSFWorkbook();
        XSSFSheet sheet = this.MTDBook.createSheet("000000-Account-Number");
        XSSFRow row0 = sheet.createRow(0);
        row0.createCell(0).setCellValue("Target Locale");
        row0.createCell(1).setCellValue("Asset");
        row0.createCell(2).setCellValue("Total");
        row0.createCell(3).setCellValue("ICE Match");
        row0.createCell(4).setCellValue("100%");
        row0.createCell(5).setCellValue("100-90%");
        row0.createCell(6).setCellValue("90-80%");
        row0.createCell(7).setCellValue("80-70%");
        row0.createCell(8).setCellValue("70-60%");
        row0.createCell(9).setCellValue("60-0%");
        row0.createCell(10).setCellValue("Repetition");
        row0.createCell(11).setCellValue("Cost Estimate");
        row0.createCell(12).setCellValue("MT Fuzzy Words");
        row0.createCell(13).setCellValue("Multiple 100%");

    }


    public ArrayList<Row> findUsefulRows() {
//        System.out.println("xxxxxxxxfxgfgxfgxfg");
        ArrayList<Row> rows = new ArrayList<Row>();
        XSSFSheet sheet = this.XTMBook.getSheetAt(0);
//        System.out.println(sheet.getRow(0).getCell(0).getStringCellValue());
        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
//            System.out.println(i);
            XSSFCell cell = sheet.getRow(i).getCell(0);

            if(cell!=null) {
                if (cell.getStringCellValue().equals("All")) {
                    rows.add(cell.getRow());
                }
            }
        }

//        System.out.println(rows);

//        System.out.println(rows.size()+"----------------------------------------------------");

        return rows;

    }

    public void parseRowInformation(Row row) throws IOException {
//        System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");

        String Languag = row.getCell(1).getStringCellValue();
        String Language = Languag.replace(" ", "");
        double total = row.getCell(2).getNumericCellValue();
        double leverageMatch = row.getCell(5).getNumericCellValue();
        double ICEMatch = row.getCell(3).getNumericCellValue() + row.getCell(4).getNumericCellValue();
        double NientyPercentagePlus = row.getCell(6).getNumericCellValue();
        double EigthyPercentPlus = row.getCell(7).getNumericCellValue();
        double SeventyPercentPlus = row.getCell(8).getNumericCellValue();
        double RepetitiveCounts = row.getCell(10).getNumericCellValue();
        double NewWords = row.getCell(14).getNumericCellValue();

        XSSFSheet sheet = this.MTDBook.getSheetAt(0);

        XSSFRow row1 = sheet.createRow(1);

        row1.createCell(0).setCellValue(Language);
        row1.createCell(1).setCellValue("AutoGenerate");
        row1.createCell(2).setCellValue(total);
        row1.createCell(3).setCellValue(ICEMatch);
        row1.createCell(4).setCellValue(leverageMatch);
        row1.createCell(5).setCellValue(NientyPercentagePlus);
        row1.createCell(6).setCellValue(EigthyPercentPlus);
        row1.createCell(7).setCellValue(SeventyPercentPlus);
        row1.createCell(8).setCellValue(0);
        row1.createCell(9).setCellValue(NewWords);
        row1.createCell(10).setCellValue(RepetitiveCounts);
        row1.createCell(11).setCellValue(0);
        row1.createCell(12).setCellValue(0);
        row1.createCell(13).setCellValue(0);

        XSSFRow row2 = sheet.createRow(2);

        for (int i = 0; i < row1.getPhysicalNumberOfCells(); i++) {

//            System.out.println(i);

//            row1.getCell(i).setCellType(CellType.STRING);

            if (row1.getCell(i).getCellType() == CellType.NUMERIC) {
                row2.createCell(i).setCellValue(row1.getCell(i).getNumericCellValue());
            } else if (row1.getCell(i).getCellType() == CellType.STRING) {
                row2.createCell(i).setCellValue(row1.getCell(i).getStringCellValue());
            }


        }

        row2.getCell(1).setCellValue(" Total");
        row2.getCell(0).setCellValue("");

        String subfix = ADPLogConverter.bundle.getString(Language);

        String csvString = this.xlsxToCSV(this.MTDBook);

        FileOutputStream fos = new FileOutputStream(new File(this.folderpath) + "\\" + "scope_info_" + subfix + ".csv");

        fos.write(csvString.getBytes());

    }

    public void writeDocument(ArrayList<Row> rows) throws IOException {

        for (Row row : rows) {
            this.parseRowInformation(row);
        }

    }

    public String xlsxToCSV(XSSFWorkbook book) {
        XSSFSheet sheet = book.getSheetAt(0);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {

            Row row = sheet.getRow(i);

            for (int j = 0; j < row.getPhysicalNumberOfCells(); j++) {

                Cell cell = row.getCell(j);

                if (i > 0) {

                    if (cell.getCellType() == CellType.NUMERIC) {
//                        System.out.println(cell.getNumericCellValue());
                        double temp= cell.getNumericCellValue();
//                        Double d = new Double(temp);
                        int csvInt = (int)temp;

                        sb.append(csvInt);
                    } else {
                        sb.append("\"").append(cell.getStringCellValue()).append("\"");
                    }
                } else {
                    if (cell.getCellType() == CellType.NUMERIC) {
                        double temp= cell.getNumericCellValue();
//                        Double d = new Double(temp);
                        int csvInt = (int)temp;

                        sb.append(csvInt);
                    } else {
                        sb.append(cell.getStringCellValue());
                    }
                }

                if (j < row.getPhysicalNumberOfCells() - 1) {
                    sb.append(",");
                }
            }

            sb.append("\n");

        }


        return sb.toString();
    }


    public static void main(String[] args) throws IOException, InvalidFormatException, InterruptedException {

        System.out.println("Welcome. It's me again.");
//        Thread.sleep(1000);
        System.out.println("I forgot to ask again: What is the absolute path of the folder containing the XTM log (make sure the folder has this log only)??");

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String XTMLogPath = br.readLine();

//        System.out.println("Let me also know where you want me to put all the generated logs");
//
//        String folderpath = br.readLine();

        ADPLogConverter converter = new ADPLogConverter(XTMLogPath, XTMLogPath);

        ArrayList<Row> rows = converter.findUsefulRows();
        converter.writeDocument(rows);
        System.out.println(converter.xlsxToCSV(converter.MTDBook));

    }


}
