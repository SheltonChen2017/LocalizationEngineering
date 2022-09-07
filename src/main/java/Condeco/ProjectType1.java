package Condeco;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class ProjectType1 {

    String folderPath;
    String sourcePath;
    File sourceFile;
    List<File> targets = new ArrayList();

    HashMap<String, String> sourceMap = new HashMap<String, String>();

    public ProjectType1(String folderPath, String sourcePath) throws IOException, InvalidFormatException {
        this.folderPath = folderPath;
        this.sourcePath = sourcePath;
        this.sourceFile = new File(sourcePath);
        this.targets = this.loadFiles(folderPath);
        this.loadSourceMap(this.sourceFile, this.sourceMap, 0, 1);

    }

    private HashMap<String, String> loadSourceMap(File file, HashMap<String, String> map, int key, int value) throws IOException, InvalidFormatException {

        XSSFWorkbook book = new XSSFWorkbook(file);

        XSSFSheet sheet = book.getSheetAt(0);

        for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {

                XSSFRow row = sheet.getRow(i);

                XSSFCell cell = row.getCell(key);
                String keyString = cell.getStringCellValue();
                XSSFCell valueStringCell = row.getCell(value);
                String valueString = valueStringCell.getStringCellValue();
                map.put(keyString, valueString);
//                System.out.println(keyString + ":" + valueString);

        }

        return map;


    }

    private List<File> loadFiles(String folderPath) {

        List<File> files = Arrays.asList(new File(folderPath).listFiles());


        return files;

    }


    public void produce() throws IOException, InvalidFormatException {

        for (File target : this.targets) {

            XSSFWorkbook targetBook = new XSSFWorkbook(target);

            XSSFSheet targetSheet = targetBook.getSheetAt(0);

            for (int i = 0; i < 517; i++) {
                XSSFRow targetRow = targetSheet.getRow(i);
                    XSSFCell targetCellKey = targetRow.getCell(0);
                    String keyString = targetCellKey.getStringCellValue();
                if (this.sourceMap.containsKey(keyString)) {
                    XSSFCell targetCell = targetRow.createCell(1);
                    targetCell.setCellValue(this.sourceMap.get(keyString));
                    this.sourceMap.remove(keyString);
                }


            }
            Set<String> keys = this.sourceMap.keySet();
            for(String key: keys){
                XSSFRow newRow = targetSheet.createRow(targetSheet.getPhysicalNumberOfRows() + 1);
                newRow.createCell(0).setCellValue(key);
                newRow.createCell(1).setCellValue(this.sourceMap.get(key));
            }

            this.loadSourceMap(this.sourceFile, this.sourceMap, 0,1);

            String folderPath = target.getParent();

            String fileName = target.getName();

            File newFile = new File(folderPath + "\\" + fileName + "_.xlsx");

            FileOutputStream fos = new FileOutputStream(newFile);
System.out.println("about to write");
            targetBook.write(fos);

        }

    }

    public static void main(String[] args) throws IOException, InvalidFormatException {
        ProjectType1 starter = new ProjectType1("C:\\Users\\trunk\\OneDrive\\桌面\\210477\\preppedSourcefILES", "C:\\Users\\trunk\\OneDrive\\桌面\\210477\\Copy of Unified_Mobile_App_-_IOS_English_US_EDITED.xlsx");
        starter.produce();
    }

}
