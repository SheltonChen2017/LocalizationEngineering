package FICO.PreProduction;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractLoader implements PropertiesLoader {

    String sourceFolder;
    String targetFolder;

    ArrayList<File> sourceList = new ArrayList<File>();
    ArrayList<File> targetList = new ArrayList<File>();

    Integer rowNum = 1;

    HashMap<String, File> sourceMap = new HashMap<>();
    HashMap<String, File> targetMap = new HashMap<>();

    HashMap<String, File> frFrMap = new HashMap<>();
    //    HashMap<String, File> frCaMap = new HashMap<>();
//    HashMap<String, File> ptPtMap = new HashMap<>();
    ArrayList<File> frList = new ArrayList<File>();
//    ArrayList<File> frCaList = new ArrayList<File>();
//    ArrayList<File> ptPtList = new ArrayList<File>();

    public static final String sourceFolderFilterRegex = "(DMLocalizationFiles_20200518)(.*)";
    public static final String targetFolderFilterRegex = "(DMLocalizationFiles_ToBeTranslated_2-9-21)(.*)";

    XSSFWorkbook workbook = new XSSFWorkbook();


    public AbstractLoader(String sourceFolder, String tagetFolder) {
        this.sourceFolder = sourceFolder;
        this.targetFolder = tagetFolder;
        this.sourceList = this.loadPropertiesFilesFromFolder(this.sourceFolder, this.sourceList);
        this.targetList = this.loadPropertiesFilesFromFolder(this.targetFolder, this.targetList);
        this.sortFiles(this.sourceList, this.sourceMap, sourceFolderFilterRegex);
        this.sortFiles(this.targetList, this.targetMap, targetFolderFilterRegex);

        this.loadVariant(this.targetFolder, "FR_FR", this.frList);
//        this.loadVariant(this.targetFolder, "FR_CA", this.frCaList);
//        this.loadVariant(this.targetFolder, "PT_PT", this.ptPtList);

        this.sortFiles(this.frList, this.frFrMap, targetFolderFilterRegex);
//        this.sortFiles(this.frCaList, this.frCaMap, targetFolderFilterRegex);
//        this.sortFiles(this.ptPtList, this.ptPtMap, targetFolderFilterRegex);


    }

    public ArrayList<File> loadVariant(String folder, String lan, ArrayList<File> lanList) {
        File[] files = new File(folder).listFiles();
//System.out.println(targetFolder);
        for (File f : files) {
            String name = f.getName().toUpperCase();

            if (!f.isDirectory() && name.contains(lan)) {
                lanList.add(f);

            } else if (f.isDirectory()) {
                this.loadVariant(f.getPath(), lan, lanList);
            }


        }

//        this.sortFiles();

        return lanList;
    }


    public void sortFiles(ArrayList<File> list, HashMap<String, File> map, String regex) {

        Pattern pattern = Pattern.compile(regex);

        for (File file : list) {

            String parentPath = file.getParent();
//System.out.println(file.getPath());
            Matcher matcher = pattern.matcher(parentPath);
//
            boolean b = matcher.find();
//            System.out.println(b);

            String adaptedParentPath = matcher.group(2);

            map.put(adaptedParentPath, file);

//            if(file.getName().contains("fr_FR")){
//                System.out.println(file.getName());
//            }


        }


    }

    {

        XSSFSheet sheet = this.workbook.createSheet();

    }


    public ArrayList<File> loadPropertiesFilesFromFolder(String path, ArrayList<File> resultList) {

        File[] files = new File(path).listFiles();

        for (File file : files) {

            if (!file.isDirectory()) {
                if (file.getPath().endsWith("properties") && !file.getName().toUpperCase().contains("FR_FR") && !file.getName().toUpperCase().contains("PT_PT")
                        && !file.getName().toUpperCase().contains("FR_CA")) {
                    resultList.add(file);
                }
            } else {

                this.loadPropertiesFilesFromFolder(file.getPath(), resultList);

            }

        }


        return resultList;

    }


}
