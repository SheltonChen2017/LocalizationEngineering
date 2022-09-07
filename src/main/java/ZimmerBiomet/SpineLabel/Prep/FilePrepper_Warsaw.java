package ZimmerBiomet.SpineLabel.Prep;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FilePrepper_Warsaw {
    String folder;

    public static String sheetName = "English Content";

    List<File> srcFiles = new ArrayList<>();

    public FilePrepper_Warsaw(String folder) {
        this.folder = folder;

        File[] files = new File(folder).listFiles();

        for(File f: files){
            if(!f.isDirectory()){
                this.srcFiles.add(f);
            }
        }

    }


    public void produce() throws IOException, InvalidFormatException {

        for(File f: this.srcFiles){


            this.produce(f);



        }


    }

    private void produce(File f) throws IOException, InvalidFormatException {

        XSSFWorkbook workbook = new XSSFWorkbook(f);

        XSSFSheet sheet = workbook.getSheet(this.sheetName);



    }


}
