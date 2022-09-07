package FICO.Deprecated;

import FICO.Util.OrderedProperties;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* Documentation: This script comments out strings based on comparison between language variants and the new source files. It does not take account of the
 * the differences between the old and new source files. Therefore, usage of this script should limited only in specific cases. Otherwise, this script is deprecated.
* */

public class PropertiesUpdater_old {
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

    private static final String sourceFolderFilterRegex = "(DMLocalizationFiles_20200518)(.*)";
    private static final String targetFolderFilterRegex = "(DMLocalizationFiles_ToBeTranslated_2-9-21)(.*)";

    XSSFWorkbook workbook = new XSSFWorkbook();

    public PropertiesUpdater_old(String sourceFolder, String tagetFolder) {
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

    private ArrayList<File> loadVariant(String folder, String lan, ArrayList<File> lanList) {
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


    private void sortFiles(ArrayList<File> list, HashMap<String, File> map, String regex) {

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


    public void produce() throws IOException {

        int n = 0;
        Set<String> sourceKeys = this.sourceMap.keySet();
//        Set<String> targetKeys = this.targetMap.keySet();
        for (String key : sourceKeys) {


            if (this.targetMap.containsKey(key)) {

                File sourceFile = this.sourceMap.get(key);
                File targetFile = this.targetMap.get(key);

                this.createReport(key, sourceFile, targetFile, this.workbook);

            } else {

                System.out.println("Problematic Folder is " + key);

            }
        }

        File report = new File("C:\\Users\\trunk\\OneDrive\\桌面\\Career Related Access\\workaholic\\1120111_FicoDevelopment\\test run" + "awesomeReport.xlsx");

        this.workbook.write(new FileOutputStream(report));

        this.updateLanguageFiles();

    }

    private void updateLanguageFiles() throws IOException {
//
        this.update(this.targetMap, this.frFrMap);



    }

    private void update(HashMap<String, File> englishMap, HashMap<String, File> variantMap) throws IOException {

        Set<String> folderNames = englishMap.keySet();

        for (String folderName : folderNames) {
            if (variantMap.containsKey(folderName)) {
                File englishFile = englishMap.get(folderName);
                File variantFile = variantMap.get(folderName);

                this.update0(englishFile, variantFile);


            }
        }


    }

    private void update0(File englishFile, File variantFile) throws IOException {

        Properties englishProperty = new OrderedProperties();
        Properties variantProperty = new OrderedProperties();

        LinkedHashMap<String, String> insertMap = new LinkedHashMap<>();

        englishProperty.load(new BufferedInputStream(new FileInputStream(englishFile)));
        variantProperty.load(new BufferedInputStream(new FileInputStream(variantFile)));

        StringBuilder sb = new StringBuilder();

        Set<String> englishKeys = englishProperty.stringPropertyNames();
        Set<String> variantKeys = variantProperty.stringPropertyNames();

        for (String key : englishKeys) {

//            System.out.println(englishFile.getPath());
//            System.out.println(key);

            String value = (String) englishProperty.get(key);

            if (!variantKeys.contains(key)) {
//                variantProperty.put(key, value);
                insertMap.put(key, value);

            }
        }

//        FileReader reader = new FileReader(variantFile);

        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(variantFile)));

        String temp;

        while((temp=reader.readLine())!=null){
            if(!temp.startsWith("#")){
                sb.append("#").append(temp).append("\r\n");
            } else {
                sb.append(temp).append("\r\n");
            }

        }

        Set<String> insertKeys = insertMap.keySet();

        for(String insertKey: insertKeys){

            String insertValue = insertMap.get(insertKey);

            sb.append(insertKey).append("=").append(insertValue).append("\r\n");

        }


//        Set<Map.Entry<String, String>> entries = insertMap.entrySet();

//        System.out.println(sb.toString());

//        String s = reader.readLine();


        FileOutputStream fos = new FileOutputStream(new File(variantFile.getParent() + "\\" + variantFile.getName()));

        fos.write(sb.toString().getBytes());

//
////        variantProperty.store(fos);
//        FileWriter writer = new FileWriter(new File(variantFile.getParent() + "\\" + variantFile.getName() + "_readapted.properties"));
//
//        variantProperty.store(writer, "");

    }

    private void createReport(String folderpath, File sourceFile, File targetFile, XSSFWorkbook workbook) throws IOException {

        Properties sourceProperties = new Properties();
        Properties targetProperties = new Properties();

        BufferedInputStream sourceStream = new BufferedInputStream(new FileInputStream(sourceFile));
        BufferedInputStream targetStream = new BufferedInputStream(new FileInputStream(targetFile));
        sourceProperties.load(sourceStream);
        targetProperties.load(targetStream);
        Set<Object> sourceKeys = sourceProperties.keySet();
        Set<Object> targetKeys = targetProperties.keySet();
//        int n = 0;
        for (Object targetKey : targetKeys) {

            boolean contains = sourceKeys.contains(targetKey);

            if (contains == false) {
//                System.out.println("For target file " + targetFile.getName() + " under folder path " + folderpath + ", " + "key " + targetKey + " is a new insertion. And the new value inserted is " + targetProperties.get(targetKey));
                this.writeReport(targetFile.getName(), folderpath, targetKey, targetProperties.get(targetKey), workbook);

            }
        }


    }

    private void writeReport(String targetFileName, String folderpath, Object targetKeyO, Object targetValueO, XSSFWorkbook workbook) {
        String targetKey = (String) targetKeyO;
        String targetValue = (String) targetValueO;

        XSSFSheet sheet = workbook.getSheetAt(0);

        XSSFRow row = sheet.createRow(this.rowNum++);

        row.createCell(1).setCellValue(targetFileName);
        row.createCell(0).setCellValue(folderpath);
        row.createCell(2).setCellValue(targetKey);
        row.createCell(3).setCellValue(targetValue);

//        System.out.println(sheet.getPhysicalNumberOfRows());
    }


    public static void main(String[] args) throws IOException {
        PropertiesUpdater_old testProo = new PropertiesUpdater_old(
                "C:\\Users\\trunk\\OneDrive\\桌面\\Career Related Access\\workaholic\\1120111_FicoDevelopment\\Test\\Old\\DMLocalizationFiles_20200518"

                , "C:\\Users\\trunk\\OneDrive\\桌面\\Career Related Access\\workaholic\\1120111_FicoDevelopment\\Test\\New\\DMLocalizationFiles_ToBeTranslated_2-9-21");

        testProo.produce();


    }
}
