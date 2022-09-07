package Deprecated;

import ZimmerBiomet.SpineLabel.cleanup.Util;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PreQaWsFilesCompiler {
    public static String regexNum = "(\\d*)(\\.?)(\\d+)";
    public static String regexHolder = "\\[([\\d\\s]+)\\]";
    public static ArrayList box = new ArrayList();
    public static ArrayList productName = new ArrayList();
    //    public static String commaPeriodRegex = "";
    List<File> folders;
    HashMap<String, XSSFWorkbook> srcs = new HashMap<String, XSSFWorkbook>();
    String folderForSrc;
    String folderForWsTranslations;
    String folderForCompiledTranslations;

    public PreQaWsFilesCompiler(String folderForSrc, String folderForWsTranslations, String folderForCompiledTranslations) throws IOException, InvalidFormatException {
        this.folderForSrc = folderForSrc;
        this.folderForWsTranslations = folderForWsTranslations;
        this.folderForCompiledTranslations = folderForCompiledTranslations;

        File[] files = new File(this.folderForSrc).listFiles();

        for (File f : files) {
            this.srcs.put(f.getName(), new XSSFWorkbook(f));
        }

        this.folders = Arrays.asList(new File(folderForWsTranslations).listFiles());
    }

    public void produce() throws IOException, InvalidFormatException {

        for (File folder : this.folders) {

            File[] translations = folder.listFiles();

            for (File translation : translations) {
                System.out.println(translation.getPath());
                this.produce(this.srcs, translation, this.parseLanguage(folder.getName()));

            }

        }

    }

    private String parseLanguage(String name) {
        Pattern p = Pattern.compile("-([a-zA-Z]+)\\s");
        Matcher matcher = p.matcher(name);

        matcher.find();

        return matcher.group(1).trim();
    }

    private void produce(HashMap<String, XSSFWorkbook> srcs, File translation, String lan) throws IOException, InvalidFormatException {

        Set<String> names = srcs.keySet();

        for (String name : names) {

            if (translation.getName().toLowerCase().contains(name.toLowerCase())) {
                XSSFWorkbook transBook = new XSSFWorkbook(translation);
                int n = this.findLanColumn(srcs.get(name), lan);

//                ArrayList<Integer> integers = this.findMultipleMaxColumn(srcs.get(name), lan);

//                for(Integer n: integers){
                    this.flip(srcs.get(name), transBook, n);
//                }



            }


        }


    }

    private int findLanColumn(XSSFWorkbook sheets, String lan) {


        XSSFSheet sheet = sheets.getSheetAt(0);

        XSSFRow row = sheet.getRow(0);

        for (int j = 0; j < row.getPhysicalNumberOfCells(); j++) {

            try {
                XSSFCell cell = row.getCell(j);
                System.out.println(lan.toLowerCase());
                System.out.println(cell.getStringCellValue().toLowerCase().contains(lan.toLowerCase()));
//                System.out.println(cell.getStringCellValue().toLowerCase().trim());
//                cell.getStringCellValue().toLowerCase().contains(lan.toLowerCase())
                if (cell.getStringCellValue().toLowerCase().contains(lan.toLowerCase())) {
                    return j;
                }

            } catch (NullPointerException e) {

                System.out.println("nullPointerException at findLanColumn");
//                e.printStackTrace();
                System.out.println("the number of j is " + j);
                continue;

            }

        }

        return 0;
    }



    //Method "Flip" interprets the placeholder
    private void flip(XSSFWorkbook srcBook, XSSFWorkbook transBook, int n) {
        for (int m = 0; m < srcBook.getNumberOfSheets(); m++) {
            XSSFSheet sheetSrc = srcBook.getSheetAt(m);
            XSSFSheet sheetTrans = transBook.getSheetAt(m);
            System.out.println("the number of n is " + n);
            flipSheet(n, sheetSrc, sheetTrans);
        }

    }

    private void flipSheet(int n, XSSFSheet sheetSrc, XSSFSheet sheetTrans) {

        int m = Util.findMaxNumOfRows(sheetSrc);
        ArrayList<Integer> descriptions = Util.findRelevantColumns(sheetSrc);

//        for(Integer relevantColNum: descriptions){
//
//        }

        for(int i =0 ; i<descriptions.size();i++){

            if(i==0){
                flipSheet0(n, sheetSrc, sheetTrans, m, descriptions.get(i));
            } else{
                flipSheet0(n+21, sheetSrc, sheetTrans, m, descriptions.get(i));
            }

        }


//        Integer releventColNum = descriptions.get(0);

    }

    private void flipSheet0(int n, XSSFSheet sheetSrc, XSSFSheet sheetTrans, int m, Integer releventColNum) {
        for (int i = 1; i <= m; i++) {

            try {


                XSSFCell srcCell = sheetSrc.getRow(i).getCell(releventColNum);
//
                XSSFCell transCell = sheetTrans.getRow(i).getCell(releventColNum);

                String valueSrc = srcCell.getStringCellValue();
                String valueTrans = transCell.getStringCellValue();

                System.out.println("src Value is " + valueSrc + " while trans Value is " + valueTrans);

                Pattern p1 = Pattern.compile(this.regexNum);

                Pattern p2 = Pattern.compile(this.regexHolder);

                Matcher m1 = p1.matcher(valueSrc);

                while (m1.find()) {
                    this.box.add(m1.group(0));
                }

                Matcher m2 = p2.matcher(valueTrans);

                while (m2.find()) {
                    String holder = m2.group(1).trim();
//                System.out.println(holder);
                    int num = Integer.parseInt(holder);

                    String holderIntact = m2.group(0);
                    valueTrans = valueTrans.replace(holderIntact, this.box.get(num).toString());

                    Util.replaceDotToPeriodForNumbers(valueTrans);

                }

                XSSFCell cellTarget = sheetSrc.getRow(i).getCell(n);

                if (cellTarget == null) {
                    sheetSrc.getRow(i).createCell(n);
                }

                if (sheetSrc.getRow(i).getCell(n) != null && !sheetSrc.getRow(i).getCell(n).getStringCellValue().trim().equals("")) {
                    sheetSrc.getRow(i).getCell(n).setCellValue(valueTrans);
                }
                this.box = new ArrayList();
            } catch (NullPointerException e) {
                System.out.println(m);
                e.printStackTrace();
                continue;
            } catch (IndexOutOfBoundsException ee) {
                ee.printStackTrace();
//                System.out.println(sheetTrans.getRow(i).getCell(1).getStringCellValue());
                System.out.println("now the box is ");
                for (Object ii : this.box) {
                    System.out.println(ii + "------");
                }
                continue;

            }catch(IllegalStateException e){
                e.printStackTrace();
                System.out.println(i);
                System.out.println(n);
                continue;
            }
        }
    }


    public void write() throws IOException {

        Set<String> names = this.srcs.keySet();


        for (String name : names) {

            XSSFWorkbook book = this.srcs.get(name);

            File file = new File(this.folderForCompiledTranslations + "\\" + name);

            FileOutputStream fos = new FileOutputStream(file);

            book.write(fos);

        }

    }


    public static void main(String[] args) throws IOException, InvalidFormatException {
        PreQaWsFilesCompiler compiler = new PreQaWsFilesCompiler("P:\\Projects\\2002\\55\\2-Production\\C-Engineering\\2-Build\\Test_preQaCompilation\\master",
                "P:\\Projects\\2002\\55\\2-Production\\C-Engineering\\2-Build\\Test_preQaCompilation\\fromWs",
                "P:\\Projects\\2002\\55\\2-Production\\C-Engineering\\2-Build\\Test_preQaCompilation\\compiled");
        compiler.produce();
        compiler.write();
    }
}
