package ZimmerBiomet.SpineLabel.PostQa;

import ZimmerBiomet.SpineLabel.cleanup.Util;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
//import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PostQaCompiler {

    public static final String regex = "(.*?_)(.*)(\\.xlsx)";
    HashMap<String, XSSFWorkbook> masters = new HashMap<>();
    List<File> translations;

    String masterFolder;
    String transFolder;
    String finalCompiledFolder;

    HashMap<String, String> dictionary = new HashMap<String, String>();

    public PostQaCompiler(String masterFolder, String transFolder, String finalCompiledFolder) throws IOException, InvalidFormatException {
        this.masterFolder = masterFolder;
        this.transFolder = transFolder;
        this.finalCompiledFolder = finalCompiledFolder;

        File[] masters = new File(this.masterFolder).listFiles();

        for (File f : masters) {

            this.masters.put(f.getName(), new XSSFWorkbook(f));

        }

        this.translations = Arrays.asList(new File(transFolder).listFiles());

    }


    public void produce() throws IOException, InvalidFormatException {


        for (String masterName : this.masters.keySet()) {

            this.produce(masterName, this.translations);

        }

        this.write();

    }

    private void write() throws IOException {
        Set<String> strings = this.masters.keySet();

        for (String s : strings) {
//            String name = this.interpretFileName(s);
            File newFile = new File(this.finalCompiledFolder + "\\" + s);

            XSSFWorkbook sheets = this.masters.get(s);

            for (int i = 0; i < sheets.getNumberOfSheets(); i++) {
                XSSFSheet sheetAtStake = sheets.getSheetAt(i);
                try {
                    for (int j = 0; j < sheetAtStake.getRow(0).getPhysicalNumberOfCells(); j++) {
                        sheetAtStake.autoSizeColumn(j);
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    continue;
                }
            }

            sheets.write(new FileOutputStream(newFile));

        }
    }

    private void produce(String masterName, List<File> translations) throws IOException, InvalidFormatException {
        for (File translation : translations) {
            String translationName = translation.getName();
            String realTransFileName = this.interpretFileName(translationName);
            if (masterName.equals(realTransFileName)) {
                System.out.println(" in Method Produce");
                this.assemble(this.masters.get(masterName), translation);
            }
        }
    }

    private void assemble(XSSFWorkbook master, File translation) throws IOException, InvalidFormatException {
        this.dictionary = this.loadTranslation(translation);

//        XSSFWorkbook sheets = new XSSFWorkbook(master);

        XSSFSheet sheet = master.getSheetAt(0);

        ArrayList<Integer> relCols = Util.findRelevantColumns(sheet);

        String lanName = this.parseFileLanguage(translation.getName());

        int lanCol = Util.findLanColumn(sheet, lanName);

        for (int j = 0; j < relCols.size(); j++) {


            for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {

                try {

                    XSSFRow row = sheet.getRow(i);

                    XSSFCell cell = row.getCell(relCols.get(j));
                    System.out.println("row Num is " + i + "and col num is " + relCols.get(j));
                    String keyValue = cell.getStringCellValue().trim();

                    String transValue = this.dictionary.get(keyValue);

                    XSSFCell cellTrans = row.getCell(lanCol);

                    if (cellTrans == null) {
                        cellTrans = row.createCell(lanCol);
                    }

                    cellTrans.setCellValue(transValue);

                    System.out.println(
                            "Here I am putting src Value " + keyValue + " and trans Value " + transValue + " into the Unknown! "
                    );

                } catch (NullPointerException e) {
                    e.printStackTrace();
                    continue;
                }


            }

            lanCol = lanCol + 21;

        }

        this.dictionary = new HashMap<String, String>();

    }

    private String parseFileLanguage(String name) {
        Pattern p = Pattern.compile(this.regex);

        Matcher m = p.matcher(name);

        m.find();

        return m.group(1).substring(0, m.group(1).lastIndexOf("_")).trim();
    }


    private String interpretFileName(String translationName) {

        Pattern p = Pattern.compile(this.regex);

        Matcher m = p.matcher(translationName);

        m.find();

        System.out.println(translationName);
        return m.group(2) + m.group(3);
    }


    private HashMap<String, String> loadTranslation(File translation) throws IOException, InvalidFormatException {

        XSSFWorkbook sheets = new XSSFWorkbook(translation);

        XSSFSheet sheet = sheets.getSheetAt(0);

        for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
            XSSFRow row = sheet.getRow(i);
            XSSFCell cellSrc = row.getCell(0);
            XSSFCell cellTrans = row.getCell(2);

            if (cellSrc != null && cellTrans != null) {
                //Test Tool
//                System.out.println(
//                       "Here I am putting src Value "+ cellSrc.getStringCellValue().trim() +" and trans Value " + cellTrans.getStringCellValue()+ " into the Unknown! "
//                );
                this.dictionary.put(cellSrc.getStringCellValue().trim(), cellTrans.getStringCellValue().trim());
            }

        }


        return this.dictionary;
    }

    public static void main(String[] args) throws IOException, InvalidFormatException {

        new PostQaCompiler("C:\\Users\\trunk\\OneDrive\\desktop\\DEMO\\Project2_non-typical-excel-demo2-automation&tokenization\\forQa\\master",
                "C:\\Users\\trunk\\OneDrive\\desktop\\DEMO\\Project2_non-typical-excel-demo2-automation&tokenization\\forQa\\qaSheet",
                "C:\\Users\\trunk\\OneDrive\\desktop\\DEMO\\Project2_non-typical-excel-demo2-automation&tokenization\\compiled").produce();

    }

}
