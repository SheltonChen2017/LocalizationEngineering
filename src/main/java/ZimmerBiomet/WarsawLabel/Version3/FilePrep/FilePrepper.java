package ZimmerBiomet.WarsawLabel.Version3.FilePrep;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
//import java.io.FileNotFoundException;
//import java.util.Arrays;
import java.util.HashMap;
//import java.util.List;
import java.util.Set;

public class FilePrepper {
    String path;

    public final static String sheetName = "TV-Import File";

    HashMap<String, XSSFWorkbook> map = new HashMap<>();


    public FilePrepper(String path) throws IOException, InvalidFormatException {
        this.path = path;
        File[] files = new File(path).listFiles();

        for (File f : files) {

            this.map.put(f.getPath(), new XSSFWorkbook(f));

        }

    }

    public void generate() throws IOException {

        Set<String> strings = this.map.keySet();

        for (String path : strings) {

            XSSFWorkbook book = this.map.get(path);

            this.generate(book, path);

        }


    }

    private void generate(XSSFWorkbook book, String path) throws IOException {

        XSSFSheet sheet = book.getSheet(this.sheetName);

        XSSFWorkbook prepped = new XSSFWorkbook();

        XSSFSheet work = prepped.createSheet();

//        int n = this.findLastRow(sheet);

        int j = 0;

        for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {

            XSSFRow row = sheet.getRow(i);
            System.out.println(i);
            if (row != null) {

                System.out.println("Hello");
                XSSFCell cellLan = row.getCell(1);
                XSSFCell cellTrans = row.getCell(2);

//                if(cell)
                if (cellLan != null) {
                    System.out.println(cellLan.getStringCellValue());

                    if (cellLan.getStringCellValue().equals("EN") && cellTrans != null) {

                        System.out.println(cellTrans.getStringCellValue());

                        work.createRow(j++).createCell(0).setCellValue(cellTrans.getStringCellValue());

                    }
                }
            }


        }


        this.writeFile(prepped, path);


    }


    private void writeFile(XSSFWorkbook prepped, String path) throws IOException {

        String substring = path.substring(0, path.lastIndexOf("."));

        String preppedPath = substring + ".xlsx";

        File file = new File(preppedPath);

        String folder = file.getParent();

        String newFolder = folder + "\\" + "prepped";

        File file1 = new File(newFolder);
        if (!file1.isDirectory()) {
            file1.mkdir();
        }


        FileOutputStream fos = new FileOutputStream(file1.getPath() + "\\" + file.getName());

        prepped.write(fos);

    }

    public static void produce() throws IOException, InvalidFormatException {
        System.out.println("What is the source path?");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String s = br.readLine();

        FilePrepper filePrepper = new FilePrepper(s);

        filePrepper.generate();

    }

    public static void main(String[] args) throws IOException, InvalidFormatException {
        FilePrepper filePrepper = new FilePrepper(
                "C:\\Users\\trunk\\OneDrive\\desktop\\DEMO\\Automation\\Project1_non-typical-excel-demo1-automation");

        filePrepper.generate();

    }

    /**
     *
     *
     * */
//
}
/*

 * */