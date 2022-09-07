package Deprecated;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class ProductNameEnabler {
    String folder;

    List<File> files = new ArrayList<>();

    HashMap<String, XSSFWorkbook> fileMap = new HashMap();

    public ProductNameEnabler(String folder) {
        this.folder = folder;
        this.groupFiles(new File(folder));


    }

    public List<File> groupFiles(File ff) {


        File[] files = ff.listFiles();

        for (File f : files) {

            if (!f.isDirectory()) {

                this.files.add(f);

            } else {
                this.groupFiles(f);
            }

        }
        return this.files;
    }

    public void produce() throws IOException, InvalidFormatException {

        for (File f : this.files) {

            this.fileMap.put(f.getPath(), new XSSFWorkbook(f));
        }

        Set<String> books = this.fileMap.keySet();


    }

}
