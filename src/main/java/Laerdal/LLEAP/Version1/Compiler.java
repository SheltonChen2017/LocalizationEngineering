package Laerdal.LLEAP.Version1;

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

public class Compiler {
    String pathForAllTranslations;
    String pathForPreppedFiles;

    ArrayList<XSSFWorkbook> sourceBooks = new ArrayList<XSSFWorkbook>();

    File[] files;

    public Compiler(String pathForAllTranslations, String pathForPreppedFiles) throws IOException, InvalidFormatException {
        this.pathForAllTranslations = pathForAllTranslations;
        this.pathForPreppedFiles = pathForPreppedFiles;

        File file = new File(pathForPreppedFiles);

        this.files = file.listFiles();

        for (File f : this.files) {

            XSSFWorkbook book = new XSSFWorkbook(f);
            sourceBooks.add(book);

        }

    }

    public String languageParser(String path) {
        Pattern p = Pattern.compile("-[a-zA-Z]+ ");

        Matcher matcher = p.matcher(path);

        if (matcher.find()) {
            return matcher.group(0).trim().substring(1);
        }

        return null;


    }

    public void compile() throws IOException, InvalidFormatException {


        List<File> folders = Arrays.asList(new File(this.pathForAllTranslations).listFiles());

        for (File folder : folders) {
            String lan = this.languageParser(folder.getPath());
//System.out.println(lan);
            if (!lan.equalsIgnoreCase("English")) {

                this.copyTranslation(this.sourceBooks, folder, lan);

            }


        }


    }

    private void copyTranslation(ArrayList sourceBooks, File folder, String lan) throws IOException, InvalidFormatException {

        List<File> files = Arrays.asList(folder.listFiles());

        ArrayList<XSSFWorkbook> translations = new ArrayList<XSSFWorkbook>();


        for (File f : files) {
            XSSFWorkbook translation = new XSSFWorkbook(f);
//            System.out.println(f.getPath());
            translations.add(translation);
        }

        this.doCopy(sourceBooks, translations, lan);

    }

    private void doCopy(ArrayList<XSSFWorkbook> sourceBooks, ArrayList<XSSFWorkbook> translations, String lan) throws IOException {

        for (int i = 0; i < sourceBooks.size(); i++) {


            this.doCopy0(sourceBooks.get(i), translations.get(i), lan);


        }


    }

    private void doCopy0(XSSFWorkbook source, XSSFWorkbook translation, String lan) throws IOException {

//        System.out.println(translation);

        XSSFSheet ssheet = source.getSheetAt(0);
        XSSFSheet tsheet = translation.getSheetAt(0);

//定位column

        XSSFRow lanRow = ssheet.getRow(0);

        int col=0;

        for(int i=2; i<lanRow.getPhysicalNumberOfCells();i++){

            String lanColumn = lanRow.getCell(i).getStringCellValue();



//            System.out.println( lanColumn.toLowerCase().startsWith(lan.toLowerCase()));

            if(lanColumn.toLowerCase().startsWith(lan.toLowerCase())){
                System.out.println(lanColumn.toLowerCase()+"'''''''''''''''''''''''''''''''''''''''''''''");
                col = i;
            }

        }

        for(int m=1; m<ssheet.getPhysicalNumberOfRows();m++){

            XSSFRow row = ssheet.getRow(m);
            XSSFCell cell = row.createCell(col);

            XSSFRow trow = tsheet.getRow(m);
            XSSFCell tcell = trow.getCell(1);
            String value = tcell.getStringCellValue();



            cell.setCellValue(value);

        }


    }


    public void write() throws IOException {

        for(int i=0; i<this.files.length; i++){

            File file = new File(files[i].getPath() + "_" + "compiled.xlsx");

            FileOutputStream fileOutputStream = new FileOutputStream(file);

            this.sourceBooks.get(i).write(fileOutputStream);

        }
//        for(int i=0;i<this.sourceBooks.size();i++){
//            for(int m=0; m<sourceBooks.get(i).getSheetAt(0))
//        }


    }


    public static void main(String[] args) throws IOException, InvalidFormatException {

        Compiler compiler = new Compiler("P:\\Projects\\2002\\77\\2-Production\\C-Engineering\\2-Build\\excel\\translations", "P:\\Projects\\2002\\77\\2-Production\\C-Engineering\\2-Build\\excel\\masters");

        compiler.compile();

        compiler.write();
    }

}
