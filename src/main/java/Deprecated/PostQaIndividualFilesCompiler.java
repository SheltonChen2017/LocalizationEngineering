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
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import java.io.FileNotFoundException;

public class PostQaIndividualFilesCompiler {
    public static final String masterFolder = "P:\\Projects\\1912\\56\\2-Production\\C-Engineering\\2-Build\\20200306_compile\\master";
    public static final String translationFolders = "P:\\Projects\\1912\\56\\2-Production\\C-Engineering\\2-Build\\20200306_compile\\translations";
    public static final String targetPath = "P:\\Projects\\1912\\56\\2-Production\\C-Engineering\\2-Build\\20200306_compile\\compiled";
    public static final String regex = "(.*?_)(.*\\.xlsx)";
    List<File> masters;
    List<File> folders;

    HashMap<String, XSSFWorkbook> map = new HashMap<String, XSSFWorkbook>();

    public PostQaIndividualFilesCompiler() throws IOException, InvalidFormatException {
//        this.masterFolder = masterFolder;
//        this.translationFolders = translationFolders;
        this.masters = Arrays.asList(new File(this.masterFolder).listFiles());
        this.folders = Arrays.asList(new File(this.translationFolders).listFiles());
        for (File f : this.masters) {

            this.map.put(f.getName(), new XSSFWorkbook(f));

        }
    }

    public void produce() throws IOException, InvalidFormatException {


        for (File folder : this.folders) {


            this.produce(folder, this.map);

        }


    }

    private void produce(File folder, HashMap<String, XSSFWorkbook> map) throws IOException, InvalidFormatException {

        File[] translations = folder.listFiles();


        for (File translation : translations) {

            String keyName = this.parseFileName(translation.getName());
            String lan = this.parseLan(translation.getName());

            if (map.containsKey(keyName)) {

                XSSFWorkbook sheets = map.get(keyName);

                this.copyTranslation(sheets, new XSSFWorkbook(translation), lan);


            }


        }

    }

    private String parseLan(String nameOriginal) {
        Pattern p = Pattern.compile(this.regex);
        Matcher matcher = p.matcher(nameOriginal);

        matcher.find();

        StringBuilder sb = new StringBuilder();
//        matcher.group(3);

        sb.append(matcher.group(1).substring(0, matcher.group(1).lastIndexOf("_")));
        String name = sb.toString();
        System.out.println(name);
        return name;
    }

    private void copyTranslation(XSSFWorkbook original, XSSFWorkbook translation, String lan) {
        XSSFSheet sheetOriginal = original.getSheetAt(0);
        XSSFSheet transOriginal = translation.getSheetAt(0);

        XSSFRow rowLan = sheetOriginal.getRow(0);
        int n = 0;
        for (int i = 5; i < rowLan.getPhysicalNumberOfCells(); i++) {

            if (lan.equalsIgnoreCase(rowLan.getCell(i).getStringCellValue()) || lan.equalsIgnoreCase("Portuguese")) {

            }

            System.out.println(rowLan.getCell(i).getStringCellValue().toLowerCase().trim().contains(lan.toLowerCase()));
            if (rowLan.getCell(i).getStringCellValue().toLowerCase().trim().contains(lan.toLowerCase())) {

                n++;

                if (n == 1) {

                    //copy translation for First Column

                    for (int j = 1; j <= sheetOriginal.getLastRowNum(); j++) {

                        try {
                            XSSFCell cellTrans = transOriginal.getRow(j).getCell(4);

                            System.out.println("-----");

                            if (cellTrans != null) {

                                System.out.println(cellTrans.getStringCellValue());
                                sheetOriginal.getRow(j).createCell(i).setCellValue(cellTrans.getStringCellValue());

                            }
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                            continue;
                        }

                    }


                } else if (n == 2) {
                    //copy translation for the second column

                    for (int j = 1; j <= sheetOriginal.getLastRowNum(); j++) {

                        XSSFCell cellTrans = transOriginal.getRow(j).getCell(8);

                        if (cellTrans != null) {

                            sheetOriginal.getRow(j).createCell(i).setCellValue(cellTrans.getStringCellValue());

                        }


                    }


                }

            }

        }
    }


    public void write() throws IOException {

        Set<String> strings = this.map.keySet();
        int n = 0;
        for (String s : strings) {

            XSSFWorkbook sheets = this.map.get(s);

            File file = new File(this.targetPath + "\\" + s);

            FileOutputStream fos = new FileOutputStream(file);

            sheets.write(fos);

            n++;
        }


    }


    private String parseFileName(String nameOriginal) {

        Pattern p = Pattern.compile(this.regex);
        Matcher matcher = p.matcher(nameOriginal);

        matcher.find();

        StringBuilder sb = new StringBuilder();
        sb.append(matcher.group(2));
        String name = sb.toString();
        System.out.println(name);

        return name;
    }


    public static void main(String[] args) throws IOException, InvalidFormatException {
        PostQaIndividualFilesCompiler compiler = new PostQaIndividualFilesCompiler();
        compiler.produce();

        compiler.write();

    }
}
