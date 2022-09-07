package FICO.PreProduction;

import FICO.Util.OrderedProperties;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.*;

public class PropertiesUpdater extends AbstractLoader implements PropertiesLoader {

    public PropertiesUpdater(String sourceFolder, String tagetFolder) {
        super(sourceFolder, tagetFolder);
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
        this.update(this.sourceMap, this.targetMap, this.frFrMap);


    }

    private void update(HashMap<String, File> sourceMap, HashMap<String, File> englishMap, HashMap<String, File> variantMap) throws IOException {

        Set<String> folderNames = englishMap.keySet();

        for (String folderName : folderNames) {
            if (variantMap.containsKey(folderName)) {
                File englishFile = englishMap.get(folderName);
                File variantFile = variantMap.get(folderName);
                File sourceFile = sourceMap.get(folderName);

                if (englishFile != null && variantFile != null && sourceFile != null) {
                    this.update0(sourceFile, englishFile, variantFile);
                }

            }
        }


    }

    private void update0(File sourceFile, File englishFile, File variantFile) throws IOException {

        Properties englishProperty = new OrderedProperties();
        Properties variantProperty = new OrderedProperties();
        Properties sourceProperty = new OrderedProperties();

        LinkedHashMap<String, String> insertMap = new LinkedHashMap<>();

        englishProperty.load(new BufferedInputStream(new FileInputStream(englishFile)));
        variantProperty.load(new BufferedInputStream(new FileInputStream(variantFile)));
        sourceProperty.load(new BufferedInputStream(new FileInputStream(sourceFile)));
        StringBuilder sb = new StringBuilder();

        Set<String> englishKeys = englishProperty.stringPropertyNames();
        Set<String> variantKeys = variantProperty.stringPropertyNames();

        Set<String> sourceKeys = sourceProperty.stringPropertyNames();

        for (String key : englishKeys) {

            String value = (String) englishProperty.get(key);

            if (!sourceKeys.contains(key) || !variantKeys.contains(key)) {
//                variantProperty.put(key, value);
                insertMap.put(key, value);
            }
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(variantFile)));
//System.out.println(variantFile.getName());
        String temp;

        while ((temp = reader.readLine()) != null) {
            if (!temp.startsWith("#")) {
                sb.append("#").append(temp).append("\r\n");
            } else {
                sb.append(temp).append("\r\n");
            }

        }

        Set<String> insertKeys = insertMap.keySet();

        for (String insertKey : insertKeys) {

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
        PropertiesUpdater testProo = new PropertiesUpdater(
                "C:\\Users\\trunk\\OneDrive\\桌面\\Career Related Access\\workaholic\\1120111_FicoDevelopment\\Test\\Old\\DMLocalizationFiles_20200518"
                , "C:\\Users\\trunk\\OneDrive\\桌面\\Career Related Access\\workaholic\\1120111_FicoDevelopment\\Test\\New\\DMLocalizationFiles_ToBeTranslated_2-9-21");

        testProo.produce();

    }
}