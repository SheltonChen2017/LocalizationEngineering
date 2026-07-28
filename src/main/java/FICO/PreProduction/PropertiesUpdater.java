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

        report = new File(this.targetFolder, "localization-update-report.xlsx");
        try (FileOutputStream output = new FileOutputStream(report)) {
            this.workbook.write(output);
        }

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

        try (Reader reader = utf8Reader(englishFile)) {
            englishProperty.load(reader);
        }
        try (Reader reader = utf8Reader(variantFile)) {
            variantProperty.load(reader);
        }
        try (Reader reader = utf8Reader(sourceFile)) {
            sourceProperty.load(reader);
        }
        StringBuilder sb = new StringBuilder();

        Set<String> englishKeys = englishProperty.stringPropertyNames();
        Set<String> variantKeys = variantProperty.stringPropertyNames();

        Set<String> sourceKeys = sourceProperty.stringPropertyNames();

        for (String key : englishKeys) {

            String value = (String) englishProperty.get(key);

            if (!variantKeys.contains(key)) {
//                variantProperty.put(key, value);
                insertMap.put(key, value);
            }
        }

        BufferedReader reader = new BufferedReader(utf8Reader(variantFile));
//System.out.println(variantFile.getName());
        String temp;

        while ((temp = reader.readLine()) != null) {
            sb.append(temp).append(System.lineSeparator());

        }

        Set<String> insertKeys = insertMap.keySet();

        for (String insertKey : insertKeys) {

            String insertValue = insertMap.get(insertKey);

            sb.append(escapeProperty(insertKey)).append("=").append(escapeProperty(insertValue))
                    .append(System.lineSeparator());

        }


//        Set<Map.Entry<String, String>> entries = insertMap.entrySet();

//        System.out.println(sb.toString());

//        String s = reader.readLine();


        reader.close();
        java.nio.file.Path destination = variantFile.toPath();
        java.nio.file.Path temporary = java.nio.file.Files.createTempFile(destination.getParent(),
                variantFile.getName(), ".tmp");
        try {
            java.nio.file.Files.write(temporary, sb.toString().getBytes(java.nio.charset.StandardCharsets.UTF_8));
            try {
                java.nio.file.Files.move(temporary, destination,
                        java.nio.file.StandardCopyOption.REPLACE_EXISTING,
                        java.nio.file.StandardCopyOption.ATOMIC_MOVE);
            } catch (java.nio.file.AtomicMoveNotSupportedException e) {
                java.nio.file.Files.move(temporary, destination,
                        java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            }
        } finally {
            java.nio.file.Files.deleteIfExists(temporary);
        }

//
////        variantProperty.store(fos);
//        FileWriter writer = new FileWriter(new File(variantFile.getParent() + "\\" + variantFile.getName() + "_readapted.properties"));
//
//        variantProperty.store(writer, "");

    }

    private void createReport(String folderpath, File sourceFile, File targetFile, XSSFWorkbook workbook) throws IOException {

        Properties sourceProperties = new Properties();
        Properties targetProperties = new Properties();

        try (Reader sourceStream = utf8Reader(sourceFile);
             Reader targetStream = utf8Reader(targetFile)) {
            sourceProperties.load(sourceStream);
            targetProperties.load(targetStream);
        }
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


    private static Reader utf8Reader(File file) throws IOException {
        return new InputStreamReader(new FileInputStream(file), java.nio.charset.StandardCharsets.UTF_8);
    }

    private static String escapeProperty(String value) {
        return value.replace("\\", "\\\\")
                .replace("\r", "\\r").replace("\n", "\\n").replace("\t", "\\t")
                .replace("=", "\\=").replace(":", "\\:");
    }

    public static void main(String[] args) throws IOException {
        PropertiesUpdater testProo = new PropertiesUpdater(
                "C:\\Users\\trunk\\OneDrive\\桌面\\Career Related Access\\workaholic\\1120111_FicoDevelopment\\Test\\Old\\DMLocalizationFiles_20200518"
                , "C:\\Users\\trunk\\OneDrive\\桌面\\Career Related Access\\workaholic\\1120111_FicoDevelopment\\Test\\New\\DMLocalizationFiles_ToBeTranslated_2-9-21");

        testProo.produce();

    }
}
